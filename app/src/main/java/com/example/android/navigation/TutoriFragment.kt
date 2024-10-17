package com.example.android.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.android.navigation.databinding.FragmentTutoriBinding


class TutoriFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTutoriBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tutori, container, false)
        // Inflate the layout for this fragment

        return binding.root

    }

}