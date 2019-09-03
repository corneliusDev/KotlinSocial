package com.photoglyde.justincornelius.photoglyde.Utilities

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import com.photoglyde.justincornelius.photoglyde.Adapters.ImagePreview
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.Web.MapFragment
import kotlinx.android.synthetic.main.activity_main.*

class AnimateWindow {


    interface AnimationEnd {
        fun animationOver()
    }


    @SuppressLint("ClickableViewAccessibility")
    fun animateDown(context: Context, source: View) {

        val anim = AlphaAnimation(1.0f, 0.0f)
        anim.duration = 500
        source.startAnimation(anim)

        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                source.visibility = View.GONE
            }

            override fun onAnimationStart(p0: Animation?) {

            }
        })

    }


    @SuppressLint("ClickableViewAccessibility")
    fun animateUp(context: Context, source: View, callBack: AnimationEnd) {

        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 500
        source.startAnimation(anim)

        anim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {

                println("Map ANI")
               callBack.animationOver()
            }

            override fun onAnimationStart(p0: Animation?) {

            }
        })

    }



}