package com.example.picturesshop.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.picturesshop.R
import com.example.picturesshop.models.Review
import kotlin.properties.Delegates

class ReviewsAdapter(
    var reviews: MutableList<Review>,
    private val onDeleteClickListener: OnDeleteClickListener,
    private val recUserId: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnDeleteClickListener {
        fun onDeleteClick(position: Int)
    }




    @SuppressLint("NotifyDataSetChanged")
    inner class ReviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userNameTextView = itemView.findViewById<TextView>(R.id.reviewerName)
        val qualityLevelTextView = itemView.findViewById<TextView>(R.id.qualityLevelTextView)
        val commentTextView = itemView.findViewById<TextView>(R.id.commentContentTextView)
        val deleteButton = itemView.findViewById<Button>(R.id.deleteReviewButton)
        val relLay = itemView.findViewById<RelativeLayout>(R.id.relLayDel)
        var id by Delegates.notNull<Int>()
        var userId by Delegates.notNull<Int>()
        lateinit var comment : String
        var qualityLevel by Delegates.notNull<Int>()

        @SuppressLint("SetTextI18n")
        fun bindReview(
            userName: String, userId: Int, qualityLevel: Int, comment: String, id: Int
        ) {
            userNameTextView.text = userName
            qualityLevelTextView.text = "Quality level: $qualityLevel"
            this.userId = userId
            commentTextView.text = comment
            this.id = id
            this.comment = comment
            this.qualityLevel = qualityLevel
        }

        init {
            deleteButton.setOnClickListener {
                val position = layoutPosition
                onDeleteClickListener.onDeleteClick(position)
                notifyDataSetChanged()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var holder: RecyclerView.ViewHolder

        holder = ReviewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.review_list_item, parent, false)
        )

        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentItem = reviews[position]
        if (holder is ReviewHolder) {
            holder.bindReview(
                currentItem.user, currentItem.userId, currentItem.qualityLevel, currentItem.comment, currentItem.id
            )

            if(currentItem.userId != recUserId ){
                holder.relLay.visibility = View.GONE
            }
        }


    }

    override fun getItemCount(): Int {
        return reviews.size
    }
}
