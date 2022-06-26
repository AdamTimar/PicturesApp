package com.example.picturesshop.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.forEach
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.picturesshop.R
import com.example.picturesshop.adapters.OrdersAdapter
import com.example.picturesshop.models.PictureToOrder
import com.example.picturesshop.models.Review
import com.example.picturesshop.retrofit.accesslayers.PictureAccessLayer
import com.example.picturesshop.retrofit.accesslayers.ReviewAccessLayer
import com.example.picturesshop.retrofit.models.AddOrderModel
import com.example.picturesshop.retrofit.models.AddReviewModel
import com.example.picturesshop.retrofit.models.OrderModel
import com.example.picturesshop.utils.Constants
import com.example.picturesshop.viewModels.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class OrderFragment : Fragment(), OrdersAdapter.OnDataInsertListener, OrdersAdapter.OnDeleteClickListener {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var shared: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var priceTextView: TextView
    private lateinit var buyButton: Button
    private lateinit var addressTextInpLay: TextInputLayout
    private lateinit var addressEditText: EditText
    private lateinit var orderButton: Button
    private lateinit var lay: RelativeLayout
    private var orderDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        shared = requireActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
    }


    @SuppressLint("SetTextI18n", "CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_order, container, false)

        recyclerView = rootView.findViewById(R.id.ordersRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = OrdersAdapter(viewModel.order, this, this, shared.getString(Constants.ROLE,"")!!.compareTo("Admin") == 0)
        priceTextView = rootView.findViewById(R.id.fullPriceTextView)
        buyButton = rootView.findViewById(R.id.buyButton)
        priceTextView.text = "${calculateFullPrice()} Lei"
        addressTextInpLay = rootView.findViewById(R.id.addressTextInp)
        addressEditText = rootView.findViewById(R.id.addressEditText)
        orderButton = rootView.findViewById(R.id.buyButton)
        lay = rootView.findViewById(R.id.ordButLay)

        buyButton.setOnClickListener {
            if (addressEditText.text.isEmpty())
                addressTextInpLay.error = "Please not let this field empty"
            else {
                buyButton.isEnabled = false
                getAddOrderObserver()
            }
        }

        if(shared.getString(Constants.ROLE,"")!!.compareTo("Admin") == 0){
            orderButton.visibility = View.GONE
            addressTextInpLay.visibility = View.GONE
            addressEditText.visibility = View.GONE
            lay.visibility = View.GONE
            recyclerView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        }


        return rootView
    }

    @SuppressLint("SetTextI18n")
    override fun onDataInsert(position: Int, quantity: Int, price: Float) {
        val item = viewModel.order[position]
        item.quantity = quantity
        item.price = price / item.quantity

        priceTextView.text = "${calculateFullPrice()} Lei"
    }

    override fun onDeleteClick(position: Int) {
        viewModel.order.removeAt(position)
        if (viewModel.order.size == 0) {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView, PicturesListFragment())
            fragmentTransaction.commit()
        }

    }

    private fun calculateFullPrice(): Float {
        var fullPrice = viewModel.order.map {
            it.price * it.quantity
        }.sum()

        return fullPrice
    }

    private fun getAddOrderObserver() {

        var orders = mutableListOf<OrderModel>()
        viewModel.order.forEach {
            orders.add(OrderModel(it.pictureId, it.dimensionId, it.quantity))
        }

        orderDisposable = PictureAccessLayer.addOrderObservable(
            shared.getString(Constants.TOKEN, "")!!, AddOrderModel(
                orders,
                shared.getInt(Constants.ID, 0), addressEditText.text.toString()
            )
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
                            "Order added",
                            Toast.LENGTH_LONG
                        )
                    toast.setGravity(Gravity.TOP, 0, 0)
                    toast.show()
                    viewModel.order = mutableListOf<PictureToOrder>()
                },
                {
                    buyButton.isEnabled = true
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
        super.onDestroyView()
        if(orderDisposable!=null)
            if(!orderDisposable!!.isDisposed)
                orderDisposable!!.dispose()
    }
}