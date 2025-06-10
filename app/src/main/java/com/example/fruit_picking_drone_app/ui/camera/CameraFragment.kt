package com.example.fruit_picking_drone_app.ui.camera

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fruit_picking_drone_app.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var cameraViewModel: CameraViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cameraViewModel = ViewModelProvider(this).get(CameraViewModel::class.java)
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("CAMERA_DEBUG", "=== CameraFragment onViewCreated ===")

        // ViewModel'den gelen yazıyı TextView'a set edelim
        cameraViewModel.text.observe(viewLifecycleOwner) { txt ->
            binding.textCamera.text = txt
        }

        // Butona tıklanınca server'ın video feed'ini aç
        binding.buttonOpenStream.setOnClickListener {
            val streamUrl = "http://172.20.10.4:5001/video_feed"
            Log.d("CAMERA_DEBUG", "Opening camera stream URL: $streamUrl")
            
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(streamUrl))

            try {
                // show chooser so any browser or compatible app appears
                val chooser = Intent.createChooser(intent, "Open camera stream with")
                startActivity(chooser)
            } catch (e: ActivityNotFoundException) {
                Log.e("CAMERA_DEBUG", "No app available to open camera stream", e)
                Toast.makeText(requireContext(),
                    "No application available to view this link",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}