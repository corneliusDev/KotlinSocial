package com.photoglyde.justincornelius.photoglyde.UI.adapter


import androidx.paging.PagedListAdapter
import android.graphics.Color
import android.net.Uri
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.photoglyde.justincornelius.photoglyde.data.*
import com.photoglyde.justincornelius.photoglyde.utilities.ItemsComparison
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
import kotlinx.android.synthetic.main.nested_recycler.view.*
import kotlinx.android.synthetic.main.profile_top.view.*
import kotlinx.android.synthetic.main.view_holder_exoplayer_basic.view.*
import kotlin.coroutines.coroutineContext


class FeedAdapter : PagedListAdapter<CoreData, FeedAdapter.SimplePlayerViewHolder>(ItemsComparison()) {

    private val viewPool = androidx.recyclerview.widget.RecyclerView.RecycledViewPool()

    lateinit var itemClickListener: OnItemClickListener

    private lateinit var itemView:View

    lateinit var onItemLockClickListener: OnItemLockClickListener


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimplePlayerViewHolder {

    when(viewType){

        0 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.full_view, parent, false)

        5 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.horizontal_rows, parent, false)

        6 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_exoplayer_basic, parent, false)

        9 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_row_similar, parent, false)

        10 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.button_profile, parent, false)

        11 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.profile_top, parent, false)

        12 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.nested_recycler, parent, false)

    }
    return SimplePlayerViewHolder(itemView)
  }


    override fun getItemViewType(position: Int): Int {

        var returnPos = 0

        if (GlobalValues.whatsNew) returnPos = 9


        when(getItem(position)?.type){

            TYPE_VIDEO ->{
                returnPos  = 6
            }

            TYPE_PHOTO -> {
                returnPos = 0

                if (GlobalValues.whatsNew) returnPos = 9
            }

            CATEGORY -> {
                returnPos = 0
                if (GlobalValues.whatsNew) returnPos = 5

            }



            HEADER -> {
                returnPos = 4
            }

            COLLECTION ->{
                returnPos  = 11
            }

        }


    return returnPos
  }



    override fun onBindViewHolder(holder: SimplePlayerViewHolder, position: Int) {


        if (holder.itemViewType == 5) {

        val item = getItem(position)
        val textCateg = holder.itemView.placeNameH
        Picasso.get().load(getItem(position)?.categ_image_uri).fit().into(holder.itemView.placeImageH)
        val resize = holder.itemView.placeCard1.layoutParams
        resize.width = GlobalValues.widthWindow/2
        resize.height = GlobalValues.widthWindow/2
        textCateg.text = item?.categ_name
        textCateg.bringToFront()

    }else if(holder.itemViewType == 6){

        val item = getItem(position)
        val autoHeightChild = holder.itemView.player.layoutParams
        val cardLinear = holder.itemView.card_linear.layoutParams as androidx.recyclerview.widget.StaggeredGridLayoutManager.LayoutParams
        val playerMargins = holder.itemView.player.layoutParams as RelativeLayout.LayoutParams
        val exoImageMargins = holder.itemView.exo_image.layoutParams as RelativeLayout.LayoutParams
        val resize = holder.itemView.exo_image.layoutParams
        val playerWrapper = holder.itemView.card_linear.layoutParams as androidx.recyclerview.widget.StaggeredGridLayoutManager.LayoutParams
        holder.itemView.card.elevation = 0f
        var width = GlobalValues.widthWindow

        when {
            GlobalValues.whatsNew -> {
                width = GlobalValues.widthWindow/2
                cardLinear.height = width
                val changeMargins = holder.itemView.card.layoutParams as ConstraintLayout.LayoutParams
                holder.itemView.video_back.setBackgroundResource(R.color.gray)
                holder.itemView.text_underlay_video.visibility = View.GONE
                holder.itemView.player.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL

            }
            GlobalValues.videoWatch -> {
                width = GlobalValues.widthWindow
                playerMargins.height = width
                holder.itemView.card.radius = 0f
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
                holder.itemView.text_underlay_video.visibility = View.GONE
            }
            else -> {
                width = GlobalValues.widthWindow
                holder.itemView.card.radius = 0f
                holder.itemView.cell_header.visibility = View.GONE
                val changeMargins = holder.itemView.exo_margin.layoutParams as FrameLayout.LayoutParams
                holder.itemView.player.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                changeMargins.setMargins(0,40,0,40)
                playerMargins.setMargins(0,0,0,0)
                exoImageMargins.setMargins(0,0,0,0)
                holder.itemView.text_underlay_video.visibility = View.GONE
            }
        }
        val ratio = item?.height?.div(item.width!!)
        val height = width.times(ratio!!)

        autoHeightChild.height = width
        autoHeightChild.width = width

        resize.height = width
        resize.width = width

        Picasso.get().load(item.video_image).fit().into(holder.itemView.exo_image)

        holder.itemView.exo_image.bringToFront()

        holder.itemView.text_underlay_video.bringToFront()

        holder.bind(Uri.parse(item.video_uri))

        if (GlobalValues.whatsNew) holder.itemView.cell_header.visibility = View.GONE

    }else if(holder.itemViewType == 0){

        val item = getItem(position)

        GlobalValues.picassoUnit.add(Picasso.get().load(item?.urls?.regular))
        Picasso.get().load(item?.urls?.small).into(holder.itemView.profile_image)

        val autoHeightChild = holder.itemView.placeImageHere.layoutParams
        autoHeightChild.width = GlobalValues.widthWindow
        val calculatedHeight = (GlobalValues.widthWindow).times(item?.height!!.div(item.width!!)).toInt()
        autoHeightChild.height = calculatedHeight

        val cellHead = holder.itemView.cell_header_layout.layoutParams
        cellHead.width = GlobalValues.widthWindow

        GlobalValues.transitionRatioHeight.add(calculatedHeight)

        Picasso.get().load(item.urls?.regular).into(holder.itemView.placeImageHere)
        holder.itemView.placeImageHere.bringToFront()

        holder.itemView.text_underlay.bringToFront()

        holder.itemView.textFullView.bringToFront()

        holder.itemView.textFullView.text = item.user?.location






    }else if (holder.itemViewType == 9) {

        val item = getItem(position)

        val new = Uri.parse(item?.urls?.small)

        val autoHeightChild = holder.itemView.placeImage.layoutParams
        autoHeightChild.width = GlobalValues.widthWindow/2
        autoHeightChild.height = (GlobalValues.widthWindow/2).times(item?.height!!.div(item.width!!)).toInt()
        holder.itemView.placeImage.elevation = 0F

        if (GlobalValues.cameFromExa) holder.itemView.transitionName = "expanded_tag"

        Picasso.get().load(new).into(holder.itemView.placeImage)
        holder.itemView.lower_description.text = item.user?.location

        holder.itemView.lower_description.setTextColor(Color.WHITE)



    }else if(holder.itemViewType == 11) {
        val shave = 30

        val item = getItem(position)

        val mContainerTop = holder.itemView.top1Image.layoutParams
        val mContainerBottom = holder.itemView.top2Image.layoutParams
        val mTop1 = holder.itemView.top1.layoutParams as LinearLayout.LayoutParams
        val mTop2 = holder.itemView.top2.layoutParams as LinearLayout.LayoutParams

        val mBottom1 = holder.itemView.bottom1.layoutParams as LinearLayout.LayoutParams
        val mBottom2 = holder.itemView.bottom2.layoutParams as LinearLayout.LayoutParams

        mContainerTop.height = GlobalValues.heightWindow / 8
        mContainerBottom.height = GlobalValues.heightWindow / 8


        mTop1.width = GlobalValues.widthWindow / 4 - shave
        mTop1.setMargins(0, 0, 8, 8)
        mTop2.width = GlobalValues.widthWindow / 4 - shave
        mTop2.setMargins(8, 0, 0, 8)

        holder.itemView.top1.elevation = 0F
        holder.itemView.top2.elevation = 0F
        holder.itemView.bottom1.elevation = 0F
        holder.itemView.bottom2.elevation = 0F
        holder.itemView.button_see_all.elevation = 0F


        mTop1.height = GlobalValues.widthWindow / 4 - shave
        mTop2.height = GlobalValues.widthWindow / 4 - shave

       // holder.itemView.setBackgroundColor(Color.WHITE)
        var image1 = item?.collection?.first
        var image2 = item?.collection?.second

        Picasso.get().load(image1).into(holder.itemView.top1Image)
        Picasso.get().load(image2).into(holder.itemView.top2Image)

        val bottomContainerWidthFirst = GlobalValues.widthWindow / 4 - shave
        val bottomContainerWidthFirstEnd = GlobalValues.widthWindow / 4 - shave


        mBottom1.width = bottomContainerWidthFirst
        mBottom2.width = bottomContainerWidthFirstEnd

        mBottom1.height = GlobalValues.widthWindow / 4 - shave
        mBottom2.height = GlobalValues.widthWindow / 4 - shave

        mBottom1.setMargins(0, 8, 8, 0)
        mBottom2.setMargins(8, 8, 0, 0)

        var image3 = item?.collection?.third
        var image4 = item?.collection?.fourth

        Picasso.get().load(image3).into(holder.itemView.bottom1Image)
        Picasso.get().load(image4).into(holder.itemView.bottom2Image)

        holder.itemView.button_see_all.visibility = View.GONE
        holder.itemView.header.visibility = View.GONE

        holder.itemView.textProfile.text = item?.user?.location

        }
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun SetOnLock(onItemLockClickListener: OnItemLockClickListener){
        this.onItemLockClickListener = onItemLockClickListener
    }



    inner class SimplePlayerViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), ToroPlayer,
        View.OnClickListener, View.OnLongClickListener {

        private var playerView: PlayerView
        private var helper: ExoPlayerViewHelper? = null
        private var mediaUri:Uri? = null
        private var listenerTorro: ToroPlayer.EventListener? = null


        init {

            if (itemView.placeImageH != null) itemView.placeImageH.setOnClickListener(this)
            if (itemView.placeImageHere != null) itemView.placeImageHere.setOnLongClickListener(this)
            if (itemView.placeImageHere != null) itemView.placeImageHere.setOnClickListener(this)
            if (itemView.placeImage != null) itemView.placeImage.setOnLongClickListener(this)
            if (itemView.placeImage != null) itemView.placeImage.setOnClickListener(this)

            if (itemView.top1Image != null) itemView.top1Image.setOnClickListener(this)
            if (itemView.top2Image != null) itemView.top2Image.setOnClickListener(this)
            if (itemView.bottom1Image != null) itemView.bottom1Image.setOnClickListener(this)
            if (itemView.bottom1Image != null) itemView.bottom2Image.setOnClickListener(this)


            if (itemView.findViewById<PlayerView>(R.id.player) != null){
                playerView = itemView.findViewById(R.id.player) as PlayerView
                itemView.player.setOnClickListener(this)


               //
                if (itemView.findViewById<PlayerView>(R.id.exo_image) != null) {
                    itemView.exo_image.setOnClickListener(this)

                }

            }else{
                playerView = PlayerView(itemView.context)
            }
        }

        override fun onLongClick(p0: View?): Boolean {
            helper?.volume = 0.70f
            onItemLockClickListener.onItemLongClick(itemView, adapterPosition, getItem(position))
            return true
        }


        override fun initialize(container: Container, playbackInfo: PlaybackInfo) {

            if (helper == null) {

                itemView.exo_image.bringToFront()

                helper = ExoPlayerViewHelper(this, mediaUri!!, null, Configuration.config1!!)





            }else{

            }

            if (GlobalValues.whatsNew){
                println("CONTROLER$$$$$$$$$$$$")
                playerView.useController = false }


            if (listenerTorro == null) {

                listenerTorro = object : ToroPlayer.EventListener{
                    override fun onBuffering() {

                        itemView.exo_image.alpha = 1f

                    }

                    override fun onFirstFrameRendered() {

                    }

                    override fun onPlaying() {

                        itemView.exo_image.alpha = 0.0f

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

        override fun onClick(view: View) {
            helper?.volume = 0.50f
            itemClickListener.onItemClick(itemView, adapterPosition, getItem(position)!!)
        }

        internal fun bind(media: Uri) {
            this.mediaUri = media
        }

    }









}











