package com.photoglyde.justincornelius.photoglyde.utilities

import android.view.View
import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator
import com.photoglyde.justincornelius.photoglyde.data.BOTTOM_NAV_ANITIME
import com.photoglyde.justincornelius.photoglyde.UI.adapter.FeedAdapter
import com.photoglyde.justincornelius.photoglyde.UI.adapter.OnItemLockClickListener
import com.photoglyde.justincornelius.photoglyde.data.CoreData
import com.photoglyde.justincornelius.photoglyde.data.GlobalValues
import com.photoglyde.justincornelius.photoglyde.UI.custom.ImagePreview
import kotlinx.android.synthetic.main.adapter_row_similar.view.*

object FeedAdapterListener : OnItemLockClickListener {

    override fun onItemLongClick(view: View, position: Int, core: CoreData?) {

        GlobalValues.sendIndexToDetail = position

        println("Inside Feed Adapter +++++++++++++++++")

        ViewPropertyObjectAnimator.animate(view.placeImage).scaleY(1.0f).scaleX(1.0f).setDuration(BOTTOM_NAV_ANITIME).start()

        if (view.placeImage != null) {

            ImagePreview()
                .show(view.context, view.placeImage, core, object : ImagePreview.ExpandActivity{

                override fun onCallback(action:String) {

                }
            })

        }else{
         //   Helper.show(view.context, view.player, core)
        }

    }


}