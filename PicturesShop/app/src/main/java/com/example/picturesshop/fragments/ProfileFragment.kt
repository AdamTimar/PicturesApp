package com.example.picturesshop.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.picturesshop.R
import com.example.picturesshop.utils.Constants


class ProfileFragment : Fragment() {

    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var birthDateTextView: TextView
    private lateinit var shared: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        shared = requireActivity().getSharedPreferences(Constants.SHAREDPREF, Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        nameTextView = rootView.findViewById(R.id.fullNameTextView)
        emailTextView = rootView.findViewById(R.id.profileEmailTextViewContent)
        birthDateTextView = rootView.findViewById(R.id.profileBirthDateTextViewContent)

        nameTextView.text = shared.getString(Constants.FIRSTNAME,"")+ " " + shared.getString(Constants.LASTNAME,"")
        emailTextView.text = shared.getString(Constants.EMAIL,"")
        birthDateTextView.text = shared.getString(Constants.BIRTHDATE, "")!!.substringBefore('T')


        return rootView
    }


}