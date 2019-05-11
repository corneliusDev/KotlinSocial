package com.photoglyde.justincornelius.photoglyde.ProfileFragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.photoglyde.justincornelius.photoglyde.Adapters.BindingVertical
import com.photoglyde.justincornelius.photoglyde.Adapters.ProfileAdapter
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals

import com.photoglyde.justincornelius.photoglyde.R

import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.fragment_my_content.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MyContent.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MyContent.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MyContent : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit private var staggeredLayoutManager: StaggeredGridLayoutManager
    private var listener: OnFragmentInteractionListener? = null
    lateinit private var adapterProfile: BindingVertical

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }


    override fun onPause() {
        super.onPause()
        println("======applied from onresume1 ${GlobalVals.recyclerState2}")
        if (my_content != null) GlobalVals.myContentState = my_content.layoutManager?.onSaveInstanceState()

    }

    override fun onResume() {
        super.onResume()

        println("======applied from onresume 2${GlobalVals.recyclerState2}")

        if (GlobalVals.myContentState != null){
            staggeredLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            my_content?.layoutManager = staggeredLayoutManager
            println("======applied from onresume ${GlobalVals.recyclerState2}")
            my_content.layoutManager?.onRestoreInstanceState(GlobalVals.recyclerState2)

            my_content?.adapter = adapterProfile
        }else{
            println("======applied from null ${GlobalVals.recyclerState2}")
            setUpAdapter()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_content, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpAdapter()







    }


    //    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
//    }

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
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyContent.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() : MyContent {
            val fragment = MyContent()
            val args = Bundle()

            return fragment


        }
    }

    fun setUpAdapter(){
//        println("======applied from setup ${GlobalVals.recyclerState2}")
//        staggeredLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
//        my_content.layoutManager = staggeredLayoutManager
//        adapterProfile = BindingVertical(
//            this@MyContent.requireContext(),
//            GlobalVals.currentUser!!.userImages, 0
//        )
//        my_content.isNestedScrollingEnabled = true
//        my_content.adapter = adapterProfile
    }
}
