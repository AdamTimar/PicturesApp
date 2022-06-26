package com.example.picturesshop.adapters

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.picturesshop.R
import com.example.picturesshop.activities.MainActivity
import com.example.picturesshop.models.Picture
import com.example.picturesshop.models.PictureToOrder
import com.example.picturesshop.retrofit.models.UsersOrder

class AdminOrdersAdapter (
    var orderList: List<UsersOrder>,
    private val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener {
        fun onClick(position: Int)
    }


    @SuppressLint("NotifyDataSetChanged")
    inner class AdminOrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var userNameTextView: TextView = itemView.findViewById<TextView>(R.id.ordererNameTextView)

        val orderDateTextView = itemView.findViewById<TextView>(R.id.orderDateContentTextView)

        val locationTextView = itemView.findViewById<TextView>(R.id.locationContentTextView)

        val priceTextView = itemView.findViewById<TextView>(R.id.orderPriceAdminContentTextView)

        var userId = 0
        var orderDate = ""

        init {
            itemView.setOnClickListener {
                onClickListener.onClick(layoutPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bindOrder(
            userName: String, orderDate: String, priceP: Float, location: String, userId: Int
        ) {

            userNameTextView.text = userName
            priceTextView.text = priceP.toString()
            locationTextView.text = location
            orderDateTextView.text = orderDate

            this.userId = userId
            this.orderDate = orderDate

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var holder: RecyclerView.ViewHolder

        holder = AdminOrderHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.admins_order_list_item, parent, false)
        )

        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = orderList[position]
        if (holder is AdminOrdersAdapter.AdminOrderHolder) {
            holder.bindOrder(
                currentItem.userName, currentItem.orderDate.substringBefore('.'),currentItem.price, currentItem.location, currentItem.userId
            )

        }

    }

    override fun getItemCount(): Int {
        return orderList.size
    }

}