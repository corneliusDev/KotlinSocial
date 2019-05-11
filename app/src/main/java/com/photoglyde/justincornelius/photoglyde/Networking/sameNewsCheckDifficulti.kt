package com.photoglyde.justincornelius.photoglyde.Networking
import android.support.v7.util.DiffUtil
import com.photoglyde.justincornelius.photoglyde.Data.GrabImageData


class sameNewsCheckDifficulti : DiffUtil.ItemCallback<NYCTimesDataResponse>() {

  override fun areItemsTheSame(p0: NYCTimesDataResponse, p1: NYCTimesDataResponse): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getChangePayload(oldItem: NYCTimesDataResponse, newItem: NYCTimesDataResponse): Any? {
    return super.getChangePayload(oldItem, newItem)
  }

  override fun areContentsTheSame(p0: NYCTimesDataResponse, p1: NYCTimesDataResponse): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}
