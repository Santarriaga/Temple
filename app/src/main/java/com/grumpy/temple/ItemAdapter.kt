package com.grumpy.temple

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grumpy.temple.fragments.HomeFragmentDirections
import com.grumpy.temple.models.Item

class ItemAdapter(private val context : Context, private val dataSet: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
       val item = dataSet[position]

        holder.itemName.text = item.itemName
        holder.itemPrice.text = item.itemPrice

        Glide.with(holder.itemView).load(item.imgUrl).into(holder.itemImage)

        holder.cardView.setOnClickListener{
            val itemID = item.id
            val action = HomeFragmentDirections.actionHomeFragmentToItemDetailsFragment(itemID)
            holder.itemView.findNavController().navigate(action)

        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        val itemName : TextView = view.findViewById(R.id.product_name)
        val itemPrice : TextView = view.findViewById(R.id.product_price)
        val itemImage : ImageView = view.findViewById(R.id.product_image)
        val cardView : CardView = view.findViewById(R.id.parent)
        val itemLogo : ImageView = view.findViewById(R.id.brand_logo)
    }
}