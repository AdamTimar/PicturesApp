package com.example.picturesshop.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.view.marginTop
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.picturesshop.R
import com.example.picturesshop.adapters.ReviewsAdapter
import com.example.picturesshop.models.Review
import com.example.picturesshop.retrofit.accesslayers.PictureAccessLayer
import com.example.picturesshop.retrofit.accesslayers.ReviewAccessLayer
import com.example.picturesshop.retrofit.models.AddReviewModel
import com.example.picturesshop.utils.Constants
import com.example.picturesshop.viewModels.MainActivityViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlin.properties.Delegates
import android.view.ViewGroup.MarginLayoutParams




class ReviewsFragment : Fragment(), ReviewsAdapter.OnDeleteClickListener{

    private lateinit var shared: SharedPreferences
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var commentEditText: EditText
    private lateinit var addButton: Button
    private lateinit var backButton: Button
    private lateinit var qualitySpinner : AppCompatSpinner
    private lateinit var qualityTextView : TextView
    private lateinit var reviewTextView: TextView
    private var pictureId by Delegates.notNull<Int>()
    private lateinit var recView: RecyclerView
    private lateinit var disposable: Disposable
    private  var deleteDisposable: Disposable? = null
    private  var addDisposable: Disposable? = null
    private var reviewList = mutableListOf<Review>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        shared = requireActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        pictureId = requireArguments().getInt(Constants.pictureID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_reviews, container, false)
        commentEditText = rootView.findViewById(R.id.commentEditText)
        addButton = rootView.findViewById(R.id.addReviewButton)
        qualitySpinner = rootView.findViewById(R.id.qualityLevelSpinner)
        recView = rootView.findViewById(R.id.reviewsRecyclerView)
        qualityTextView = rootView.findViewById(R.id.addReviewTextView)
        reviewTextView = rootView.findViewById(R.id.addQualityLevelTextView)
        backButton = rootView.findViewById(R.id.backToPicButton)

        qualitySpinner.adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, listOf("1","2","3","4","5"))
        getPictureReviewsObserver()

        addButton.setOnClickListener {
            getAddReviewObserver()
        }

        if(shared.getString(Constants.ROLE,"")!!.compareTo("Admin") == 0){
            addButton.visibility=View.GONE
            qualitySpinner.visibility = View.GONE
            commentEditText.visibility = View.GONE
            reviewTextView.visibility = View.GONE
            qualityTextView.visibility = View.GONE
            val params = recView.layoutParams as MarginLayoutParams
            params.topMargin = 130

        }

        backButton.setOnClickListener {

            val manager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            val fragment = PictureFragment()
            val bundle = Bundle()
            bundle.putString(Constants.PICTURENAME, requireArguments().getString(Constants.PICTURENAME))
            bundle.putInt(Constants.DISCOUNT, requireArguments().getInt(Constants.DISCOUNT))
            bundle.putString(Constants.CONTENT, requireArguments().getString(Constants.CONTENT))
            bundle.putInt(Constants.PICTUREID, requireArguments().getInt(Constants.PICTUREID))
            bundle.putString(Constants.IMAGEURL, requireArguments().getString(Constants.IMAGEURL))
            fragment.arguments = bundle
            transaction.replace(R.id.fragmentContainerView, fragment)
            transaction.commit()

        }

        return rootView
    }

    private fun getPictureReviewsObserver(){
        disposable = ReviewAccessLayer.getPictureReviewsObservable(
            shared.getString(Constants.TOKEN,"")!!,
            pictureId
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    val list = mutableListOf<Review>()
                    it.forEach {
                        list.add(Review(user = it.userName, userId = it.userId, comment = it.comment, qualityLevel = it.quality, id = it.id, pictureId = pictureId))
                    }

                    reviewList.addAll(list)

                    recView.adapter = ReviewsAdapter(reviewList,this, shared.getInt(Constants.ID,0))
                    recView.layoutManager = LinearLayoutManager(requireContext())
                },
                {
                    val toast =
                        Toast.makeText(
                            context,
                            it.message.toString(),
                            Toast.LENGTH_LONG
                        )
                    toast.setGravity(Gravity.TOP, 0, 0)
                    toast.show()
                })

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getDeleteReviewByIdObserver(id: Int, pos: Int){
        deleteDisposable = ReviewAccessLayer.getDeleteReviewByIdObservable(
            shared.getString(Constants.TOKEN,"")!!,
            id
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    reviewList.removeAt(pos)
                    recView.adapter?.notifyDataSetChanged()
                },
                {
                    val toast =
                        Toast.makeText(
                            context,
                            it.message.toString(),
                            Toast.LENGTH_LONG
                        )
                    toast.setGravity(Gravity.TOP, 0, 0)
                    toast.show()
                })

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAddReviewObserver() {
        addDisposable = ReviewAccessLayer.getAddReviewObservable(shared.getString(Constants.TOKEN, "")!!,AddReviewModel(userId = shared.getInt(Constants.ID, 0)!!, pictureId = pictureId, qualityLevel = (qualitySpinner.selectedItem as String).toInt(), comment = commentEditText.text.toString())
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    reviewList.add(Review(it.userName, it.comment,it.quality, it.id, it.userId, it.pictureId ))
                    recView.adapter?.notifyDataSetChanged()
                    commentEditText.setText("")
                    qualitySpinner.setSelection(0, true)

                },
                {
                    val toast =
                        Toast.makeText(
                            context,
                            it.message.toString(),
                            Toast.LENGTH_LONG
                        )
                    toast.setGravity(Gravity.TOP, 0, 0)
                    toast.show()
                })
    }



        override fun onDestroyView() {
        if(disposable.isDisposed)
            disposable.dispose()
        if(deleteDisposable != null)
            if(!deleteDisposable!!.isDisposed)
                deleteDisposable!!.dispose()
        if(addDisposable != null) {
            if(!addDisposable!!.isDisposed)
                addDisposable!!.dispose()
        }
        super.onDestroyView()
    }

    override fun onDeleteClick(position: Int) {
        val item = reviewList[position]
        getDeleteReviewByIdObserver(item.id, position)
    }

}