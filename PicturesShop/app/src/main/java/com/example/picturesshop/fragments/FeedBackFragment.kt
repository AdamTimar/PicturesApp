package com.example.picturesshop.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.picturesshop.R
import com.example.picturesshop.utils.Constants

class FeedBackFragment : Fragment() {

    private lateinit var emailSentTextView : TextView
    private lateinit var backToLoginButton : Button
    private lateinit var feedBackTypeTextView : TextView

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_feedback, container, false)

        emailSentTextView = rootView.findViewById(R.id.messageTextView)
        backToLoginButton = rootView.findViewById(R.id.backToLoginButton)
        feedBackTypeTextView = rootView.findViewById(R.id.feedBackTitleTextView)


        if(arguments?.getBoolean(Constants.REGISTRATIONFEEDBACK) == true) {
            emailSentTextView.text = "Your account has been verified with success!"
            feedBackTypeTextView.text = "Verification result"

        }


        if(arguments?.getBoolean(Constants.PASSWORDCHANGEFEEDBACK) == true) {
            emailSentTextView.text = "Your password has been changed with success!"
            feedBackTypeTextView.text = "Password change"
        }

        setOnClickListenerForBackToLoginButton()
        return rootView
    }

    private fun setOnClickListenerForBackToLoginButton(){
        backToLoginButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.loginFragmentContainerView, LoginFragment())
                .commit()
        }
    }

}