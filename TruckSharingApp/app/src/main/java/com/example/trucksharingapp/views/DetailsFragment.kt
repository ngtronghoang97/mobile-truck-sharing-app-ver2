package com.example.trucksharingapp.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.trucksharingapp.R
import com.example.trucksharingapp.prefmanager.SharedPrefManager

class DetailsFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsFragment()
    }

    var pref: SharedPrefManager? = null

    private var itemLocation: String? = ""
    private var pickUpTime: String? = ""
    private var receiverName: String? = ""
    private var weight: String? = ""
    private var strWidth: String? = ""
    private var height: String? = ""
    private var length: String? = ""
    private var goodType: String? = ""
    private var vehicleType: String? = ""

    val MY_PERMISSIONS_REQUEST_LOCATION = 99

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_details, container, false)

        pref = SharedPrefManager(this.requireContext())

        itemLocation = arguments?.getString("itemLocation")
        pickUpTime = arguments?.getString("pickUpTime")
        receiverName = arguments?.getString("receiverName")
        weight = arguments?.getString("weight")
        strWidth = arguments?.getString("width")
        height = arguments?.getString("height")
        length = arguments?.getString("length")
        goodType = arguments?.getString("goodType")
        vehicleType = arguments?.getString("vehicleType")

        val detSender = rootView.findViewById<TextView>(R.id.detSender)
        val detTime = rootView.findViewById<TextView>(R.id.detTime)
        val detReceiver = rootView.findViewById<TextView>(R.id.detReceiver)
        val detDropLocation = rootView.findViewById<TextView>(R.id.detDropLocation)
        val detWeight = rootView.findViewById<TextView>(R.id.detWeight)
        val detIndustry = rootView.findViewById<TextView>(R.id.detIndustry)
        val detWidth = rootView.findViewById<TextView>(R.id.detWidth)
        val detHeight = rootView.findViewById<TextView>(R.id.detHeight)
        val detLength = rootView.findViewById<TextView>(R.id.detLength)
        val getEstimateLocationBtn = rootView.findViewById<AppCompatButton>(R.id.getEstimateLocationBtn)

        detSender.text = pref!!.getFULL_NAME()
        detTime.text = pickUpTime
        detReceiver.text = receiverName
        detDropLocation.text = itemLocation
        detWeight.text = "$weight kg"
        detIndustry.text = goodType
        detWidth.text = "$strWidth m"
        detHeight.text = "$height m"
        detLength.text = "$length m"

        getEstimateLocationBtn.setOnClickListener { checkLocationPermission() }

        return rootView
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this.requireContext())
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(
                            context as Activity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()
            } else {
                // We can request the permission.
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        } else {
            // Permission previously granted
            intentToLocationMap()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // If request is cancelled, the result arrays are empty.
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) if (grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {

            // permission was granted, do location-related task you need to do.
            if (ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                intentToLocationMap()
            }
        } else {
            // permission denied! Disable the functionality that depends on this permission.
            Toast.makeText(this.requireContext(), "Location Permission Not Granted.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun intentToLocationMap() {
        val bundle = Bundle()
        bundle.putString("dropLocation", itemLocation)
        val transaction =
            (this.activity as AppCompatActivity).supportFragmentManager.beginTransaction()
        val fragmentTwo = LocationFragment.newInstance()
        fragmentTwo.arguments = bundle
        transaction.replace(R.id.containerMain, fragmentTwo)
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Let's handle onClick back btn
                    (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.containerMain, HomeFragment.newInstance()).commitNow()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }
}