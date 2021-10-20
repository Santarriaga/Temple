package com.grumpy.temple.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.TextView

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.grumpy.temple.R

import com.grumpy.temple.adapters.ItemCartAdapter
import com.grumpy.temple.viewmodel.SharedViewModel
import java.text.NumberFormat


class BottomSheetFragment : Fragment() {


    //view model
    private val model : SharedViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var closeBtn: ImageView
    private lateinit var itemCount : TextView
    private lateinit var mTotal : TextView
    private lateinit var mSubtotal : TextView
    private lateinit var mTax : TextView
    private lateinit var mShipping : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView_cart)
        closeBtn = view.findViewById(R.id.close_btn)
        itemCount = view.findViewById(R.id.item_count)
        mTotal = view.findViewById(R.id.total)
        mSubtotal = view.findViewById(R.id.subtotal)
        mShipping = view.findViewById(R.id.shipping)
        mTax = view.findViewById(R.id.tax)


        //set up recycler view
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ItemCartAdapter(requireContext(), model.cart)


        //set item count
        val count = model.cart.size.toString()
        itemCount.text = getString(R.string.item_count,count)

        //navigate to home fragment
        closeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_bottomSheetFragment_to_homeFragment)
        }

        calculateTotal()
    }

    private fun calculateTotal(){

        var subtotal = 0.0
        val shipping = 8.99


        for (item in model.cart){
            val cost = item.itemPrice.toDoubleOrNull()
           if(cost != null){
               subtotal += cost
           }
        }

        val formattedShipping = NumberFormat.getCurrencyInstance().format(shipping)
        mShipping.text = getString(R.string.shipping,formattedShipping)

        val formattedSub = NumberFormat.getCurrencyInstance().format(subtotal)
        mSubtotal.text = getString(R.string.subTotal,formattedSub)

        val tax = .08 * subtotal
        val formattedTax = NumberFormat.getCurrencyInstance().format(tax)
        mTax.text = getString(R.string.tax,formattedTax)

        val totalCost = tax + subtotal + shipping
        val formattedTotal =  NumberFormat.getCurrencyInstance().format(totalCost)
        mTotal.text = getString(R.string.total, formattedTotal)



    }


}