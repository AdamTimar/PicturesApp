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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.picturesshop.R
import com.example.picturesshop.adapters.PicturesAdapter
import com.example.picturesshop.models.Picture
import com.example.picturesshop.retrofit.accesslayers.PictureAccessLayer
import com.example.picturesshop.utils.Constants
import com.example.picturesshop.viewModels.MainActivityViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class PicturesListFragment : Fragment(), PicturesAdapter.OnItemClickListener{

    private lateinit var picturesRecyclerView : RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var spinner: Spinner
    private var picturesDisposable : Disposable? = null
    private lateinit var picturesAdapter : PicturesAdapter
    private lateinit var shared: SharedPreferences
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var addButton: FloatingActionButton
    private var pictures : MutableList<Picture> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        shared = requireActivity().getSharedPreferences(Constants.SHAREDPREF,Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_pictures_list, container, false)

        searchView = rootView.findViewById(R.id.pictureListSearchView)
        spinner = rootView.findViewById(R.id.dropStatus)

        addButton = rootView.findViewById(R.id.fab)

        addButton.setOnClickListener {
            val manager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, AddPictureFragment())
            transaction.commit()
        }

        if(shared.getString(Constants.ROLE,"")!!.compareTo("User") == 0)
            addButton.visibility = View.GONE

        picturesRecyclerView = rootView.findViewById(R.id.picturesRecyclerView)


        picturesRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        getPicturesObserver()

        return rootView
    }



    override fun onDestroyView() {
        if(picturesDisposable!=null){
            if(!picturesDisposable!!.isDisposed)
                picturesDisposable!!.dispose()
        }
        super.onDestroyView()
    }

    private fun getPicturesObserver(){

        picturesDisposable = PictureAccessLayer.getPicturesObservable(shared.getString(Constants.TOKEN, null)!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { it ->
                    val pics = mutableListOf<Picture>()
                    it.forEach {
                        pics.add(Picture(it.id, it.name, it.imageUrl, it.content, it.discount))
                    }

                    pictures = mutableListOf<Picture>()
                    pictures.addAll(pics)

                    viewModel.pictures.addAll(pics)

                    picturesAdapter = PicturesAdapter(pictures, this)

                    picturesRecyclerView.adapter = picturesAdapter

                    setItemsForSpinner()
                    setOnSearchViewTextChangedListener()
                },
                {
                    picturesAdapter = PicturesAdapter(pictures, this)

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

    override fun onItemClick(position: Int) {
        val picture = pictures[position]
        val bundle = Bundle()

        bundle.putInt(Constants.PICTUREID, picture.id)
        bundle.putString(Constants.CONTENT, picture.content)
        bundle.putInt(Constants.DISCOUNT, picture.discount)
        bundle.putString(Constants.PICTURENAME, picture.name)
        bundle.putString(Constants.IMAGEURL, picture.imageUrl)

        val manager: FragmentManager = requireActivity().supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        val pictureFragment = PictureFragment()
        pictureFragment.arguments = bundle
        transaction.replace(R.id.fragmentContainerView, pictureFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setItemsForSpinner() {

        val list: MutableList<String> = ArrayList()
        list.add("Ascending by name")
        list.add("Descending by name")
        list.add("Ascending by content")
        list.add("Descending by content")

        val adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_dropdown_item,list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemSelected(
                arg0: AdapterView<*>?,
                arg1: View?,
                arg2: Int,
                arg3: Long
            ) {

                when (arg2) {
                    0 -> pictures.sortBy { it.name.lowercase() }
                    1 -> pictures.sortByDescending { it.name.lowercase() }
                    2 -> pictures.sortBy { it.content.lowercase() }
                    3 -> pictures.sortByDescending { it.content.lowercase() }
                }

                picturesAdapter.picturesList = pictures
                picturesAdapter.notifyDataSetChanged()

            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }
    }

    private fun setOnSearchViewTextChangedListener(){

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String): Boolean {

                pictures = searchResult(newText)
                picturesAdapter.picturesList = pictures
                picturesAdapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

        })
    }

    private fun searchResult(titleP: String) : MutableList<Picture>{

        val result = viewModel.pictures.filter {
            it.name.contains(titleP,ignoreCase = true)
        }

        return result.toMutableList()
    }

}