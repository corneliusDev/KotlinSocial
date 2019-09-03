package com.photoglyde.justincornelius.photoglyde

import android.annotation.SuppressLint
import android.app.Dialog
import android.arch.paging.PagedList
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Video.VideoColumns.CATEGORY
import android.util.Log
import android.view.LayoutInflater
import com.google.android.exoplayer2.ui.PlayerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.photoglyde.justincornelius.photoglyde.Adapters.ImagePreviewerUtils
import com.photoglyde.justincornelius.photoglyde.Camera.ImageSaver
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals.test
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals.videoWatch
import com.photoglyde.justincornelius.photoglyde.Networking.DataDump
import com.photoglyde.justincornelius.photoglyde.Networking.PostUN
import com.photoglyde.justincornelius.photoglyde.Networking.UnSplashService
import com.photoglyde.justincornelius.photoglyde.Utilities.PREFIX_FILE
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import kotlin.math.floor

object Helper {


    fun getDateTimeLong(): Long {
        val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.US)
        val sdf1 = SimpleDateFormat("MM")
        val sdf2 = SimpleDateFormat("dd")
        val sdf3 = SimpleDateFormat("yyyy")
        val currentDate = sdf.format(Date()).toLong()
        println("========test date ${currentDate}")


        return currentDate

    }

    fun getVideoHeight(uri: Uri?, contex: Context): ArrayList<Int> {
        var bmp: Bitmap? = null
        val mediaRetriever = MediaMetadataRetriever()
        mediaRetriever.setDataSource(contex, uri)
        bmp = mediaRetriever.frameAtTime
        val videoHeight = bmp.getHeight()
        val videoWidth = bmp.getWidth()

        println("======== here is the video dimension $videoHeight and $videoWidth")

        val dimen = ArrayList<Int>()
        dimen.add(videoWidth)
        dimen.add(videoHeight)

        return dimen
    }

    fun queryFile(context: Context, uri:Uri) : String? {

        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver?.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()

        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val picturePath = cursor?.getString(columnIndex!!)
        cursor?.close()

        return picturePath
    }


    fun getImageUri(inContext: Context, inImage: Bitmap?): Uri {

        val bytes = ByteArrayOutputStream()
        inImage?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)

        println("======= damm this ${path}")
        return Uri.parse(path)
    }




    class VideoRender(
        /**
         * The JPEG image
         */
        private val context: Context,

        /**
         * The file we save the image into.
         */
        private val file: String?
    ) : Runnable {

        override fun run() {
            println("======== we are attempting to try")
            try {
                println("======== we are attempting to save ${file.toString()} and ${file} and ${file}")

                val dir = context.getExternalFilesDir(null)


                println("======== we are attempting to save ${dir} and ${file} and ${file}  \"${dir.absolutePath}/74.mp4\"")






                val bitmap =
                    ThumbnailUtils.createVideoThumbnail(file, MediaStore.Video.Thumbnails.MINI_KIND)

                println("=====thumbnail bytes $bitmap and ${bitmap.byteCount}")
                val thumbnailUri = Helper.getImageUri(context, bitmap)
                val thumbnailFile = Helper.queryFile(context, thumbnailUri)
                val createdOn = Helper.getDateTimeLong()
                val videoUri = Uri.parse(file)
                val dimen = Helper.getVideoHeight(videoUri, context)
                val width = dimen.first().toDouble()
                val height = dimen.last().toDouble()
                val userName = GlobalVals.currentUser?.userName

                println("=====thumbnail Uri $thumbnailUri and $thumbnailFile and ${bitmap.byteCount}")

                val recentVideo = VideoClass(
                    "", createdOn.toString(), "", width, height, "", userName, "",
                    "", TYPE_VIDEO, "", thumbnailFile, videoUri.toString()
                )

                val categ = arrayListOf<String>(FEED, RANDOM)


                PostUN().postStorageVideo(recentVideo, categ, VIDEOS)

            } catch (e: Exception) {
                println("======== we are attempting to catch any exceptions")
                Log.e("", e.toString())
            } finally {
                println("======== we are attempting finally")
            }
        }


    }

    fun main(args : List<CoreUnSplash?>) : List<CoreUnSplash> {

        var arg = ArrayList<CoreUnSplash>()

        if (videoWatch){

            arg = args as ArrayList<CoreUnSplash>

//            var data1 = CoreUnSplash()
//            data1.type = BANNER
//            arg.add(0, data1)

//            var data2 = CoreUnSplash()
//            data2.type = HEADER
//            data2.categ_name = "Top Videos Today"
//            arg.add(1, data2)

        }else if(GlobalVals.whatsNew && !GlobalVals.cameFromExa) {

        println("INside Helper ${args.size}")


            arg = args as ArrayList<CoreUnSplash>
            val from = 0
            // randomly inserts categories inside list
            val to = args.size.div(2)
            val random = Random()
            val categs = GlobalVals.listCateg
            val arraySize = floor((to.div(2)).toFloat())

            if (categs.size > 0 && to > 0) {
                val amplititudes = IntArray(categs.size / 2) { random.nextInt(to - from) + from }.asList()


                print(amplititudes)

                var data1 = CoreUnSplash()
                data1.type = "GRID"

                print("Index pair ${amplititudes}")
                for (i in 0..amplititudes.size.minus(1)) {
                    arg.add(amplititudes[i], GlobalVals.listCateg[categs.count()-1]!!)
                    GlobalVals.listCateg.removeAt(categs.count()-1)
                   // arg.add(amplititudes[i], data1)

                }
            }


//






        }else{


//            var data1 = CoreUnSplash()
////            data1.type = BANNER
////            arg.add(0, data1)
////
////            var data2 = CoreUnSplash()
////            data2.type = "GRID"
////            arg.add(0, data2)



         //   println("EXOFIRST: " + args.size)
       //     arg = args.filter { it?.type.toString() != "collection" } as ArrayList<CoreUnSplash>
           // println("EXOSECOND: " + arg.size)


            arg = args as ArrayList<CoreUnSplash>
//            var data2 = CoreUnSplash()
//            data2.type = HEADER
//            data2.categ_name = "Top Post Today"
//            arg.add(1, data2)
//

        }


        return arg

    }





    @SuppressLint("ClickableViewAccessibility")
    fun show(context: Context, source: PlayerView, core:CoreUnSplash?) {
        val background = ImagePreviewerUtils().getBlurredScreenDrawable(context, source.rootView)


        val dialogView = LayoutInflater.from(context).inflate(R.layout.view_player, null)
        var imageView = dialogView.findViewById(R.id.previewer_player) as PlayerView



        imageView.player = source.player

        var resize = imageView.layoutParams
        val width = GlobalVals.widthWindow
        resize.width = width
        //add null check
        val ratio = core?.height?.div(core.width!!)
        val finalHeight = width.times(ratio!!)

        resize.height = finalHeight.toInt()






        val dialog = Dialog(context, R.style.ImagePreviewerTheme)
        dialog.window.setBackgroundDrawable(background)
        dialog.setContentView(dialogView)
        dialog.show()


        dialogView.setOnClickListener {
            dialog.dismiss()
            source.player.release()


        }





    }





//    fun apiCall(search:String?){
//
//        val api = UnSplashService.createService()
//
//        api.getPosts("photos",search, count.toString(), "29").enqueue(object : retrofit2.Callback<UnSplashBegin> {
//            override fun onFailure(call: retrofit2.Call<UnSplashBegin>, t: Throwable) {
//                println("=======this is unsplash error ${t.message} and ${t.localizedMessage} and ${t.localizedMessage} and ${t}")
//            }
//
//            override fun onResponse(
//                call: retrofit2.Call<UnSplashBegin>,
//                response: retrofit2.Response<UnSplashBegin>
//            ) {
//
//                count++
//                println("=======this is unsplash${response.body()} ${response.body()?.results}  ${response.code()} and ${response.raw()}\" ${response.message()}\"")
//
//                val splashData = response.body()
//
//                DataDump().dumpNow(search, splashData!!)
//            }
//        })
//    }

    }
