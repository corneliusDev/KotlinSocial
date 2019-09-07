package com.photoglyde.justincornelius.photoglyde.Camera

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.constraint.ConstraintSet
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.photoglyde.justincornelius.photoglyde.Camera.Camera2BasicFragment
import com.photoglyde.justincornelius.photoglyde.MainActivity
import com.photoglyde.justincornelius.photoglyde.R


import kotlinx.android.synthetic.main.activity_camer_open.*
import kotlinx.android.synthetic.main.activity_camera_api.*
import kotlinx.android.synthetic.main.content_camer_open.*
import java.io.IOException
import android.graphics.Bitmap
import android.app.Activity
import android.app.PendingIntent.getActivity


class CamerOpenActivity : AppCompatActivity() {

    override fun onPause() {
        super.onPause()
        supportFragmentManager.beginTransaction().remove(fragment).commit()
        println("==========on pause")

    }

    override fun onResume() {

        fragment = Camera2BasicFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()

        super.onResume()
    }



     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
         println("======hitting request1")
        if (resultCode == Activity.RESULT_OK)
            println("======hitting request2")
            when (requestCode) {
                GALLERY_REQUEST -> {
                    val selectedImage = data?.data?.normalizeScheme()

                    val typeMedia = this.contentResolver
                    val type = typeMedia.getType(selectedImage)



                    println("======from data ${selectedImage?.normalizeScheme().toString()} and ${type}")

                    val i = Intent(this, EditPhoto::class.java)
                    i.putExtra("SELECTED", selectedImage.toString())
                    startActivityForResult(i, 102)



                }
            }
    }



    lateinit var fragment: Camera2BasicFragment
    private var GALLERY_REQUEST = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camer_open)


        fragment = Camera2BasicFragment.newInstance()

        savedInstanceState ?: supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()

        println("=======on open am")

        headerPhoto.bringToFront()
        backCameraView.bringToFront()
        switchCamerView.bringToFront()
        backCameraView.setOnClickListener {

            backCameraView.visibility = View.INVISIBLE
            switchCamerView.visibility = View.INVISIBLE
            finish()


        }

        select.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101)
        }


    }

    override fun finish() {
        val data = Intent()


        data.putExtra("returnData", "my swoon")

        setResult(AppCompatActivity.RESULT_OK, data)
        super.finish()


    }
}
