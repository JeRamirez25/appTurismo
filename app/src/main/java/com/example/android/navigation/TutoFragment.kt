package com.example.android.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.android.navigation.databinding.FragmentTutoBinding


class TutoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding:FragmentTutoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tuto, container, false)

        binding.imageButton.setOnClickListener (
            Navigation.createNavigateOnClickListener(R.id.action_tutoFragment_to_tutoriFragment)
        )
        return binding.root
    }

}