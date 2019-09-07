package com.photoglyde.justincornelius.photoglyde.Camera

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.photoglyde.justincornelius.photoglyde.Helper

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_photo.*
import java.io.File
import java.net.URI
import android.graphics.Bitmap

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.photoglyde.justincornelius.photoglyde.Adapters.LocationAddAdapter
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.Networking.PostUN
import com.photoglyde.justincornelius.photoglyde.R
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_edit_photo.view.*
import java.lang.Exception


class EditPhoto : AppCompatActivity() {
    private lateinit var selectedImage:Uri
    lateinit var adapterLocation:LocationAddAdapter
    lateinit var adapterTags:LocationAddAdapter
    lateinit var mLocationEditText:EditText
    lateinit var mTagEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.photoglyde.justincornelius.photoglyde.R.layout.activity_edit_photo)

        mLocationEditText = findViewById<EditText>(R.id.location_edit)
        mTagEditText = findViewById<EditText>(R.id.location_tags_edittext)


        val recyclernTags = findViewById<RecyclerView>(R.id.tags_list)
        val arrayTags = ArrayList<String?>()
        val adapterTags = LocationAddAdapter(arrayTags, { partItem: ArrayList<String?>, position: Int -> clickedTags(partItem, position) })
        recyclernTags.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        recyclernTags.adapter = adapterTags





        val recyclerLocation = findViewById<RecyclerView>(R.id.location_tags_edit)
        val arrayLocation = ArrayList<String?>()
        val adapterLocation = LocationAddAdapter(arrayLocation, { partItem: ArrayList<String?>, position: Int -> clickedLocation(partItem, position) })
        recyclerLocation.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        recyclerLocation.adapter = adapterLocation



        val locationValue = mLocationEditText.text

        location_button.setOnClickListener {
            println("this is the entered value ${mLocationEditText.text} and ${mLocationEditText.text.toString().trim()}")
            if (TextUtils.isEmpty(mLocationEditText.text)) {
                location_edit.error = "Add a Location"
                return@setOnClickListener
            }


            arrayLocation.add(mLocationEditText.text.toString().trim())
            recyclerLocation.adapter = adapterLocation
            adapterLocation.notifyItemInserted(arrayLocation.size - 1)
            adapterLocation.notifyDataSetChanged()
            location_edit.text.clear()


        }


        val tagValue = mTagEditText.text

        tags_button.setOnClickListener {
            println("this is the entered value ${tagValue} and ${tagValue.toString().trim()}")
            if (TextUtils.isEmpty(mTagEditText.text)) {
                location_tags_edittext.error = "Add a Location"
                return@setOnClickListener
            }


            arrayTags.add(mTagEditText.text.toString().trim())
            recyclernTags.adapter = adapterTags
            adapterTags.notifyItemInserted(arrayTags.size - 1)
            adapterTags.notifyDataSetChanged()
            location_tags_edittext.text?.clear()


        }









        println("FILE: " + intent.getStringExtra("source"))

        selectedImage = Uri.parse(intent.getStringExtra("source"))

      //  FileHandler(this,File(selectedImage.toString()).name.toString(), selectedImage)

        println("=======selected $selectedImage")



//        val target: Target = object : Target {
//            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                println("************ Prepared")
//            }
//
//            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
//                println("************ Failed")
//            }
//
//            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
//                println("************ Bitmap")
//
//                if (bitmap != null){
//                  //  saveme(this, file, bitmap)
//                }
//            }
//        }

        Picasso.get().load(selectedImage)
            .into(selected_photo)

//        Picasso.get().load(selectedImage)
//            .into(target)

        close_edit_photo.setOnClickListener {
           finish()
        }



     //   FileHandler(this, File(selectedImage.toString()).name, selectedImage).run()


    }



//    fun onClick(view:View){
//
//        var dimensions = ArrayList<Int>()
//
//        dimensions = getDropboxIMGSize(selectedImage)
//
//        println("========== here is the Uri dimension $dimensions")
//
//       // val imageNew = ImageClass(selectedImage, Helper.getDateTimeLong().toInt(), null,null,null,GlobalVals.currentUser!!.userName, , dimensions[1])
//
//        //check rules for nullanility here
//        val profile_image = GlobalVals.currentUser!!.userImageURI.toString()
//        val imageNew = ImageClass()
//        imageNew.urls = Urls("","",selectedImage.toString())
//        imageNew.created = Helper.getDateTimeLong().toString()
//        imageNew.username = GlobalVals.currentUser!!.userName
//        imageNew.height = dimensions[1].toDouble()
//        imageNew.width = dimensions[0].toDouble()
//        imageNew.profile_image = ProfileImage("",profile_image)
//        GlobalVals.currentUser?.userImages?.add(imageNew)
//
//
//
//
//
//       // PostUN().postStoragePhoto(imageNew, ArrayList<String>(), TYPE_PHOTO)
//
//
//
//
//
//        println("========== image class size check ${GlobalVals.currentUser?.userImages?.last()?.height}")
//
//        finish()
//
//    }

    override fun finish() {
        val data = Intent()
        setResult(AppCompatActivity.RESULT_OK, data)
        super.finish()


    }

    private fun clickedTags(partItem: ArrayList<String?>, index: Int) {



    }

    private fun clickedLocation(partItem: ArrayList<String?>, index: Int) {



    }





}

fun getDropboxIMGSize(uri:Uri): ArrayList<Int>{

    var dimen = ArrayList<Int>()
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(File(uri.path).absolutePath, options)
    val imageHeight = options.outHeight
    val imageWidth = options.outWidth

    println("======= dimensions rendered $${imageWidth} and ${imageHeight}")
    dimen.add(imageWidth)
    dimen.add(imageHeight)

    return dimen

}




