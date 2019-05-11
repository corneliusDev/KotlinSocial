package com.photoglyde.justincornelius.photoglyde.Adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.v7.graphics.Palette
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.photoglyde.justincornelius.photoglyde.Data.Data
import com.photoglyde.justincornelius.photoglyde.Data.FoodNames
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals
import com.photoglyde.justincornelius.photoglyde.Data.UserInfo
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.R.id.card
import com.photoglyde.justincornelius.photoglyde.R.id.placeName
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.horizontal_rows.view.*
import kotlinx.android.synthetic.main.horizontal_rows_circular.view.*
import kotlinx.android.synthetic.main.horizontal_rows_explore_categs.view.*
import kotlinx.android.synthetic.main.news_layout.view.*
import kotlinx.android.synthetic.main.news_layout_copy.view.*
import kotlinx.android.synthetic.main.vertical_rows.view.*


class BindingHorizontal(private var context: Context, private var layoutViews:Int) : RecyclerView.Adapter<BindingHorizontal.ViewHolder>() {

  lateinit var itemClickListener: OnItemClickListener

  override fun getItemCount() = GlobalVals.listCateg.size



  override fun getItemViewType(position: Int): Int {

    var itemPosition = layoutViews

      when(layoutViews){

          0 ->{
              itemPosition = 0
          }

          5 -> {
              itemPosition = 5
          }

      }

    println("======current profile position and ${layoutViews} and $itemPosition and $position")

    return itemPosition
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    var itemView = LayoutInflater.from(parent.context).inflate(R.layout.horizontal_rows, parent, false)

    when(viewType){



      0 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.horizontal_rows_circular, parent, false)

        5 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.horizontal_rows, parent, false)

        6 -> itemView = LayoutInflater.from(parent.context).inflate(R.layout.horizontal_rows_explore_categs, parent, false)


    }





    return ViewHolder(itemView)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    when(holder.itemViewType){
      0 ->{


          val item = GlobalVals.listCateg[position]

          val textCateg = holder.itemView.placeNameH2
          println("=====inside categ")

          textCateg.text = "Womans Style"

          Picasso.get().load(item?.categ_image_uri)
              .fit()
              .into(holder.itemView.circle)


          val resize = holder.itemView.placeCard12.layoutParams
          resize.width = GlobalVals.widthWindow/4
          resize.height = GlobalVals.widthWindow/3

          textCateg.text = item?.categ_name
          textCateg.bringToFront()


      }


      1 -> {


       val item = GlobalVals.listCateg[position]

        val image = item?.urls?.thumb
        val width = item?.width?.toDouble()
        val height = item?.height?.toDouble()

        val title = item?.username
        val target = holder.itemView.imageHere1
        val imageTarget = holder.itemView.imageHere1
       // val abstract = holder.itemView.abstracts

        val windowWidth = GlobalVals.widthWindow.toDouble()/3
        val windowHeight = windowWidth/2

        val ratio = height?.div(width!!)!!

        val finalWidth = windowWidth.toInt() - 10
        val finalHeight = windowHeight.times(ratio).toInt() - 10

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


          if (position ==0){
              resize.setMargins(1,1,1,0)
          }else{
              resize.setMargins(1,1,1,0)
          }



        println(
          "we are im the news apadper ${title} widthWindow ${GlobalVals.widthWindow} ratio $ratio width ${GlobalVals.widthWindow / 2} height ${GlobalVals.widthWindow.times(
            ratio!!.toInt()
          )}"
        )

        Picasso.get().load(image).resize(finalWidth, finalHeight).into(imageTarget)
       // holder.itemView.title.layoutParams.width = finalWidth
      //  holder.itemView.title.text = title

        println("=============we are im the news apadper ${image}")
      }


        5 ->{


                    val item = GlobalVals.listCateg[position]

                    val textCateg = holder.itemView.placeNameH
                    println("=====inside categ")

                    textCateg.text = "Womans Style"

                    Picasso.get().load(item?.categ_image_uri)
                        .fit()
                        .into(holder.itemView.placeImageH)


                    val resize = holder.itemView.placeCard1.layoutParams
                    resize.width = GlobalVals.widthWindow/2
                    resize.height = GlobalVals.widthWindow/2

                    textCateg.text = item?.categ_name
                    textCateg.bringToFront()







        }


        6 ->{


            val item = GlobalVals.listCateg[position]

            val textCateg = holder.itemView.placeNameHExplore

            textCateg.setTextColor(Color.DKGRAY)
            println("=====inside categ")

            textCateg.textSize = 20f

//                    Picasso.get().load(item?.categ_image_uri)
//                        .fit()
//                        .into(holder.itemView.placeImageH)

           // holder.itemView.placeImageHExplore.setBackgroundResource(R.color.light_gray)

            val card_params = holder.itemView.placeCardExplore



            card_params.radius = 80f

            val card_layout = card_params as CardView





            val resize = holder.itemView.placeCardExplore.layoutParams
            // resize.width = GlobalVals.widthWindow/3
            resize.height = GlobalVals.widthWindow/6


            textCateg.text = item?.categ_name
            textCateg.bringToFront()

        }





    }



  //    Palette.from(photo).generate { palette ->

   //   }

  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    init {
      //itemView.placeHolder.setOnClickListener(this)

        if (itemView.placeImageH != null){
            println("===== lsitiner is adapted")


            itemView.placeImageH.setOnClickListener(this)
        }

    }

    override fun onClick(view: View) = itemClickListener.onItemClick(itemView, adapterPosition)

  }

  interface OnItemClickListener {
    fun onItemClick(view: View, position: Int){

    }

  }

  fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
    this.itemClickListener = itemClickListener
  }
}