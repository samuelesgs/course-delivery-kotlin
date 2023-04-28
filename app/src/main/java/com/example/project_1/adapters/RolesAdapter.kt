package com.example.project_1.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_1.R
import com.example.project_1.activities.client.home.ClientHomeActivity
import com.example.project_1.activities.delivery.home.DeliveryHomeActivity
import com.example.project_1.activities.restaurant.home.RestaurantHomeActivity
import com.example.project_1.models.Rol
import com.example.project_1.utils.SharedPref

class RolesAdapter(val context: Context, val roles : ArrayList<Rol>) : RecyclerView.Adapter<RolesAdapter.RolesViewHolder>() {

    val sharedPref = SharedPref(context as Activity)

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : RolesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_roles, parent, false)
        return RolesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roles.size
    }

    override fun onBindViewHolder(holder: RolesViewHolder, position: Int) {
        val rol = roles[position]
        holder.textRol.text = rol.name
        Glide.with(context).load(rol.image).into(holder.imageUser)
        holder.itemView.setOnClickListener {
            goToRoleHome(rol)
        }
    }

    private fun goToRoleHome(rol : Rol) {
        var intent : Intent ? = null
        when (rol.name) {
            "CLIENTE" -> {
                intent = Intent(context, ClientHomeActivity::class.java)
            }
            "RESTAURANTE" -> {
                intent = Intent(context, RestaurantHomeActivity::class.java)
            }
            "REPARTIDOR" -> {
                intent = Intent(context, DeliveryHomeActivity::class.java)
            }
        }
        if (intent != null) {
            sharedPref.save("rol", rol.name)
            context.startActivity(intent)
        }
    }

    class RolesViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val textRol : TextView
        val imageUser : ImageView

        init {
            textRol = view.findViewById(R.id.textRol)
            imageUser = view.findViewById(R.id.imageUser)
        }
    }
}