import cv2
import numpy as np
import threading
import time
import requests
from ultralytics import YOLO
from requests_toolbelt.multipart.encoder import MultipartEncoder
from queue import Queue

# === CONFIG ===
CONFIDENCE    = 0.8
IOU_THRESHOLD = 0.5
IMG_SIZE      = 960
DISPLAY_W, DISPLAY_H = 1280, 720

MODEL_PATH   = r"C:\Users\ardah\OneDrive\Masaüstü\yazılım\drone_project\yolov11\models\obb\yolo11l-obb\best (10).pt"
SERVER_URL   = "http://172.20.10.7:5001/api/harvest"
MJPEG_URL    = "http://172.20.10.7:5001/api/video/upload"

model = YOLO(MODEL_PATH)

seen_ids = set()
id_remap = {}
next_sequential_id = 1
frame_count = 0
last_send_time = time.time()

latest_frame = None
frame_lock = threading.Lock()

# MJPEG için queue
mjpeg_queue = Queue(maxsize=1)

def frame_grabber():
    global latest_frame
    cap = cv2.VideoCapture(0)
    cap.set(cv2.CAP_PROP_BUFFERSIZE, 1)
    while cap.isOpened():
        ret, frame = cap.read()
        if ret:
            with frame_lock:
                latest_frame = frame.copy()
        else:
            time.sleep(0.01)
    cap.release()

def mjpeg_stream_sender():
    while True:
        try:
            frame = mjpeg_queue.get()  # Wait for new frame
            ret, jpeg = cv2.imencode('.jpg', frame)
            if not ret:
                continue
            
            # Send raw JPEG data
            headers = {
                'Content-Type': 'image/jpeg'
            }
            requests.post(MJPEG_URL, data=jpeg.tobytes(), headers=headers)
        except Exception as e:
            print(f"❌ Streaming error: {e}")
            time.sleep(0.1)  # Add a small delay on error to prevent tight loop

threading.Thread(target=frame_grabber, daemon=True).start()
threading.Thread(target=mjpeg_stream_sender, daemon=True).start()

cv2.namedWindow('Live Detection', cv2.WINDOW_NORMAL)
cv2.resizeWindow('Live Detection', DISPLAY_W, DISPLAY_H)

while True:
    with frame_lock:
        frame = latest_frame.copy() if latest_frame is not None else None

    if frame is None:
        time.sleep(0.005)
        continue

    results = model.track(frame,
                          conf=CONFIDENCE,
                          iou=IOU_THRESHOLD,
                          imgsz=IMG_SIZE,
                          persist=True,
                          classes=[0],
                          device="cuda",
                          stream=False,
                          verbose=False)[0]

    if getattr(results, 'obb', None) and results.obb.xyxy is not None and results.obb.id is not None:
        boxes = results.obb.xyxy.cpu().numpy().astype(int)
        ids   = results.obb.id.cpu().numpy()
    elif getattr(results, 'boxes', None) and results.boxes.xyxy is not None and results.boxes.id is not None:
        boxes = results.boxes.xyxy.cpu().numpy().astype(int)
        ids   = results.boxes.id.cpu().numpy()
    else:
        boxes = np.empty((0,4), dtype=int)
        ids   = np.array([],       dtype=int)

    for (x1, y1, x2, y2), original_tid in zip(boxes, ids):
        tid0 = int(original_tid)
        if tid0 not in id_remap:
            id_remap[tid0] = next_sequential_id
            next_sequential_id += 1
        tid = id_remap[tid0]
        is_new = tid not in seen_ids
        seen_ids.add(tid)
        color = (0, 255, 0) if is_new else (0, 0, 255)
        label = f"{'NEW' if is_new else 'OLD'} - ID:{tid}"
        cv2.rectangle(frame, (x1, y1), (x2, y2), color, 2)
        cv2.putText(frame, label, (x1, y1 - 10),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.8, color, 2)

    total_count = len(seen_ids)
    cv2.putText(frame, f"Objects Counted: {total_count}",
                (20, 50), cv2.FONT_HERSHEY_SIMPLEX, 1, (255,255,255), 2)
    
    
    now = time.time()
    if now - last_send_time >= 1.0:
        payload = {
            'timestamp': int(now),
            'count': total_count
        }
        try:
            response = requests.post(SERVER_URL, json=payload, timeout=2)
            if response.status_code == 200:
                print(f"✅ Sent fruit count: {total_count}")
            else:
                print(f"⚠️ Server responded with status: {response.status_code}")
        except Exception as e:
            print(f"❌ Failed to send data: {e}")
        last_send_time = now
    
    # Sadece en güncel frame'i queue'ya koy (eski varsa sil)
    if not mjpeg_queue.full():
        mjpeg_queue.put(frame.copy())
    else:
        try:
            mjpeg_queue.get_nowait()
        except:
            pass
        mjpeg_queue.put(frame.copy())

    disp = cv2.resize(frame, (DISPLAY_W, DISPLAY_H))
    cv2.imshow('Live Detection', disp)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break
    frame_count += 1

cv2.destroyAllWindows()