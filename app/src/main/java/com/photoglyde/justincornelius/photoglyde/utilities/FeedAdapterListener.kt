package com.photoglyde.justincornelius.photoglyde.utilities

import android.view.View
import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator
import com.photoglyde.justincornelius.photoglyde.Data.BOTTOM_NAV_ANITIME
import com.photoglyde.justincornelius.photoglyde.UI.adapter.FeedAdapter
import com.photoglyde.justincornelius.photoglyde.Data.CoreData
import com.photoglyde.justincornelius.photoglyde.Data.GlobalValues
import com.photoglyde.justincornelius.photoglyde.UI.adapter.SimplePlayerViewHolder
import com.photoglyde.justincornelius.photoglyde.UI.custom.ImagePreview
import kotlinx.android.synthetic.main.adapter_row_similar.view.*
import kotlinx.android.synthetic.main.view_holder_exoplayer_basic.view.*

object FeedAdapterListener : FeedAdapter.OnItemLockClickListener {

    override fun onItemLongClick(view: View, position: Int, core: CoreData?) {

        GlobalValues.sendIndexToDetail = position

        ViewPropertyObjectAnimator.animate(view.placeImage).scaleY(1.0f).scaleX(1.0f).setDuration(BOTTOM_NAV_ANITIME).start()

        if (view.placeImage != null) {

            ImagePreview()
                .show(view.context, view.placeImage, core, object : ImagePreview.ExpandActivity{

                override fun onCallback(action:String) {

                }
            })

        }else{
            Helper.show(view.context, view.player, core)
        }

    }


}