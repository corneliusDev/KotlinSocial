package com.photoglyde.justincornelius.photoglyde.utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.google.android.exoplayer2.ui.PlayerView

import com.photoglyde.justincornelius.photoglyde.UI.custom.ImagePreviewerUtils
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.UI.activity.ExapandDetailActivity
import java.util.*
import kotlin.collections.ArrayList

object Helper {
    // Normally this would be done serverside, but for the purpose of this app, this function randomly inserts categories by popping them off the category stack.
    fun main(args : List<CoreUnSplash?>) : List<CoreUnSplash> {

            var arg: ArrayList<CoreUnSplash>? = null

            if(GlobalValues.whatsNew && !GlobalValues.cameFromExa) {
                arg = args as ArrayList<CoreUnSplash>
                val from = 0
                // randomly inserts categories inside list
                val to = args.size.div(2)
                val random = Random()
                val categories = GlobalValues.listCateg

                if (categories.size > 0 && to > 0) {

                    val amplititudes = IntArray(categories.size.div(3)) { random.nextInt(to - from) + from }.asList()
                    val data1 = CoreUnSplash()
                    data1.type = "GRID"

                    for (i in 0..amplititudes.size.minus(1)) {
                        arg.add(amplititudes[i], GlobalValues.listCateg[categories.count()-1]!!)
                        GlobalValues.listCateg.removeAt(categories.count()-1)
                    }
                }
            }

        return arg?: args as List<CoreUnSplash>

    }





    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    fun show(context: Context, source: PlayerView, core:CoreUnSplash?) {
        val background = ImagePreviewerUtils()
            .getBlurredScreenDrawable(context, source.rootView)


        val dialogView = LayoutInflater.from(context).inflate(R.layout.view_player, null)
        val imageView = dialogView.findViewById(R.id.previewer_player) as PlayerView

        imageView.player = source.player

        var resize = imageView.layoutParams
        val width = GlobalValues.widthWindow
        resize.width = width
        //add null check
        val ratio = core?.height?.div(core.width!!)
        val finalHeight = width.times(ratio!!)

        resize.height = finalHeight.toInt()

        val dialog = Dialog(context, R.style.ImagePreviewerTheme)
        dialog.window?.setBackgroundDrawable(background)
        dialog.setContentView(dialogView)
        dialog.show()

        dialogView.setOnClickListener {
            dialog.dismiss()
            source.player.release()

        }

    }

    fun deliverIntent(core: CoreUnSplash?, context: Context, ref1:String?, ref2:String?, increment:Int?) : Intent {
        val transitionIntent = ExapandDetailActivity.newIntent(context)
        transitionIntent.putExtra(TYPE, core?.type)
        transitionIntent.putExtra(IMAGE_URI, core?.urls?.regular?: core?.categ_image_uri)
        transitionIntent.putExtra(HEIGHT, core?.height)
        transitionIntent.putExtra(WIDTH, core?.width)
        transitionIntent.putExtra(CAME_FROM, HOME)
        transitionIntent.putExtra(IMAGE_TAG, core?.user?.location)
        transitionIntent.putExtra(REF_1, ref1)
        transitionIntent.putExtra(REF_2, ref2)
        transitionIntent.putExtra(INCREMENT, increment)

        return transitionIntent
    }

    fun deliverOptions(view: View, activity: Activity) : ActivityOptionsCompat {


        return if (view.findViewById<ImageView>(R.id.placeImage) != null){
            val imagePair = Pair.create(view.findViewById(R.id.placeImage) as View, "x")
            val pairs = mutableListOf(imagePair)
            ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pairs.toTypedArray())
        }else if (view.findViewById<ImageView>(R.id.placeImageH) != null){
            val imagePair = Pair.create(view.findViewById(R.id.placeImageH) as View, "x")
            val pairs = mutableListOf(imagePair)
            ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pairs.toTypedArray())
        }else{
            val imagePair = Pair.create(view.findViewById(R.id.placeImageHere) as View, "x")
            val pairs = mutableListOf(imagePair)
            ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pairs.toTypedArray())
        }


    }

    }
