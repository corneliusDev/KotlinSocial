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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.photoglyde.justincornelius.photoglyde.UI.adapter.FeedAdapter
import com.photoglyde.justincornelius.photoglyde.data.*
import com.photoglyde.justincornelius.photoglyde.utilities.Helper
import com.photoglyde.justincornelius.photoglyde.UI.custom.ImagePreview
import com.photoglyde.justincornelius.photoglyde.utilities.ScrollDownListener
import com.photoglyde.justincornelius.photoglyde.data.ImageDataSource
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.UI.adapter.OnItemClickListener
import com.photoglyde.justincornelius.photoglyde.UI.adapter.OnItemLockClickListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_test2.*
import kotlinx.android.synthetic.main.adapter_row_similar.view.*
import kotlinx.android.synthetic.main.test_include.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.math.roundToInt



class ExapandDetailActivity : AppCompatActivity()  {

    private lateinit var toolbar: Toolbar
    var type:String? = ""
    private lateinit var adapterProfile: FeedAdapter
    private lateinit var api: DatabaseReference


      companion object {
            fun newIntent(context: Context): Intent {
                return Intent(context, ExapandDetailActivity::class.java)
            }
      }

    override fun onResume() {
        super.onResume()


    }

    private val longClickListener = object : OnItemLockClickListener {

        override fun onItemLongClick(view: View, position: Int, core:CoreData?) {

            GlobalValues.currentCore = core

            ImagePreview().show(this@ExapandDetailActivity, view.placeImage, core, object : ImagePreview.ExpandActivity{

                override fun onCallback(action:String) {

                    when(action){

                        EXPANDED_IMAGE ->{
                            ActivityCompat.startActivity(this@ExapandDetailActivity,
                                Helper.deliverIntent(core, this@ExapandDetailActivity, null, null, null), Helper.deliverOptions(view, this@ExapandDetailActivity)?.toBundle()
                            )
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

    private val onItemClickListenerVertical = object : OnItemClickListener {
        override fun onItemClick(view: View, position: Int, data:CoreData) {

            GlobalValues.awayFromFrag = true
            ActivityCompat.startActivity(view.context, Helper.deliverIntent(data, this@ExapandDetailActivity, null, null, null),
                Helper.deliverOptions(view, this@ExapandDetailActivity)?.toBundle())
            finishAfterTransition()

        }
    }

  override fun onCreate(savedInstanceState: Bundle?){
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_test2)

      setUpToolBar()

      val cameFrom:String? = intent.getStringExtra(CAME_FROM)
      type = intent.getStringExtra(TYPE).toString()
      val image_categ = intent.getStringExtra(IMAGE_URI).toString()
      if (GlobalValues.cameFromCateg)   text_underlay_expand.visibility = View.INVISIBLE


      if (cameFrom == HOME){
          val imageTag:String? = intent.getStringExtra(IMAGE_TAG)
          collapsingToolbarLayout.setBackgroundResource(R.color.dark_gray)
          toolbar.setBackgroundResource(R.color.dark_gray)
          if (!GlobalValues.whatsNew) text_categ.text = imageTag
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

      GlobalScope.launch {

          delay(1000L)

          if (type == GlobalValues.CATEGORY) {
              runOnUiThread {
                  GlobalValues.cameFromCateg = true
                  val ref1 = intent.getStringExtra(REF_1).toString()
                  val ref2 = intent.getStringExtra(REF_2).toString()
                  val increment = intent.getIntExtra(INCREMENT, 0)
                  api = FirebaseDatabase.getInstance().getReference(ref1).child(ref2)
                  if (increment > 0) initializeList()
              }
          }
      }

  }

  private fun loadPlace(uri:String?) {

      supportPostponeEnterTransition()

      Picasso.get().load(Uri.parse(uri)).fit().noFade().centerCrop().into(placeImage2, object : Callback {

                  override fun onSuccess() {

                      supportStartPostponedEnterTransition()

                  }
                  override fun onError(e: Exception?) {

                      supportStartPostponedEnterTransition()

                  }
              })

  }

  override fun onBackPressed() {

     // finish()

      println("=============================: " + GlobalValues.whatsNew)
      if (GlobalValues.whatsNew && !GlobalValues.cameFromCateg){
          finishAfterTransition()
      }else{
            finish()
      }


  }

    override fun onPause() {
        super.onPause()
      //  GlobalValues.whatsNew = false
    }

    private fun initializeList() {

        adapterProfile = FeedAdapter()
        expanded_staggered_list?.apply {
            layoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(2, androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL)
            expanded_staggered_list.adapter = adapterProfile
        }

        adapterProfile.SetOnLock(longClickListener)
        adapterProfile.setOnItemClickListener(onItemClickListenerVertical)

       //blanked out for no wifi work session
        val config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(true)
            .build()

        val liveData = initializedPagedListBuilder(config).build()
        liveData.observe(this, androidx.lifecycle.Observer<PagedList<CoreData>> { pagedList -> adapterProfile.submitList(pagedList) })

        ScrollDownListener().show(this@ExapandDetailActivity, expanded_staggered_list, object : ScrollDownListener.HideShow{

            override fun onCallback(animate: String) {

            }
        })

    }




    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<String, CoreData> {
        val dataSourceFactory = object : DataSource.Factory<String, CoreData>() {
            override fun create(): DataSource<String, CoreData> {
                return ImageDataSource(api)
            }
        }
        return LivePagedListBuilder<String, CoreData>(dataSourceFactory, config)
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


