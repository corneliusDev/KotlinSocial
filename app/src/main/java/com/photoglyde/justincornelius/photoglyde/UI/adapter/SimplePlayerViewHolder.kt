package com.photoglyde.justincornelius.photoglyde.UI.adapter

import android.net.Uri
import android.view.View
import android.widget.AdapterView
import com.google.android.exoplayer2.ui.PlayerView
import com.photoglyde.justincornelius.photoglyde.Data.Configuration
import com.photoglyde.justincornelius.photoglyde.Data.CoreData
import com.photoglyde.justincornelius.photoglyde.Data.GlobalValues
import com.photoglyde.justincornelius.photoglyde.R
import im.ene.toro.ToroPlayer
import im.ene.toro.ToroUtil
import im.ene.toro.exoplayer.ExoPlayerViewHelper
import im.ene.toro.media.PlaybackInfo
import im.ene.toro.widget.Container
import kotlinx.android.synthetic.main.adapter_row_similar.view.*
import kotlinx.android.synthetic.main.full_view.view.*
import kotlinx.android.synthetic.main.horizontal_rows.view.*
import kotlinx.android.synthetic.main.view_holder_exoplayer_basic.view.*

 class SimplePlayerViewHolder(itemView: View, core: CoreData?) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), ToroPlayer, View.OnClickListener, View.OnLongClickListener {


    private lateinit var playerView: PlayerView
    private var helper: ExoPlayerViewHelper? = null
    private var mediaUri: Uri? = null
     lateinit var itemClickListener: FeedAdapter.OnItemClickListener
    private var listenerTorro: ToroPlayer.EventListener? = null
     lateinit var onItemLockClickListener: FeedAdapter.OnItemLockClickListener
     private var data:CoreData? = null


    init {
        data = core
        //  if (itemView.placeImage != null) itemView.placeImage.setOnLongClickListener(this)
        if (itemView.placeImageH != null) itemView.placeImageH.setOnClickListener(this)
        if (itemView.placeImageHere != null) itemView.placeImageHere.setOnLongClickListener(this)
        if (itemView.placeImageHere != null) itemView.placeImageHere.setOnClickListener(this)
        if (itemView.placeImage != null) itemView.placeImage.setOnLongClickListener(this)
        if (itemView.placeImage != null) itemView.placeImage.setOnClickListener(this)
   //      if (itemView.placeImage != null) itemView.placeImage.setOnTouchListener(this)

        // if (itemView.findViewById<Button>(R.id.button_profile_1) != null) itemView.button_profile_1.listener



        if (itemView.findViewById<PlayerView>(R.id.player) != null){
            playerView = itemView.findViewById(R.id.player) as PlayerView
            println("=====video click init")
            itemView.player.setOnClickListener(this)
            itemView.exo_image.setOnLongClickListener(this)

            itemView.exo_image.setOnClickListener(this)
        }else{
            println("=====video click init null")
            playerView = PlayerView(itemView.context)


        }



    }




    override fun initialize(container: Container, playbackInfo: PlaybackInfo) {
        if (helper == null) {


//            val trackSelector = DefaultTrackSelector()
//            val loadControl = DefaultLoadControl()
//            val renderersFactory = DefaultRenderersFactory(this)

            itemView.exo_image.bringToFront()

            helper = ExoPlayerViewHelper(this, mediaUri!!, null, Configuration.config1!!)

            if (GlobalValues.whatsNew){
                container.player.hideController()
            }else if(!GlobalValues.whatsNew){
                container.player.showController()
            }else if(GlobalValues.videoWatch){
                container.player.showController()
            }






        }
        if (listenerTorro == null) {

            listenerTorro = object : ToroPlayer.EventListener{
                override fun onBuffering() {

                    itemView.exo_image.alpha = 1f
                    //  itemView.exo_image.bringToFront()

                    if (itemView.load_video != null) {

                        //   itemView.load_video.visibility = View.VISIBLE
                        itemView.load_video.playAnimation()

                    }

                }

                override fun onFirstFrameRendered() {

                }

                override fun onPlaying() {
                    //if (itemView.animationLoad != null) itemView.animationLoad.visibility = View.GONE
                    itemView.exo_image.alpha = 0.0f
                    if (itemView.load_video != null) {
//                            itemView.load_video.visibility = View.INVISIBLE
//                            itemView.load_video.pauseAnimation()
                        val volumeSet = helper
                        volumeSet?.volume = 0f

                    }
                }

                override fun onPaused() {

                    itemView.exo_image.visibility = View.VISIBLE

                }

                override fun onCompleted() {

                }
            }
            helper!!.addPlayerEventListener(listenerTorro!!)
        }
        helper!!.initialize(container, playbackInfo)
    }
    override fun getPlayerView(): View {
        return playerView
    }

    override fun getCurrentPlaybackInfo(): PlaybackInfo {
        return if (helper != null) helper!!.latestPlaybackInfo else PlaybackInfo()
    }

    override fun play() {
        if (helper != null){
            helper!!.play()
        }
    }

    override fun pause() {

        if (helper != null){
            helper!!.pause()
        }

    }

    override fun isPlaying(): Boolean {
        return helper != null && helper!!.isPlaying
    }

    override fun release() {
        if (listenerTorro != null) {
            helper?.removePlayerEventListener(listenerTorro!!)
            listenerTorro = null
        }
        helper?.release()
        helper = null
    }

    override fun wantsToPlay(): Boolean {
        return ToroUtil.visibleAreaOffset(this, itemView.parent) >= 0.85
    }

    override fun getPlayerOrder(): Int {
        return adapterPosition
    }


    internal fun bind(media: Uri) {
        this.mediaUri = media
    }



     override fun onClick(view: View) {
         println("SIMPLE VIEW CLICK")
         helper?.volume = 0.50f
         itemClickListener.onItemClick(itemView, adapterPosition, data)
     }


     override fun onLongClick(p0: View?): Boolean {

         helper?.volume = 0.70f

         onItemLockClickListener.onItemLongClick(itemView, adapterPosition, data)

         return true
     }

     interface OnItemClickListener {
         fun onItemClick(view: View, position: Int, data: CoreData?)
     }
//

//
//     interface OnItemLockClickListener{
//
//         fun onItemLongClick(view: View, position: Int, core: CoreData?)
//
//     }




 }