package com.photoglyde.justincornelius.photoglyde.VideoPlayback

class MainControllerContract {
    interface Presenter {


        //once URI is obtained, use this interface to intiate
        fun showVideo(videoUrl: String)
    }
}