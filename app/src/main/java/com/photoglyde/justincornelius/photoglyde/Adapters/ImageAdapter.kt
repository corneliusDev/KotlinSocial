/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.photoglyde.justincornelius.photoglyde.Adapters

import android.arch.paging.PagedListAdapter
import android.net.Uri
import android.support.constraint.ConstraintSet
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator
import com.google.android.exoplayer2.ui.PlayerView
import com.photoglyde.justincornelius.photoglyde.Data.*

import com.photoglyde.justincornelius.photoglyde.Networking.sameCheckDifficulti
import com.photoglyde.justincornelius.photoglyde.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import com.volokh.danylo.visibility_utils.scroll_utils.ItemsPositionGetter
import im.ene.toro.ToroPlayer
import im.ene.toro.ToroUtil
import im.ene.toro.exoplayer.ExoPlayerViewHelper
import im.ene.toro.media.PlaybackInfo
import im.ene.toro.widget.Container
import kotlinx.android.synthetic.main.adapter_row.view.*
import kotlinx.android.synthetic.main.adapter_row_similar.view.*
import kotlinx.android.synthetic.main.full_view.view.*
import kotlinx.android.synthetic.main.view_holder_exoplayer_basic.view.*


class ImageAdapter : PagedListAdapter<CoreUnSplash, ImageAdapter.ViewHolder>(sameCheckDifficulti()), StateSaver {

  private val set = ConstraintSet()
  lateinit var itemClickListener: OnItemClickListener
  lateinit var itemPositionGetter:ItemsPositionGetter
  lateinit var onItemLockClickListener:OnItemLockClickListener
  lateinit var onItemTouchListener: speakTouchListener




  override fun getItemCount(): Int {
    return super.getItemCount()
    //print()
  }

  override fun getItemViewType(position: Int): Int {


    return super.getItemViewType(position)



  }

  override fun saveState(adapterPosition: Int, itemPosition: Int, lastItemAngleShift: Double) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    var view:View


        view = LayoutInflater.from(parent.context).inflate(R.layout.full_view, parent, false)


    return ViewHolder(view)
  }



  override fun onBindViewHolder(holder: ViewHolder, position: Int) {

  }


  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

    init {
      itemView.placeImageHere.setOnClickListener(this)
      itemView.placeImageHere.setOnLongClickListener(this)
      //itemView.placeImage.setOnTouchListener(this)
    }




    override fun onClick(view: View) = itemClickListener.onItemClick(itemView, adapterPosition)
    override fun onLongClick(p0: View?): Boolean {


      println("=======i am insude")



      onItemLockClickListener.onItemLongClick(itemView, adapterPosition)
      return true
    }


  }

  interface OnItemClickListener {
    fun onItemClick(view: View, position: Int)

  }

  interface speakTouchListener {

    fun onTouch(p0: View?, p1: MotionEvent?)

  }

  interface OnItemLockClickListener{

    fun onItemLongClick(view: View, position: Int)

  }

  fun setTouchListener(onItemTouchListener: speakTouchListener){

    this.onItemTouchListener = onItemTouchListener

  }


  fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
    this.itemClickListener = itemClickListener
  }

  fun setOnItemItemPositionGetter(itemPositionGetter: ItemsPositionGetter) {
    this.itemPositionGetter = itemPositionGetter
  }

  fun SetOnLock(onItemLockClickListener:OnItemLockClickListener){

    this.onItemLockClickListener = onItemLockClickListener
  }


  inner class SimplePlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ToroPlayer, View.OnClickListener, View.OnLongClickListener {


    lateinit var playerView: PlayerView
    private var helper: ExoPlayerViewHelper? = null
    var mediaUri:Uri? = null
    //lateinit var itemClickListener: ProfileAdapter.OnItemClickListener
    var listenerTorro: ToroPlayer.EventListener? = null



    //  lateinit var onItemLockClickListener: ProfileAdapter.OnItemLockClickListener

    init {

      //  if (itemView.placeImage != null) itemView.placeImage.setOnLongClickListener(this)
      if (itemView.placeImageHere != null) itemView.placeImageHere.setOnClickListener(this)
      if (itemView.placeImageHere != null) itemView.placeImageHere.setOnLongClickListener(this)
      if (itemView.placeImage != null) itemView.placeImage.setOnLongClickListener(this)
     // if (itemView.placeImage != null) itemView.placeImage.setOnTouchListener(this)

      // if (itemView.findViewById<Button>(R.id.button_profile_1) != null) itemView.button_profile_1.listener



      if (itemView.findViewById<PlayerView>(R.id.player) != null){
        playerView = itemView.findViewById(R.id.player) as PlayerView
      }else{
        playerView = PlayerView(itemView.context)
      }



    }

    var donw = true

//    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
//      return mDetector.onTouchEvent(p1).let { result ->
//        if (!result &&donw) {
//
//          donw = false
//          val action = p1?.actionMasked
//          ViewPropertyObjectAnimator.animate(p0?.placeImage).scaleY(.95f).scaleX(.95f)
//            .setDuration(200).start()
//          println("===========profile down")
//          if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
//            p0?.getParent()?.requestDisallowInterceptTouchEvent(false)
//            println("===========profile up")
//            ViewPropertyObjectAnimator.animate(p0?.placeImage).scaleY(1.0f).scaleX(1.0f)
//              .setDuration(200).start()
//            true
//          } else false
//        } else false
//      }
////                .let {
////                println("=====inside progfile adapter")
////
////                if (donw) {
////                    donw = false
////                    ViewPropertyObjectAnimator.animate(p0?.placeImage).scaleY(.95f).scaleX(.95f)
////                        .setDuration(200).start()
////                }
////
////
////                val action = p1?.actionMasked
////
////                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
////                    if (true) {
////                        p0?.getParent()?.requestDisallowInterceptTouchEvent(false)
////                        ViewPropertyObjectAnimator.animate(p0?.placeImage).scaleY(1.0f).scaleX(1.0f)
////                            .setDuration(200).start()
////                        // dialog.root.smoothScrollBy(0, delta)
////                    }
////
////
////                }
////
////            }
//
//    }

    override fun onLongClick(p0: View?): Boolean {


      println("=======i am insude")








      onItemLockClickListener.onItemLongClick(itemView, adapterPosition)
      return false
    }


    override fun initialize(container: Container, playbackInfo: PlaybackInfo) {
      if (helper == null) {
        println("========= inside helper $mediaUri $helper and ${Configuration.config1} and $this")
        helper = ExoPlayerViewHelper(this, mediaUri!!, null, Configuration.config1!!)
      }
      if (listenerTorro == null) {

        listenerTorro = object : ToroPlayer.EventListener{
          override fun onBuffering() {


            if (itemView.load_video != null) itemView.load_video.playAnimation()




          }

          override fun onFirstFrameRendered() {



          }

          override fun onPlaying() {
            //if (itemView.animationLoad != null) itemView.animationLoad.visibility = View.GONE
            itemView.exo_image.visibility = View.INVISIBLE
            if (itemView.load_video != null)  itemView.load_video.visibility = View.GONE
          }

          override fun onPaused() {

            println("=======video is paused")

          }

          override fun onCompleted() {

          }
        }



        helper!!.addPlayerEventListener(listenerTorro!!)

      }
      helper!!.initialize(container, playbackInfo)
    }
    override fun getPlayerView(): View {
      println("========over 1")



      return playerView
    }

    override fun getCurrentPlaybackInfo(): PlaybackInfo {
      println("========over 2")
      return if (helper != null) helper!!.latestPlaybackInfo else PlaybackInfo()
    }

    override fun play() {

      println("========over 6")
      if (helper != null){
        helper!!.play()
        println("========over 7")
        println("=========htting we ared playing")
      }
    }

    override fun pause() {
      println("========over 8")

      itemView.exo_image.visibility = View.VISIBLE
      if (helper != null){
        println("========over 9")
        helper!!.pause()

      }
    }

    override fun isPlaying(): Boolean {

      println("===========the video is playing")
      // itemView.exo_image.visibility = View.GONE
      //  println("========over 10 ${helper != null} and ${helper!!.isPlaying}")
      return helper != null && helper!!.isPlaying
    }

    override fun release() {
      println("========over 11")
      if (listenerTorro != null) {
        helper?.removePlayerEventListener(listenerTorro!!)
        listenerTorro = null
      }
      helper?.release()
      helper = null
    }

    override fun wantsToPlay(): Boolean {
      println("========over 13 ${ToroUtil.visibleAreaOffset(this, itemView.parent)}")
      return ToroUtil.visibleAreaOffset(this, itemView.parent) >= 0.85
    }

    override fun getPlayerOrder(): Int {
//            println("========over 14 $adapterPosition")
      return adapterPosition
    }

    override fun onClick(view: View) = itemClickListener.onItemClick(itemView, adapterPosition)

    internal fun bind(media: Uri) {
      println("========over 15 $media")
      this.mediaUri = media


    }


  }







}
