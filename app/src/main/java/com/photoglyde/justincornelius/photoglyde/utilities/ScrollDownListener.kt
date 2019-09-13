package com.photoglyde.justincornelius.photoglyde.utilities

import android.annotation.SuppressLint
import android.content.Context
import com.photoglyde.justincornelius.photoglyde.Data.DOWN
import com.photoglyde.justincornelius.photoglyde.Data.UP

class ScrollDownListener {

    private var scrolDown = true

    interface HideShow {
        fun onCallback(animate:String)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun show(context: Context, source: androidx.recyclerview.widget.RecyclerView, hideShow: HideShow) {
        source.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && scrolDown) {
                    scrolDown = false
                    hideShow.onCallback(DOWN)
                } else if (dy < 0 && !scrolDown) {
                    scrolDown = true
                    hideShow.onCallback(UP)
                }
            }
    })
}

}