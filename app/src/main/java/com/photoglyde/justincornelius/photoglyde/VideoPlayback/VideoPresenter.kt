package com.photoglyde.justincornelius.photoglyde.VideoPlayback

import java.lang.ref.WeakReference

class VideoPresenter(videoViewView: VideoControllerContract.View) : VideoControllerContract.Presenter {

    private val view = WeakReference(videoViewView)

    private val mediaPlayer = MediaPlayerInit()

    override fun deactivate() {
    }

    override fun getPlayer() = mediaPlayer

    override fun play(url: String) = mediaPlayer.play(url)

    override fun releasePlayer() = mediaPlayer.releasePlayer()

    override fun setMediaSessionState(isActive: Boolean) {
        mediaPlayer.setMediaSessionState(isActive)
    }

}