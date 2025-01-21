package com.anime.application.dashBoard.youtube

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.anime.application.databinding.ActivityFullscreenExampleBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FullscreenExampleActivity : AppCompatActivity() {

    // Declare the binding variable
    private lateinit var binding: ActivityFullscreenExampleBinding
    private lateinit var youTubePlayer: YouTubePlayer
    private var isFullscreen = false

    // BackPressed callback to handle fullscreen and exit
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isFullscreen) {
                // If in fullscreen, exit fullscreen
                youTubePlayer.toggleFullscreen()
            } else {
                this@FullscreenExampleActivity.finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize the binding
        binding = ActivityFullscreenExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle onBackPressed callback
        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        val fullscreenViewContainer = binding.fullScreenViewContainer

        val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .fullscreen(1) // enable full screen button
            .build()

        // Initialize the YouTubePlayerView manually to pass IFramePlayerOptions
        binding.youtubePlayerView.enableAutomaticInitialization = false

        // Fullscreen listener to handle entering and exiting fullscreen
        binding.youtubePlayerView.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                isFullscreen = true

                // The video will continue playing in fullscreenView
                binding.youtubePlayerView.visibility = View.GONE
                fullscreenViewContainer.visibility = View.VISIBLE
                fullscreenViewContainer.addView(fullscreenView)

            }

            override fun onExitFullscreen() {
                isFullscreen = false

                // The video will continue playing in the player
                binding.youtubePlayerView.visibility = View.VISIBLE
                fullscreenViewContainer.visibility = View.GONE
                fullscreenViewContainer.removeAllViews()
            }
        })

        // Initialize YouTube player and load a video
        binding.youtubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@FullscreenExampleActivity.youTubePlayer = youTubePlayer
                youTubePlayer.loadVideo("-tviZNY6CSw", 0f)

                // Set click listener for fullscreen button
                binding.enterFullscreenButton.setOnClickListener {
                    youTubePlayer.toggleFullscreen()
                }
            }
        }, iFramePlayerOptions)

        lifecycle.addObserver(binding.youtubePlayerView)
    }
}
