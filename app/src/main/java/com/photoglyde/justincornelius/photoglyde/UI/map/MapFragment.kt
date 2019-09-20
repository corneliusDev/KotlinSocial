package com.photoglyde.justincornelius.photoglyde.UI.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.photoglyde.justincornelius.photoglyde.data.GlobalValues

import com.photoglyde.justincornelius.photoglyde.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MapFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MapFragment : androidx.fragment.app.Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {


        val location = GlobalValues.currentCore?.user?.location
        var lat_long:LatLng?


        if (location != null){
            lat_long = getLocationFromName(location)
        }else{
            lat_long = null
        }


        println("Map Ready: " + location)

        val target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {

                val height = 200
                val width = 200

                if (bitmap != null && lat_long != null){
                    val smallMarker = Bitmap.createScaledBitmap(bitmap, width, height, false)
                    googleMap.addMarker(MarkerOptions().position(lat_long).title("Marker in Sydney")).setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                }


            }
        }





        if (lat_long != null) {

               // googleMap.moveCamera(CameraUpdateFactory.newLatLng(lat_long))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat_long, 5f))
                Picasso.get().load(GlobalValues.currentCore?.urls?.regular).into(target)



        }else{

            showToast("No Location")
        }




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event


//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnVideoWatchListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnVideoWatchListener")
//        }
//    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        println("Hitting Created")


        val mapFragment = childFragmentManager.findFragmentById(R.id.map)

        mapFragment as SupportMapFragment
//        val mapFragment = activity?.supportFragmentManager?.findFragmentById(R.id.map)


        Handler().postDelayed({


            mapFragment.getMapAsync(this)

        }, 500)


        view?.findViewById<ImageView>(R.id.close_map)?.setOnClickListener {

           // listener?.onVideoWatchInteraction("Map Close", this)

            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.setCustomAnimations(R.anim.slide_down,
                R.anim.slide_down)?.commit()
        }





        // Add a marker in Sydney and move the camera
//        val location = GlobalValues.currentCore?.user?.location
//        if (location != null) {
//
//            val lat_long = getLocationFromName(location)
//
//            if (lat_long != null) {
//                mMap.addMarker(MarkerOptions().position(lat_long).title("Marker in Sydney"))
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(lat_long))
//            }
//        }else{
//            showToast("No Location")
//        }

    }

    private fun getLocationFromName(location_name: String?) : LatLng? {
        // //To change body of created functions use File | Settings | File Templates.
        println("searchThisText is calling $location_name")
        var geocodeMatches: List<Address>? = null

        val new = Geocoder(this.context)

        try {
            println("SEARCHTHISTEXT: founddd")
            geocodeMatches = new.getFromLocationName(location_name, 1)

        } catch (e: IndexOutOfBoundsException) {
            println("SEARCHTHISTEXT: not founddd")
            e.printStackTrace()
        }

        println("geocode matches = ${geocodeMatches?.size}")
        if (geocodeMatches != null) {
            if (geocodeMatches.size > 0) {
                val location = LatLng(geocodeMatches[0].latitude, geocodeMatches[0].longitude)
                return location
              //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 5f))
            } else {
                showToast("Cannot find location. Please enter another.")
                return null

            }

        }else{
            return null
        }



    }

    private fun showToast(message : String) {
        val toaster = Toast.makeText(this.context, message, Toast.LENGTH_SHORT)

        toaster.show()
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
        fun onFragmentInteraction(ani: String, fragment: androidx.fragment.app.Fragment)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(): MapFragment {
            val fragment = MapFragment()
            val args = Bundle()

            return fragment
        }
    }
}
