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
import com.example.project_1.R
import com.example.project_1.activities.client.products.detail.ClientProductsDetailActivity
import com.example.project_1.models.Product
import com.example.project_1.utils.SharedPref

class ShoppingBagAdapter(val context: Activity, val products : ArrayList<Product>) :
    RecyclerView.Adapter<ShoppingBagAdapter.ProductBagViewHolder>() {

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductBagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carview_shopping_bag, parent, false)
        return ProductBagViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductBagViewHolder, position: Int) {
        Log.i("ADAPTER", "onBindViewHolder: "+ position)
        val product = products[position]
        holder.textName.text = product.name
        holder.textBalance.text = "${product.price}$"
        //Glide.with(context).load(product.image1).into(holder.imageProduct)
        holder.itemView.setOnClickListener { goToDetail(product) }
    }

    private fun goToDetail(product: Product) {
        val intent = Intent(context, ClientProductsDetailActivity::class.java)
        intent.putExtra("product", product.toJson())
        context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        Log.i("ADAPTER", "getItemCount: ${products.size}")
        return products.size
    }

    class ProductBagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textName : TextView
        val imageProduct : ImageView
        val imagePlus : ImageView
        val imageNegative : ImageView
        val textQuantity : TextView
        val textBalance : TextView
        val imageDelete : ImageView


        init {
            textName = view.findViewById(R.id.textName)
            imageProduct = view.findViewById(R.id.imageProduct)
            imagePlus = view.findViewById(R.id.imagePlus)
            imageNegative = view.findViewById(R.id.imageNegative)
            textQuantity = view.findViewById(R.id.textQuantity)
            textBalance = view.findViewById(R.id.textBalance)
            imageDelete = view.findViewById(R.id.imageDelete)
        }
    }
}