package com.photoglyde.justincornelius.photoglyde.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair

import com.photoglyde.justincornelius.photoglyde.data.*
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.UI.activity.ExapandDetailActivity
import java.util.*
import kotlin.collections.ArrayList

object Helper {
    // Normally this would be done serverside, but for the purpose of this app, this function randomly inserts categories by popping them off the category stack.

    fun main(args : List<CoreData?>) : List<CoreData> {

            var arg: ArrayList<CoreData>? = null

            if(GlobalValues.whatsNew && !GlobalValues.cameFromCateg) {

                arg = args as ArrayList<CoreData>
                val from = 0
                // randomly inserts categories inside list
                val to = args.size.div(2)
                val random = Random()
                val categories = GlobalValues.listCateg

                if (categories.size > 0 && to > 0) {

                    val amplititudes = IntArray(categories.size.div(3)) { random.nextInt(to - from) + from }.asList()
                    val data1 = CoreData()
                    data1.type = "GRID"

                    for (i in 0..amplititudes.size.minus(1)) {
                        arg.add(amplititudes[i], GlobalValues.listCateg[categories.count()-1]!!)
                        GlobalValues.listCateg.removeAt(categories.count()-1)
                    }
                }
            }

        return arg?: args as List<CoreData>

    }


    fun deliverIntent(core: CoreData?, context: Context, ref1:String?, ref2:String?, increment:Int?) : Intent {
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

    fun deliverOptions(view: View, activity: Activity) : ActivityOptionsCompat? {


        return when {
            view.findViewById<ImageView>(R.id.placeImage) != null -> {
                val imagePair = Pair.create(view.findViewById(R.id.placeImage) as View, "x")
                val pairs = mutableListOf(imagePair)
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pairs.toTypedArray())
            }
            view.findViewById<ImageView>(R.id.placeImageH) != null -> {
                val imagePair = Pair.create(view.findViewById(R.id.placeImageH) as View, "x")
                val pairs = mutableListOf(imagePair)
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pairs.toTypedArray())
            }
            view.findViewById<ImageView>(R.id.placeImageHere) != null -> {
                val pairs = mutableListOf<Pair<View, String>>()
                if (!GlobalValues.cameFromCateg) {
                    val imagePair = Pair.create(view.findViewById(R.id.placeImageHere) as View, "x")
                    val textPair = Pair.create(view.findViewById(R.id.textFullView) as View, "categ_text")
                    val underPair = Pair.create(view.findViewById(R.id.text_underlay) as View, "under_lay")
                    pairs.addAll(mutableListOf(imagePair, textPair, underPair))
                }
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pairs.toTypedArray())
            }

            else ->{
                null
            }
        }


    }

    }
