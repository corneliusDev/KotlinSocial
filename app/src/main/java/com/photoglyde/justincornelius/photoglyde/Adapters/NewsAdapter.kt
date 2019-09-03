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
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals
import com.photoglyde.justincornelius.photoglyde.Data.GrabImageData
import com.photoglyde.justincornelius.photoglyde.MainActivity
import com.photoglyde.justincornelius.photoglyde.Networking.NYCTimesDataResponse
import com.photoglyde.justincornelius.photoglyde.Networking.NYCTimesResponse
import com.photoglyde.justincornelius.photoglyde.Networking.sameCheckDifficulti
import com.photoglyde.justincornelius.photoglyde.Networking.sameNewsCheckDifficulti
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.R.id.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_row.view.*
import kotlinx.android.synthetic.main.item1.view.*
import kotlinx.android.synthetic.main.nested_categories.view.*
import kotlinx.android.synthetic.main.nested_recycler.view.*
import kotlinx.android.synthetic.main.news_layout.view.*
import kotlinx.android.synthetic.main.news_layout_copy.view.*
import kotlinx.android.synthetic.main.profile_top.*
import kotlinx.android.synthetic.main.profile_top.view.*


class NewsAdapter : PagedListAdapter<NYCTimesDataResponse, NewsAdapter.ViewHolder>(sameNewsCheckDifficulti()), StateSaver {

  private val set = ConstraintSet()
  lateinit var itemClickListener: OnItemClickListener
  lateinit var onItemLockClickListener: OnItemLockClickListener
 // lateinit var onItemTouchListener: ImageAdapter.speakTouchListener
  var count = 0


    override fun getItemViewType(position: Int): Int {

    println("my position $position")
    if (position == 40) {
      return 2
    }
    if (position == 41) {
      return 1
    }
    return 0


  }

  override fun saveState(adapterPosition: Int, itemPosition: Int, lastItemAngleShift: Double) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    var view: View
    // if (viewType != 4) {

    println("==================view holder ${GlobalVals.isExpanded}")


    if (viewType == 1) {
      view = LayoutInflater.from(parent.context).inflate(R.layout.profile_top, parent, false)
    } else if (viewType == 2) {
      view = LayoutInflater.from(parent.context).inflate(R.layout.nested_categories, parent, false)
//      RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//      view.setLayoutParams(lp);
    } else if(viewType == 3) {

      view = LayoutInflater.from(parent.context).inflate(R.layout.nested_recycler, parent, false)

    }else{

        view = LayoutInflater.from(parent.context).inflate(R.layout.news_layout, parent, false)

      }


    //view = LayoutInflater.from(parent.context).inflate(R.layout.news_layout, parent, false)


    return ViewHolder(view)
  }


  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = getItem(position)
    if (holder.itemViewType == 1) {


      val image = item?.urlToImage
      val mContainerTop = holder.itemView.top1Image.layoutParams
      val mContainerBottom = holder.itemView.top2Image.layoutParams
      val mTop1 = holder.itemView.top1.layoutParams as LinearLayout.LayoutParams
      val mTop2 = holder.itemView.top2.layoutParams as LinearLayout.LayoutParams

      val mBottom1 = holder.itemView.bottom1.layoutParams as LinearLayout.LayoutParams
      val mBottom2 = holder.itemView.bottom2.layoutParams as LinearLayout.LayoutParams

      val buttonSeeAll = holder.itemView.button_see_all.layoutParams as LinearLayout.LayoutParams
      val header = holder.itemView.header.layoutParams as LinearLayout.LayoutParams
      header.setMargins(0, 0, 0, 0)


      mContainerTop.height = GlobalVals.heightWindow / 5
      mContainerBottom.height = GlobalVals.heightWindow / 5


      mTop1.width = GlobalVals.widthWindow / 2 - 55
      mTop1.setMargins(40, 5, 15, 5)
      mTop2.width = GlobalVals.widthWindow / 2 - 55
      mTop2.setMargins(15, 5, 40, 5)

      holder.itemView.top1.elevation = 0F
      holder.itemView.top2.elevation = 0F
      holder.itemView.bottom1.elevation = 0F
      holder.itemView.bottom2.elevation = 0F
      holder.itemView.button_see_all.elevation = 0F

      buttonSeeAll.setMargins(40, 50, 40, 50)


//      holder.itemView.top1.setBackgroundResource(R.drawable.usej)
//      holder.itemView.top2.setBackgroundResource(R.drawable.usej)

//
//      GlobalVals.picassoUnit[0].into(holder.itemView.top1Image)
//      GlobalVals.picassoUnit[1].into(holder.itemView.top2Image)
//
//      GlobalVals.picassoUnit[2].resize(GlobalVals.widthWindow / 2, GlobalVals.widthWindow / 2)
//        .into(holder.itemView.bottom1Image)
//      GlobalVals.picassoUnit[4].resize(GlobalVals.widthWindow / 2, GlobalVals.widthWindow / 2)
//        .into(holder.itemView.bottom2Image)




      println("======check width ${GlobalVals.widthWindow.toDouble().times(0.57).toInt()}")

      val bottomContainerWidthFirst = GlobalVals.widthWindow / 2 - 55
      val bottomContainerWidthFirstEnd = GlobalVals.widthWindow / 2 - 55


      mBottom1.width = bottomContainerWidthFirst
      mBottom2.width = bottomContainerWidthFirstEnd

      mBottom1.height = GlobalVals.heightWindow / 5
      mBottom2.height = GlobalVals.heightWindow / 5

      mBottom1.setMargins(40, 5, 15, 5)
      mBottom2.setMargins(15, 5, 40, 5)


    } else if (holder.itemViewType == 0) {

//
//      val resources = holder.itemView.context.resources
//
//      val image = item?.urlToImage
//      //val width = item?.mediaa?.first()?.media_metadata?.last()?.width?.toDouble()
//      //val height = item?.mediaa?.first()?.media_metadata?.last()?.height?.toDouble()
//
//      val title = item?.title
//      val target = holder.itemView.stock_image
//      val imageTarget = holder.itemView.imageHere
//      val abstract = holder.itemView.abstracts
//
//      val windowWidth = GlobalVals.widthWindow.toDouble().div(1.05)
//      val windowHeight = GlobalVals.widthWindow.toDouble().div(1.4)
//
//      val ratio = windowHeight.div(windowWidth)
//
//      val finalWidth = windowWidth.toInt() - 30
//      val finalHeight = windowHeight.times(ratio).toInt() - 30
//
//      val resize = target.layoutParams as LinearLayout.LayoutParams
//
//
//      abstract.text = item?.description
//
//
//
//      println(
//        "=========api dimensions ${finalWidth} and ${finalHeight} and ${finalHeight.div(finalWidth)} and ${GlobalVals.widthWindow} and ${GlobalVals.widthWindow.times(
//          finalHeight.div(finalWidth)
//        )} and ${GlobalVals.widthWindow.times(finalHeight.div(finalWidth)).toInt()}"
//      )
//
//
//      //resize.height = GlobalVals.widthWindow.toDouble().times(ratio)
//
//      println(
//        "we are im the news apadper ${title} widthWindow ${GlobalVals.widthWindow} ratio $ratio width ${GlobalVals.widthWindow / 2} height ${GlobalVals.widthWindow.times(
//          ratio!!.toInt()
//        )}"
//      )
//
//      Picasso.get().load(image).fit().into(imageTarget)
//      holder.itemView.title.layoutParams.width = finalWidth
//      holder.itemView.title.text = title
//
//      println("=============we are im the news apadper ${image}")


    } else if (holder.itemViewType == 2) {


      holder.itemView.categ1.text = GlobalVals.categoryList[0]
      holder.itemView.categ2.text = GlobalVals.categoryList[1]
      holder.itemView.categ3.text = GlobalVals.categoryList[2]
      holder.itemView.categ4.text = GlobalVals.categoryList[3]
      holder.itemView.categ5.text = GlobalVals.categoryList[4]
      holder.itemView.categ6.text = GlobalVals.categoryList[5]


    }


    // val autoHeightChild = holder.itemView.placeImage.layoutParams
    // autoHeightChild.width = GlobalVals.widthWindow/2

    //GlobalVals.test2.add(new)

//  GlobalVals.picassoUnit.add(Picasso.get().load(GlobalVals.test2[position]))
//
//    GlobalVals.picassoUnit[position].into(holder.itemView.placeImage)

  }


  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener,
    View.OnLongClickListener {

    init {

     // itemView.imageHere.setOnClickListener(this)
     // itemView.imageHere.setOnLongClickListener(this)

    }


    override fun onClick(view: View) = itemClickListener.onItemClick(itemView, adapterPosition)
    override fun onLongClick(p0: View?): Boolean {

      onItemLockClickListener.onItemLongClick(itemView, adapterPosition)
      return true
    }

  }

  interface OnItemClickListener {
    fun onItemClick(view: View, position: Int)

  }

   fun setOnItemClickListener(itemClickListener: NewsAdapter.OnItemClickListener) {
    this.itemClickListener = itemClickListener
  }


  interface speakTouchListener {

    fun onTouch(p0: View?, p1: MotionEvent?)

  }

  interface OnItemLockClickListener {

    fun onItemLongClick(view: View, position: Int)

  }
}


