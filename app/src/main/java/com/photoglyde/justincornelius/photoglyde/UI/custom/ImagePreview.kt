package com.photoglyde.justincornelius.photoglyde.UI.custom

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import com.photoglyde.justincornelius.photoglyde.R
import kotlinx.android.synthetic.main.view_image_previewer.*
import android.util.TypedValue
import android.view.*
import android.widget.*
import com.photoglyde.justincornelius.photoglyde.Data.*
import kotlin.math.roundToInt


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
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
        val background = ImagePreviewerUtils()
            .getBlurredScreenDrawable(context, source.rootView)



        val dialogView = LayoutInflater.from(context).inflate(R.layout.view_image_previewer, null)
        val imageView = dialogView.findViewById(R.id.previewer_image) as ImageView




        val copy = source.drawable.constantState.newDrawable()
        imageView.setImageDrawable(copy)


        val dialog = Dialog(context, R.style.ImagePreviewerTheme)

        dialog.window.setBackgroundDrawable(background)
        dialog.setContentView(dialogView)
        val param = dialog.card.layoutParams as LinearLayout.LayoutParams
        val paramTopText = dialog.top_text.layoutParams as RelativeLayout.LayoutParams
        val paramSideText = dialog.side_text.layoutParams as LinearLayout.LayoutParams
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
        dialog.side_text.visibility = View.INVISIBLE
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

                println("start: " + startX + "and half window: " + GlobalValues.widthWindow.div(2))

                if (startX < GlobalValues.widthWindow.div(2)){
                    sign = 1
                    staticX = 100
                    leftShift = 0
                }


                if (!GlobalValues.whatsNew) {
                    param.setMargins(20, shift, 20, 0)

                }else{

                    if (sign<0){
                        param.setMargins(GlobalValues.widthWindow.div(2), shift, 20, 0)
                    }else{
                        param.setMargins(20, shift, 20, 0)
                    }

                }


                dialog.card.layoutParams = param
                dialog.top_text.layoutParams = paramTopText
                dialog.bottom_text.layoutParams = paramBottomText
                dialog.side_text.layoutParams = paramSideText
                imageView.visibility = View.VISIBLE

                if (dialog.isShowing) {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                    val action = event.actionMasked
                    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                        println("Action Pointer Up Top")

                        when(action_touch){

                            MAP_OPEN -> {
                             //   dialog.top_text.setTextColor(Color.BLACK)
                                dialog.dismiss()
                                listener.onCallback(MAP_OPEN)
                            }

                            EXPANDED_IMAGE -> {

                                new = 1
                                v.parent.requestDisallowInterceptTouchEvent(false)
                             //   listener.onCallback("expand_image")
                                dialog.dismiss()
                            }

                            else -> {
                                new = 1
                                v.parent.requestDisallowInterceptTouchEvent(false)
                                listener.onCallback(SAVE)
                                dialog.dismiss()
                            }
                        }
                        return true
                    }
                }

                if (home_count == 0) {


                    println("Top: " + OGX.minus(leftShift))
                    println("Side: " + OGX.minus(leftShift))
                    println("Bottom: " + OGX.plus(staticX).plus(200))
                    println("OG X: " + OGX)
                    println("OG Y: " + OG)
                    println("Findger X: " + movementX)
                    println("Findger Y: " + movementY)
                    println("Total: " + GlobalValues.widthWindow)




                if (sign<0){
                    println("ON NEGATIVE")
                    paramTopText.setMargins(OGX.minus(leftShift), movementY - GlobalValues.heightWindow.div(5), 20, 0)

                    paramBottomText.setMargins(OGX.minus(leftShift), movementY, 20, 0)

                    paramSideText.setMargins(OGX.minus(staticX).plus(200), OG - GlobalValues.heightWindow.div(8), 20, 0)

                    iconLabelParm.setMargins(0,movementY.minus(GlobalValues.heightWindow.div(3)),0,0)



                }else{

                    paramTopText.setMargins(OGX.minus(leftShift), movementY - GlobalValues.heightWindow.div(5), 20, 0)

                    paramBottomText.setMargins(OGX.minus(leftShift), movementY, 20, 0)

                    paramSideText.setMargins(OGX.plus(staticX).plus(200), OG - GlobalValues.heightWindow.div(8), 20, 0)


                    iconLabelParm.setMargins(OGX.plus(staticX).plus(300), movementY.minus(GlobalValues.heightWindow.div(3)),20,0)

                }
//
                    dialog.icon_label.layoutParams = iconLabelParm
//
                   home_count++
//
                }


//
                    println("width: " + GlobalValues.widthWindow + " and height: " + GlobalValues.heightWindow)

                    println("Start X: " + startX + " and movementAdjust: " + movementX + " and " + (movementY > startY.plus(GlobalValues.widthWindow.div(10))))

                    println("Current Point: " + startX.plus(staticX.times(sign)) + " and Half: " + movementX + " and " + staticX.times(sign) + " and " + movementX.plus(staticX.times(sign)))



                 //    Move object top
                    if (startY > movementYAdjust && movementY > startY.minus(GlobalValues.widthWindow.div(10))){
                        paramTopText.setMargins(OGX.plus(startY.minus(movementY).times(sign)).minus(leftShift), movementY - GlobalValues.heightWindow.div(5), 20, 0)

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
                    if (startY < movementYAdjust && movementY < startY.plus(GlobalValues.widthWindow.div(10))){

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
                        paramSideText.setMargins(movementX.plus(staticX.times(sign)).plus(200), OG - GlobalValues.heightWindow.div(8), 20, 0)

                        if (changeRightBool){
                            dialog.side_text.setBackgroundResource(R.drawable.ic_expand)
                            dialog.icon_text.text = ""
                            changeRightBool = false
                            println("MOVE RIGHT BLACK")
                        }

                    }else if (startX < movementX && sign > 0 && noneAreBlue()){
                        dialog.side_text.setBackgroundResource(R.drawable.ic_expand_blue)
                        dialog.icon_text.text = "Expand"
                        changeRightBool = true
                        println("MOVE RIGHT BLUE")
                    }



                    // Move object left side of finger
                    if (sign < 0) if (startX > movementX && startX.minus(GlobalValues.widthWindow.div(10)) < movementX){
                        paramSideText.setMargins(movementX.minus(staticX).plus(200).minus(movementY.minus(startY)), OG - GlobalValues.heightWindow.div(8), 20, 0)

                       // paramSideText1.setMargins(OGX.minus(staticX).plus(200), OG - GlobalValues.heightWindow.div(8), 20, 0)

                        if (changeLeftBool){
                            dialog.side_text.setBackgroundResource(R.drawable.ic_expand)
                            dialog.icon_text.text = ""
                            changeLeftBool = false
                            println("MOVE LEFT BLACK")
                        }

                    }else if (startX > movementX && noneAreBlue()){
                        dialog.side_text.setBackgroundResource(R.drawable.ic_expand_blue)
                        dialog.icon_text.text = "Expand"
                        changeLeftBool = true
                        println("MOVE LEFT BLUE")
                    }


                dialog.side_text.visibility = View.VISIBLE
                dialog.bottom_text.visibility = View.VISIBLE
                dialog.set_pin.visibility = View.VISIBLE
                dialog.icon_label.visibility = View.VISIBLE
                return false

            }
        })

    }

    fun noneAreBlue() : Boolean{
        return !changeTopBool && !changeRightBool && !changeLeftBool && !changeBottomBool
    }

}