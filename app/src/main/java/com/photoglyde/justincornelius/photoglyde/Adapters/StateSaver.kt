package com.photoglyde.justincornelius.photoglyde.Adapters



     interface StateSaver {

        fun saveState(adapterPosition: Int, itemPosition: Int, lastItemAngleShift: Double)
    }
