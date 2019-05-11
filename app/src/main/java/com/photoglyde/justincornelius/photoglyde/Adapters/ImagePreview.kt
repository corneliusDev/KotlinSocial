package com.photoglyde.justincornelius.photoglyde.Adapters

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.view.View.OnTouchListener
import android.graphics.drawable.Drawable
import android.graphics.drawable.BitmapDrawable
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import com.photoglyde.justincornelius.photoglyde.ExapandDetailActivity
import com.photoglyde.justincornelius.photoglyde.ExploreActivity
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.R.id.root
import kotlinx.android.synthetic.main.view_image_previewer.*
import kotlin.math.abs
import android.opengl.ETC1.getHeight
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.util.TypedValue
import android.view.*
import com.photoglyde.justincornelius.photoglyde.Fragments.PostingOptions
import kotlin.math.roundToInt
import android.os.VibrationEffect
import android.os.Build
import android.support.v4.content.ContextCompat.getSystemService
import android.os.Vibrator
import android.support.v4.view.MotionEventCompat
import android.widget.*
import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator
import com.google.obf.ev
import com.photoglyde.justincornelius.photoglyde.Data.Data
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals
import com.photoglyde.justincornelius.photoglyde.R.id.image
import kotlinx.android.synthetic.main.activity_main.*


class ImagePreview {



    interface ExpandActivity {
        fun onCallback(source: View)
    }


    @SuppressLint("ClickableViewAccessibility")
    fun show(context: Context, source: ImageView, listener: ExpandActivity) {
        val background = ImagePreviewerUtils().getBlurredScreenDrawable(context, source.getRootView())


        val dialogView = LayoutInflater.from(context).inflate(R.layout.view_image_previewer, null)
        var imageView = dialogView.findViewById(R.id.previewer_image) as ImageView


        val copy = source.getDrawable().getConstantState().newDrawable()
        imageView.setImageDrawable(copy)


        val dialog = Dialog(context, R.style.ImagePreviewerTheme)
        dialog.getWindow().setBackgroundDrawable(background)
        dialog.setContentView(dialogView)

        val param = dialog.card.layoutParams as LinearLayout.LayoutParams
        val paramTopText = dialog.top_text.layoutParams as LinearLayout.LayoutParams
        val paramSideText1 = dialog.side_text1.layoutParams as RelativeLayout.LayoutParams


//        param1.setMargins(20,450,20,20)
//
//        dialog.container4.layoutParams = param1


        dialog.show()
        var new = 0
        var numbers: IntArray = intArrayOf(2, 1)
        var numbers2: IntArray = intArrayOf(2, 1)


        var ogViewPosition: IntArray = intArrayOf(2, 1)

        var topTextPOS = dialog.top_text.getLocationInWindow(numbers)



            source.getLocationInWindow(ogViewPosition)
            val xOg = ogViewPosition[0]
            val yOg = ogViewPosition[1]
        val changePreviewr = imageView.layoutParams as LinearLayout.LayoutParams
            val sizeX = source.width
            val sizeY = source.height
            val toilbarAdjust =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56F, context.resources.getDisplayMetrics())
                    .toInt()

        val top_text_left = 20
        val top_text_top = yOg - toilbarAdjust
        val side_text_1X = xOg + sizeX + 15
        val side_text_1Y = top_text_top + sizeY / 2


            // println("=====tool bar $toilbarAdjust and $sizeX and $sizeY")


if (GlobalVals.cameFromMain) {
    dialog.card.layoutParams.width = sizeX
    dialog.card.layoutParams.height = sizeY
    changePreviewr.height = sizeY

    println("========source dimensions $sizeX and $sizeY")

    param.setMargins(20, 0, 20, 0)
    dialog.card.layoutParams = param
    // println("========check param sizeX$sizeX and sizeY$sizeY and xOG$xOg and yOG$yOg and ${dialog.top_text.layoutParams.width/2}")
    paramTopText.setMargins(top_text_left, top_text_top, 20, 0)
    dialog.top_text.layoutParams = paramTopText

    paramSideText1.setMargins(side_text_1X, 0, 20, 0)
}else{
    dialog.side_text1.visibility = View.GONE
    dialog.top_text.visibility = View.GONE
}

//        dialog.card.getLocationInWindow(numbers)
//        dialog.container4.getLocationInWindow(numbers2)


        source.setOnTouchListener(object : View.OnTouchListener {
            var x1: Float? = 0.toFloat()
            var x2: Float? = 0.toFloat()
            var velocityX1: Float? = 0.toFloat()
            var velocityX2: Float? = 0.toFloat()
            var flingCount = 0

            val lastChild = dialog.root.getChildAt(dialog.root.getChildCount() - 1)
            val bottom = lastChild.getBottom() + dialog.root.getPaddingBottom()
            val sy = dialog.root.getScrollY()
            val sh = dialog.root.getHeight()

            val delta = bottom - (sy + sh)
            var count = 0
            var count1 = 0
            var count2 = 0
            var count3 = 0
            var OG = 0
            var OGX = 0
            val param = dialog.card.layoutParams as LinearLayout.LayoutParams
            val param1 = dialog.card.layoutParams as LinearLayout.LayoutParams
            var dismiss = false
            var mLastTouchX = 0f
            var mLastTouchY = 0f
            var mActivePointerId = 0
            var mPosX = 0f
            var mPosY = 0f




            var layoutParams = imageView.layoutParams


            override fun onTouch(v: View, event: MotionEvent): Boolean {


                if (dialog.isShowing()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true)
                    // println("======current action e")
                    val action = event.actionMasked
                    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                        if (true) {
                            new = 1
                            v.getParent().requestDisallowInterceptTouchEvent(false)
                            // dialog.root.smoothScrollBy(0, delta)
                            dialog.dismiss()
                        }
                        return true
                    }
                }
                if (count == 0) {

                    OG = event.rawY.toInt()
                    OGX = event.rawX.toInt()
                    count++
                }

                if(GlobalVals.cameFromMain) {
                    count++
                   // println("======check top rawY ${event.rawY} and rawX ${event.rawX} and OGHEIGHT ${GlobalVals.heightWindow}")
                    //keeps top text moving up and never down from starting position
                    if (top_text_top > top_text_top + (event.rawY - OG).toInt() && top_text_top - (event.rawY - OG) * (.7) < top_text_top + 75) {
                        paramTopText.setMargins(
                            top_text_left,
                            (top_text_top + ((event.rawY - OG) * (.7)).toInt()),
                            20,
                            0
                        )
                        dialog.top_text.layoutParams = paramTopText


                        param.setMargins(20, -((event.rawY - OG) * (.7)).toInt(), 20, 0)
                        dialog.card.layoutParams = param

                        var topTextPOS = dialog.top_text.getLocationInWindow(numbers)
                        count2 = 0
                    } else {
                        if (top_text_top - (event.rawY - OG) * (.7) < top_text_top + 75) {
                            param.setMargins(20, 0, 20, 0)
                            dialog.card.layoutParams = param
                        } else {
                            //top reached
                            if (count2 == 0) {
                                count2++

                                if (dialog.top_text.currentTextColor == Color.BLACK) {
                                    dialog.top_text.setTextColor(Color.GRAY)
                                } else {
                                    dialog.top_text.setTextColor(Color.BLACK)
                                }
                                vibrate(context)
                            }
                        }
                    }
                    if (side_text_1X < side_text_1X + ((event.rawX - OGX) * (.2)).toInt() && side_text_1X + ((event.rawX - OGX) * (.4)).toInt() < (side_text_1X + 65)) {
                        paramSideText1.setMargins(
                            side_text_1X + ((event.rawX - OGX) * (.7)).toInt(),
                            side_text_1Y,
                            20,
                            0
                        )
                        count3 = 0
                    } else {
                        if (count3 == 0 && side_text_1X + ((event.rawX - OGX) * (.2)).toInt() > side_text_1X + 30) {

                            if (dialog.side_text1.currentTextColor == Color.BLACK) {
                                dialog.side_text1.setTextColor(Color.GRAY)
                                vibrate(context)
                            } else {
                                dialog.side_text1.setTextColor(Color.BLACK)
                                dialog.side_text1.visibility = View.GONE
                                vibrate(context)
                                param.setMargins(20, 0, 20, 300)
                                dialog.card.layoutParams = param


                                val expandWidth = source.width
                                val expandHeight = source.height



                                val ratio = expandHeight.toDouble().div(expandWidth!!)

                                println("=========height of expanded $expandWidth and $expandHeight and $ratio")

                                val width = GlobalVals.widthWindow - 50
                                val widthCalc = width.toDouble().times(ratio)
                                val height = widthCalc.toInt()
                               // println("==========new picture values $ratio and $width and $height and OG $expandHeight and $expandWidth and ${GlobalVals.upSplash[Data.sendIndexToDetail].urls?.regular}")
                                ViewPropertyObjectAnimator.animate(dialog.card).width(width).height(height)
                                    .setDuration(200)
                                    .start()
                                //ViewPropertyObjectAnimator.animate(dialog.card).height(height).setDuration(400).start()
                                ViewPropertyObjectAnimator.animate(dialog.previewer_image).width(width).height(height)
                                    .setDuration(200).start()
                                val cutOff = GlobalVals.heightWindow.times((4).toDouble().div(5))
                                val checkOffset = yOg + height
                               // println("========checking CheckoffSet $checkOffset cuttOFF $cutOff and raw ${event.rawY} and height $height")
                                if (checkOffset > GlobalVals.heightWindow) {

                                    val offSet = checkOffset - GlobalVals.heightWindow + 100
                                    ViewPropertyObjectAnimator.animate(dialog.root).verticalMargin(-offSet.toInt())
                                        .setDuration(500).start()

                                }

                            }

                            count3++
                        }

                    }
                }else{

                    val ev = event

                    val action = MotionEventCompat.getActionMasked(ev)



                    when (action) {
                        MotionEvent.ACTION_DOWN -> {
                            MotionEventCompat.getActionIndex(ev).also { pointerIndex ->
                                // Remember where we started (for dragging)
                                mLastTouchX = MotionEventCompat.getX(ev, pointerIndex)
                                mLastTouchY = MotionEventCompat.getY(ev, pointerIndex)
                            }

                            // Save the ID of this pointer (for dragging)
                            mActivePointerId = MotionEventCompat.getPointerId(ev, 0)
                        }

                        MotionEvent.ACTION_MOVE -> {
                            // Find the index of the active pointer and fetch its position
                            val (x: Float, y: Float) =
                                    MotionEventCompat.findPointerIndex(ev, mActivePointerId).let { pointerIndex ->
                                        // Calculate the distance moved
                                        MotionEventCompat.getX(ev, pointerIndex) to
                                                MotionEventCompat.getY(ev, pointerIndex)
                                    }

                            mPosX += x - mLastTouchX
                            mPosY += y - mLastTouchY


                            val sizeYAdd = yOg - 200 - (OG-event.rawY)
                            val sizeXAdd = xOg  - (OGX-event.rawX)
                          //  println("=======add this  and $sizeYAdd and $sizeXAdd")

                            val finalY = sizeYAdd
                            val finalX = sizeXAdd


                            dialog.top.x = finalX
                            dialog.top.y = finalY


                            if (count == 1){
                             //   println("======= we did animate $count")

                                ViewPropertyObjectAnimator.animate(dialog.bottom_radius).verticalMargin(1800)
                                    .setDuration(500).start()
                                count++
                            }

                            //println("========move size ${sizeXAdd-yOg} and ${OG-event.rawY}")

                            if (abs(OG-event.rawY) > 300 && count ==2){

                                dialog.bottom_radius.setBackgroundResource(R.drawable.circle_drop)
                                dialog.inner_text.setColorFilter(Color.blue(R.color.blue))

                                dialog.card.bringToFront()
                                dialog.previewer_image.bringToFront()

                                dialog.bottom_radius.z = 0f

                                ViewPropertyObjectAnimator.animate(dialog.top).scaleY(0.50f).scaleX(0.50f)
                                    .setDuration(300).start()

                                ViewPropertyObjectAnimator.animate(dialog.bottom_radius).scaleY(1.8f).scaleX(1.8f)
                                    .setDuration(200).start()
                                ViewPropertyObjectAnimator.animate(dialog.inner_text).scaleY(1.8f).scaleX(1.8f).alpha(0.80f)
                                    .setDuration(200).start()
                                vibrate(context)

                                count++

                                println("======== insdie check")

                            }

                            //println("=======bottom check ${abs(OG-event.rawY -300)} and ${event.rawY} and ${GlobalVals.heightWindow - 400}")

//                            if (abs(OG-event.rawY -300) > 650 && count ==3){
//
//                                dialog.bottom_radius.setBackgroundColor(Color.GREEN)
//
//
//                            }


                            if (abs(event.rawY) > (GlobalVals.heightWindow -100) && count == 3){

                                dialog.bottom_radius.setBackgroundResource(R.drawable.circle_drop_dark)

                                ViewPropertyObjectAnimator.animate(dialog.bottom_radius).verticalMargin(GlobalVals.heightWindow + 200).horizontalMargin(-1000)
                                    .setDuration(500).start()
                                ViewPropertyObjectAnimator.animate(dialog.top).verticalMargin(-GlobalVals.heightWindow + 200).horizontalMargin(1000).scaleY(0.50f).scaleX(0.50f)
                                    .setDuration(500).start()
                                count++

//                                ViewPropertyObjectAnimator.animate(dialog.top).scaleY(0.50f).scaleX(0.50f)
//                                    .setDuration(300).start()


                            }








                            // Remember this touch position for the next move event
                            mLastTouchX = x
                            mLastTouchY = y



                        }
                        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                           // mActivePointerId = INVALID_POINTER_ID
                        }
                        MotionEvent.ACTION_POINTER_UP -> {

                            MotionEventCompat.getActionIndex(ev).also { pointerIndex ->
                                MotionEventCompat.getPointerId(ev, pointerIndex)
                                    .takeIf { it == mActivePointerId }
                                    ?.run {
                                        // This was our active pointer going up. Choose a new
                                        // active pointer and adjust accordingly.
                                        val newPointerIndex = if (pointerIndex == 0) 1 else 0
                                        mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex)
                                        mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex)
                                        mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex)
                                    }
                            }
                        }
                    }


                }

                return false

            }
        })







        // private fun showToast(message : Int) = Toast.makeText(this.act?.baseContext, message, Toast.LENGTH_SHORT).show()
    }

    fun vibrate(context: Context) {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v!!.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            v!!.vibrate(40)
        }
    }
}