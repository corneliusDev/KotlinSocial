package com.photoglyde.justincornelius.photoglyde.Camera

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.photoglyde.justincornelius.photoglyde.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_photo.*

class FinalPost : AppCompatActivity() {

    lateinit var selectedImage:Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_post)

        selectedImage = Uri.parse(intent.getStringExtra("source"))

        println("=======selected $selectedImage")

        Picasso.get().load(selectedImage).into(selected_photo)
    }
}
