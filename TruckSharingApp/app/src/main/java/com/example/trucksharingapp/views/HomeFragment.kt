package com.example.trucksharingapp.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.trucksharingapp.MainActivity
import com.example.trucksharingapp.R
import com.example.trucksharingapp.adapter.AdapterOrders

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    var adapterOrders : AdapterOrders? = null
    var itemsRecycler : RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val floatingBtn = rootView.findViewById<FloatingActionButton>(R.id.floatingBtn)
        floatingBtn.setOnClickListener {
            (this.activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.containerMain, NewDeliveryFragment.newInstance())
                .commitNow()
        }

        val a = this.requireActivity() as MainActivity

        /**
         * Init adapter and recyclerView
         * Set data to UI
         */
        itemsRecycler = rootView.findViewById(R.id.homeRecyclerView)

        val layoutManager = LinearLayoutManager(this@HomeFragment.context)
        itemsRecycler!!.layoutManager = layoutManager

        adapterOrders = AdapterOrders(a.readOrdersData(), this@HomeFragment.requireContext())
        itemsRecycler!!.adapter = adapterOrders

        // Check if data is empty
        val noDataTv = rootView.findViewById<TextView>(R.id.noData)
        if (a.readOrdersData().isEmpty()) {
            noDataTv.visibility = View.VISIBLE
            itemsRecycler!!.visibility = View.GONE
        } else {
            itemsRecycler!!.visibility = View.VISIBLE
            noDataTv.visibility = View.GONE
        }

        return rootView
    }
}