package com.grumpy.temple.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.grumpy.temple.R
import com.grumpy.temple.models.Item
import com.grumpy.temple.viewmodel.SharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.text.NumberFormat


class ItemDetailsFragment : Fragment() {

    private val TAG = "ItemDetailFragment"

    private val productCollectionRef = Firebase.firestore.collection("products")

    private val args : ItemDetailsFragmentArgs by navArgs()

    private lateinit var itemPrice : TextView
    private lateinit var itemTitle : TextView
    private lateinit var itemImage: ImageView
    private lateinit var brandLogo: ImageView
    private lateinit var mBrand : TextView
    private lateinit var itemDescription : TextView
    private lateinit var cartBtn : Button
    private lateinit var product: Item
    private lateinit var closeBtn : ImageView

    //shared view model
    private val model : SharedViewModel by activityViewModels()

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
        itemDescription = view.findViewById(R.id.item_description)
        mBrand = view.findViewById(R.id.brand)
        brandLogo = view.findViewById(R.id.logo)
        cartBtn = view.findViewById(R.id.btn_add_to_Cart)
        closeBtn = view.findViewById(R.id.close)


        val itemID = args.itemId
        retrieveItemInfo(itemID)

        cartBtn.setOnClickListener {
            model.cart.add(product)
            Log.d(TAG, "Item Added to cart$itemID")
            findNavController().navigate(R.id.action_itemDetailsFragment_to_bottomSheetFragment)
        }

        closeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_itemDetailsFragment_to_homeFragment)
        }

    }


    private fun retrieveItemInfo(itemID: String) = CoroutineScope(Dispatchers.IO).launch {
        var imgUrl : String = ""
        var itemName : String = ""
        var price : String = ""
        var description : String = ""
        var brand : String = ""
        var logoUrl  : String = ""
        try{
            val doc = productCollectionRef.document(itemID).get().await()

            if(doc != null){
                doc.id
                    imgUrl = doc["imgUrl"].toString()
                    price = doc["price"].toString()
                    itemName = doc["name"].toString()
                    description = doc["description"].toString()
                    logoUrl = doc["logoUrl"].toString()
                    brand = doc["brand"].toString()
            }

            withContext(Dispatchers.Main){

                val formattedPrice = NumberFormat.getCurrencyInstance().format(price.toDouble())
                itemPrice.text = getString(R.string.price,formattedPrice)
                itemTitle.text = itemName
                Glide.with(this@ItemDetailsFragment).load(imgUrl).into(itemImage)
                Glide.with(this@ItemDetailsFragment).load(logoUrl).into(brandLogo)
                mBrand.text = brand
                itemDescription.text = description
                product = Item(itemID,itemName,price,imgUrl,logoUrl,brand,description)
            }

        }catch (e : Exception){
          Log.w(TAG,e)
        }
    }


}