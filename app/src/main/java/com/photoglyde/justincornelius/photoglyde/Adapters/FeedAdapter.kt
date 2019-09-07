package com.photoglyde.justincornelius.photoglyde.Adapters


import android.arch.paging.PagedListAdapter
import android.graphics.Color
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.Networking.sameCheckDifficulti
import com.photoglyde.justincornelius.photoglyde.R
import com.squareup.picasso.Picasso
import im.ene.toro.ToroPlayer
import im.ene.toro.ToroUtil
import im.ene.toro.exoplayer.ExoPlayerViewHelper
import im.ene.toro.media.PlaybackInfo
import im.ene.toro.widget.Container
import kotlinx.android.synthetic.main.adapter_row_similar.view.*
import kotlinx.android.synthetic.main.cell_header_profile.view.*
import kotlinx.android.synthetic.main.full_view.view.*
import kotlinx.android.synthetic.main.horizontal_rows.view.*
import kotlinx.android.synthetic.main.layout_header.view.*
import kotlinx.android.synthetic.main.nested_favorites.view.*
import kotlinx.android.synthetic.main.nested_recycler.view.*
import kotlinx.android.synthetic.main.profile_top.view.*
import kotlinx.android.synthetic.main.view_holder_exoplayer_basic.view.*


class
FeedAdapter : PagedListAdapter<CoreUnSplash, FeedAdapter.SimplePlayerViewHolder>(sameCheckDifficulti()) {

    private val viewPool = RecyclerView.RecycledViewPool()

  lateinit var itemClickListener: OnItemClickListener

  private lateinit var itemView:View


    lateinit var onItemLockClickListener: OnItemLockClickListener


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimplePlayerViewHolder {


    when(viewType){

      0 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.full_view, parent, false)

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

        11 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.profile_top, parent, false)

        12 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.nested_recycler, parent, false)



    }




    return SimplePlayerViewHolder(itemView)
  }



    override fun getItemViewType(position: Int): Int {

        var returnPos = 0

        if (GlobalVals.whatsNew) returnPos = 9


        when(getItem(position)?.type){

            TYPE_VIDEO ->{
                returnPos  = 6
            }

            "GRID" ->{
                returnPos  = 1
            }


            TYPE_PHOTO -> {
                returnPos = 0

                if (GlobalVals.whatsNew) returnPos = 9
            }

            GlobalVals.CATEGORY -> {
                returnPos = 0

                if (GlobalVals.whatsNew){
                    println("======return POS")
                    returnPos = 5
                }
            }

            BANNER ->{
                returnPos = 8
            }

            HEADER -> {
                returnPos = 4
            }

            "collection" ->{
                returnPos  = 11
            }

        }


    return returnPos
  }



    override fun onBindViewHolder(holder: SimplePlayerViewHolder, position: Int) {


      //println(" ======now pos $position ${layoutViews.size}")

    if (holder.itemViewType == 1){


        if (GlobalVals.whatsNew){

            val shave = 40

            val item = getItem(position)

            val mContainerTop = holder.itemView.top1Image.layoutParams
            val mContainerBottom = holder.itemView.top2Image.layoutParams
            val mTop1 = holder.itemView.top1.layoutParams as LinearLayout.LayoutParams
            val mTop2 = holder.itemView.top2.layoutParams as LinearLayout.LayoutParams

            val mBottom1 = holder.itemView.bottom1.layoutParams as LinearLayout.LayoutParams
            val mBottom2 = holder.itemView.bottom2.layoutParams as LinearLayout.LayoutParams
            val header = holder.itemView.header.layoutParams as LinearLayout.LayoutParams

            val buttonSeeAll = holder.itemView.button_see_all.layoutParams as LinearLayout.LayoutParams


            //header.setMargins(40, 20, 40, 40)
            val headerText = holder.itemView.header


        //    val headerTextMargin = holder.itemView.header as LinearLayout.LayoutParams
            //headerTextMargin.setMargins(40,40,40,40)

            mContainerTop.height = GlobalVals.heightWindow / 8
            mContainerBottom.height = GlobalVals.heightWindow / 8


            mTop1.width = GlobalVals.widthWindow / 4 - shave
            mTop1.setMargins(0, 0, 8, 8)
            mTop2.width = GlobalVals.widthWindow / 4 - shave
            mTop2.setMargins(8, 0, 0, 8)

            holder.itemView.top1.elevation = 0F
            holder.itemView.top2.elevation = 0F
            holder.itemView.bottom1.elevation = 0F
            holder.itemView.bottom2.elevation = 0F
            holder.itemView.button_see_all.elevation = 0F


            mTop1.height = GlobalVals.widthWindow / 4 - shave
            mTop2.height = GlobalVals.widthWindow / 4 - shave

            holder.itemView.setBackgroundColor(Color.WHITE)
            var image1 = "https://images.unsplash.com/photo-1556227703-ab57dbc6f839?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1231&q=80"
            var image2 = "https://images.unsplash.com/photo-1558556859-f575da01d52f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=700&q=60"

            Picasso.get().load(image1).into(holder.itemView.top1Image)
            Picasso.get().load(image2).into(holder.itemView.top2Image)

           // buttonSeeAll.setMargins(40, 80, 40, 40)
//
//            holder.itemView.top1Image.setBackgroundResource(R.drawable.usej)
//
//            holder.itemView.top2Image.setBackgroundResource(R.drawable.usej)

//            Picasso.get().load(item?.urls?.small).fit().into(holder.itemView.top1Image)
//            Picasso.get().load(item?.urls?.small).fit().into(holder.itemView.top2Image)

            println("======check width ${GlobalVals.widthWindow.toDouble().times(0.57).toInt()}")

            val bottomContainerWidthFirst = GlobalVals.widthWindow / 4 - shave
            val bottomContainerWidthFirstEnd = GlobalVals.widthWindow / 4 - shave


            mBottom1.width = bottomContainerWidthFirst
            mBottom2.width = bottomContainerWidthFirstEnd

            mBottom1.height = GlobalVals.widthWindow / 4 - shave
            mBottom2.height = GlobalVals.widthWindow / 4 - shave

            mBottom1.setMargins(0, 8, 8, 0)
            mBottom2.setMargins(8, 8, 0, 0)

            var image3 = "https://images.unsplash.com/photo-1558525231-069834d4c006?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=700&q=60"
            var image4 = "https://images.unsplash.com/photo-1558543166-d9ee53d5a353?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2098&q=80"

            Picasso.get().load(image3).into(holder.itemView.bottom1Image)
            Picasso.get().load(image4).into(holder.itemView.bottom2Image)

//
//            holder.itemView.bottom1Image.setBackgroundResource(R.drawable.usej)
//            holder.itemView.bottom2Image.setBackgroundResource(R.drawable.usej)

//            Picasso.get().load(item?.urls?.small).fit().into(holder.itemView.bottom1Image)
//            Picasso.get().load(item?.urls?.small).fit().into(holder.itemView.bottom2Image)

            holder.itemView.button_see_all.visibility = View.GONE
            holder.itemView.header.visibility = View.GONE
        }else {


            val mContainerTop = holder.itemView.top1Image.layoutParams
            val mContainerBottom = holder.itemView.top2Image.layoutParams
            val mTop1 = holder.itemView.top1.layoutParams as LinearLayout.LayoutParams
            val mTop2 = holder.itemView.top2.layoutParams as LinearLayout.LayoutParams

            val mBottom1 = holder.itemView.bottom1.layoutParams as LinearLayout.LayoutParams
            val mBottom2 = holder.itemView.bottom2.layoutParams as LinearLayout.LayoutParams
            val header = holder.itemView.header.layoutParams as LinearLayout.LayoutParams

            val buttonSeeAll = holder.itemView.button_see_all.layoutParams as LinearLayout.LayoutParams


            header.setMargins(40, 0, 40, 0)
            val headerText = holder.itemView.header
            //headerText.text = "created by me"

                //  val headerTextMargin = holder.itemView.header as LinearLayout.LayoutParams
            //headerTextMargin.setMargins(40,40,40,40)

            mContainerTop.height = GlobalVals.heightWindow / 5
            mContainerBottom.height = GlobalVals.heightWindow / 5


            mTop1.width = GlobalVals.widthWindow / 2 - 45
            mTop1.setMargins(40, 5, 5, 0)
            mTop2.width = GlobalVals.widthWindow / 2 - 45
            mTop2.setMargins(5, 5, 40, 0)

            holder.itemView.top1.elevation = 0F
            holder.itemView.top2.elevation = 0F
            holder.itemView.bottom1.elevation = 0F
            holder.itemView.bottom2.elevation = 0F
            holder.itemView.button_see_all.elevation = 0F


            mTop1.height = GlobalVals.heightWindow / 5
            mTop2.height = GlobalVals.heightWindow / 5

            holder.itemView.setBackgroundColor(Color.WHITE)



            //buttonSeeAll.setMargins(40, 80, 40, 40)

//            holder.itemView.top1.setBackgroundResource(R.drawable.usej)
//            holder.itemView.top2.setBackgroundResource(R.drawable.usej)

            var image1 = "https://images.unsplash.com/photo-1556227703-ab57dbc6f839?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1231&q=80"
            var image2 = "https://images.unsplash.com/photo-1558556859-f575da01d52f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=700&q=60"

            Picasso.get().load(image1).into(holder.itemView.top1Image)
            Picasso.get().load(image2).into(holder.itemView.top2Image)

            println("======on EXPLORE ${GlobalVals.widthWindow.toDouble().times(0.57).toInt()}")

            val bottomContainerWidthFirst = GlobalVals.widthWindow / 2 - 45
            val bottomContainerWidthFirstEnd = GlobalVals.widthWindow / 2 - 45


            mBottom1.width = bottomContainerWidthFirst
            mBottom2.width = bottomContainerWidthFirstEnd

            mBottom1.height = GlobalVals.heightWindow / 5
            mBottom2.height = GlobalVals.heightWindow / 5

            mBottom1.setMargins(40, 0, 5, 0)
            mBottom2.setMargins(5, 0, 40, 0)

            var image3 = "https://images.unsplash.com/photo-1558525231-069834d4c006?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=700&q=60"
            var image4 = "https://images.unsplash.com/photo-1558543166-d9ee53d5a353?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2098&q=80"

            Picasso.get().load(image3).into(holder.itemView.bottom1Image)
            Picasso.get().load(image4).into(holder.itemView.bottom2Image)

//            holder.itemView.bottom1Image.setBackgroundResource(R.drawable.usej)
//            holder.itemView.bottom2Image.setBackgroundResource(R.drawable.usej)

            holder.itemView.button_see_all.visibility = View.GONE
            holder.itemView.header.visibility = View.GONE

        }



    }else if (holder.itemViewType == 2){


      holder.itemView.list_favorites.isNestedScrollingEnabled = true


     // val bottomLine = holder.itemView.bottom_line as ViewGroup.MarginLayoutParams
//      val headerContainer = holder.itemView.lower_header
//      headerContainer.setMargins(40,0,40,0)







    }else if(holder.itemViewType == 4){

        val target = holder.itemView.header_news
//
//        val targetMargins = holder.itemView.layoutHeader as LinearLayout
//        targetMargins.setMargins(40,0,40,40)

        target.text = holder.itemView.context.getText(R.string.top_videos)

       // println("======profile adapter check ${layoutViews.size} and ${layoutViews}")


    }else if (holder.itemViewType == 5) {

        val item = getItem(position)

        val textCateg = holder.itemView.placeNameH


        val categoryText = holder.itemView.placeNameH


        Picasso.get().load(getItem(position)?.categ_image_uri)
            .fit()
            .into(holder.itemView.placeImageH)


        val resize = holder.itemView.placeCard1.layoutParams
        resize.width = GlobalVals.widthWindow/2
        resize.height = GlobalVals.widthWindow/2

        textCateg.text = item?.categ_name
        textCateg.bringToFront()

        println("======inside feed apdapter")


    //video
    }else if(holder.itemViewType == 6){

        println("=======video is parsed")
        val item = getItem(position)
        val autoHeightChild = holder.itemView.player.layoutParams
        var cardLinear = holder.itemView.card_linear.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val playerMargins = holder.itemView.player.layoutParams as RelativeLayout.LayoutParams
        val exoImageMargins = holder.itemView.exo_image.layoutParams as RelativeLayout.LayoutParams
        val resize = holder.itemView.exo_image.layoutParams
        val playerWrapper = holder.itemView.card_linear.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val anim = holder.itemView.load_video.layoutParams
        holder.itemView.card.elevation = 0f
        var width = GlobalVals.widthWindow

        when {
            GlobalVals.whatsNew -> {
                width = GlobalVals.widthWindow/2
                cardLinear.height = width
                holder.itemView.card.radius = 30f
                holder.itemView.botton_buttons.visibility = View.GONE
                val changeMargins = holder.itemView.card.layoutParams as ConstraintLayout.LayoutParams
                holder.itemView.video_back.setBackgroundResource(R.color.gray)
                exoImageMargins.setMargins(0,0,0,0)
                playerMargins.setMargins(0,0,0,0)
               // changeMargins.setMargins(10,20,10,20)
                playerWrapper.setMargins(30,30,30,30)



                holder.itemView.player.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                holder.itemView.load_video.visibility = View.GONE

            }
            GlobalVals.videoWatch -> {
                holder.itemView.cell_header_bottom.visibility = View.GONE
                width = GlobalVals.widthWindow
                playerMargins.height = width
                holder.itemView.card.radius = 0f

                holder.itemView.botton_buttons.visibility = View.GONE
                holder.itemView.cell_header.visibility = View.VISIBLE
                holder.itemView.video_back.setBackgroundResource(R.color.gray)
                holder.itemView.player.showController()
                val changeMargins = holder.itemView.card.layoutParams as ConstraintLayout.LayoutParams
                holder.itemView.player.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                holder.itemView.player.showController()
                changeMargins.setMargins(0,100,0,100)
                playerMargins.setMargins(0,0,0,0)
                exoImageMargins.setMargins(0,0,0,0)
                playerWrapper.setMargins(0,0,0,10)
                Picasso.get().load(item?.video_image).into(holder.itemView.profile_image)
            }
            else -> {

                holder.itemView.cell_header_bottom.visibility = View.GONE
                width = GlobalVals.widthWindow
                holder.itemView.card.radius = 0f
                holder.itemView.botton_buttons.visibility = View.GONE
                holder.itemView.cell_header.visibility = View.GONE
                val changeMargins = holder.itemView.exo_margin.layoutParams as FrameLayout.LayoutParams
                holder.itemView.player.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                changeMargins.setMargins(0,40,0,40)
                holder.itemView.load_video.visibility = View.GONE
                playerMargins.setMargins(0,0,0,0)
                exoImageMargins.setMargins(0,0,0,0)
                holder.itemView.text_underlay_video.visibility = View.GONE

            }
        }
        val ratio = item?.height?.div(item.width!!)
        val height = width.times(ratio!!)

       // holder.itemView.ani_holder.bringToFront()


        autoHeightChild.height = width
        autoHeightChild.width = width

        resize.height = width
        resize.width = width





        println("The Video URL: " + item.video_uri)

        Picasso.get().load(item.video_image).fit().into(holder.itemView.exo_image)

        holder.itemView.exo_image.bringToFront()

        holder.itemView.text_underlay_video.bringToFront()

        holder.bind(Uri.parse(item.video_uri))

       // holder.itemView.load_video.bringToFront()









        if (GlobalVals.whatsNew) holder.itemView.cell_header.visibility = View.GONE



       // holder.itemView.load_video.progress = 0.1f

//        anim.height = width/3
//        anim.width = width/3

    }else if(holder.itemViewType == 0){

        val item = getItem(position)

        GlobalVals.picassoUnit.add(Picasso.get().load(item?.urls?.regular))
        Picasso.get().load(item?.urls?.small).into(holder.itemView.profile_image)

        val autoHeightChild = holder.itemView.placeImageHere.layoutParams
        autoHeightChild.width = GlobalVals.widthWindow
        val calculatedHeight = (GlobalVals.widthWindow).times(item?.height!!.div(item.width!!)).toInt()
        autoHeightChild.height = calculatedHeight

        val cellHead = holder.itemView.cell_header_layout.layoutParams
        cellHead.width = GlobalVals.widthWindow


        GlobalVals.transitionRatioHeight.add(calculatedHeight)



      //  "/data/user/0/com.photoglyde.justincornelius.photoglyde/files/food.jpg"

        Picasso.get().load(item.urls?.regular).into(holder.itemView.placeImageHere)
        holder.itemView.placeImageHere.bringToFront()

        holder.itemView.text_underlay.bringToFront()

        holder.itemView.textFullView.bringToFront()

        holder.itemView.textFullView.text = item.user?.location




    }else if (holder.itemViewType == 8){


        println("===== inside horizontal")
        GlobalVals.picassoUnit.add("")
        GlobalVals.transitionRatioHeight.add(GlobalVals.heightWindow/2)

            val childLayoutManager = StaggeredGridLayoutManager(1, LinearLayout.HORIZONTAL)

            val header = holder.itemView.horizontal_name
            header.text = holder.itemView.context.getText(R.string.watch_list)
            holder.itemView.rv_child.apply {

                this.layoutManager = childLayoutManager


                this.adapter = BindingHorizontal(holder.itemView.context, 0)
                setRecycledViewPool(viewPool)
            }



    }else if (holder.itemViewType == 9) {

        val item = getItem(position)

        val resources = holder.itemView.context.resources

        val new = Uri.parse(item?.urls?.small)

        val autoHeightChild = holder.itemView.placeImage.layoutParams
        autoHeightChild.width = GlobalVals.widthWindow/2
        autoHeightChild.height = (GlobalVals.widthWindow/2).times(item?.height!!.div(item.width!!)).toInt()
        holder.itemView.placeImage.elevation = 0F

        Picasso.get().load(new).into(holder.itemView.placeImage)
        holder.itemView.lower_description.text = item.user?.location

        holder.itemView.lower_description.setTextColor(Color.WHITE)



    }else if(holder.itemViewType == 11) {
        val shave = 40

        val item = getItem(position)

        val mContainerTop = holder.itemView.top1Image.layoutParams
        val mContainerBottom = holder.itemView.top2Image.layoutParams
        val mTop1 = holder.itemView.top1.layoutParams as LinearLayout.LayoutParams
        val mTop2 = holder.itemView.top2.layoutParams as LinearLayout.LayoutParams

        val mBottom1 = holder.itemView.bottom1.layoutParams as LinearLayout.LayoutParams
        val mBottom2 = holder.itemView.bottom2.layoutParams as LinearLayout.LayoutParams
        val header = holder.itemView.header.layoutParams as LinearLayout.LayoutParams

        val buttonSeeAll = holder.itemView.button_see_all.layoutParams as LinearLayout.LayoutParams


        //header.setMargins(40, 20, 40, 40)
        val headerText = holder.itemView.header


        //    val headerTextMargin = holder.itemView.header as LinearLayout.LayoutParams
        //headerTextMargin.setMargins(40,40,40,40)

        mContainerTop.height = GlobalVals.heightWindow / 8
        mContainerBottom.height = GlobalVals.heightWindow / 8


        mTop1.width = GlobalVals.widthWindow / 4 - shave
        mTop1.setMargins(0, 0, 8, 8)
        mTop2.width = GlobalVals.widthWindow / 4 - shave
        mTop2.setMargins(8, 0, 0, 8)

        holder.itemView.top1.elevation = 0F
        holder.itemView.top2.elevation = 0F
        holder.itemView.bottom1.elevation = 0F
        holder.itemView.bottom2.elevation = 0F
        holder.itemView.button_see_all.elevation = 0F


        mTop1.height = GlobalVals.widthWindow / 4 - shave
        mTop2.height = GlobalVals.widthWindow / 4 - shave

       // holder.itemView.setBackgroundColor(Color.WHITE)
        var image1 = item?.collection?.first
        var image2 = item?.collection?.second

        Picasso.get().load(image1).into(holder.itemView.top1Image)
        Picasso.get().load(image2).into(holder.itemView.top2Image)

        // buttonSeeAll.setMargins(40, 80, 40, 40)
//
//            holder.itemView.top1Image.setBackgroundResource(R.drawable.usej)
//
//            holder.itemView.top2Image.setBackgroundResource(R.drawable.usej)

//            Picasso.get().load(item?.urls?.small).fit().into(holder.itemView.top1Image)
//            Picasso.get().load(item?.urls?.small).fit().into(holder.itemView.top2Image)

        println("======check width ${GlobalVals.widthWindow.toDouble().times(0.57).toInt()}")

        val bottomContainerWidthFirst = GlobalVals.widthWindow / 4 - shave
        val bottomContainerWidthFirstEnd = GlobalVals.widthWindow / 4 - shave


        mBottom1.width = bottomContainerWidthFirst
        mBottom2.width = bottomContainerWidthFirstEnd

        mBottom1.height = GlobalVals.widthWindow / 4 - shave
        mBottom2.height = GlobalVals.widthWindow / 4 - shave

        mBottom1.setMargins(0, 8, 8, 0)
        mBottom2.setMargins(8, 8, 0, 0)

        var image3 = item?.collection?.third
        var image4 = item?.collection?.fourth

        Picasso.get().load(image3).into(holder.itemView.bottom1Image)
        Picasso.get().load(image4).into(holder.itemView.bottom2Image)

//
//            holder.itemView.bottom1Image.setBackgroundResource(R.drawable.usej)
//            holder.itemView.bottom2Image.setBackgroundResource(R.drawable.usej)

//            Picasso.get().load(item?.urls?.small).fit().into(holder.itemView.bottom1Image)
//            Picasso.get().load(item?.urls?.small).fit().into(holder.itemView.bottom2Image)

        holder.itemView.button_see_all.visibility = View.GONE
        holder.itemView.header.visibility = View.GONE

        holder.itemView.textProfile.text = item?.user?.location

        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, data:CoreUnSplash)
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface OnItemLockClickListener{

        fun onItemLongClick(view: View, position: Int, core:CoreUnSplash?)

    }




















    inner class SimplePlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ToroPlayer,
        View.OnClickListener, View.OnLongClickListener {


        private lateinit var playerView: PlayerView
        private var helper: ExoPlayerViewHelper? = null
        private var mediaUri:Uri? = null
       // lateinit var itemClickListener: FeedAdapter.OnItemClickListener
       private var listenerTorro: ToroPlayer.EventListener? = null



        //  lateinit var onItemLockClickListener: ProfileAdapter.OnItemLockClickListener

        init {

            //  if (itemView.placeImage != null) itemView.placeImage.setOnLongClickListener(this)
            if (itemView.placeImageH != null) itemView.placeImageH.setOnClickListener(this)
            if (itemView.placeImageHere != null) itemView.placeImageHere.setOnLongClickListener(this)
            if (itemView.placeImageHere != null) itemView.placeImageHere.setOnClickListener(this)
            if (itemView.placeImage != null) itemView.placeImage.setOnLongClickListener(this)
            if (itemView.placeImage != null) itemView.placeImage.setOnClickListener(this)
            // if (itemView.placeImage != null) itemView.placeImage.setOnTouchListener(this)

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

        override fun onLongClick(p0: View?): Boolean {

            helper?.volume = 0.70f

            onItemLockClickListener.onItemLongClick(itemView, adapterPosition, getItem(position))
println("=====lock click")
            return true
        }


        override fun initialize(container: Container, playbackInfo: PlaybackInfo) {
            if (helper == null) {


//            val trackSelector = DefaultTrackSelector()
//            val loadControl = DefaultLoadControl()
//            val renderersFactory = DefaultRenderersFactory(this)

                itemView.exo_image.bringToFront()


                helper = ExoPlayerViewHelper(this, mediaUri!!, null, Configuration.config1!!)
                    //container.player.hideController()





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




            return playerView
        }

        override fun getCurrentPlaybackInfo(): PlaybackInfo {

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

            println("=========where is the video ${playerView.player.currentPosition}")


            if (helper != null){
                println("========over 9")
                helper!!.pause()

            }
        }

        override fun isPlaying(): Boolean {

          //  println("===========the video is playing")


            // itemView.exo_image.visibility = View.GONE
            //  println("========over 10 ${helper != null} and ${helper!!.isPlaying}")
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
          //  println("========over 13 ${ToroUtil.visibleAreaOffset(this, itemView.parent)}")
            return ToroUtil.visibleAreaOffset(this, itemView.parent) >= 0.85
        }

        override fun getPlayerOrder(): Int {
//            println("========over 14 $adapterPosition")
            return adapterPosition
        }

        override fun onClick(view: View) {
            helper?.volume = 0.50f
            itemClickListener.onItemClick(itemView, adapterPosition, getItem(position)!!)
        }

        internal fun bind(media: Uri) {

            this.mediaUri = media


        }


    }




    fun SetOnLock(onItemLockClickListener: OnItemLockClickListener){

        this.onItemLockClickListener = onItemLockClickListener
    }







}











