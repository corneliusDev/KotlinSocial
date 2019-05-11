package com.photoglyde.justincornelius.photoglyde.VideoPlayback

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer

interface VideoPlayer {
    fun play(url: String)

    fun getPlayerImpl(context: Context): ExoPlayer

    fun releasePlayer()

    fun setMediaSessionState(isActive: Boolean)
}