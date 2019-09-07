package com.photoglyde.justincornelius.photoglyde.Networking
import android.support.v7.util.DiffUtil
import com.photoglyde.justincornelius.photoglyde.Data.CoreUnSplash

class sameCheckDifficulti : DiffUtil.ItemCallback<CoreUnSplash>() {
  override fun areItemsTheSame(p0: CoreUnSplash, p1: CoreUnSplash): Boolean {
    return false
  }

  override fun areContentsTheSame(p0: CoreUnSplash, p1: CoreUnSplash): Boolean {
    return false
  }
}
