package com.example.picturesshop.fragments

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import com.example.picturesshop.R
import com.google.android.material.textfield.TextInputLayout


import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.util.Log

import androidx.core.content.ContextCompat
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import java.io.File
import androidx.activity.result.ActivityResultCallback

import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.google.android.material.imageview.ShapeableImageView
import androidx.activity.result.contract.ActivityResultContracts

import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat.startActivityForResult

import android.provider.MediaStore
import android.provider.SyncStateContract
import android.view.Gravity
import android.widget.*
import com.example.picturesshop.activities.MainActivity
import java.io.IOException
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.view.forEach
import androidx.lifecycle.ViewModelProvider
import com.example.picturesshop.adapters.PicturesAdapter
import com.example.picturesshop.models.Picture
import com.example.picturesshop.models.UploadResponse
import com.example.picturesshop.retrofit.accesslayers.PictureAccessLayer
import com.example.picturesshop.retrofit.models.AddPictureModel
import com.example.picturesshop.utils.Constants
import com.example.picturesshop.viewModels.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class AddPictureFragment : Fragment() {

    private lateinit var addImageImv: ImageView
    private lateinit var addPicButton: Button
    private lateinit var imageView: ShapeableImageView
    private lateinit var nameTextInp: TextInputLayout
    private lateinit var nameEditText: EditText
    private lateinit var spinner: AppCompatSpinner
    private val IMAGE_REQ = 1
    private var imagePath: Uri? = null
    private val TAG = "upload"
    private lateinit var shared: SharedPreferences
    private lateinit var viewModel: MainActivityViewModel
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shared = requireActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        viewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_picture, container, false)

        addImageImv = rootView.findViewById(R.id.imgAdd)
        addPicButton = rootView.findViewById(R.id.btnUpload)
        imageView = rootView.findViewById(R.id.imgPic)
        nameTextInp = rootView.findViewById(R.id.addPictureNameTextInp)
        nameEditText = rootView.findViewById(R.id.addPictureNameEditText)
        spinner = rootView.findViewById(R.id.chooseContentSpinner)

        val adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_dropdown_item,viewModel.contents.map{it.name})
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        addImageImv.setOnClickListener {
            openImageChooser()
        }

        addPicButton.setOnClickListener {

            if(nameEditText.text.isEmpty())
                nameTextInp.error = "Please fill this field"

            else {
                if (imagePath == null) {
                    val toast =
                        Toast.makeText(
                            context,
                            "Please select image",
                            Toast.LENGTH_LONG
                        )
                    toast.setGravity(Gravity.TOP, 0, 0)
                    toast.show()
                }
                else {
                    addPicButton.isEnabled = false
                    MediaManager.get().upload(imagePath)
                        .callback(object : UploadCallback {
                            override fun onStart(requestId: String) {
                                Log.d(TAG, imagePath?.path!!)
                            }

                            override fun onProgress(
                                requestId: String,
                                bytes: Long,
                                totalBytes: Long
                            ) {
                                Log.d(TAG, "onStart: " + "uploading")
                            }

                            override fun onSuccess(
                                requestId: String,
                                resultData: Map<*, *>?
                            ) {
                                Log.d(TAG, "onStart: " + "usuccess")
                                val gson = Gson()
                                var str = gson.toJson(resultData)
                                var uplResp = gson?.fromJson(str, UploadResponse::class.java)
                                Log.d(TAG, uplResp.url)

                                getAddPictureObserver(uplResp.url)

                            }

                            override fun onError(
                                requestId: String,
                                error: ErrorInfo
                            ) {
                                val toast =
                                    Toast.makeText(
                                        context,
                                        error.toString(),
                                        Toast.LENGTH_LONG
                                    )
                                toast.setGravity(Gravity.TOP, 0, 0)
                                toast.show()
                                addPicButton.isEnabled = true
                            }

                            override fun onReschedule(
                                requestId: String,
                                error: ErrorInfo
                            ) {
                                Log.d(TAG, "onStart: $error")
                            }
                        }).dispatch()
                }
            }
        }

        return rootView
    }



    fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQ) {
            if (resultCode == RESULT_OK) {

                 imagePath = data?.data
                if (imagePath != null) {
                    val path: String? = imagePath?.path
                    imageView.setImageURI(imagePath)
                }

            }
        }
    }

    private fun getAddPictureObserver(url: String){

        disposable = PictureAccessLayer.addPictureObservable( shared.getString(Constants.TOKEN, "")!!,AddPictureModel(nameEditText.text.toString(), url, viewModel.contents.first { it.name.compareTo(spinner.selectedItem as String) == 0 }.id))
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
                            "Picture added",
                            Toast.LENGTH_LONG
                        )

                    addPicButton.isEnabled = false
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
        super.onDestroyView()
        if(disposable != null){
            if(!disposable!!.isDisposed)
                disposable!!.dispose()
        }
    }



}