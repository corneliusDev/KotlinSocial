package com.photoglyde.justincornelius.photoglyde.Adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import com.photoglyde.justincornelius.photoglyde.R
import kotlinx.android.synthetic.main.view_image_previewer.*
import kotlin.math.abs
import android.util.TypedValue
import android.view.*
import android.os.VibrationEffect
import android.os.Build
import android.os.Vibrator
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.view.MotionEventCompat
import android.support.v4.view.ViewCompat
import android.widget.*
import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals
import com.photoglyde.justincornelius.photoglyde.ExapandDetailActivity
import com.photoglyde.justincornelius.photoglyde.Utilities.FileHandler
import kotlinx.android.synthetic.main.full_view.view.*
import javax.microedition.khronos.opengles.GL
import kotlin.math.roundToInt


class ImagePreview {

    var changeTopBool = false
    var changeRightBool = false
    var changeLeftBool = false
    var changeBottomBool = false



    interface ExpandActivity {
        fun onCallback(action:String)
    }


    @SuppressLint("ClickableViewAccessibility")
    fun show(context: Context, source: ImageView, listener: ExpandActivity) {
        val background = ImagePreviewerUtils().getBlurredScreenDrawable(context, source.rootView)



        val dialogView = LayoutInflater.from(context).inflate(R.layout.view_image_previewer, null)
        var imageView = dialogView.findViewById(R.id.previewer_image) as ImageView




        val copy = source.drawable.constantState.newDrawable()
        imageView.setImageDrawable(copy)


        val dialog = Dialog(context, R.style.ImagePreviewerTheme)

        dialog.window.setBackgroundDrawable(background)
        dialog.setContentView(dialogView)
        val param = dialog.card.layoutParams as LinearLayout.LayoutParams
        val paramTopText = dialog.top_text.layoutParams as RelativeLayout.LayoutParams
        val paramSideText1 = dialog.side_text1.layoutParams as LinearLayout.LayoutParams
        val paramBottomText = dialog.bottom_text.layoutParams as LinearLayout.LayoutParams
        val iconLabelParm = dialog.icon_label.layoutParams as RelativeLayout.LayoutParams


        dialog.show()
        var new = 0
        var numbers: IntArray = intArrayOf(2, 1)
        var numbers2: IntArray = intArrayOf(2, 1)
        var ogViewPosition: IntArray = intArrayOf(2, 1)
        source.getLocationInWindow(ogViewPosition)
        val xOg = ogViewPosition[0]
        val yOg = ogViewPosition[1]
        val changePreviewr = imageView.layoutParams as LinearLayout.LayoutParams
        imageView.visibility = View.GONE
        dialog.icon_label.visibility = View.INVISIBLE
        dialog.side_text1.visibility = View.INVISIBLE
        dialog.bottom_text.visibility = View.INVISIBLE
        dialog.set_pin.visibility = View.INVISIBLE
        val sizeX = source.width
        val sizeY = source.height
        val ratio = sizeY.toDouble().div(sizeX.toDouble()).roundToInt()
        println("Ratio Preview: " + ratio)
        val toilbarAdjust =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56F, context.resources.displayMetrics)
                .toInt()

        val top_text_left = 20
        val top_text_top = yOg
        val side_text_1X = xOg + sizeX + 15
        val side_text_1Y = top_text_top
        val shift = yOg - 50








        source.setOnTouchListener(object : View.OnTouchListener {

            val lastChild = dialog.root.getChildAt(dialog.root.childCount - 1)
            val bottom = lastChild.bottom + dialog.root.paddingBottom
            val sy = dialog.root.scrollY
            val sh = dialog.root.height

            val delta = bottom - (sy + sh)
            var count = 0
            var home_count = 0
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
            var dir = 1


            var action_touch = ""






            var layoutParams = imageView.layoutParams


            override fun onTouch(v: View, event: MotionEvent): Boolean {
                dialog.card.layoutParams.height = sizeY
                dialog.card.layoutParams.width = sizeX
                changePreviewr.height = sizeY
                changePreviewr.width = sizeX

                if (count == 0) {

                    OG = event.rawY.toInt()
                    OGX = event.rawX.toInt()
                    count++

                }

                val startY = OG
                val startX = OGX



                val movementY = event.rawY.toInt()
                val movementX = event.rawX.toInt()

                val movementYAdjust = movementY
                val movementYAdjustBottom = movementY
                var staticX = 600
                val movementXAdjust = movementX + staticX

                var leftShift = 300




                var sign = -1

                println("start: " + startX + "and half window: " + GlobalVals.widthWindow.div(2))

                if (startX < GlobalVals.widthWindow.div(2)){
                    sign = 1
                    staticX = 100
                    leftShift = 0
                }


                if (!GlobalVals.whatsNew) {
                    param.setMargins(20, shift, 20, 0)

                }else{

                    if (sign<0){
                        param.setMargins(GlobalVals.widthWindow.div(2), shift, 20, 0)
                    }else{
                        param.setMargins(20, shift, 20, 0)
                    }

                }


                dialog.card.layoutParams = param
                dialog.top_text.layoutParams = paramTopText
                dialog.bottom_text.layoutParams = paramBottomText
                dialog.side_text1.layoutParams = paramSideText1
                imageView.visibility = View.VISIBLE

                if (dialog.isShowing) {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                    val action = event.actionMasked
                    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                        println("Action Pointer Up Top")

                        when(action_touch){

                            "map_open" -> {
                             //   dialog.top_text.setTextColor(Color.BLACK)
                                dialog.dismiss()
                                listener.onCallback("map_open")
                            }

                            "expand_image" -> {
                                new = 1
                                v.parent.requestDisallowInterceptTouchEvent(false)
                             //   listener.onCallback("expand_image")
                                dialog.dismiss()
                            }

                            else -> {
                                new = 1
                                v.parent.requestDisallowInterceptTouchEvent(false)
                                listener.onCallback("Save")
                                dialog.dismiss()
                            }
                        }
                        return true
                    }
                }







//                    if (home_count == 0) {
//

//
//
//
//
//
//                        dialog.side_text1.visibility = View.VISIBLE
//                        dialog.bottom_text.visibility = View.VISIBLE
//                        dialog.top_text.visibility = View.VISIBLE
//
//                        home_count++
//
//                    }


                if (home_count == 0) {


                    println("Top: " + OGX.minus(leftShift))
                    println("Side: " + OGX.minus(leftShift))
                    println("Bottom: " + OGX.plus(staticX).plus(200))
                    println("OG X: " + OGX)
                    println("OG Y: " + OG)
                    println("Findger X: " + movementX)
                    println("Findger Y: " + movementY)
                    println("Total: " + GlobalVals.widthWindow)




                if (sign<0){
                    println("ON NEGATIVE")
                    paramTopText.setMargins(OGX.minus(leftShift), movementY - GlobalVals.heightWindow.div(5), 20, 0)

                    paramBottomText.setMargins(OGX.minus(leftShift), movementY, 20, 0)

                    paramSideText1.setMargins(OGX.minus(staticX).plus(200), OG - GlobalVals.heightWindow.div(8), 20, 0)

                    iconLabelParm.setMargins(0,movementY.minus(GlobalVals.heightWindow.div(3)),0,0)



                }else{

                    paramTopText.setMargins(OGX.minus(leftShift), movementY - GlobalVals.heightWindow.div(5), 20, 0)

                    paramBottomText.setMargins(OGX.minus(leftShift), movementY, 20, 0)

                    paramSideText1.setMargins(OGX.plus(staticX).plus(200), OG - GlobalVals.heightWindow.div(8), 20, 0)


                    iconLabelParm.setMargins(OGX.plus(staticX).plus(300), movementY.minus(GlobalVals.heightWindow.div(3)),20,0)

                }
//
                    dialog.icon_label.layoutParams = iconLabelParm
//
                   home_count++
//
                }


//
                    println("width: " + GlobalVals.widthWindow + " and height: " + GlobalVals.heightWindow)

                    println("Start X: " + startX + " and movementAdjust: " + movementX + " and " + (movementY > startY.plus(GlobalVals.widthWindow.div(10))))

                    println("Current Point: " + startX.plus(staticX.times(sign)) + " and Half: " + movementX + " and " + staticX.times(sign) + " and " + movementX.plus(staticX.times(sign)))



                 //    Move object top
                    if (startY > movementYAdjust && movementY > startY.minus(GlobalVals.widthWindow.div(10))){
                        paramTopText.setMargins(OGX.plus(startY.minus(movementY).times(sign)).minus(leftShift), movementY - GlobalVals.heightWindow.div(5), 20, 0)

                        if (changeTopBool){
                            dialog.set_pin.setBackgroundResource(R.drawable.ic_pin)
                            dialog.icon_text.text = ""
                            changeTopBool = false
                            println("TURN ME BLACK")
                        }


                    }else if (startY > movementYAdjust && noneAreBlue()){
                        dialog.set_pin.setBackgroundResource(R.drawable.ic_pin_blue)
                        dialog.icon_text.text = "Map"

                        changeTopBool = true
                        println("TURN ME BLUE")
                    }





                    // Move object bottom
                    if (startY < movementYAdjust && movementY < startY.plus(GlobalVals.widthWindow.div(10))){

                        dir = -1

                        paramBottomText.setMargins(OGX.minus(startY.minus(movementY).times(sign)).minus(leftShift), movementY, 20, 0)

                        if (changeBottomBool){
                            dialog.bottom_text.setBackgroundResource(R.drawable.ic_icon_save)
                            dialog.icon_text.text = ""
                            changeBottomBool = false
                            println("TURN ME BLACK")
                        }

                    }else if (startY < movementYAdjust && noneAreBlue()){
                        dialog.bottom_text.setBackgroundResource(R.drawable.ic_icon_save_blue)
                        dialog.icon_text.text = "Save"
                        changeBottomBool = true
                    }




                    // Move object right side of finger
                    if (startX < movementX && movementX < startX.plus(staticX.times(sign))){
                        paramSideText1.setMargins(movementX.plus(staticX.times(sign)).plus(200), OG - GlobalVals.heightWindow.div(8), 20, 0)

                        if (changeRightBool){
                            dialog.side_text1.setBackgroundResource(R.drawable.ic_expand)
                            dialog.icon_text.text = ""
                            changeRightBool = false
                            println("MOVE RIGHT BLACK")
                        }

                    }else if (startX < movementX && sign > 0 && noneAreBlue()){
                        dialog.side_text1.setBackgroundResource(R.drawable.ic_expand_blue)
                        dialog.icon_text.text = "Expand"
                        changeRightBool = true
                        println("MOVE RIGHT BLUE")
                    }



                    // Move object left side of finger
                    if (sign < 0) if (startX > movementX && startX.minus(GlobalVals.widthWindow.div(10)) < movementX){
                        paramSideText1.setMargins(movementX.minus(staticX).plus(200).minus(movementY.minus(startY)), OG - GlobalVals.heightWindow.div(8), 20, 0)

                       // paramSideText1.setMargins(OGX.minus(staticX).plus(200), OG - GlobalVals.heightWindow.div(8), 20, 0)

                        if (changeLeftBool){
                            dialog.side_text1.setBackgroundResource(R.drawable.ic_expand)
                            dialog.icon_text.text = ""
                            changeLeftBool = false
                            println("MOVE LEFT BLACK")
                        }

                    }else if (startX > movementX && noneAreBlue()){
                        dialog.side_text1.setBackgroundResource(R.drawable.ic_expand_blue)
                        dialog.icon_text.text = "Expand"
                        changeLeftBool = true
                        println("MOVE LEFT BLUE")
                    }


                dialog.side_text1.visibility = View.VISIBLE
                dialog.bottom_text.visibility = View.VISIBLE
                dialog.set_pin.visibility = View.VISIBLE
                dialog.icon_label.visibility = View.VISIBLE


//
//                else if(GlobalVals.whatsNew){
//
//                    println("Came From Explore")
//
//
//
//                    dialog.card.layoutParams.height = sizeY + 100
//                    dialog.card.layoutParams.width = sizeX
//
//                    changePreviewr.height = sizeY
//                    changePreviewr.width = sizeX
//
//
//
//                    param.setMargins(20, shift, 20, 0)
//                    dialog.card.layoutParams = param
//                    // println("========check param sizeX$sizeX and sizeY$sizeY and xOG$xOg and yOG$yOg and ${dialog.top_text.layoutParams.width/2}")
//                    paramTopText.setMargins(top_text_left, top_text_top - 100, 20, 0)
//                    dialog.top_text.layoutParams = paramTopText
//
//                    //   paramSideText1.setMargins(side_text_1X, 0, 20, 0)
//
//                    paramSideText1.setMargins(side_text_1X , side_text_1Y, 20, 0)
//
//                }else{
//                    dialog.side_text1.visibility = View.GONE
//                    dialog.top_text.visibility = View.GONE
//                }











//                if(GlobalVals.cameFromMain) {
//                    println("Came From Main")
//                    count++
//                   // println("======check top rawY ${event.rawY} and rawX ${event.rawX} and OGHEIGHT ${GlobalVals.heightWindow}")
//                    //keeps top text moving up and never down from starting position
//                    if (top_text_top > top_text_top + (event.rawY - OG).toInt() && top_text_top - (event.rawY - OG) * (.7) < top_text_top + 75) {
//                        println("Came From Main 1")
//
//
//                     //   paramTopText.setMargins(top_text_left, (top_text_top + ((event.rawY - OG) * (.7)).toInt()), 20, 0)
//
//                        paramSideText1.setMargins(side_text_1X , event.rawY.toInt(), 20, 0)
//
//
//                        dialog.top_text.layoutParams = paramTopText
//
//
//                        param.setMargins(20, shift, 20, 0)
//                        dialog.card.layoutParams = param
//
//                        var topTextPOS = dialog.top_text.getLocationInWindow(numbers)
//                        count2 = 0
//                    } else {
//                        println("Came From Main 2")
//                        if (top_text_top - (event.rawY - OG) * (.7) < top_text_top + 75) {
//                            println("Came From Main 3")
//                            param.setMargins(20, shift, 20, 0)
//                            dialog.card.layoutParams = param
//                        } else {
//                            println("Came From Main 4")
//                            //top reached
//                            if (count2 == 0) {
//                                count2++
//
//                                if (dialog.top_text.currentTextColor == Color.BLACK) {
//
//                                    println("Click One Top")
//                                    dialog.top_text.setTextColor(Color.MAGENTA)
//                                } else {
//
//                                    println("Click One Bottom")
//
//                                    action_touch = "map_open"
//
//
//
//                                }
//                                vibrate(context)
//                            }
//                        }
//                    }
//
//                    if (side_text_1X < side_text_1X + ((event.rawX - OGX) * (.2)).toInt() && side_text_1X + ((event.rawX - OGX) * (.4)).toInt() < (side_text_1X + 65)) {
//                        println("Came From Main 5")
//                        paramSideText1.setMargins(side_text_1X + ((event.rawX - OGX) * (.7)).toInt(), side_text_1Y, 20, 0)
//                        count3 = 0
//                    } else {
//                        println("Came From Main 6")
//                        if (count3 == 0 && side_text_1X + ((event.rawX - OGX) * (.2)).toInt() > side_text_1X + 30) {
//                            println("Came From Main 7")
//
//                            if (dialog.side_text1.currentTextColor == Color.BLACK) {
//                                println("Came From Main 8")
//
//                                dialog.side_text1.setTextColor(Color.GRAY)
//                                vibrate(context)
//                            } else {
//
//                                 if (GlobalVals.whatsNew){
//                                println("Came From Main 9")
//                                dialog.side_text1.setTextColor(Color.BLACK)
//                                dialog.side_text1.visibility = View.GONE
//                                vibrate(context)
//                                param.setMargins(20, 0, 20, 300)
//                                dialog.card.layoutParams = param
//
//
//                                val expandWidth = source.width
//                                val expandHeight = source.height
//
//
//                                val ratio = expandHeight.toDouble().div(expandWidth)
//
//                                //  println("=========height of expanded $expandWidth and $expandHeight and $ratio")
//
//                                val width = GlobalVals.widthWindow - 50
//                                val widthCalc = width.toDouble().times(ratio)
//                                val height = widthCalc.toInt()
//                                // println("==========new picture values $ratio and $width and $height and OG $expandHeight and $expandWidth and ${GlobalVals.upSplash[Data.sendIndexToDetail].urls?.regular}")
//                                ViewPropertyObjectAnimator.animate(dialog.card).width(width).height(height)
//                                    .setDuration(200)
//                                    .start()
//                                //ViewPropertyObjectAnimator.animate(dialog.card).height(height).setDuration(400).start()
//                                ViewPropertyObjectAnimator.animate(dialog.previewer_image).width(width).height(height)
//                                    .setDuration(200).start()
//                                val cutOff = GlobalVals.heightWindow.times((4).toDouble().div(5))
//                                val checkOffset = yOg + height
//                                // println("========checking CheckoffSet $checkOffset cuttOFF $cutOff and raw ${event.rawY} and height $height")
//                                if (checkOffset > GlobalVals.heightWindow) {
//
//                                    val offSet = checkOffset - GlobalVals.heightWindow + 100
//                                    ViewPropertyObjectAnimator.animate(dialog.root).verticalMargin(-offSet.toInt())
//                                        .setDuration(500).start()
//
//
//                                }
//
//                            }else{
//
//
//                                action_touch = "expand_image"
//                                 listener.onCallback("expand_image")
//                              //   dialog.dismiss()
//
//
//
//
//
//
//
//                                 }
//
//                            }
//
//                            count3++
//                        }
//
//                    }
//                }

                return false

            }
        })

    }

    fun noneAreBlue() : Boolean{
        return !changeTopBool && !changeRightBool && !changeLeftBool && !changeBottomBool
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