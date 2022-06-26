package com.example.picturesshop.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.Toast
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.cloudinary.android.MediaManager
import com.example.marketplaceproject.fragments.RegisterFragment
import com.example.picturesshop.R
import com.example.picturesshop.fragments.AdminsOrdersFragment
import com.example.picturesshop.fragments.OrderFragment
import com.example.picturesshop.fragments.PicturesListFragment
import com.example.picturesshop.fragments.ProfileFragment
import com.example.picturesshop.retrofit.accesslayers.PictureAccessLayer
import com.example.picturesshop.retrofit.accesslayers.UserAccessLayer
import com.example.picturesshop.retrofit.models.Content
import com.example.picturesshop.retrofit.models.Size
import com.example.picturesshop.utils.Constants
import com.example.picturesshop.viewModels.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    lateinit var mainActivityViewModel : MainActivityViewModel
    var config: HashMap<String, String> = HashMap()
    lateinit var sharedPref : SharedPreferences
    lateinit var contentsDisposable : Disposable
    lateinit var sizesDisposable : Disposable
    lateinit var navigationView: BottomNavigationView
    lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        config["cloud_name"] = "dkmsq7uct"
        config["api_key"] = "268334567378527"
        config["api_secret"] = "74mdHR8o4N6pj6kUFeyXAf349Ac"

        if(!Constants.mediaManagerInitialized) {
            MediaManager.init(this, config)
            Constants.mediaManagerInitialized = true
        }

        sharedPref = getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)

        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        mainActivityViewModel.pictures = mutableListOf()

        navigationView = findViewById(R.id.bottom_navigation_view)

        logoutButton = findViewById(R.id.logout)

        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        navigationView.menu.forEach {
            it.isEnabled = false
        }

        setOnClickListenerForNavigationItems()
        createContentsObserver()
    }

    private fun createContentsObserver(){
        contentsDisposable = PictureAccessLayer.getPictureContentsObservable(sharedPref.getString(Constants.TOKEN,"")!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    val listC = mutableListOf<Content>()
                    it.forEach {
                        listC.add(Content(it.id,it.name))
                    }
                    mainActivityViewModel.contents = listC

                    createSizesObserver()
                },
                {
                    val toast =
                        Toast.makeText(
                            this,
                            it.message.toString(),
                            Toast.LENGTH_LONG
                        )
                    toast.setGravity(Gravity.TOP, 0, 0)
                    toast.show()
                })

    }

    private fun createSizesObserver(){
        sizesDisposable = PictureAccessLayer.getPictureSizesObservable(sharedPref.getString(Constants.TOKEN,"")!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    val listS = mutableListOf<Size>()
                    it.forEach {
                        listS.add(Size(it.id,it.size,it.price))
                    }
                    mainActivityViewModel.sizes = listS

                    navigationView.menu.forEach {
                        it.isEnabled = true
                    }

                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragmentContainerView, PicturesListFragment())
                    fragmentTransaction.commit()

                },
                {
                    val toast =
                        Toast.makeText(
                            this,
                            it.message.toString(),
                            Toast.LENGTH_LONG
                        )
                    toast.setGravity(Gravity.TOP, 0, 0)
                    toast.show()
                })
    }

    override fun onBackPressed() {
        return
    }


    private fun setOnClickListenerForNavigationItems() {

        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
            val newFragment: Fragment
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            when (it.itemId) {
                R.id.picturesItem -> {
                    newFragment = PicturesListFragment()
                    transaction.replace(R.id.fragmentContainerView, newFragment)
                    transaction.commit()
                    true
                }

                R.id.orderItem -> {
                    if(sharedPref.getString(Constants.ROLE, "")!!.compareTo("User") == 0) {
                        if (mainActivityViewModel.order.size > 0) {
                            newFragment = OrderFragment()
                            transaction.replace(R.id.fragmentContainerView, newFragment)
                            transaction.commit()
                            true
                        }
                    }
                    else {
                        if (sharedPref.getString(Constants.ROLE, "")!!.compareTo("Admin") == 0) {
                            newFragment = AdminsOrdersFragment()
                            transaction.replace(R.id.fragmentContainerView, newFragment)
                            transaction.commit()
                        }

                    }
                    true
                }

                R.id.profileItem -> {
                    newFragment = ProfileFragment()
                    transaction.replace(R.id.fragmentContainerView, newFragment)
                    transaction.commit()
                    true
                }

            }
            false
        }
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

}