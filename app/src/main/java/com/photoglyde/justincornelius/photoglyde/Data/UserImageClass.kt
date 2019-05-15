package com.photoglyde.justincornelius.photoglyde.Data

import android.net.Uri

class UserImageClass(name:String, desc:String, image:Uri) {

        var name:String? = null
        var desc:String? = null
        var image:Uri? = null


    init {


        this.name = name
        this.desc = desc
        this.image = image


    }


}