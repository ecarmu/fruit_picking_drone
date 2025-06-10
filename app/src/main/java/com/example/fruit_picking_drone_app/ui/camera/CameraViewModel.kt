package com.example.fruit_picking_drone_app.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {

    private val _text = MutableLiveData<String>("Live camera feed from your server! 🎥\nTap the button below to open the stream.")
    val text: LiveData<String> = _text
}