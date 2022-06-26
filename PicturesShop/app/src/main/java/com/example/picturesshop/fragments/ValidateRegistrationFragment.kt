package com.example.picturesshop.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Patterns
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.picturesshop.R
import com.example.picturesshop.retrofit.accesslayers.UserAccessLayer
import com.example.picturesshop.utils.Constants
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*


class ValidateRegistrationFragment : Fragment() {

    private lateinit var codeEditText: EditText
    private lateinit var codeTextInputLayout: TextInputLayout
    private lateinit var verifyButton: Button
    private var verificationDisposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_validate_registration, container, false)
        codeEditText = rootView.findViewById(R.id.codeEditText)
        codeTextInputLayout = rootView.findViewById(R.id.codeTextInputLayout)
        verifyButton = rootView.findViewById(R.id.verifyButton)

        setOnClickListenerForRegisterButton()

        return rootView
    }

    private fun setOnClickListenerForRegisterButton() {

        verifyButton.setOnClickListener {

            var areErrors = false

            if (codeEditText.text.isEmpty()) {
                codeTextInputLayout.error = "Please fill the code field!"
                areErrors = true
            }

            else{
                if(codeEditText.text.length<8){
                    codeTextInputLayout.error = "Code length must be 8 characters!"
                    areErrors = true
                }
            }

            if (!areErrors) {
                createVerificationObserver()
            }

        }

    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun createVerificationObserver() {
        verificationDisposable = UserAccessLayer.getVerificationObservable(
            arguments!!.getString(Constants.EMAIL)!!,
            codeEditText.text.toString()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    val fragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = FeedBackFragment()
                    val bundle = Bundle()
                    bundle.putBoolean(Constants.REGISTRATIONFEEDBACK, true)
                    fragment.arguments = bundle
                    fragmentTransaction.replace(R.id.loginFragmentContainerView, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
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


    override fun onDestroy() {
        super.onDestroy()
        if(verificationDisposable!=null) {
            if (!verificationDisposable!!.isDisposed)
                verificationDisposable!!.dispose()
        }
    }


}