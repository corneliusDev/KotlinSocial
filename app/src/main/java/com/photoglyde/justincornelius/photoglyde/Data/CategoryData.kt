package com.photoglyde.justincornelius.photoglyde.Data


data class CategoriesList(
    var categ_list:ArrayList<CategoryData>
)

data class CategoryData(
    var categ_name:String? = "",
    var categ_image_uri:String? = "",
    var categ_created:String? = ""
)