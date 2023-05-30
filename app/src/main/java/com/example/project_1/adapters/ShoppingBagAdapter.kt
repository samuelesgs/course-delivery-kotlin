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

class ShoppingBagAdapter(val context: Activity, val products : ArrayList<Product>) :
    RecyclerView.Adapter<ShoppingBagAdapter.ProductBagViewHolder>() {

    val sharedPref = SharedPref(context)
    private val TAG = "SHOPPING_BAG_ADAPTER"

    init {
        (context as ClientShoppingBagActivity).setTotal(getTotal())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductBagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carview_shopping_bag, parent, false)
        return ProductBagViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductBagViewHolder, position: Int) {
        val product = products[position]
        holder.textName.text = product.name
        Log.i(TAG, "onBindViewHolder: ${product.quantity} ${product.price}")
        val balance = product.price * product.quantity
        holder.textBalance.text = "${balance}$"
        holder.textQuantity.text = "${product.quantity}"
        Glide.with(context).load(product.image1).into(holder.imageProduct)
        holder.itemView.setOnClickListener { goToDetail(product) }
        holder.imagePlus.setOnClickListener { addItem(product, holder) }
        holder.imageNegative.setOnClickListener { removeItem(product, holder) }
        holder.imageDelete.setOnClickListener { deleteItem(position) }
    }

    private fun getTotal() : Double {
        var total = 0.0
        for (p in products) {
            total += (p.quantity * p.price)
        }
        return total
    }

    private fun deleteItem(position: Int) {
        products.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, products.size)
        sharedPref.save("order", products)
        (context as ClientShoppingBagActivity).setTotal(getTotal())
    }

    private fun goToDetail(product: Product) {
        val intent = Intent(context, ClientProductsDetailActivity::class.java)
        intent.putExtra("product", product.toJson())
        context.startActivity(intent)
    }

    private fun getIndexOf(idProduct: String) : Int {
        for ((pos, p) in products.withIndex()) {
            if (p.id == idProduct) {
                return pos
            }
        }
        return -1
    }

    private fun addItem(product: Product, holder : ProductBagViewHolder) {
        val index = getIndexOf(product.id!!)
        product.quantity = product.quantity!! + 1
        products[index].quantity = product.quantity
        calculate(product, holder)
    }

    private fun removeItem(product: Product, holder: ProductBagViewHolder) {
        val index = getIndexOf(product.id!!)
        val quantity = product.quantity
        if (quantity!! > 1) {
            product.quantity = product.quantity!! - 1
            products[index].quantity = product.quantity
            calculate(product, holder)
        }
    }

    private fun calculate(product: Product, holder: ProductBagViewHolder) {
        holder.textQuantity.text = "${product.quantity}"
        holder.textBalance.text = "${product.quantity!! * product.price!!}"
        sharedPref.save("order", products)
    }

    override fun getItemCount(): Int {
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