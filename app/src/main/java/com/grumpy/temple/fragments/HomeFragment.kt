package com.grumpy.temple.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.grumpy.temple.adapters.ItemAdapter
import com.grumpy.temple.R
import com.grumpy.temple.models.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception



class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    private val productCollectionRef = Firebase.firestore.collection("products")
    private val dataset = mutableListOf<Item>()
    private lateinit var recyclerView : RecyclerView
    private lateinit var fab :FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView= view.findViewById(R.id.recycler_view)
        fab = view.findViewById(R.id.fab)

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_bottomSheetFragment)
        }

        retrieveProducts()

    }

    private fun retrieveProducts () = CoroutineScope(Dispatchers.IO).launch {
        try {
            val documents = productCollectionRef.get().await()
            for(doc in documents){
                //  val item = document.toObject<Item>()
                    val id = doc.id
                    val name = doc["name"].toString()
                    val price = doc["price"].toString()
                    val imgUrl = doc["imgUrl"].toString()
                    val logoUrl = doc["logoUrl"].toString()
                    dataset.add(Item(id,name,price,imgUrl,logoUrl,"",""))
            }
            withContext(Dispatchers.Main){
                recyclerView.layoutManager =LinearLayoutManager(requireContext())
                recyclerView.adapter = ItemAdapter(requireContext(),dataset)
            }

        }catch (e : Exception){
            Log.w(TAG, e)
        }

    }



}