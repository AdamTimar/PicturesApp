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

class OrdersAdapter (
    var orderList: MutableList<PictureToOrder>,
    private val onDataInsertListener: OnDataInsertListener,
    private val onDeleteClickListener: OnDeleteClickListener,
    private val isAdmin: Boolean
    ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnDataInsertListener {
        fun onDataInsert(position: Int, quantity: Int, price: Float)
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(position: Int)
    }


    @SuppressLint("NotifyDataSetChanged")
        inner class OrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var pictureImageView: ImageView = itemView.findViewById<ImageView>(R.id.orderPictureImageview)

            val pictureNameTextView = itemView.findViewById<TextView>(R.id.orderPictureNameTextView)

            val sizeTextView = itemView.findViewById<TextView>(R.id.orderSizeTextView)

            val quantityEditText = itemView.findViewById<EditText>(R.id.orderQuantityEditText)

            val quantityTextView = itemView.findViewById<TextView>(R.id.orderQuantityTextView)

            val priceTextView = itemView.findViewById<TextView>(R.id.orderPriceTextView)

            val deleteImageView = itemView.findViewById<ImageView>(R.id.deletePictureFromOrder)

            var quantity = 1
            var price: Float = 0.0f
            var originalPrice = 0.0f

            var sizeId = 0
            var pictureId = 0

            init{
                deleteImageView.setOnClickListener {
                    onDeleteClickListener.onDeleteClick(layoutPosition)
                    notifyDataSetChanged()
                }
            }

            @SuppressLint("SetTextI18n")
            fun bindOrder(
                pictureName: String, size: String,  priceP: Float, quantityP: Int, pictureIdP: Int, sizeIdP: Int
            ) {


                pictureNameTextView.text = pictureName
                sizeTextView.text = size
                quantityEditText.setText(quantityP.toString())
                quantity = quantityP
                price = priceP
                priceTextView.text = (priceP * quantity).toString() +" Lei"
                originalPrice = priceP
                sizeId = sizeIdP
                pictureId = pictureIdP
                quantityTextView.text = quantity.toString()

                quantityEditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        onDataInsertListener.onDataInsert(layoutPosition, quantity, price)
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if(quantityEditText.text.isNotEmpty()) {
                            if (quantityEditText.text.toString().compareTo("0") == 0) {
                                quantityEditText.setText(quantity.toString())
                                price = originalPrice * quantity
                                priceTextView.text =  (priceP * quantity).toString() +" Lei"
                                Log.d("originalI",originalPrice.toString())
                                Log.d("quantityI",originalPrice.toString())
                            }

                            else {
                                quantity = quantityEditText.text.toString().toInt()
                                price = originalPrice * quantity
                                priceTextView.text = price.toString() + " Lei"
                                Log.d("originalE",originalPrice.toString())
                                Log.d("quantityE",originalPrice.toString())
                            }

                            onDataInsertListener.onDataInsert(layoutPosition, quantity, price)
                        }
                    }

                    @SuppressLint("SetTextI18n")
                    override fun afterTextChanged(editable: Editable?) {


                    }
                })

            }



        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var holder: RecyclerView.ViewHolder

        holder = OrderHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.order_list_item, parent, false)
        )

        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = orderList[position]
        if (holder is OrdersAdapter.OrderHolder) {
            holder.bindOrder(
                currentItem.pictureName,
                currentItem.dimension,
                currentItem.price,
                currentItem.quantity,
                currentItem.pictureId,
                currentItem.dimensionId
            )

            Glide.with(holder.itemView.context)
                .load(currentItem.imageUrl)
                .into(holder.pictureImageView)

            if(isAdmin) {
                holder.quantityEditText.visibility = View.INVISIBLE
                holder.deleteImageView.visibility = View.INVISIBLE
            }
            else
                holder.quantityTextView.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return orderList.size
    }

}