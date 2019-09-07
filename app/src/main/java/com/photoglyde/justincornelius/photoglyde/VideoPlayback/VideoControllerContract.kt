package com.photoglyde.justincornelius.photoglyde.VideoPlayback



interface VideoControllerContract {

    interface Presenter {

        fun deactivate()

        fun getPlayer(): VideoPlayer

        fun play(url: String)

        fun releasePlayer()

        fun setMediaSessionState(isActive: Boolean)
    }

    interface View
}