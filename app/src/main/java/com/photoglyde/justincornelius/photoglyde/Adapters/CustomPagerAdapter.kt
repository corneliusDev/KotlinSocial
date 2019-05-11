package com.photoglyde.justincornelius.photoglyde.Adapters

import android.graphics.Color
import com.nshmura.recyclertablayout.RecyclerTabLayout


import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.photoglyde.justincornelius.photoglyde.R
import kotlinx.android.synthetic.main.layout_custom_view01_tab.view.*

/**
 * Created by Shinichi Nishimura on 2015/07/22.
 */
class CustomPagerAdapter(viewPager: ViewPager) :
    RecyclerTabLayout.Adapter<CustomPagerAdapter.ViewHolder>(viewPager) {

    private val mAdapater: MyPagerAdapter

    init {
        mAdapater = mViewPager.adapter as MyPagerAdapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_custom_view01_tab, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val colorItem = mAdapater.getPageTitle(position)
        val button = holder.itemView.buttonPager

        button.text = colorItem



        val LayoutParams  =  LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )



        if(position == 0){
            LayoutParams.setMargins(30,0,40,0)
        }else if(position == 1){
            LayoutParams.setMargins(50,0,50,0)
        }else{
            LayoutParams.setMargins(40,0,30,0)
        }
        button.setBackgroundColor(Color.WHITE)
        button.elevation = 0f
        button.layoutParams = LayoutParams


        //holder.title.setText("hello")
        //holder.color.setBackgroundColor(Color.BLACK)


        val name = SpannableString(colorItem)
        val background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.rounded_button_post_list_option)
        if (position == currentIndicatorPosition) {
         //   name.setSpan(StyleSpan(Typeface.BOLD), 0, name.length, 0)
            button.background = background

        }

        //button.text = name

    }

    override fun getItemCount(): Int {
        return mAdapater.getCount()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

       // var color: View
       // var title: TextView

        init {
           // title = itemView.findViewById(R.id.title)
           // color = itemView.findViewById(R.id.color)

            itemView.buttonPager.setOnClickListener {
                println("======custom postion $adapterPosition")
                //viewPager.currentItem = adapterPosition
                viewPager.setCurrentItem(adapterPosition, true)




            }
        }
    }
}
