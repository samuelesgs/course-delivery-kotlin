package com.example.project_1.adapters

import android.app.Activity
import android.content.Context
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
import com.example.project_1.activities.client.products.list.ClientProductsListActivity
import com.example.project_1.models.Category
import com.example.project_1.utils.SharedPref

class CategoriesAdapter(val context: Context, val categories : ArrayList<Category>) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    val sharedPref = SharedPref(context as Activity)

    private val TAG = "CATEGORIES_ADAPTER";

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesAdapter.CategoriesViewHolder {
        return CategoriesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cardview_categories, parent, false))
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = categories[position]
        holder.textViewName.text = category.name
        Glide.with(context).load(category.image).into(holder.imageViewCategory)
        holder.itemView.setOnClickListener {
            goToCategory(category)
        }
    }


    private fun goToCategory(category: Category) {
        val intent = Intent(context, ClientProductsListActivity::class.java)
        intent.putExtra("idCategory", category.id)
        context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class CategoriesViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val imageViewCategory : ImageView
        val textViewName : TextView
        init {
            textViewName = view.findViewById(R.id.textName)
            imageViewCategory = view.findViewById(R.id.imageViewCategory)
        }
    }

}