package com.example.project_1.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_1.R
import com.example.project_1.models.Product
import com.example.project_1.utils.SharedPref

class ProductAdapter(val context: Context, val products : ArrayList<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductsViewHolder>() {

    val sharedPref= SharedPref(context as Activity)
    private val TAG = "PRODUCT_ADAPTER"

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsViewHolder {
        return ProductsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cardview_product, parent, false))
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = products[position];
        holder.textName.text = product.name
        Glide.with(context).load(product.image1).into(holder.imageViewProduct)
        holder.itemView.setOnClickListener {  }
    }


    override fun getItemCount(): Int {
        return products.size
    }

    class ProductsViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imageViewProduct : ImageView
        val textName : TextView
        val textPrice : TextView

        init {
            imageViewProduct = view.findViewById(R.id.imageProduct)
            textName = view.findViewById(R.id.textName)
            textPrice = view.findViewById(R.id.textPrice)
        }
    }

}