package com.example.project_1.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_1.R
import com.example.project_1.activities.client.orders.detail.ClientOrdersDetailActivity
import com.example.project_1.models.Order

class OrdersClientAdapter(val context: Activity, val orders: ArrayList<Order>) : RecyclerView.Adapter<OrdersClientAdapter.OrdersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        return OrdersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cardview_orders, parent, false))
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val model = orders[position]
        holder.textOrder.text = model.id
        holder.textClient.text = "${model.client?.name}"
        holder.textDelivery.text = "${model.address?.address}"

        holder.itemView.setOnClickListener {
            goToOrderDetail(model)
        }
    }

    private fun goToOrderDetail(order: Order) {
        val intent = Intent(context, ClientOrdersDetailActivity::class.java)
        intent.putExtra("order", order.toJson())
        context.startActivity(intent)
    }

    class OrdersViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textOrder : TextView
        val textDate : TextView
        val textClient : TextView
        val textDelivery : TextView

        init {
            textOrder = view.findViewById(R.id.textOrder)
            textDate = view.findViewById(R.id.textDate)
            textClient = view.findViewById(R.id.textClient)
            textDelivery = view.findViewById(R.id.textDelivery)
        }

    }

}