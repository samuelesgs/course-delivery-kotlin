package com.example.project_1.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_1.R
import com.example.project_1.activities.client.products.detail.ClientProductsDetailActivity
import com.example.project_1.activities.client.shopping_bag.ClientShoppingBagActivity
import com.example.project_1.models.Product
import com.example.project_1.utils.SharedPref

class OrderProductsAdapter(val context: Activity, val products : ArrayList<Product>) :
    RecyclerView.Adapter<OrderProductsAdapter.OrderProductsViewHolder>() {

    val sharedPref = SharedPref(context)
    private val TAG = "ORDER_PRODUCTS_ADAPTER"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_order_products, parent, false)
        return OrderProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderProductsViewHolder, position: Int) {
        val product = products[position]
        holder.textName.text = product.name
        holder.textQuantity.text = "${product.quantity}"
        Glide.with(context).load(product.image1).into(holder.imageProduct)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class OrderProductsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textName : TextView
        val imageProduct : ImageView
        val textQuantity : TextView


        init {
            textName = view.findViewById(R.id.textName)
            imageProduct = view.findViewById(R.id.imageProduct)
            textQuantity = view.findViewById(R.id.textQuantity)
        }
    }
}