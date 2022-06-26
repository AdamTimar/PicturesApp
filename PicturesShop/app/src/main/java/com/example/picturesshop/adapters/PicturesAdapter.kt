package com.example.picturesshop.adapters;

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.picturesshop.R
import com.example.picturesshop.models.Picture

class PicturesAdapter(
        var picturesList: MutableList<Picture>,
        private val onItemClickListener: OnItemClickListener
    ) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        interface OnItemClickListener {
            fun onItemClick(position: Int)
        }



        @SuppressLint("NotifyDataSetChanged")
        inner class PictureHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

                private var id: Int = 0

                var pictureImageView: ImageView = itemView.findViewById<ImageView>(R.id.pictureListItemImageview)

                private val pictureNameTextView = itemView.findViewById<TextView>(R.id.pictureListItemNameTextView)

                private val discountTextView = itemView.findViewById<TextView>(R.id.discountInListItem)

                @SuppressLint("SetTextI18n")
                fun bindPicture(
                       pictureName: String, pictureId: Int, content: String, discount: Int
                ) {
                    pictureNameTextView.text = "$pictureName ($content)"
                    id = pictureId

                    if(discount == 0) {
                        discountTextView.visibility = View.GONE
                        pictureNameTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    }
                    else
                        discountTextView.text = "-$discount% discount"
                }

            init {
                itemView.setOnClickListener {
                    val position = layoutPosition
                    onItemClickListener.onItemClick(position)
                }
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                lateinit var holder: RecyclerView.ViewHolder

                holder = PictureHolder(
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.picture_list_item, parent, false)
                )


                return holder
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

                val currentItem = picturesList[position]
                if (holder is PictureHolder) {
                        holder.bindPicture(
                                currentItem.name,
                                currentItem.id,
                                currentItem.content,
                            currentItem.discount
                        )

                    Glide.with(holder.itemView.context)
                        .load(currentItem.imageUrl)
                        .into(holder.pictureImageView)
                }



        }

        override fun getItemCount(): Int {
                return picturesList.size
        }
}
