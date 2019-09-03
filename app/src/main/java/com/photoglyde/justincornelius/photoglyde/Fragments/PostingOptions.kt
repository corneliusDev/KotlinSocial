package com.photoglyde.justincornelius.photoglyde.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.photoglyde.justincornelius.photoglyde.Adapters.ImagePreviewerUtils


import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_camer_open.*
import kotlinx.android.synthetic.main.fragment_posting_options.*
import android.provider.MediaStore
import android.graphics.Bitmap
import android.R.attr.data
import android.app.Activity
import android.graphics.Color
import android.util.Log
import com.photoglyde.justincornelius.photoglyde.Camera.EditPhoto
import com.photoglyde.justincornelius.photoglyde.Camera.PhotoEditor
import kotlinx.android.synthetic.main.activity_camera_api.*

import java.io.IOException
import ly.img.android.pesdk.backend.exif.Exify.TAG
import android.view.animation.AnimationUtils.loadAnimation
import android.view.animation.AnimationUtils
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_posting_options.view.*
import android.R.attr.data
import com.photoglyde.justincornelius.photoglyde.Camera.CamerOpenActivity
import com.photoglyde.justincornelius.photoglyde.VideoPlayback.VideoActivity
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PostingOptions.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PostingOptions.newInstance] factory method to
 * create an instance of this fragment.
 *
 */



class PostingOptions : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private val GALLERY_REQUEST = 102
    private val SDK_RETURN = 109


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("======hitting request1")
        if (resultCode == Activity.RESULT_OK)
            println("======hitting request2")
        when (requestCode) {
            GALLERY_REQUEST -> {


                println("====posting on result")

                //removes only when preceding activities have finished
                activity?.containerOptionsDim?.visibility = View.GONE
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()


                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = context?.contentResolver?.query(data?.data, filePathColumn, null, null, null)
                cursor?.moveToFirst()

                val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                val picturePath = cursor?.getString(columnIndex!!)
                cursor?.close()



                    val selectedVideoPath = picturePath


                    try {
                        if (selectedVideoPath == null) {

                        } else {

                            println("=======posting options path check ${selectedVideoPath} and ${selectedVideoPath}")


                            val intent = Intent(this.context, VideoActivity::class.java)
                            intent.putExtra("VIDEO", selectedVideoPath)
                            startActivityForResult(intent, GALLERY_REQUEST)

                        }
                    } catch (e: IOException) {
                        //#debug
                        e.printStackTrace()
                    }





            }

            SDK_RETURN ->{
                activity?.containerOptionsDim?.visibility = View.GONE
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
    }

//    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation {
//
//         val TAG = "MyFragment"
//
//        val anim = AnimationUtils.loadAnimation(activity, nextAnim)
//
//        anim.setAnimationListener(object : Animation.AnimationListener {
//
//            override fun onAnimationRepeat(p0: Animation?) {
//                Log.d(TAG, "Animation repeat.")
//            }
//
//            override fun onAnimationEnd(p0: Animation?) {
//                Log.d(TAG, "Animation end.")
//            }
//
//            override fun onAnimationStart(p0: Animation?) {
//                Log.d(TAG, "Animation start.");
//            }
//        })
//
//        return anim
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.photoglyde.justincornelius.photoglyde.R.layout.fragment_posting_options, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {



//
        close.setOnClickListener {


            listener?.onFragmentInteraction(this, "close")

        }
//
        camera.setOnClickListener {
            val intent = Intent(this.context, CamerOpenActivity::class.java)
            intent.putExtra("option", "gallery")
            startActivity(intent)
//
       }
//
        gallery.setOnClickListener {

            val intent = Intent(this.context, PhotoEditor::class.java)
            intent.putExtra("option", "gallery")
            startActivityForResult(intent, SDK_RETURN)





           // fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_down, R.anim.slide_down).commit()


        }


        video_text.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "video/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST)


        }









        super.onActivityCreated(savedInstanceState)
    }

    // TODO: Rename method, update argument and hook method into UI event


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(fragement:Fragment, action:String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PostingOptions.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() : PostingOptions {
            val fragment = PostingOptions()
            val args = Bundle()

            return fragment


        }
    }


     fun onClick(view: View){


         if (view == video_text){

             when(camera_text.text){

                 "camera" ->{
                     camera_text.text = "record"
                     video_text.text = "photo"

                     camera_text.setTextColor(Color.RED)
                     camera.setBackgroundResource(com.photoglyde.justincornelius.photoglyde.R.drawable.ic_movie_symbol_of_video_camera)
                 }

                 "video" ->{
                     //open camera to video recorder

                     camera_text.text = "photo"
                     video_text.text = "record"

                     camera_text.setTextColor(Color.GRAY)
                     camera.setBackgroundResource(com.photoglyde.justincornelius.photoglyde.R.drawable.ic_photo_camera)

                 }

             }

         }else if (view == camera){

             when(camera_text.text){

                 "camera" ->{val intent = Intent(this.context, PhotoEditor::class.java)
                     intent.putExtra("option", "camera")
                     startActivityForResult(intent, 102)}

                 "video" ->{
                     //open camera to video recorder
                 }

             }

         }else if(view == gallery){

             when(camera_text.text){

                 "camera" ->{val intent = Intent(this.context, PhotoEditor::class.java)
                     intent.putExtra("option", "camera")
                     startActivityForResult(intent, 102)}

                 "video" ->{
                     //open camera to video recorder

                     val intent = Intent(Intent.ACTION_PICK)
                     intent.type = "video/*"
                     intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                     startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101)

                 }

             }

         }





     }




}
