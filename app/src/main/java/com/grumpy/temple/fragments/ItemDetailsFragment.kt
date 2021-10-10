package com.grumpy.temple.fragments

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.grumpy.temple.R
import com.grumpy.temple.models.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception


class ItemDetailsFragment : Fragment() {

    private val TAG = "ItemDetailFragment"

    private val productCollectionRef = Firebase.firestore.collection("products")

    private val args : ItemDetailsFragmentArgs by navArgs()

    private lateinit var itemPrice : TextView
    private lateinit var itemTitle : TextView
    private lateinit var itemImage: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //assign views
        itemImage = view.findViewById(R.id.item_image)
        itemPrice = view.findViewById(R.id.item_price)
        itemTitle = view.findViewById(R.id.item_title)

        val itemID = args.itemId
        retrieveItemInfo(itemID)



    }


    private fun retrieveItemInfo(itemID: String) = CoroutineScope(Dispatchers.IO).launch {
        var imgUrl : String = ""
        var itemName : String = ""
        var price : String = ""
        try{
            val doc = productCollectionRef.document(itemID).get().await()

            if(doc != null){
                doc.id
                    imgUrl = doc["imgUrl"].toString()
                    price = doc["price"].toString()
                    itemName = doc["name"].toString()
            }

            withContext(Dispatchers.Main){
                itemPrice.text = price
                itemTitle.text = itemName
                Glide.with(this@ItemDetailsFragment).load(imgUrl).into(itemImage)
            }

        }catch (e : Exception){
          Log.w(TAG,e)
        }
    }


}