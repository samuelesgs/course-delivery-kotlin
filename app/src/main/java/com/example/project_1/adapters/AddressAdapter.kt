package com.example.project_1.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_1.R
import com.example.project_1.activities.client.address.list.ClientAddressListActivity
import com.example.project_1.models.Address
import com.example.project_1.utils.SharedPref
import com.google.gson.Gson

class AddressAdapter(val context: Activity, val address: ArrayList<Address>) :
    RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    val sharedPref = SharedPref(context)
    val gson = Gson()
    var prev = 0
    var positionAddressSession = 0

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_address, parent, false)
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val model = address[position]
        holder.textUbication.text = model.address
        holder.textNeighboord.text = model.neighborhood
        holder.textUbication.text = "${model.lat} / ${model.lng}"
        holder.itemView.setOnClickListener {
            (context as ClientAddressListActivity).resetValue(prev)
            context.resetValue(positionAddressSession)
            prev = position
            holder.imageCheck.visibility = View.VISIBLE
            saveAddress(model.toJson())
        }
        if (!sharedPref.getData("address").isNullOrBlank()) {
            val adr = gson.fromJson(sharedPref.getData("address"), Address::class.java)
            if (adr.id == model.id) {
                positionAddressSession = position
                holder.imageCheck.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return address.size
    }

    private fun saveAddress(data : String) {
        val ad = gson.fromJson(data, Address::class.java)
        sharedPref.save("address", ad)
    }

    class AddressViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        val textDirection: TextView
        val textNeighboord: TextView
        val textUbication : TextView
        val imageCheck : ImageView

        init {
            textDirection = view.findViewById(R.id.textDirection)
            textNeighboord = view.findViewById(R.id.textNeighboord)
            textUbication = view.findViewById(R.id.textUbication)
            imageCheck = view.findViewById(R.id.imageView)
        }

    }


}
