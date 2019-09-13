package com.photoglyde.justincornelius.photoglyde.UI.activity
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.*
import com.photoglyde.justincornelius.photoglyde.UI.adapter.FeedAdapter
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.utilities.Helper
import com.photoglyde.justincornelius.photoglyde.UI.custom.ImagePreview
import com.photoglyde.justincornelius.photoglyde.utilities.ScrollDownListener
import com.photoglyde.justincornelius.photoglyde.Data.ImageDataSource
import com.photoglyde.justincornelius.photoglyde.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_test2.*
import kotlinx.android.synthetic.main.adapter_row_similar.view.*
import kotlinx.android.synthetic.main.test_include.*
import java.lang.Exception
import kotlin.math.roundToInt



class ExapandDetailActivity : AppCompatActivity()  {

    private lateinit var staggeredLayoutManager: androidx.recyclerview.widget.StaggeredGridLayoutManager
    private lateinit var toolbar: Toolbar
    var type:String? = ""
    private lateinit var adapterProfile: FeedAdapter


      companion object {
            fun newIntent(context: Context): Intent {
              val intent = Intent(context, ExapandDetailActivity::class.java)
              return intent
            }
      }

    override fun onResume() {
        super.onResume()
        GlobalValues.whatsNew = true
    }

    private val longClickListener = object : FeedAdapter.OnItemLockClickListener {

        override fun onItemLongClick(view: View, position: Int, core:CoreUnSplash?) {

            GlobalValues.currentCore = core

            GlobalValues.sendIndexToDetail = position

            ImagePreview()
                .show(this@ExapandDetailActivity, view.placeImage, object : ImagePreview.ExpandActivity{

                override fun onCallback(action:String) {

                    when(action){

                        EXPANDED_IMAGE ->{
                            ActivityCompat.startActivity(this@ExapandDetailActivity,
                                Helper.deliverIntent(
                                    core,
                                    this@ExapandDetailActivity,
                                    null,
                                    null,
                                    null
                                ), Helper.deliverOptions(
                                    view,
                                    this@ExapandDetailActivity
                                ).toBundle())
                        }

                    }

                }
            })
            expanded_image_holder_sec.bringToFront()
            expanded_linear_sec.bringToFront()
            expanded_image_sec.bringToFront()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){

            android.R.id.home ->{
                onBackPressed()
            }

        }

        return super.onOptionsItemSelected(item)

    }

    private val onItemClickListenerVertical = object : FeedAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int, data:CoreUnSplash) {

            GlobalValues.isExpanded = true
            GlobalValues.awayFromFrag = true
            ActivityCompat.startActivity(view.context,
                Helper.deliverIntent(
                    data,
                    this@ExapandDetailActivity,
                    null,
                    null,
                    null
                ), Helper.deliverOptions(view, this@ExapandDetailActivity).toBundle())
            finishAfterTransition()

        }
    }

  override fun onCreate(savedInstanceState: Bundle?){
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_test2)

      text_categ.bringToFront()
      setUpToolBar()

      val cameFrom:String? = intent.getStringExtra(CAME_FROM)
      type = intent.getStringExtra(TYPE).toString()
      val image_categ = intent.getStringExtra(IMAGE_URI).toString()


      if (cameFrom == HOME){
          val imageTag:String? = intent.getStringExtra(IMAGE_TAG)
          collapsingToolbarLayout.setBackgroundResource(R.color.dark_gray)
          toolbar.setBackgroundResource(R.color.dark_gray)
          text_categ.text = imageTag
      }

      val dimensions = placeImage2.layoutParams
      val imageHeight = intent.getDoubleExtra(HEIGHT, 0.0)
      val imageWidth = intent.getDoubleExtra(WIDTH, 0.0)

    // run refers to the context object as a lambda receiver
     if (imageHeight != 0.0) {
         val ratio = imageHeight.run { div(imageWidth) }
         val ratioHeight = GlobalValues.run { widthWindow.times(ratio).roundToInt() }
         dimensions.apply {
             this.height = ratioHeight
             this.width = GlobalValues.widthWindow
         }
     }else{
         dimensions.apply {
             this.height = GlobalValues.widthWindow
             this.width = GlobalValues.widthWindow
         }
     }

      loadPlace(image_categ)

  }

  private fun loadPlace(uri:String?) {
      supportPostponeEnterTransition()
          Picasso.get()
              .load(Uri.parse(uri))
              .fit()
              .noFade()
              .centerCrop()
              .into(placeImage2, object : Callback {
                  override fun onSuccess() {

                      supportStartPostponedEnterTransition()

                      if (type == GlobalValues.CATEGORY) {
                          val ref1 = intent.getStringExtra(REF_1).toString()
                          val ref2 = intent.getStringExtra(REF_2).toString()
                          val increment = intent.getIntExtra(INCREMENT, 0)
                          if (increment > 0) initializeList(ref1, ref2, increment)
                      }

                  }
                  override fun onError(e: Exception?) {
                      supportStartPostponedEnterTransition()
                  }
              })

  }

  override fun onBackPressed() {

      GlobalValues.isExpanded = false

      if (GlobalValues.awayFromFrag) {
          GlobalValues.awayFromFrag = false
          finish()
      }else{
          finish()
      }

  }

    override fun onPause() {
        super.onPause()
        GlobalValues.whatsNew = false
    }

    private fun initializeList(ref1:String?, ref2:String?, count:Int) {

        adapterProfile = FeedAdapter()
        expanded_staggered_list?.apply {
            layoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(
                2,
                androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
            )
            expanded_staggered_list.adapter = adapterProfile
        }

        adapterProfile.SetOnLock(longClickListener)
        adapterProfile.setOnItemClickListener(onItemClickListenerVertical)

       //blanked out for no wifi work session
        val config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(false)
            .build()
        GlobalValues.test = GlobalValues.test + 1

        val liveData = initializedPagedListBuilder(config, ref1, ref2, count).build()
        liveData.observe(this, androidx.lifecycle.Observer<PagedList<CoreUnSplash>> { pagedList ->
                adapterProfile.submitList(pagedList)
        })

        ScrollDownListener()
            .show(this@ExapandDetailActivity, expanded_staggered_list, object : ScrollDownListener.HideShow{
            override fun onCallback(animate: String) {

            }
        })

    }




    private fun initializedPagedListBuilder(config: PagedList.Config, ref1:String?, ref2:String?, count:Int):
            LivePagedListBuilder<String, CoreUnSplash> {
        val dataSourceFactory = object : DataSource.Factory<String, CoreUnSplash>() {
            override fun create(): DataSource<String, CoreUnSplash> {
                return ImageDataSource(ref1, ref2, count)
            }
        }
        return LivePagedListBuilder<String, CoreUnSplash>(dataSourceFactory, config)
    }

    private fun setUpToolBar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24px)
        }
        window.apply {
            sharedElementEnterTransition.duration = 100L
        }
    }
}


