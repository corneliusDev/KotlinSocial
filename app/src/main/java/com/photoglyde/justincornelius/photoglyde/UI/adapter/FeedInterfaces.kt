package com.photoglyde.justincornelius.photoglyde.UI.adapter

import android.view.View
import com.photoglyde.justincornelius.photoglyde.data.CoreData

interface OnItemLockClickListener{
    fun onItemLongClick(view: View, position: Int, core: CoreData?)
}

interface OnItemClickListener {
    fun onItemClick(view: View, position: Int, data:CoreData)
}

interface AnimationEnd {
    fun animationOver()
}