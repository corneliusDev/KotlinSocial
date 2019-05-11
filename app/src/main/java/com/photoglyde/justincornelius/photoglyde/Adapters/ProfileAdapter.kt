package com.photoglyde.justincornelius.photoglyde.Adapters

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.service.autofill.UserData
import android.support.v4.content.ContextCompat
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.graphics.Palette
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.*
import android.widget.*
import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals.transitionRatioHeight
import com.photoglyde.justincornelius.photoglyde.Networking.sameCheckDifficulti
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.R.id.*

import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import im.ene.toro.ToroPlayer
import im.ene.toro.ToroUtil
import im.ene.toro.exoplayer.ExoPlayerViewHelper

import im.ene.toro.media.PlaybackInfo
import im.ene.toro.widget.Container
import kotlinx.android.synthetic.main.activity_news_web_view.view.*
import kotlinx.android.synthetic.main.adapter_row.view.*
import kotlinx.android.synthetic.main.adapter_row_similar.view.*
import kotlinx.android.synthetic.main.adapter_row_video.view.*
import kotlinx.android.synthetic.main.button_profile.view.*
import kotlinx.android.synthetic.main.cell_header_profile.view.*
import kotlinx.android.synthetic.main.full_view.view.*
import kotlinx.android.synthetic.main.horizontal_rows.view.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.nested_favorites.view.*
import kotlinx.android.synthetic.main.nested_recycler.view.*
import kotlinx.android.synthetic.main.news_layout.view.*
import kotlinx.android.synthetic.main.news_layout_copy.view.*
import kotlinx.android.synthetic.main.post_bottom_button.view.*
import kotlinx.android.synthetic.main.profile_header.view.*
import kotlinx.android.synthetic.main.profile_top.view.*
import kotlinx.android.synthetic.main.vertical_rows.view.*
import kotlinx.android.synthetic.main.view_holder_exoplayer_basic.view.*
import kotlinx.android.synthetic.main.view_image_previewer.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class ProfileAdapter(private var context: Context, private var unSplash:List<CoreUnSplash>, private var layoutViews:ArrayList<Int>) : PagedListAdapter<CoreUnSplash, ProfileAdapter.SimplePlayerViewHolder>(
    sameCheckDifficulti()) {

    private val viewPool = RecyclerView.RecycledViewPool()

  lateinit var itemClickListener: OnItemClickListener

  lateinit var itemView:View


    lateinit var onItemTouchListener: speakTouchListener

    lateinit var onItemLockClickListener: OnItemLockClickListener

  override fun getItemCount() = layoutViews.size

    private val listenerAdapter = object : GestureDetector.SimpleOnGestureListener() {


        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            println("===========hello 1")
            return super.onSingleTapUp(e)
        }

        override fun onDown(e: MotionEvent?): Boolean {
            println("===========hello 2")

            return super.onDown(e)
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            println("===========hello 3")
            return super.onFling(e1, e2, velocityX, velocityY)
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            println("===========hello 4")
            return super.onDoubleTap(e)
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            println("===========hello 5")
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onContextClick(e: MotionEvent?): Boolean {
            println("===========hello 6")
            return super.onContextClick(e)
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            println("===========hello 7")
            return super.onSingleTapConfirmed(e)
        }

        override fun onShowPress(e: MotionEvent?) {
            println("===========hello 8")
            super.onShowPress(e)
        }

        override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
            println("===========hello 9")
            return super.onDoubleTapEvent(e)
        }

        override fun onLongPress(e: MotionEvent?) {
            println("===========hello 10")
            super.onLongPress(e)
        }
    }

    private val mDetector: GestureDetector = GestureDetector(context, listenerAdapter)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimplePlayerViewHolder {



    when(viewType){

      0 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.profile_header, parent, false)

      1 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.profile_top, parent, false)

      2 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.nested_favorites, parent, false)

        3 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.news_layout, parent, false)

        4 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_header, parent, false)

        5 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.horizontal_rows, parent, false)

        6 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_exoplayer_basic, parent, false)

        7 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.full_view, parent, false)

        8 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.nested_recycler, parent, false)
        9 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_row_similar, parent, false)

        10 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.button_profile, parent, false)



    }




    return SimplePlayerViewHolder(itemView)
  }



    override fun getItemViewType(position: Int): Int {

    var itemPosition = layoutViews[position]


    return itemPosition
  }

  override fun onBindViewHolder(holder: SimplePlayerViewHolder, position: Int) {


      println(" ======now pos $position ${layoutViews.size}")

    if (holder.itemViewType == 1){


      val mContainerTop = holder.itemView.top1Image.layoutParams
      val mContainerBottom = holder.itemView.top2Image.layoutParams
      val mTop1 = holder.itemView.top1.layoutParams as LinearLayout.LayoutParams
      val mTop2 = holder.itemView.top2.layoutParams as LinearLayout.LayoutParams

      val mBottom1 = holder.itemView.bottom1.layoutParams as LinearLayout.LayoutParams
      val mBottom2 = holder.itemView.bottom2.layoutParams as LinearLayout.LayoutParams
      val header = holder.itemView.header.layoutParams as LinearLayout.LayoutParams

      val buttonSeeAll = holder.itemView.button_see_all.layoutParams as LinearLayout.LayoutParams


      header.setMargins(40,20,40,40)
      val headerText = holder.itemView.header
      headerText.text = "created by me"

        val headerTextMargin = holder.itemView.header as LinearLayout.LayoutParams
        //headerTextMargin.setMargins(40,40,40,40)

      mContainerTop.height = GlobalVals.heightWindow/5
      mContainerBottom.height = GlobalVals.heightWindow/5


      mTop1.width = GlobalVals.widthWindow/2 - 45
      mTop1.setMargins(40,5,5,0)
      mTop2.width = GlobalVals.widthWindow/2 - 45
      mTop2.setMargins(5,5,40,0)

      holder.itemView.top1.elevation = 0F
      holder.itemView.top2.elevation = 0F
      holder.itemView.bottom1.elevation = 0F
      holder.itemView.bottom2.elevation = 0F
      holder.itemView.button_see_all.elevation = 0F



      buttonSeeAll.setMargins(40,80,40,40)






//      holder.itemView.top1.setBackgroundResource(R.drawable.usej)
//      holder.itemView.top2.setBackgroundResource(R.drawable.usej)


//
//      GlobalVals.picassoUnit[0].into(holder.itemView.top1Image)
//      GlobalVals.picassoUnit[1].into(holder.itemView.top2Image)
//
//      GlobalVals.picassoUnit[2].resize( GlobalVals.widthWindow/2, GlobalVals.widthWindow/2).into(holder.itemView.bottom1Image)
//      GlobalVals.picassoUnit[4].resize( GlobalVals.widthWindow/2, GlobalVals.widthWindow/2).into(holder.itemView.bottom2Image)




      println("======check width ${GlobalVals.widthWindow.toDouble().times(0.57).toInt()}")

      val bottomContainerWidthFirst = GlobalVals.widthWindow/2 - 45
      val bottomContainerWidthFirstEnd = GlobalVals.widthWindow/2 - 45


      mBottom1.width = bottomContainerWidthFirst
      mBottom2.width = bottomContainerWidthFirstEnd

      mBottom1.height = GlobalVals.heightWindow/5
      mBottom2.height = GlobalVals.heightWindow/5

      mBottom1.setMargins(40,5,5,5)
      mBottom2.setMargins(5,5,40,5)



    }else if (holder.itemViewType == 2){


      holder.itemView.list_favorites.isNestedScrollingEnabled = true


     // val bottomLine = holder.itemView.bottom_line as ViewGroup.MarginLayoutParams
//      val headerContainer = holder.itemView.lower_header
//      headerContainer.setMargins(40,0,40,0)







    }else if (holder.itemViewType == 3){

        val item = GlobalVals.savedNews[position]
        val resources = holder.itemView.context.resources

        val image = item.urlToImage
        //val width = item?.mediaa?.first()?.media_metadata?.last()?.width?.toDouble()
        //val height = item?.mediaa?.first()?.media_metadata?.last()?.height?.toDouble()

        val title = item.title
        val target = holder.itemView.stock_image
        val imageTarget = holder.itemView.imageHere
        val abstract = holder.itemView.abstracts

        val windowWidth = GlobalVals.widthWindow.toDouble().div(1.05)
        val windowHeight = GlobalVals.widthWindow.toDouble().div(1.2)

        val ratio = windowHeight.div(windowWidth)

        val finalWidth = windowWidth.toInt()/1.3
        val finalHeight = windowHeight.times(ratio).toInt() - 30

        val resize = target.layoutParams as LinearLayout.LayoutParams


        resize.width = finalWidth.toInt()
        resize.height = finalHeight

       // val news_target = holder.itemView.news_target as LinearLayout.LayoutParams
       // news_target.setMargins(0,0,0,0)

        resize.setMargins(0,0,0,0)


        //abstract.layoutParams.width = finalWidth


       // abstract.text = item?.description



        println(
            "=========api dimensions ${finalWidth} and ${finalHeight} and ${finalHeight.div(finalWidth)} and ${GlobalVals.widthWindow} and ${GlobalVals.widthWindow.times(
                finalHeight.div(finalWidth)
            )} and ${GlobalVals.widthWindow.times(finalHeight.div(finalWidth)).toInt()}"
        )


        //resize.height = GlobalVals.widthWindow.toDouble().times(ratio)

        println(
            "we are im the news apadper ${title} widthWindow ${GlobalVals.widthWindow} ratio $ratio width ${GlobalVals.widthWindow / 2} height ${GlobalVals.widthWindow.times(
                ratio.toInt()
            )}"
        )

        Picasso.get().load(image).fit().into(imageTarget)
       // holder.itemView.title.layoutParams.width = finalWidth.toInt()
        //holder.itemView.title.text = title

        println("=============we are im the news apadper ${image}")

//
//
//            val item = GlobalVals.savedNews[position]
//            val resources = holder.itemView.context.resources
//
//            val image = item?.urlToImage
////            val width = item?.media?.first()?.media_metadata?.last()?.width?.toDouble()
////            val height = item?.media?.first()?.media_metadata?.last()?.height?.toDouble()
//
//           // val title = item?.title
//            val target = holder.itemView.stock_image
//            val imageTarget = holder.itemView.imageHere
//            val abstract = holder.itemView.abstracts
//
//            val windowWidth = GlobalVals.widthWindow.toDouble().div(1.30)
//            val windowHeight = GlobalVals.widthWindow.toDouble().div(1.30)
//
//            val ratio = windowHeight.div(windowWidth)
//
//            val finalWidth = windowWidth.toInt() - 30
//            val finalHeight = windowHeight.times(ratio).toInt() - 30
//
//            val resize = target.layoutParams as LinearLayout.LayoutParams
//
//
//
//            val imageTargetMargins = holder.itemView.imageHere
//        imageTargetMargins.setPadding(40,40,5,40)
//
//
//
//
//            resize.width = finalWidth
//            resize.height = finalHeight
//
//            abstract.layoutParams.width = finalWidth
//
//            abstract.text = item?.title
//
//
//
//            println(
//                "=========api dimensions ${finalWidth} and ${finalHeight} and ${finalHeight.div(finalWidth)} and ${GlobalVals.widthWindow} and ${GlobalVals.widthWindow.times(
//                    finalHeight.div(finalWidth)
//                )} and ${GlobalVals.widthWindow.times(finalHeight.div(finalWidth)).toInt()}"
//            )
//
//
//            resize.height = GlobalVals.widthWindow.toDouble().times(ratio).toInt()
//
//           // println(
//                //    "we are im the news apadper ${title} widthWindow ${GlobalVals.widthWindow} ratio $ratio width ${GlobalVals.widthWindow / 2} height ${GlobalVals.widthWindow.times(
//             //       ratio!!.toInt()
//               // )}"
//           // )
//
//            Picasso.get().load(image).fit().into(imageTarget)
//            holder.itemView.title.layoutParams.width = finalWidth
//           // holder.itemView.title.text = title
//
//            println("=============we are im the news apadper ${image}")


    }else if(holder.itemViewType == 4){

        val target = holder.itemView.header_news
//
//        val targetMargins = holder.itemView.layoutHeader as LinearLayout
//        targetMargins.setMargins(40,0,40,40)

        target.text = "saved"

        println("======profile adapter check ${layoutViews.size} and ${layoutViews}")


    }else if (holder.itemViewType == 5) {

        val item = GlobalVals.categoryList[position]

        val categoryText = holder.itemView.placeNameH
        categoryText.text = item
        println("======== here is the category $item")



    }else if(holder.itemViewType == 6){

        println("=======video is parsed")
        val autoHeightChild = holder.itemView.player.layoutParams
        val URI = "https://thedailyhustleonline.com/wp-content/uploads/2017/10/circular-profile-image.png"
       // autoHeightChild.width = GlobalVals.widthWindow/2

        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/photoglyde.appspot.com/o/video%2FVideos%2Fimages%2F1549394213458.jpg?alt=media&token=8d301c4d-6fd9-44a4-b678-46a432f96403")
            .into(holder.itemView.exo_image)


        holder.bind(Uri.parse(GlobalVals.testUri[position]))



        holder.itemView.exo_image.bringToFront()
        holder.itemView.load_video.bringToFront()

        holder.itemView.botton_buttons.visibility = View.GONE
        holder.itemView.cell_header.visibility = View.GONE

        holder.bind(Uri.parse(GlobalVals.testUri[position]))



        if (GlobalVals.fromExplore){
            holder.itemView.post_button_container.visibility = View.GONE
            holder.itemView.botton_buttons.visibility = View.GONE
            holder.itemView.botton_buttons.visibility = View.GONE
           // holder.itemView.exo_top_text.visibility = View.GONE


        }else {
//            holder.itemView.exo_list_button_1.width = GlobalVals.widthWindow/3 - 20
//            holder.itemView.exo_list_button_3.width = GlobalVals.widthWindow/3 - 20
//            holder.itemView.exo_list_button_2.width = GlobalVals.widthWindow/3 - 20
        }


    //    Picasso.get().load(URI).fit().into(holder.itemView.photo_card)


    }else if(holder.itemViewType == 7){
//
//        val autoHeightChild = holder.itemView.placeImage.layoutParams
//        autoHeightChild.width = GlobalVals.widthWindow/2
//
//



//
//       // holder.itemView.lower_description.text = GlobalVals.imageClassUser[position]?.desc

        val item = unSplash[position]

        GlobalVals.picassoUnit.add(Picasso.get().load(item.urls?.regular))





    //   val new = Picasso.get().load(item.urls?.regular)


        val resources = holder.itemView.context.resources
        val description = item.description
        val new = Uri.parse(item.urls?.small)

        holder.itemView.findViewById<TextView>(R.id.title).bringToFront()

        println("================we are loading")












        val autoHeightChild = holder.itemView.placeImageHere.layoutParams
        autoHeightChild.width = GlobalVals.widthWindow
        val calculatedHeight = (GlobalVals.widthWindow).times(item.height!!.div(item.width!!)).toInt()
        autoHeightChild.height = calculatedHeight


        GlobalVals.transitionRatioHeight.add(calculatedHeight)


       // holder.itemView.top_desc.text = description


           // holder.itemView.list_button_1.width = GlobalVals.widthWindow / 3 - 20
//            holder.itemView.list_button_3.width = GlobalVals.widthWindow / 3 - 20
//            holder.itemView.list_button_2.width = GlobalVals.widthWindow / 3 - 20


//        val icon2 = holder.itemView.list_button_2 as LinearLayout.LayoutParams
//        val icon1 = holder.itemView.list_button_1 as LinearLayout.LayoutParams
//        val icon3 = holder.itemView.list_button_3 as LinearLayout.LayoutParams
//
//
//        icon1.setMargins(0,0,100,0)
//        icon2.setMargins(100,0,100,0)
//        icon3.setMargins(100,0,100,0)

        // holder.itemView.placeImageHere.elevation = 0F

        //val dimen = Picasso.get().load(item?.name).get().
        // println("================imageAdapter position $position")

       // GlobalVals.test2.add(new)

      //  GlobalVals.picassoUnit.add(Picasso.get().load(GlobalVals.test2[position]))

       // GlobalVals.picassoUnit[position].into(holder.itemView.placeImageHere)

        val targetInto = GlobalVals.picassoUnit[position] as RequestCreator
        targetInto.into(holder.itemView.placeImageHere)

      //  Picasso.get().load(new).into(holder.itemView.placeImageHere)


    }else if (holder.itemViewType == 8){

        GlobalVals.picassoUnit.add("")
        GlobalVals.transitionRatioHeight.add(GlobalVals.heightWindow/2)




            val childLayoutManager = StaggeredGridLayoutManager(3, LinearLayout.VERTICAL)

           // val header = holder.itemView.horizontal_name
           // header.text = "my photos"
            holder.itemView.rv_child.apply {

                this.layoutManager = childLayoutManager


                this.adapter = BindingHorizontal(holder.itemView.context, 0)
                setRecycledViewPool(viewPool)
            }



    }else if (holder.itemViewType == 9) {
        val item = unSplash[position]

        val resources = holder.itemView.context.resources

        val new = Uri.parse(item.urls?.small)












        val autoHeightChild = holder.itemView.placeImage.layoutParams
        autoHeightChild.width = GlobalVals.widthWindow/3
        autoHeightChild.height = (GlobalVals.widthWindow/3).times(item.height!!.div(item.width!!)).toInt()
        holder.itemView.placeImage.elevation = 0F

        Picasso.get().load(new).into(holder.itemView.placeImage)

        //val dimen = Picasso.get().load(item?.name).get().
        // println("================imageAdapter position $position")

       // GlobalVals.test2.add(new)
//        GlobalVals.picassoUnit.add(Picasso.get().load(new))
//        GlobalVals.picassoUnit[position].into(holder.itemView.placeImage)
    }else {

//
//            val new = holder.itemView.user_image.layoutParams
//
//            new.height = 100
//            new.width = 100
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, image:RequestCreator)
    }

    fun setOnItemClickListener(itemClickListener: ProfileAdapter.OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface OnItemLockClickListener{

        fun onItemLongClick(view: View, position: Int)

    }




















    inner class SimplePlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ToroPlayer, View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {


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
            if (itemView.placeImage != null) itemView.placeImage.setOnTouchListener(this)

            // if (itemView.findViewById<Button>(R.id.button_profile_1) != null) itemView.button_profile_1.listener



            if (itemView.findViewById<PlayerView>(R.id.player) != null){
                playerView = itemView.findViewById(R.id.player) as PlayerView
            }else{
                playerView = PlayerView(itemView.context)
            }



        }

        var donw = true

        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
            return mDetector.onTouchEvent(p1).let { result ->
                if (!result &&donw) {

                    donw = false
                    val action = p1?.actionMasked
                    ViewPropertyObjectAnimator.animate(p0?.placeImage).scaleY(.95f).scaleX(.95f)
                        .setDuration(200).start()
                    println("===========profile down")
                    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                        p0?.getParent()?.requestDisallowInterceptTouchEvent(false)
                        println("===========profile up")
                        ViewPropertyObjectAnimator.animate(p0?.placeImage).scaleY(1.0f).scaleX(1.0f)
                            .setDuration(200).start()
                        true
                    } else false
                } else false
            }
//                .let {
//                println("=====inside progfile adapter")
//
//                if (donw) {
//                    donw = false
//                    ViewPropertyObjectAnimator.animate(p0?.placeImage).scaleY(.95f).scaleX(.95f)
//                        .setDuration(200).start()
//                }
//
//
//                val action = p1?.actionMasked
//
//                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
//                    if (true) {
//                        p0?.getParent()?.requestDisallowInterceptTouchEvent(false)
//                        ViewPropertyObjectAnimator.animate(p0?.placeImage).scaleY(1.0f).scaleX(1.0f)
//                            .setDuration(200).start()
//                        // dialog.root.smoothScrollBy(0, delta)
//                    }
//
//
//                }
//
//            }

        }

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

        override fun onClick(view: View) = itemClickListener.onItemClick(itemView, adapterPosition, GlobalVals.picassoUnit[adapterPosition] as RequestCreator)

        internal fun bind(media: Uri) {
            println("========over 15 $media")
            this.mediaUri = media


        }


    }




    fun SetOnLock(onItemLockClickListener: OnItemLockClickListener){

        this.onItemLockClickListener = onItemLockClickListener
    }

    interface speakTouchListener {

        fun onTouch(p0: View?, p1: MotionEvent?)

    }

    fun setTouchListener(onItemTouchListener: speakTouchListener){

        this.onItemTouchListener = onItemTouchListener

    }

}











