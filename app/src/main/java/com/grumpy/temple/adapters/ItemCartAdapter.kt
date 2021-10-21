package com.grumpy.temple.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grumpy.temple.R
import com.grumpy.temple.models.Item
import java.text.NumberFormat

class ItemCartAdapter(private val context : Context, private val dataSet: List<Item>) : RecyclerView.Adapter<ItemCartAdapter.ItemCartViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCartViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_cart,parent,false)
        return ItemCartViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemCartViewHolder, position: Int) {
        val item = dataSet[position]

        holder.itemName.text = item.itemName
        val formattedPrice = NumberFormat.getCurrencyInstance().format(item.itemPrice.toDouble())
        holder.itemPrice.text = formattedPrice
        holder.brand.text = item.brand
        Glide.with(holder.itemView).load(item.imgUrl).into(holder.itemImage)

    }

    override fun getItemCount(): Int {
       return dataSet.size
    }

    class ItemCartViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val itemName : TextView = view.findViewById(R.id.txt_title)
        val itemPrice : TextView = view.findViewById(R.id.txt_price)
        val itemImage : ImageView = view.findViewById(R.id.img_item)
        val brand : TextView = view.findViewById(R.id.txt_brand)
    }
}