package com.example.picturesshop.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.picturesshop.R
import com.example.picturesshop.retrofit.accesslayers.UserAccessLayer
import com.example.picturesshop.utils.Constants
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ValidatePasswordChangeFragment: Fragment() {

    private lateinit var codeEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var codeInputLayout: TextInputLayout
    private lateinit var newPasswordInputLayout: TextInputLayout
    private var verifyPasswordChangeDisposable: Disposable? = null
    private var passwordChangeDisposable: Disposable? = null


    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_validate_password_change, container, false)
        codeEditText = rootView.findViewById(R.id.codeChangePasswordEditText)
        codeInputLayout = rootView.findViewById(R.id.codeChangePasswordTextInputLayout)
        newPasswordEditText = rootView.findViewById(R.id.newPasswordEditText)
        newPasswordInputLayout = rootView.findViewById(R.id.newPasswordTextInputLayout)
        updateButton = rootView.findViewById(R.id.updatePwButton)


        setOnClickListenerForRegisterButton()

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        if(verifyPasswordChangeDisposable!=null) {
            if (!verifyPasswordChangeDisposable!!.isDisposed)
                verifyPasswordChangeDisposable!!.dispose()
        }

        if(passwordChangeDisposable!=null) {
            if (passwordChangeDisposable!!.isDisposed)
                passwordChangeDisposable!!.dispose()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setOnClickListenerForRegisterButton() {

        updateButton.setOnClickListener {

            codeInputLayout.error = ""
            newPasswordInputLayout.error = ""

            var areErrors = false


            if (codeEditText.text.isEmpty()) {
                codeInputLayout.error = "Please fill the code field!"
                areErrors = true
            } else if (codeEditText.text.length<8)
             {
                codeInputLayout.error = "Code length must be 8 characters!"
                areErrors = true
            }

            if (newPasswordEditText.text.isEmpty()) {
                newPasswordInputLayout.error = "Please fill the password field!"
                areErrors = true
            } else if (codeEditText.text.length<8)
            {
                codeInputLayout.error = "Password length must be at least 8 characters!"
                areErrors = true
            }

            if (!areErrors) {
                createPasswordChangeValidationObserver()
            }

        }

    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun createPasswordChangeValidationObserver() {
        verifyPasswordChangeDisposable = UserAccessLayer.getValidatePasswordChangeObservable(
            arguments!!.getString(Constants.EMAIL)!!,
            codeEditText.text.toString()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    createPasswordChangeObserver()
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


    @SuppressLint("UseRequireInsteadOfGet")
    private fun createPasswordChangeObserver() {
        passwordChangeDisposable = UserAccessLayer.getPasswordChangeObservable(
            arguments!!.getString(Constants.EMAIL)!!,
            newPasswordEditText.text.toString()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    val fragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = FeedBackFragment()
                    val bundle = Bundle()
                    bundle.putBoolean(Constants.PASSWORDCHANGEFEEDBACK, true)
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
}
