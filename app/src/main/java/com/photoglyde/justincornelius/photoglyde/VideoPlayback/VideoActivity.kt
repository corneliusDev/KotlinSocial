package com.photoglyde.justincornelius.photoglyde.VideoPlayback

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.photoglyde.justincornelius.photoglyde.R
import com.google.android.exoplayer2.ui.PlayerView
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals

import com.photoglyde.justincornelius.photoglyde.Helper

import kotlinx.android.synthetic.main.activity_video.*
import android.support.v4.view.ViewCompat.setRotation




class VideoActivity : AppCompatActivity(), VideoControllerContract.View {

    private lateinit var videoView: PlayerView

    private lateinit var presenter: VideoControllerContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

            val video = intent.getStringExtra("VIDEO")
            val seekPos = intent.getIntExtra("videoPos", 0)
        val URIuse = video


        println("====here are the values $video and ${seekPos.toLong()}")

        init2(URIuse, seekPos)


    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            presenter.releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            presenter.releasePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.deactivate()
        presenter.setMediaSessionState(false)
    }


    private fun init(uri:String?, seekPos:Int) {
        presenter = VideoPresenter(this)

        //val videoUrl = intent.getStringExtra("VIDEO")

        val videoDimen = Helper.getVideoHeight(Uri.parse(uri), this)

        println("======== here are the video dimensions ${videoDimen}")

        videoView = findViewById(R.id.video_view)
        val paramVideo = videoView.layoutParams
        val ratio = videoDimen.last().toDouble().div(videoDimen.first().toDouble())
        val calcWidth = GlobalVals.widthWindow - 100
        var calcHeight = calcWidth.toDouble().times(ratio) -200



        if (calcHeight > GlobalVals.heightWindow - 300){
            val calcHeight = GlobalVals.heightWindow - 300
        }

        println("========lets ceck new heights here $ratio and $calcWidth and $calcHeight and $uri")

        paramVideo.width = calcWidth
        paramVideo.height = calcHeight.toInt()



        videoView.player = presenter.getPlayer().getPlayerImpl(this)
        videoView.player.seekTo(seekPos.toLong())

        presenter.play(uri.toString())



        buttonCommit.setOnClickListener {
            Helper.VideoRender(this, uri).run()
            finish()
        }





    }

    private fun init2(uri:String?, seekPos:Int) {
        presenter = VideoPresenter(this)

        //val videoUrl = intent.getStringExtra("VIDEO")

        //val videoDimen = Helper.getVideoHeight(Uri.parse(uri), this)

        println("======== here are the video dimensions")

        videoView = findViewById(R.id.video_view)
        val paramVideo = videoView.layoutParams
       // val ratio = videoDimen.last().toDouble().div(videoDimen.first().toDouble())
        val calcWidth = GlobalVals.widthWindow - 100
        var calcHeight = calcWidth



        if (calcHeight > GlobalVals.heightWindow - 300){
            val calcHeight = GlobalVals.heightWindow - 300
        }

        println("========lets ceck new heights here  and $calcWidth and $calcHeight and $uri")

        paramVideo.width = calcWidth
        paramVideo.height = calcHeight.toInt()



        videoView.player = presenter.getPlayer().getPlayerImpl(this)
        videoView.player.seekToDefaultPosition(seekPos)

        presenter.play(uri.toString())

        buttonCommit.setOnClickListener {
            Helper.VideoRender(this, uri).run()
            finish()
        }





    }


    fun onClick(view:View){




    }

    override fun finish() {
        val data = Intent()


        data.putExtra("returnData", "my swoon")

        setResult(AppCompatActivity.RESULT_OK, data)
        super.finish()


    }


    companion object {

        const val VIDEO_URL_EXTRA = "video_url_extra"
        private val EXTRA_PARAM_ID = "place_id"

        fun newIntent(context: Context, position: Int): Intent {
            println("=======thid id my position $position")

            //   val new = GlobalVals.imageClassUser[position]
            val intent = Intent(context, VideoActivity::class.java)
            intent.putExtra(EXTRA_PARAM_ID, position)
            return intent
        }
    }

}
