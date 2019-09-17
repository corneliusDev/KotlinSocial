package com.photoglyde.justincornelius.photoglyde.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.photoglyde.justincornelius.photoglyde.data.ANI_DURATION

class AnimateWindow {

    interface AnimationEnd {
        fun animationOver()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun animateDown(context: Context, source: View) {

        val anim = AlphaAnimation(1.0f, 0.0f)
        anim.duration = ANI_DURATION
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
        anim.duration = ANI_DURATION
        source.startAnimation(anim)

        anim.setAnimationListener(object : Animation.AnimationListener{

            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {

               callBack.animationOver()

            }

            override fun onAnimationStart(p0: Animation?) {

            }
        })

    }



}