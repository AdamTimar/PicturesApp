package com.example.picturesshop.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.picturesshop.R


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val handler = Handler()
        val runnable = Runnable {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.loginFragmentContainerView, LoginFragment())
                .commit()
        }

        handler.postDelayed(runnable, 3000)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

}