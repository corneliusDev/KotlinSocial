package com.photoglyde.justincornelius.photoglyde.UI.custom

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator
import com.photoglyde.justincornelius.photoglyde.data.*
import com.photoglyde.justincornelius.photoglyde.R
import kotlinx.android.synthetic.main.view_image_previewer.*
import kotlin.math.abs


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ImagePreview {

    var changeTopBool = false
    var changeRightBool = false
    var changeLeftBool = false
    var changeBottomBool = false
    var previousOffset = 0
    var fingerPositionStartY = 0
    var fingerPositionStartX = 0
    var inFeedPositonX = 0
    var inFeedPositonY = 0
    var fingerLeftOrRight = 0
    var SIDE_MARGIN = 20
    var ANI_DURATION_1 = 200L


    interface ExpandActivity {
        fun onCallback(action: String)
    }


    @SuppressLint("ClickableViewAccessibility")
    fun show(context: Context, source: ImageView, data: CoreData?, listener: ExpandActivity) {

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
        var ogViewPosition: IntArray = intArrayOf(2, 1)
        source.getLocationInWindow(ogViewPosition)
        inFeedPositonX = ogViewPosition[0]
        inFeedPositonY = ogViewPosition[1]
        val changePreviewr = imageView.layoutParams as LinearLayout.LayoutParams
        imageView.visibility = View.GONE
        dialog.icon_label.visibility = View.INVISIBLE
        dialog.side_text.visibility = View.INVISIBLE
        dialog.bottom_text.visibility = View.INVISIBLE
        dialog.set_pin.visibility = View.INVISIBLE
        val sizeX = source.width
        val sizeY = source.height
        val shift = inFeedPositonY.minus(100)

        source.setOnTouchListener(object : View.OnTouchListener {

            var count = 0
            var home_count = 0
            var dir = 1
            var action_touch = ""

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                dialog.card.layoutParams.height = sizeY
                dialog.card.layoutParams.width = sizeX
                changePreviewr.height = sizeY
                changePreviewr.width = sizeX

                if (count == 0) {

                    fingerPositionStartY = event.rawY.toInt()
                    fingerPositionStartX = event.rawX.toInt()
                    count++

                }

                val startYMove = fingerPositionStartY
                val startXMove = fingerPositionStartX

                val movementY = event.rawY.toInt()
                val movementX = event.rawX.toInt()

                val movementYAdjust = movementY
                var staticX = 600
                var leftShift = 300

                fingerLeftOrRight = -1

                if (startXMove < GlobalValues.widthWindow.div(2)) {
                    fingerLeftOrRight = 1
                    staticX = 100
                    leftShift = 0
                }


                if (!GlobalValues.whatsNew) {
                    param.setMargins(SIDE_MARGIN, shift, SIDE_MARGIN, 0)

                } else {

                    if (fingerLeftOrRight < 0) {
                        param.setMargins(GlobalValues.widthWindow.div(2), shift, SIDE_MARGIN, 0)
                    } else {
                        param.setMargins(SIDE_MARGIN, shift, SIDE_MARGIN, 0)
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
                        when (action_touch) {

                            EXPANDED_IMAGE -> {

                                v.parent.requestDisallowInterceptTouchEvent(false)
                                expandStaggered(dialog, data)
                                dialog.root.setOnClickListener {
                                    collapseStaggered(dialog, data)
                                }
                            }

                            else -> {

                                v.parent.requestDisallowInterceptTouchEvent(false)
                                listener.onCallback(SAVE)
                                dialog.dismiss()
                            }
                        }
                        return true
                    }
                }

                if (home_count == 0) {

                    if (fingerLeftOrRight < 0) {

                        paramTopText.setMargins(fingerPositionStartX.minus(leftShift), movementY - GlobalValues.heightWindow.div(5), SIDE_MARGIN, 0)
                        paramBottomText.setMargins(fingerPositionStartX.minus(leftShift), movementY, SIDE_MARGIN, 0)
                        paramSideText.setMargins(fingerPositionStartX.minus(staticX).plus(200), fingerPositionStartY - GlobalValues.heightWindow.div(8), SIDE_MARGIN, 0)
                        iconLabelParm.setMargins(0, movementY.minus(GlobalValues.heightWindow.div(3)), 0, 0)


                    } else {
                        paramTopText.setMargins(fingerPositionStartX.minus(leftShift), movementY - GlobalValues.heightWindow.div(5), SIDE_MARGIN, 0)
                        paramBottomText.setMargins(fingerPositionStartX.minus(leftShift), movementY, SIDE_MARGIN, 0)
                        paramSideText.setMargins(fingerPositionStartX.plus(staticX).plus(200), fingerPositionStartY - GlobalValues.heightWindow.div(8), SIDE_MARGIN, 0)
                        iconLabelParm.setMargins(fingerPositionStartX.plus(staticX).plus(300), movementY.minus(GlobalValues.heightWindow.div(3)), SIDE_MARGIN, 0)
                    }

                    dialog.icon_label.layoutParams = iconLabelParm
                    home_count++
                }

                //    Move object top
                if (startYMove > movementYAdjust && movementY > startYMove.minus(GlobalValues.widthWindow.div(10))) {
                    paramTopText.setMargins(fingerPositionStartX.plus(startYMove.minus(movementY).times(fingerLeftOrRight)).minus(leftShift), movementY - GlobalValues.heightWindow.div(5), SIDE_MARGIN, 0)

                    if (changeTopBool) {
                        dialog.set_pin.setBackgroundResource(R.drawable.ic_pin)
                        dialog.icon_text.text = ""
                        changeTopBool = false
                    }


                } else if (startYMove > movementYAdjust && noneAreBlue()) {
                    dialog.set_pin.setBackgroundResource(R.drawable.ic_pin_blue)
                    dialog.icon_text.text = "Map"
                    action_touch = MAP_OPEN
                    changeTopBool = true
                }

                // Move object bottom
                if (startYMove < movementYAdjust && movementY < startYMove.plus(GlobalValues.widthWindow.div(10))) {

                    dir = -1
                    paramBottomText.setMargins(fingerPositionStartX.minus(startYMove.minus(movementY).times(fingerLeftOrRight)).minus(leftShift), movementY, SIDE_MARGIN, 0)

                    if (changeBottomBool) {
                        dialog.bottom_text.setBackgroundResource(R.drawable.ic_icon_save)
                        dialog.icon_text.text = ""
                        changeBottomBool = false
                    }

                } else if (startYMove < movementYAdjust && noneAreBlue()) {
                    dialog.bottom_text.setBackgroundResource(R.drawable.ic_icon_save_blue)
                    dialog.icon_text.text = "Save"
                    changeBottomBool = true
                }

                // Move object right side of finger
                if (startXMove < movementX && movementX < startXMove.plus(staticX.times(fingerLeftOrRight))) {
                    paramSideText.setMargins(movementX.plus(staticX.times(fingerLeftOrRight)).plus(200), fingerPositionStartY - GlobalValues.heightWindow.div(8), SIDE_MARGIN, 0)

                    if (changeRightBool) {
                        dialog.side_text.setBackgroundResource(R.drawable.ic_expand)
                        dialog.icon_text.text = ""
                        changeRightBool = false
                    }

                } else if (startXMove < movementX && fingerLeftOrRight > 0 && noneAreBlue()) {
                    dialog.side_text.setBackgroundResource(R.drawable.ic_expand_blue)
                    dialog.icon_text.text = "Expand"
                    changeRightBool = true
                    action_touch = EXPANDED_IMAGE
                }

                // Move object left side of finger
                if (fingerLeftOrRight < 0) if (startXMove > movementX && startXMove.minus(GlobalValues.widthWindow.div(10)) < movementX) {
                    paramSideText.setMargins(
                        movementX.minus(staticX).plus(200).minus(movementY.minus(startYMove)),
                        fingerPositionStartY - GlobalValues.heightWindow.div(8),
                        SIDE_MARGIN,
                        0
                    )
                    if (changeLeftBool) {
                        dialog.side_text.setBackgroundResource(R.drawable.ic_expand)
                        dialog.icon_text.text = ""
                        changeLeftBool = false
                    }

                } else if (startXMove > movementX && noneAreBlue()) {
                    dialog.side_text.setBackgroundResource(R.drawable.ic_expand_blue)
                    dialog.icon_text.text = "Expand"
                    changeLeftBool = true
                    action_touch = EXPANDED_IMAGE
                }


                dialog.side_text.visibility = View.VISIBLE
                dialog.bottom_text.visibility = View.VISIBLE
                dialog.set_pin.visibility = View.VISIBLE
                dialog.icon_label.visibility = View.VISIBLE
                return false

            }
        })

    }

    fun expandStaggered(dialog: Dialog, data: CoreData?) {

        val expandWidth = data?.width
        val expandHeight = data?.height
        dialog.top_text.visibility = View.GONE
        dialog.side_text.visibility = View.GONE
        dialog.bottom_text.visibility = View.GONE
        dialog.icon_text.visibility = View.GONE

        if (expandWidth != null && expandHeight != null) {

            val ratio = expandHeight.toDouble().div(expandWidth)
            val width = GlobalValues.widthWindow.minus(SIDE_MARGIN)
            val widthCalc = width.toDouble().times(ratio)
            val height = widthCalc.toInt()

            ViewPropertyObjectAnimator.animate(dialog.card).width(width).height(height).setDuration(ANI_DURATION_1).start()
            ViewPropertyObjectAnimator.animate(dialog.card).leftMargin(10).setDuration(ANI_DURATION_1).start()
            ViewPropertyObjectAnimator.animate(dialog.previewer_image).width(width).height(height).setDuration(ANI_DURATION_1).start()

            val checkOffset = inFeedPositonY + height
            previousOffset = checkOffset

            if (checkOffset > GlobalValues.heightWindow) {
                val offSet = checkOffset - GlobalValues.heightWindow
                ViewPropertyObjectAnimator.animate(dialog.root).verticalMargin(-offSet.toInt()).setDuration(300).start()
            }
        }

    }

    fun collapseStaggered(dialog: Dialog, data: CoreData?) {

        val expandWidth = data?.width
        val expandHeight = data?.height
        dialog.top_text.visibility = View.GONE
        dialog.side_text.visibility = View.GONE
        dialog.bottom_text.visibility = View.GONE
        dialog.icon_text.visibility = View.GONE
        var rootPos: IntArray = intArrayOf(2, 1)
        dialog.previewer_image.getLocationInWindow(rootPos)
        val rootX = rootPos[0]
        val rootY = rootPos[1]

        if (expandWidth != null && expandHeight != null) {
            val ratio = expandHeight.toDouble().div(expandWidth)

            val width = GlobalValues.widthWindow.div(2)
            val widthCalc = width.toDouble().times(ratio)
            val height = widthCalc.toInt()
//
            ViewPropertyObjectAnimator.animate(dialog.card).height(height).setDuration(400).start()
            ViewPropertyObjectAnimator.animate(dialog.previewer_image).width(width).height(height).setDuration(ANI_DURATION_1).start()
            ViewPropertyObjectAnimator.animate(dialog.card).width(width).height(height).setDuration(ANI_DURATION_1).start()

            val offSet = abs(inFeedPositonY).minus(abs(rootY))
            ViewPropertyObjectAnimator.animate(dialog.root).topPadding(offSet).setDuration(ANI_DURATION_1).addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        dialog.dismiss()
                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationStart(animation: Animator?) {

                    }

                }).start()

            if (fingerLeftOrRight < 0) {
                ViewPropertyObjectAnimator.animate(dialog.root).leftPadding(width).setDuration(ANI_DURATION_1).start()
            }

        }

    }

    fun noneAreBlue(): Boolean {
        return !changeTopBool && !changeRightBool && !changeLeftBool && !changeBottomBool
    }

}