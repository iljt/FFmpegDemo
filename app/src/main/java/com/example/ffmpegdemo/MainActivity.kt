package com.example.ffmpegdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ffmpegdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Example of a call to a native method
        binding.sampleText.text =  "当前 FFmpeg 版本为:" + getFFmpegVersion()
    }

    private external fun getFFmpegVersion(): String?

    companion object {
        // Used to load the 'ffmpegdemo' library on application startup.
        init {
            System.loadLibrary("ffmpegdemo")
        }
    }
}