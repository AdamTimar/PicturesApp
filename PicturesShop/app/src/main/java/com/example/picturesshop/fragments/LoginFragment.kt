package com.example.picturesshop.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns.EMAIL_ADDRESS
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.marketplaceproject.fragments.RegisterFragment
import com.example.picturesshop.R
import com.example.picturesshop.activities.LoginActivity
import com.example.picturesshop.activities.MainActivity
import com.example.picturesshop.retrofit.accesslayers.UserAccessLayer
import com.example.picturesshop.utils.Constants
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.regex.Pattern

class LoginFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var emailTextInputLayout: TextInputLayout
    private lateinit var passwordEditText: EditText
    private lateinit var passwordTextInputLayout: TextInputLayout
    private lateinit var loginButton: Button
    private lateinit var singUpButton: Button
    private lateinit var clickHereTextView: TextView
    private var loginDisposable: Disposable? = null
    private lateinit var shared: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shared = requireActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
    }

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)

        emailEditText = rootView.findViewById(R.id.userNameEditText2)
        emailTextInputLayout = rootView.findViewById(R.id.userNameTextInputLayout2)
        passwordEditText = rootView.findViewById(R.id.passwordEditText2)
        passwordTextInputLayout = rootView.findViewById(R.id.passwordTextInputLayout2)
        loginButton = rootView.findViewById(R.id.loginButton)
        singUpButton = rootView.findViewById(R.id.signUpButton)
        clickHereTextView = rootView.findViewById(R.id.clickHereTextView)

        setOnClickListenerForLoginButton()
        //goooA()
        setOnClickListenerForSignUpButton()
        setOnClickListenerForClickMeTextView()
        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        if (loginDisposable != null) {
            if (!loginDisposable!!.isDisposed)
                loginDisposable!!.dispose()
        }
    }

    private fun setOnClickListenerForSignUpButton() {

        singUpButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.loginFragmentContainerView, RegisterFragment())
            fragmentTransaction.commit()
        }
    }

    private fun setOnClickListenerForLoginButton() {

        loginButton.setOnClickListener {

            emailTextInputLayout.error = ""
            passwordTextInputLayout.error = ""

            var areErrors = false

            if (emailEditText.text.isEmpty()) {
                emailTextInputLayout.error = "Please fill the email field!"
                areErrors = true
            }

            else{
                if (!EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()) {
                    emailTextInputLayout.error = "Not an email!"
                    areErrors = true
                }

            }


            if (passwordEditText.text.isEmpty()) {
                passwordTextInputLayout.error = "Please fill the password field!"
                areErrors = true
            }

            else{
                if(passwordEditText.text.length<8){
                    passwordTextInputLayout.error = "Password length must be at least 8 characters!"
                    areErrors = true
                }

            }


            if (!areErrors) {
                createLoginObserver()
            }


        }

    }


    private fun goo() {
        loginButton.setOnClickListener {

            emailEditText.setText("AdamTimar")
            passwordEditText.setText("12345678")

            createLoginObserver()

        }
    }



    private fun setOnClickListenerForClickMeTextView(){

        clickHereTextView.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction  = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.loginFragmentContainerView, ForgotPasswordFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }




    private fun createLoginObserver() {
        loginDisposable = UserAccessLayer.getLoginObservable(
            emailEditText.text.toString(),
            passwordEditText.text.toString(),
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    val edit = shared.edit()
                    edit.putString(Constants.TOKEN, "bearer " + it.token)
                    Log.d("token", it.token)
                    edit.putInt(Constants.ID, it.id)
                    edit.putString(Constants.FIRSTNAME, it.firstName)
                    edit.putString(Constants.LASTNAME, it.lastName)
                    edit.putString(Constants.PASSWORD, passwordEditText.text.toString())
                    edit.putString(Constants.EMAIL, emailEditText.text.toString())
                    edit.putString(Constants.ROLE, it.role)
                    edit.putString(Constants.BIRTHDATE, it.birthDate)
                    edit.apply()
                    startActivity(intent)
                    requireActivity().finish()
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

    private fun gooo(){
        loginButton.setOnClickListener{

            emailEditText.setText("adamscy15@gmail.com")
            passwordEditText.setText("12345678")

            createLoginObserver()
        }
    }

    private fun goooA(){
        loginButton.setOnClickListener{

            emailEditText.setText("timaradam19@gmail.com")
            passwordEditText.setText("12345678")

            createLoginObserver()
        }
    }
}

