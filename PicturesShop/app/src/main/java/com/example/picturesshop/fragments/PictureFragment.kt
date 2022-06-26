package com.example.picturesshop.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.view.forEach
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.marketplaceproject.fragments.RegisterFragment
import com.example.picturesshop.R
import com.example.picturesshop.activities.MainActivity
import com.example.picturesshop.models.PictureToOrder
import com.example.picturesshop.retrofit.accesslayers.PictureAccessLayer
import com.example.picturesshop.retrofit.accesslayers.UserAccessLayer
import com.example.picturesshop.retrofit.models.Size
import com.example.picturesshop.utils.Constants
import com.example.picturesshop.viewModels.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlin.properties.Delegates

class PictureFragment : Fragment() {

    private var deleteDisposable: Disposable? = null
    private var pictureId by Delegates.notNull<Int>()

    private lateinit var viewModel: MainActivityViewModel

    private lateinit var shared : SharedPreferences

    private lateinit var pictureImageView: ImageView

    private lateinit var shopButton: Button

    private lateinit var deleteButton: Button


    private lateinit var imageUrl: String

    private var discount = 0

    private var _price by Delegates.notNull<Float>()

    private var pictureDisposable: Disposable? = null

    private lateinit var priceTextView: TextView
    private lateinit var contentTextView: TextView
    private lateinit var discountTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var spinner: AppCompatSpinner
    private lateinit var reviewsButton  :Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        shared = requireActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        pictureId = arguments?.getInt(Constants.PICTUREID) ?: 0
        imageUrl = arguments?.getString(Constants.IMAGEURL).toString()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_picture, container, false)
        priceTextView = rootView.findViewById(R.id.priceTextView)
        contentTextView = rootView.findViewById(R.id.contentTextView)
        spinner = rootView.findViewById(R.id.sizeSpinner)
        discountTextView = rootView.findViewById(R.id.discountTextView)
        pictureImageView = rootView.findViewById(R.id.pictureImageview)
        Glide.with(requireContext())
            .load(imageUrl)
            .into(pictureImageView)
        shopButton = rootView.findViewById(R.id.shopButton)
        titleTextView = rootView.findViewById(R.id.pictureTitleTextView)
        reviewsButton = rootView.findViewById(R.id.reviewsButton)
        deleteButton = rootView.findViewById(R.id.deletePictureButton)

        deleteButton.setOnClickListener {
            getDeletePictureObserver()
        }

        if(shared.getString(Constants.ROLE,"")!!.compareTo("User") == 0)
            deleteButton.visibility = View.INVISIBLE
        if(shared.getString(Constants.ROLE,"")!!.compareTo("Admin") == 0)
            shopButton.visibility = View.INVISIBLE


        val sizes = viewModel.sizes.map { it.dimension }.toMutableList()

        val list = viewModel.order.filter{
            it.pictureId == pictureId
        }

        reviewsButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragment = ReviewsFragment()
            val bundle = Bundle()
            bundle.putInt(Constants.PICTUREID, pictureId)
            bundle.putString(Constants.PICTURENAME, requireArguments().getString(Constants.PICTURENAME))
            bundle.putInt(Constants.DISCOUNT, requireArguments().getInt(Constants.DISCOUNT))
            bundle.putString(Constants.CONTENT, requireArguments().getString(Constants.CONTENT))
            bundle.putString(Constants.IMAGEURL, requireArguments().getString(Constants.IMAGEURL))

            fragment.arguments = bundle
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val res = mutableListOf<String>()

        sizes.forEach {
            val a = it
            var found = false
            list.forEach {
                if(it.dimension.compareTo(a) == 0) {
                    found = true
                }
            }
            if (!found)
                res.add(it)
        }



        if(res.size == 0) {


            Log.d("empty", res.size.toString())
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView, PicturesListFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        else {

            shopButton.setOnClickListener {
                viewModel.order.add(
                    PictureToOrder(
                        pictureId,
                        titleTextView.text.toString(),
                        spinner.selectedItem as String,
                        getSizeIdFromName(spinner.selectedItem as String),
                        1,
                        _price - _price*discount/100,
                        imageUrl
                    )
                )
                val fragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentContainerView, PicturesListFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

            setItemsForSpinner(res)

            var price = getPriceFromSize(spinner.selectedItem as String)
            priceTextView.text = "$price Lei"

            if(requireArguments().getInt(Constants.DISCOUNT) != 0)
                discountTextView.text = "-"+requireArguments().getInt(Constants.DISCOUNT).toString()+"% discount"
            titleTextView.text = requireArguments().getString(Constants.PICTURENAME)
            contentTextView.text = requireArguments().getString(Constants.CONTENT)

            getPictureObserver()


        }
        return rootView
    }

    @SuppressLint("SetTextI18n")
    private fun getPictureObserver(){
       pictureDisposable = PictureAccessLayer.getPictureByIdObservable(
            shared.getString(Constants.TOKEN,"")!!,
            pictureId
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    if(it.discount != 0)
                        discountTextView.text = "-"+it.discount+"% discount"
                    else
                        discountTextView.visibility = View.GONE
                    contentTextView.text = it.content
                    titleTextView.text = it.name
                    imageUrl = it.imageUrl
                    _price = getPriceFromSize(spinner.selectedItem as String)
                    discount = it.discount
                    Log.d("Price", _price.toString())
                    Glide.with(requireContext())
                        .load(it.imageUrl)
                        .into(pictureImageView)
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

    private fun getDeletePictureObserver(){
        deleteDisposable = PictureAccessLayer.deletePictureObservable(
            pictureId,
            shared.getString(Constants.TOKEN,"")!!
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view).menu.forEach {
                        it.isEnabled = false
                    }
                    val handler = Handler()
                    val runnable = Runnable {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, PicturesListFragment())
                            .commit()
                        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view).menu.forEach {
                            it.isEnabled = true
                        }
                    }

                    handler.postDelayed(runnable, 3000)
                    val toast =
                        Toast.makeText(
                            context,
                            "Picture deleted",
                            Toast.LENGTH_LONG
                        )

                    deleteButton.isEnabled = false
                    toast.setGravity(Gravity.TOP, 0, 0)
                    toast.show()
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
        if(pictureDisposable!=null){
            if(!pictureDisposable!!.isDisposed)
                pictureDisposable!!.dispose()
        }

        if(deleteDisposable!=null){
            if(!deleteDisposable!!.isDisposed)
                deleteDisposable!!.dispose()
        }
        super.onDestroyView()
    }

    @SuppressLint("ResourceType")
    private fun setItemsForSpinner(res : MutableList<String>){


        val adapter = ArrayAdapter<String>(requireContext(),R.layout.spinner_item, res)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
            override fun onItemSelected(
                arg0: AdapterView<*>?,
                arg1: View?,
                arg2: Int,
                arg3: Long
            ) {
                var price = getPriceFromSize(spinner.selectedItem as String)
                priceTextView.text = "$price Lei"
                _price = price
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }
    }

    private fun getPriceFromSize(dim: String) : Float{
        return viewModel.sizes.first {
            it.dimension.compareTo(dim) == 0
        }.price
    }

    private fun getSizeIdFromName(name: String): Int{
        return viewModel.sizes.first {
            it.dimension.compareTo(name) == 0
        }.id
    }

}