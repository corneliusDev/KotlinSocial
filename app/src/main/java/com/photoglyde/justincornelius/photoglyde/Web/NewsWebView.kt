package com.photoglyde.justincornelius.photoglyde.Web

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.widget.TextView
import android.widget.Toolbar
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals
import com.photoglyde.justincornelius.photoglyde.ExapandDetailActivity
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.R.id.animationLoad
import com.photoglyde.justincornelius.photoglyde.R.id.webview
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_news_web_view.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewsWebView : AppCompatActivity() {

    lateinit var URL:String
    lateinit var URL_IMAGE:String
    lateinit var TITLE:String

    private lateinit var toolbar: android.support.v7.widget.Toolbar
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){

            android.R.id.home ->{

               //finish()

                finishAfterTransition()
            }

        }

        return super.onOptionsItemSelected(item)
    }


    // When you go inside webpage to webpage and then click Back button app will close. So you can override back button override fun onBackPressed() and Handle back press.


    override fun onBackPressed() {
        if (webview.canGoBack()) {
            // If web view have back history, then go to the web view back history

            webview.goBack()
        }
    }


    companion object {
        val EXTRA_PARAM_ID = "place_id"

        fun newIntent(context: Context, position: Int): Intent {
            println("=======thid id my position $position")

            //   val new = GlobalVals.imageClassUser[position]
            val intent = Intent(context, NewsWebView::class.java)
            intent.putExtra(EXTRA_PARAM_ID, position)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_web_view)

        URL = intent.getStringExtra("URL")
        URL_IMAGE = intent.getStringExtra("URL_IMAGE")
        TITLE = intent.getStringExtra("TITLE")




        webview.visibility = View.INVISIBLE
        animationLoad.bringToFront()
        setUpToolBar()

        //Picasso.get().load(URL_IMAGE).into(news_transition_image)
        GlobalVals.currentCreator?.into(news_transition_image)
        val setTile = findViewById<TextView>(R.id.title)
        setTile.text = TITLE



            setUpWebView(URL)










//
//        webview.webViewClient = object : WebViewClient(){
//            override fun onPageFinished(view: WebView?, url: String?) {
//
//                webview.visibility = View.VISIBLE
//                animationLoad.cancelAnimation()
//                super.onPageFinished(view, url)
//
//
//
//            }
//
//
//        }







    }

    private fun setUpWebView(url:String){
        // Get the web view settings instance
        val settings = webview.settings

        // Enable java script in web view
        settings.javaScriptEnabled = true

        // Enable and setup web view cache
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCachePath(cacheDir.path)


        // Enable zooming in web view
        settings.setSupportZoom(true)
        settings.builtInZoomControls = true
        settings.displayZoomControls = true


        // Zoom web view text
        //settings.textZoom = 125


        // Enable disable images in web view
        settings.blockNetworkImage = false
        // Whether the WebView should load image resources
        settings.loadsImagesAutomatically = true


        // More web view settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = true  // api 26
        }
        //settings.pluginState = WebSettings.PluginState.ON
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.mediaPlaybackRequiresUserGesture = false
        }

        // More optional settings, you can enable it by yourself
        settings.domStorageEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        settings.setGeolocationEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.allowUniversalAccessFromFileURLs = true
        }

        settings.allowFileAccess = true

        // WebView settings
        webview.fitsSystemWindows = true

        /* if SDK version is greater of 19 then activate hardware acceleration
        otherwise activate software acceleration  */

        webview.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        webview.loadUrl(url)

        // Set web view client
        webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                // Page loading started
                // Do something
            }

            override fun onPageFinished(view: WebView, url: String) {
                // Page loading finished
                // Enable disable back forward button
                animationLoad.visibility = View.GONE
                webview.visibility = View.VISIBLE
            }
        }
    }


    fun setUpToolBar(){
        toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar)
        toolbar.bringToFront()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24px)

    }

    override fun finish() {
        val data = Intent()


        data.putExtra("returnData", "my swoon")

        setResult(AppCompatActivity.RESULT_OK, data)
        super.finish()


    }
    }

