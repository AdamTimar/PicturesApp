package com.example.marketplaceproject.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.picturesshop.R
import com.example.picturesshop.fragments.LoginFragment
import com.example.picturesshop.fragments.ValidateRegistrationFragment
import com.example.picturesshop.retrofit.accesslayers.UserAccessLayer
import com.example.picturesshop.utils.Constants
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern




class RegisterFragment : Fragment() {

    private lateinit var datePicker: DatePicker
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var firstNameEditText: EditText
    private lateinit var logInTextView: TextView
    private lateinit var registerButton: Button
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var lastNameInputLayout: TextInputLayout
    private lateinit var firstNameInputLayout: TextInputLayout
    private var registrationDisposable: Disposable? = null


    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_register, container, false)
        emailEditText = rootView.findViewById(R.id.emailEditText)
        passwordEditText = rootView.findViewById(R.id.passwordEditText)
        lastNameEditText = rootView.findViewById(R.id.lastNameEditText)
        firstNameEditText = rootView.findViewById(R.id.firstNameEditText)
        logInTextView = rootView.findViewById(R.id.loginTextView)
        registerButton = rootView.findViewById(R.id.registerButton)
        emailInputLayout = rootView.findViewById(R.id.emailTextInputLayout)
        passwordInputLayout = rootView.findViewById(R.id.passwordTextInputLayout)
        lastNameInputLayout = rootView.findViewById(R.id.lastNameTextInputLayout)
        firstNameInputLayout = rootView.findViewById(R.id.firstNameTextInputLayout)
        datePicker = rootView.findViewById(R.id.datePicker)

        setOnClickListenerForRegisterButton()
        setOnClickListenerForLogInTextView()

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        if(registrationDisposable!=null) {
            if (!registrationDisposable!!.isDisposed)
                registrationDisposable!!.dispose()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setOnClickListenerForRegisterButton() {

        registerButton.setOnClickListener {

            emailInputLayout.error = ""
            passwordInputLayout.error = ""

            var areErrors = false


            if (emailEditText.text.isEmpty()) {
                emailInputLayout.error = "Please fill the email field!"
                areErrors = true
            } else if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()
            ) {
                emailInputLayout.error = "Not an email!"
                areErrors = true
            }

            if (passwordEditText.text.isEmpty()) {
                passwordInputLayout.error = "Please fill the password field!"
                areErrors = true
            }

            else{
                if(passwordEditText.text.length<8){
                    passwordInputLayout.error = "Password length must be at least 8 characters"
                    areErrors = true
                }
            }

            if (lastNameEditText.text.isEmpty()) {
                lastNameInputLayout.error = "Please fill the last name field!"
                areErrors = true
            }

            if (firstNameEditText.text.isEmpty()) {
                firstNameInputLayout.error = "Please fill the first name field!"
                areErrors = true
            }

            val year: Int = datePicker.getYear()
            val month: Int = datePicker.getMonth()
            val day: Int = datePicker.getDayOfMonth()

            val calendar: Calendar = Calendar.getInstance()
            calendar.set(year, month, day)

            val format = SimpleDateFormat("yyyy-MM-dd")
            val strDate: String = format.format(calendar.time)

            if(calendar.time>Calendar.getInstance().time){
                val toast =
                    Toast.makeText(
                        context,
                        "Choose a date earlier than current date",
                        Toast.LENGTH_LONG
                    )
                toast.setGravity(Gravity.TOP, 0, 0)
                toast.show()
                areErrors = true
            }

            if (!areErrors) {
                createRegistrationObserver(strDate)
            }

        }

    }

    private fun setOnClickListenerForLogInTextView() {
        logInTextView.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.loginFragmentContainerView, LoginFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    private fun createRegistrationObserver(birthDate: String) {
        registrationDisposable = UserAccessLayer.getRegistrationObservable(
            emailEditText.text.toString(),
            passwordEditText.text.toString(),
            firstNameEditText.text.toString(),
            lastNameEditText.text.toString(),
            birthDate
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    val fragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = ValidateRegistrationFragment()
                    val bundle = Bundle()
                    bundle.putString(Constants.EMAIL, emailEditText.text.toString())
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

