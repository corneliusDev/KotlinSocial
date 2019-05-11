package com.photoglyde.justincornelius.photoglyde.VideoPlayback

import android.drm.DrmStore
import android.net.Uri

import im.ene.toro.ToroUtil

import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import im.ene.toro.ToroPlayer
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide.init
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.photoglyde.justincornelius.photoglyde.Adapters.FeedAdapter
import com.photoglyde.justincornelius.photoglyde.Adapters.ImageAdapter
import com.photoglyde.justincornelius.photoglyde.Adapters.ProfileAdapter
import com.photoglyde.justincornelius.photoglyde.Data.Configuration
import com.photoglyde.justincornelius.photoglyde.Data.CoreUnSplash

import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals

import com.photoglyde.justincornelius.photoglyde.R

import im.ene.toro.exoplayer.ExoCreator
import im.ene.toro.exoplayer.ExoPlayerViewHelper
import im.ene.toro.exoplayer.Playable
import im.ene.toro.media.PlaybackInfo
import im.ene.toro.widget.Container
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.adapter_row_similar.view.*
import kotlinx.android.synthetic.main.full_view.view.*
import kotlinx.android.synthetic.main.view_holder_exoplayer_basic.view.*
import java.util.*


//class SimplePlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ToroPlayer,
//    View.OnClickListener,
//
//    View.OnLongClickListener {
//
//
//    lateinit var playerView: PlayerView
//    private var helper: ExoPlayerViewHelper? = null
//    var mediaUri:Uri? = null
//    lateinit var itemClickListener: FeedAdapter.OnItemClickListener
//    var listenerTorro: ToroPlayer.EventListener? = null
//
//
//
//    //  lateinit var onItemLockClickListener: ProfileAdapter.OnItemLockClickListener
//
//    init {
//
//        //  if (itemView.placeImage != null) itemView.placeImage.setOnLongClickListener(this)
//      //  if (itemView.placeImageHere != null) itemView.placeImageHere.setOnClickListener(this)
//        if (itemView.placeImageHere != null) itemView.placeImageHere.setOnLongClickListener(this)
//        if (itemView.placeImage != null) itemView.placeImage.setOnLongClickListener(this)
//        if (itemView.placeImage != null) itemView.placeImage.setOnClickListener(this)
//        // if (itemView.placeImage != null) itemView.placeImage.setOnTouchListener(this)
//
//        // if (itemView.findViewById<Button>(R.id.button_profile_1) != null) itemView.button_profile_1.listener
//
//
//
//        if (itemView.findViewById<PlayerView>(R.id.player) != null){
//            playerView = itemView.findViewById(R.id.player) as PlayerView
//            println("=====video click init")
//            itemView.player.setOnClickListener(this)
//            itemView.exo_image.setOnClickListener(this)
//        }else{
//            println("=====video click init null")
//            playerView = PlayerView(itemView.context)
//        }
//
//
//
//    }
//
//
////    override fun onClick(p0: View?) {
////        println("======== video clciked")
////        var resizePlayer = playerView.layoutParams
////        resizePlayer?.width = GlobalVals.widthWindow
////        //resizePlayer.setMargins(0,0,0,0)
////
////    }
//
//    var donw = true
//
////        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
////            return mDetector.onTouchEvent(p1).let { result ->
////                if (!result &&donw) {
////
////                    donw = false
////                    val action = p1?.actionMasked
////                    ViewPropertyObjectAnimator.animate(p0?.placeImage).scaleY(.95f).scaleX(.95f)
////                        .setDuration(200).start()
////                    println("===========profile down")
////                    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
////                        p0?.getParent()?.requestDisallowInterceptTouchEvent(false)
////                        println("===========profile up")
////                        ViewPropertyObjectAnimator.animate(p0?.placeImage).scaleY(1.0f).scaleX(1.0f)
////                            .setDuration(200).start()
////                        true
////                    } else false
////                } else false
////            }
//////                .let {
//////                println("=====inside progfile adapter")
//////
//////                if (donw) {
//////                    donw = false
//////                    ViewPropertyObjectAnimator.animate(p0?.placeImage).scaleY(.95f).scaleX(.95f)
//////                        .setDuration(200).start()
//////                }
//////
//////
//////                val action = p1?.actionMasked
//////
//////                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
//////                    if (true) {
//////                        p0?.getParent()?.requestDisallowInterceptTouchEvent(false)
//////                        ViewPropertyObjectAnimator.animate(p0?.placeImage).scaleY(1.0f).scaleX(1.0f)
//////                            .setDuration(200).start()
//////                        // dialog.root.smoothScrollBy(0, delta)
//////                    }
//////
//////
//////                }
//////
//////            }
////
////        }
//
//    override fun onLongClick(p0: View?): Boolean {
//
//
//        println("=======i am insude")
//
//
//
//
//
//
//
//
//        //onItemLockClickListener.onItemLongClick(itemView, adapterPosition)
//        return false
//    }
//
//
//    override fun initialize(container: Container, playbackInfo: PlaybackInfo) {
//        if (helper == null) {
//            println("========= inside helper $mediaUri $helper and ${Configuration.config1} and $this")
//
////            val trackSelector = DefaultTrackSelector()
////            val loadControl = DefaultLoadControl()
////            val renderersFactory = DefaultRenderersFactory(this)
//
//
//            helper = ExoPlayerViewHelper(this, mediaUri!!, null, Configuration.config1!!)
//            container.player.showController()
//
//
//
//
//
//        }
//        if (listenerTorro == null) {
//
//            listenerTorro = object : ToroPlayer.EventListener{
//                override fun onBuffering() {
//
//
//
//
//                    if (itemView.load_video != null) {
//
//                        itemView.load_video.visibility = View.VISIBLE
//                        itemView.load_video.playAnimation()
//
//                    }
//
//
//
//
//                }
//
//                override fun onFirstFrameRendered() {
//
//
//
//                }
//
//                override fun onPlaying() {
//                    //if (itemView.animationLoad != null) itemView.animationLoad.visibility = View.GONE
//                    itemView.exo_image.alpha = 0.0f
//                    if (itemView.load_video != null) {
//                        itemView.load_video.visibility = View.INVISIBLE
//                        itemView.load_video.pauseAnimation()
//                        val volumeSet = helper
//                        volumeSet?.volume = 0f
//
//                    }
//                }
//
//                override fun onPaused() {
//
//                    itemView.exo_image.visibility = View.VISIBLE
//                    itemView.exo_image.bringToFront()
//
//                    println("=======video is paused")
//
//                }
//
//                override fun onCompleted() {
//
//                }
//            }
//
//
//
//            helper!!.addPlayerEventListener(listenerTorro!!)
//
//        }
//        helper!!.initialize(container, playbackInfo)
//    }
//    override fun getPlayerView(): View {
//        println("========over 1")
//
//
//
//        return playerView
//    }
//
//    override fun getCurrentPlaybackInfo(): PlaybackInfo {
//        println("========over 2")
//        return if (helper != null) helper!!.latestPlaybackInfo else PlaybackInfo()
//    }
//
//    override fun play() {
//
//        println("========over 6")
//        if (helper != null){
//            helper!!.play()
//
//
//            println("========over 7")
//            println("=========htting we ared playing")
//        }
//    }
//
//    override fun pause() {
//        println("========over 8")
//
//        println("=========where is the video ${playerView.player.currentPosition}")
//
//
//        if (helper != null){
//            println("========over 9")
//            helper!!.pause()
//
//        }
//    }
//
//    override fun isPlaying(): Boolean {
//
//        println("===========the video is playing")
//
//
//        // itemView.exo_image.visibility = View.GONE
//        //  println("========over 10 ${helper != null} and ${helper!!.isPlaying}")
//        return helper != null && helper!!.isPlaying
//    }
//
//    override fun release() {
//        println("========over 11")
//        if (listenerTorro != null) {
//            helper?.removePlayerEventListener(listenerTorro!!)
//            listenerTorro = null
//        }
//        helper?.release()
//        helper = null
//    }
//
//    override fun wantsToPlay(): Boolean {
//        println("========over 13 ${ToroUtil.visibleAreaOffset(this, itemView.parent)}")
//        return ToroUtil.visibleAreaOffset(this, itemView.parent) >= 0.85
//    }
//
//    override fun getPlayerOrder(): Int {
////            println("========over 14 $adapterPosition")
//        return adapterPosition
//    }
//
//    override fun onClick(view: View) = itemClickListener.onItemClick(itemView, adapterPosition, geti(position)?.video_uri, playerView.player.currentPosition.toInt())
//
//    internal fun bind(media: Uri) {
//        println("========over 15 $media")
//        this.mediaUri = media
//
//
//    }
//
//
//}