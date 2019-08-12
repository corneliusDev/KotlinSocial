package com.photoglyde.justincornelius.photoglyde.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import com.photoglyde.justincornelius.photoglyde.Data.DOWN
import com.photoglyde.justincornelius.photoglyde.Data.UP

class ScrollDownListener {

    private var scrolDown = true


    interface HideShow {
        fun onCallback(animate:String)
    }


    @SuppressLint("ClickableViewAccessibility")
    fun show(context: Context, source: RecyclerView, hideShow: HideShow) {
        source.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {


                super.onScrolled(recyclerView, dx, dy)
                println("===on scrolled $dy")


                if (dy > 0 && scrolDown) {
                    scrolDown = false
                    println("===We are Down $scrolDown")



                    hideShow.onCallback(DOWN)



                } else if (dy < 0 && !scrolDown) {
                    scrolDown = true
                    println("===We are Up $scrolDown")
                    hideShow.onCallback(UP)

                }




            }
    })
}
}