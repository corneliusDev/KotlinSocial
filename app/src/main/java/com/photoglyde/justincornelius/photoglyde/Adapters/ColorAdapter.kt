package com.photoglyde.justincornelius.photoglyde.Adapters

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.photoglyde.justincornelius.photoglyde.R
import ly.img.android.pesdk.ui.panels.item.ColorItem

import java.util.ArrayList


class DemoColorPagerAdapter : PagerAdapter() {

    private var mItems: List<ColorItem> = ArrayList<ColorItem>()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context)
            .inflate(R.layout.layout_page, container, false)

        val textView = view.findViewById(R.id.title) as TextView
        textView.text = "Page: " + mItems[position].name
        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return mItems.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getPageTitle(position: Int): String? {
        return mItems[position].name
    }

    fun getColorItem(position: Int): ColorItem {
        return mItems[position]
    }

    fun addAll(items: List<ColorItem>) {
        mItems = ArrayList(items)
    }
}
