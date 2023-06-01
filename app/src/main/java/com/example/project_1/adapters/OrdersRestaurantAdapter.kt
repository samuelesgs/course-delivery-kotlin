package com.example.project_1.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_1.R
import com.example.project_1.activities.restaurant.orders.detail.RestaurantOrdersDetailActivity
import com.example.project_1.models.Order

class OrdersRestaurantAdapter(val context: Activity, val orders: ArrayList<Order>) : RecyclerView.Adapter<OrdersRestaurantAdapter.OrdersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        return OrdersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cardview_orders_restaurant, parent, false))
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val model = orders[position]
        holder.textOrder.text = model.id
        holder.textClient.text = "${model.client?.name} ${model.client?.lastname}"
        holder.textDelivery.text = "${model.address?.address}"
        holder.textDate.text = model.timestamp

        holder.itemView.setOnClickListener {
            goToOrderDetail(model)
        }
    }

    private fun goToOrderDetail(order: Order) {
        val intent = Intent(context, RestaurantOrdersDetailActivity::class.java)
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