package com.photoglyde.justincornelius.photoglyde.utilities

import androidx.recyclerview.widget.DiffUtil
import com.photoglyde.justincornelius.photoglyde.data.CoreData
// finish this please

class ItemsComparison : DiffUtil.ItemCallback<CoreData>() {
    override fun areItemsTheSame(p0: CoreData, p1: CoreData): Boolean {
        return p0.id == p1.id
    }

    override fun areContentsTheSame(p0: CoreData, p1: CoreData): Boolean {
        return p0 == p1
    }
}