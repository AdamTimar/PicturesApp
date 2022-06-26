package com.example.picturesshop.fragments

import android.os.Bundle
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
import java.util.regex.Pattern


class ForgotPasswordFragment : Fragment() {

    private lateinit var emailTextInputLayout : TextInputLayout
    private lateinit var emailEditText: EditText
    private lateinit var emailMeButton : Button
    private var disposable : Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false)

        emailTextInputLayout = rootView.findViewById(R.id.emailForgotPasswordTextInputLayout)
        emailEditText = rootView.findViewById(R.id.emailForgotPasswordEditText)
        emailMeButton = rootView.findViewById(R.id.emailMeButton)

        setOnClickListenerForEmailMeButton()

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        if(disposable != null) {
            if (!disposable!!.isDisposed)
                disposable!!.dispose()
        }
    }



    private fun setOnClickListenerForEmailMeButton(){

        emailMeButton.setOnClickListener {

            emailTextInputLayout.error = ""

            var areErrors = false

            if (emailEditText.text.isEmpty()) {
                emailTextInputLayout.error = "Please fill the email field!"
                areErrors = true
            }

            else if (!Pattern.compile(Constants.EMAIL_REGEX_PATTERN).matcher(emailEditText.text)
                    .matches()
            ) {
                emailTextInputLayout.error = "Not an email!"
                areErrors = true
            }

            if(!areErrors){

                createPasswordChangeSendMail()
            }
        }
    }

    private fun createPasswordChangeSendMail(){

        disposable = UserAccessLayer.getSendPasswordChangeObservable(
            emailEditText.text.toString()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    if (it) {
                        val bundle = Bundle()
                        bundle.putString(Constants.EMAIL, emailEditText.text.toString())

                        val fragment = ValidatePasswordChangeFragment()
                        fragment.arguments = bundle

                        val transaction =
                            requireActivity().supportFragmentManager.beginTransaction()

                        transaction.replace(R.id.loginFragmentContainerView, fragment)
                            .addToBackStack(null).commit()
                    }
                    else{
                        val toast =
                            Toast.makeText(
                                context,
                                "Failed to send password change code!",
                                Toast.LENGTH_LONG
                            )
                        toast.setGravity(Gravity.TOP, 0, 0)
                        toast.show()
                    }
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