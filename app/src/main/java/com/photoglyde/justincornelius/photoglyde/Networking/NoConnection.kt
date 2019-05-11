package com.photoglyde.justincornelius.photoglyde.Networking

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.photoglyde.justincornelius.photoglyde.R
import kotlinx.android.synthetic.main.activity_no_connection.view.*

class NoConnection : AppCompatActivity() {

    override fun onResume() {

        val isConnected = ConnectionUtils().isConnectingToInternet(this)

        if (isConnected) finish()

        super.onResume()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_connection)


    }


    override fun finish() {
        val data = Intent()


        data.putExtra("returnData", "my swoon")

        setResult(AppCompatActivity.RESULT_OK, data)
        super.finish()


    }


    fun onClick(view: View) {

        val isConnected = ConnectionUtils().isConnectingToInternet(this)

        if (isConnected) finish()


    }


}
