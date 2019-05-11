package com.photoglyde.justincornelius.photoglyde.Networking

import android.net.Uri
import android.provider.MediaStore
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.obf.it
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals.listCateg
import com.photoglyde.justincornelius.photoglyde.Helper
import java.io.File
import java.io.FileInputStream

class PostUN {


    interface DownloadCategories {
       fun onCallBack(bool:Boolean)
    }

    var UID = ""


    fun postData(data: Any, categoriesList:ArrayList<String>, type:String) {

        var count = 0
        val postThis = data
        val currentUID = FirebaseDatabase.getInstance().reference.child(type).push().key






            for (categ in categoriesList) {

                println("------checking all the categories $categ and $type")

                FirebaseDatabase.getInstance().reference.child(type).child(categ).child(currentUID.toString()).setValue(postThis)

                if (count == 0) {
                    FirebaseDatabase.getInstance().reference.child(FEED).push().setValue(postThis)
                    count++
                }

//                when(type){
//
//                    TYPE_VIDEO -> {postThis = data as VideoClass}
//
//                }


            }

    }



//    fun postCategory(){
//
//        val ref = FirebaseDatabase.getInstance().getReference(GlobalVals.CATEGORY)
//
//        for (i in GlobalVals.listCateg){
//
//            val data = CoreUnSplash()
//            data.categ_created = Helper.getDateTimeLong().toString()
//            data.categ_image_uri = ""
//            data.categ_name = i
//
//            ref.child(i).setValue(data)
//
//        }
//
//    }



    fun downloadCategs(finished:DownloadCategories){

        val ref = FirebaseDatabase.getInstance().getReference(GlobalVals.CATEGORY)

        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                finished.onCallBack(false)
            }

            override fun onDataChange(p0: DataSnapshot) {
               if (p0.exists()){
                   val new  = ArrayList<CoreUnSplash?>()

                   val list = p0.children

                   list.forEach {
                       new.add(it.getValue(CoreUnSplash::class.java))
                   }

                   GlobalVals.listCateg = new

                   finished.onCallBack(true)


                   println("========= downloadwed categories $new")
               }
            }
        })




    }



    fun postStorageVideo(data: VideoClass, categoriesList:ArrayList<String>, type:String){

        val ref = FirebaseStorage.getInstance().getReference(TYPE_VIDEO).child(type)
        //val file = File("/storage/emulated/0/DCIM/Camera/20190120_101614.mp4")

        val uploadArray = ArrayList<String?>()



        uploadArray.add(data.video_image)
        uploadArray.add(data.video_uri)



        var count = 0




            val file1 = File(data.video_uri)
            val currentRef1 = ref.child("videos/${file1.name}")
            val stream1 = FileInputStream(file1)
            val uploadTask1 = currentRef1.putStream(stream1)
        println("===========here is the name ${file1.name}")
            uploadTask1.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation currentRef1.downloadUrl
            }).addOnCompleteListener { task ->

              //  println("=======task condition ${task.result} and ${task.exception}")
                if (task.isSuccessful) {



                            data.video_uri = task.result.toString()
                        count++

                        if (count == 2) postData(data, categoriesList, type)

                        println("====== here is the url 1 ${task.result}")



                       // postData(data, categoriesList, type, it.result!!)





                } else {
                    // Handle failures
                    // ...
                }
            }



        val file2 = File(data.video_image)
        val currentRef2 = ref.child("images/${file2.name}")

        val stream2 = FileInputStream(file2)
        val uploadTask2 = currentRef2.putStream(stream2)


        println("======here is the thumbnail ${data.video_image}")


        uploadTask2.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation currentRef2.downloadUrl
        }).addOnCompleteListener { task ->
          //  println("=======task condition ${task.result} and ${task.exception}")
            if (task.isSuccessful) {

                    data.video_image = task.result.toString()
                    count++

                    if (count == 2) postData(data, categoriesList, type)

                    println("====== here is the url 2 ${task.result}")





            } else {
                // Handle failures
                // ...
            }
        }




    }

    fun postStoragePhoto(data: ImageClass, categoriesList:ArrayList<String>, type:String){

        val ref = FirebaseStorage.getInstance().getReference(type).child(type)
        //val file = File("/storage/emulated/0/DCIM/Camera/20190120_101614.mp4")




        var count = 0




        val file1 = File(data.urls?.regular)
        val currentRef1 = ref.child("photos/${file1.name}")
        val stream1 = FileInputStream(file1)
        val uploadTask1 = currentRef1.putStream(stream1)
        println("===========here is the name ${file1.name}")
        uploadTask1.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation currentRef1.downloadUrl
        }).addOnCompleteListener { task ->

            //  println("=======task condition ${task.result} and ${task.exception}")
            if (task.isSuccessful) {



                data.urls?.regular = task.result.toString()
                count++

                postData(data, categoriesList, type)

                println("====== here is the url 1 ${task.result}")



                // postData(data, categoriesList, type, it.result!!)





            } else {
                // Handle failures
                // ...
            }
        }







    }

    fun contentResolver(dataSection:String, download:Uri){

        when(dataSection){



        }




    }


}