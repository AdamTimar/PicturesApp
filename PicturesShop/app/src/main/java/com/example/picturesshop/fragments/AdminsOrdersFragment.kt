package com.example.picturesshop.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.picturesshop.R
import com.example.picturesshop.adapters.AdminOrdersAdapter
import com.example.picturesshop.adapters.ReviewsAdapter
import com.example.picturesshop.models.PictureToOrder
import com.example.picturesshop.models.Review
import com.example.picturesshop.retrofit.accesslayers.PictureAccessLayer
import com.example.picturesshop.retrofit.accesslayers.ReviewAccessLayer
import com.example.picturesshop.retrofit.models.UsersOrder
import com.example.picturesshop.utils.Constants
import com.example.picturesshop.viewModels.MainActivityViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class AdminsOrdersFragment : Fragment(), AdminOrdersAdapter.OnClickListener {

    private lateinit var shared : SharedPreferences
    private  lateinit var recView: RecyclerView
    private lateinit var disposable: Disposable
    private lateinit var viewModel: MainActivityViewModel
    private var list = listOf<UsersOrder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shared = requireActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        viewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView =  inflater.inflate(R.layout.fragment_admins_orders, container, false)

        recView = rootView.findViewById(R.id.adminOrdersRecView)
        recView.layoutManager = LinearLayoutManager(requireContext())

        getOrdersObserver()

        return rootView
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getOrdersObserver(){
        disposable = PictureAccessLayer.getOrdersObservable(
            shared.getString(Constants.TOKEN,"")!!
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    list = it
                    recView.adapter = AdminOrdersAdapter(list,this)
                    recView.adapter!!.notifyDataSetChanged()
                    Log.d("orders",list.toString())
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

    override fun onClick(position: Int) {

        viewModel.order = mutableListOf()

        list.forEach {
            if(it.userId == list[position].userId && it.orderDate.compareTo(list[position].orderDate) == 0)
                it.pictures.forEach {
                    viewModel.order.add(PictureToOrder(0, it.pictureName, it.size, 0, it.quantity, it.price, it.imageUrl))
                }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, OrderFragment())
            .commit()
    }


}