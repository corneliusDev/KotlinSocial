package com.photoglyde.justincornelius.photoglyde.Adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.photoglyde.justincornelius.photoglyde.Data.Data
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals
import com.photoglyde.justincornelius.photoglyde.Data.ImageClass
import com.photoglyde.justincornelius.photoglyde.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.full_view.view.*
import kotlinx.android.synthetic.main.horizontal_rows.view.*
import kotlinx.android.synthetic.main.nested_recycler.view.*
import kotlinx.android.synthetic.main.news_layout.view.*
import kotlinx.android.synthetic.main.vertical_rows.view.*


class BindingVertical(private var context: Context, private var userImageData: ArrayList<ImageClass>, private var viewType:Int) : RecyclerView.Adapter<BindingVertical.ViewHolder>() {

  lateinit var itemClickListener: OnItemClickListener
  private val viewPool = RecyclerView.RecycledViewPool()

  override fun getItemCount() : Int
  {

    var returnCount = 0

    if (viewType == 0) {
      if (userImageData?.size != null) returnCount = userImageData!!.size
    }else{
      returnCount = 1
    }


    return returnCount
  }


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


    var itemView = LayoutInflater.from(parent.context).inflate(R.layout.news_layout, parent, false)

    when(viewType){


        0 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.news_layout, parent, false)

        1 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.nested_recycler, parent, false)

        2 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.full_view, parent, false)



    }




    return ViewHolder(itemView)
  }

  override fun getItemViewType(position: Int): Int {

println("=====this is the view type $viewType")

    return viewType
  }



  override fun onBindViewHolder(holder: ViewHolder, position: Int) {


    when(holder.itemViewType){

    0-> {

      val item = userImageData[position]

      val image = item.urls?.regular
      val width = item.width?.toDouble()
      val height = item.height?.toDouble()

//      val title = item?.username
      val target = holder.itemView.imageHere1
      val imageTarget = holder.itemView.imageHere1
      // val abstract = holder.itemView.abstracts

      val windowWidth = GlobalVals.widthWindow.toDouble() / 3
      val windowHeight = windowWidth

      val ratio = height?.div(width!!)
      println("=======here is the ratio $ratio and height $height and width $width")

      val finalWidth = windowWidth.toInt() - 10
      val finalHeight = windowHeight.times(ratio!!).toInt() - 10

      println("=======here is the ratio two $ratio and height $finalHeight and width $finalWidth")

      // var bindingWrapper = holder.itemView.text_wrapper


      val resize = target.layoutParams as LinearLayout.LayoutParams


      // abstract.text = item?.description



      println(
        "=========api dimensions ${finalWidth} and ${finalHeight} and ${finalHeight.div(finalWidth)} and ${GlobalVals.widthWindow} and ${GlobalVals.widthWindow.times(
          finalHeight.div(finalWidth)
        )} and ${GlobalVals.widthWindow.times(finalHeight.div(finalWidth)).toInt()}"
      )


      resize.width = finalWidth
      resize.height = finalHeight


      if (position == 0) {
        resize.setMargins(1, 1, 1, 1)
      } else {
        resize.setMargins(1, 1, 1, 1)
      }



//      println(
//        "we are im the news apadper ${title} widthWindow ${GlobalVals.widthWindow} ratio $ratio width ${GlobalVals.widthWindow / 2} height ${GlobalVals.widthWindow.times(
//          ratio!!.toInt()
//        )}"
//      )

      Picasso.get().load(image).resize(finalWidth, finalHeight).into(imageTarget)
      // holder.itemView.title.layoutParams.width = finalWidth
      //  holder.itemView.title.text = title

      println("=============we are im the news apadper ${image}")
    }


      1 -> {
        val childLayoutManager = StaggeredGridLayoutManager(3, LinearLayout.VERTICAL)
        holder.itemView.rv_child.apply {

          this.layoutManager = childLayoutManager


          this.adapter = BindingHorizontal(holder.itemView.context, 0)
          setRecycledViewPool(viewPool)
        }

      }

        2 ->{

//            val item = GlobalVals.upSplash[position]
//            val resources = holder.itemView.context.resources
//            val description = item.description
//            val new = Uri.parse(item.urls?.small)
//            val autoHeightChild = holder.itemView.placeImageHere.layoutParams
//            autoHeightChild.width = GlobalVals.widthWindow
//            autoHeightChild.height = (GlobalVals.widthWindow).times(item.height!!.div(item.width!!)).toInt()
//           // holder.itemView.top_desc.text = description
//            Picasso.get().load(new).into(holder.itemView.placeImageHere)

        }




    }



  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
     // itemView.placeHolderV.setOnClickListener(this)
    }

    override fun onClick(view: View) = itemClickListener.onItemClick(itemView, adapterPosition)
  }

  interface OnItemClickListener {
    fun onItemClick(view: View, position: Int)
  }

  fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
    this.itemClickListener = itemClickListener
  }
}