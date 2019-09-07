package com.photoglyde.justincornelius.photoglyde.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_posting_options.*
import android.provider.MediaStore
import android.app.Activity
import android.graphics.Color

import java.io.IOException
import com.photoglyde.justincornelius.photoglyde.Camera.CamerOpenActivity
import com.photoglyde.justincornelius.photoglyde.Camera.EditPhoto
import com.photoglyde.justincornelius.photoglyde.VideoPlayback.VideoActivity
import java.io.FileNotFoundException


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
    private val VIDEO_REQUEST = 103
    private val SDK_RETURN = 109


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        activity?.containerOptionsDim?.visibility = View.GONE
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()

        if (resultCode == Activity.RESULT_OK)

        when (requestCode) {

            VIDEO_REQUEST -> {

                queryFile(data, VIDEO_REQUEST)

            }

            GALLERY_REQUEST -> {

                queryFile(data, GALLERY_REQUEST)

            }

            SDK_RETURN ->{
                activity?.containerOptionsDim?.visibility = View.GONE
                activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            }
        }
    }

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

        close.setOnClickListener {

            listener?.onFragmentInteraction(this, "close")

        }

        camera.setOnClickListener {
            val intent = Intent(this.context, CamerOpenActivity::class.java)
            intent.putExtra("option", "gallery")
            startActivity(intent)

       }

        gallery.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST)

        }

        video_text.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "video/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), VIDEO_REQUEST)


        }









        super.onActivityCreated(savedInstanceState)
    }



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
        val TAG: String = PostingOptions::class.java.simpleName

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() : PostingOptions {
            val fragment = PostingOptions()
            val args = Bundle()

            return fragment


        }
    }


    private fun queryFile(data: Intent?, route:Int){

        try {

            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context?.contentResolver?.query(data?.data, filePathColumn, null, null, null)
            cursor?.moveToFirst()

            val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
            val picturePath = cursor?.getString(columnIndex!!)
            cursor?.close()

            val selectedPath = picturePath

            if (selectedPath != null) {

                when(route){

                    GALLERY_REQUEST -> {
                        val intent = Intent(this.context, EditPhoto::class.java)
                        intent.putExtra("IMAGE", selectedPath)
                        startActivityForResult(intent, GALLERY_REQUEST)
                    }

                    VIDEO_REQUEST -> {
                        val intent = Intent(this.context, VideoActivity::class.java)
                        intent.putExtra("VIDEO", selectedPath)
                        startActivityForResult(intent, GALLERY_REQUEST)
                    }



                }

                println("=======posting options path check ${selectedPath}")

            }

        } catch (e: FileNotFoundException) {
            //#debug
            e.printStackTrace()
        }
    }


//     fun onClick(view: View){
//
//
//         if (view == video_text){
//
//             when(camera_text.text){
//
//                 "camera" ->{
//                     camera_text.text = "record"
//                     video_text.text = "photo"
//
//                     camera_text.setTextColor(Color.RED)
//                     camera.setBackgroundResource(com.photoglyde.justincornelius.photoglyde.R.drawable.ic_movie_symbol_of_video_camera)
//                 }
//
//                 "video" ->{
//                     //open camera to video recorder
//
//                     camera_text.text = "photo"
//                     video_text.text = "record"
//
//                     camera_text.setTextColor(Color.GRAY)
//                     camera.setBackgroundResource(com.photoglyde.justincornelius.photoglyde.R.drawable.ic_photo_camera)
//
//                 }
//
//             }
//
//         }else if (view == camera){
//
//             when(camera_text.text){
//
//                 "camera" ->{val intent = Intent(this.context, PhotoEditor::class.java)
//                     intent.putExtra("option", "camera")
//                     startActivityForResult(intent, 102)}
//
//                 "video" ->{
//                     //open camera to video recorder
//                 }
//
//             }
//
//         }else if(view == gallery){
//
//             when(camera_text.text){
//
//                 "camera" ->{val intent = Intent(this.context, PhotoEditor::class.java)
//                     intent.putExtra("option", "camera")
//                     startActivityForResult(intent, 102)}
//
//                 "video" ->{
//                     //open camera to video recorder
//
//                     val intent = Intent(Intent.ACTION_PICK)
//                     intent.type = "video/*"
//                     intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                     startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101)
//
//                 }
//
//             }
//
//         }
//
//
//
//
//
//     }




}
