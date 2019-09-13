package com.photoglyde.justincornelius.photoglyde.utilities

import androidx.recyclerview.widget.DiffUtil
import com.photoglyde.justincornelius.photoglyde.Data.CoreUnSplash
// finish this please

class ItemsComparison : DiffUtil.ItemCallback<CoreUnSplash>() {
    override fun areItemsTheSame(p0: CoreUnSplash, p1: CoreUnSplash): Boolean {
        return p0.id == p1.id
    }

    override fun areContentsTheSame(p0: CoreUnSplash, p1: CoreUnSplash): Boolean {
        return p0 == p1
    }
}