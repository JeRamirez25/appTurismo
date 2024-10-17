package com.example.android.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.android.navigation.databinding.FragmentGameWonBinding
import com.example.android.navigation.databinding.FragmentTuBinding


class TuFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTuBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tu, container, false)
        // Inflate the layout for this fragment
        binding.imageButton.setOnClickListener (
            Navigation.createNavigateOnClickListener(R.id.action_tuFragment_to_tutoFragment)
        )

        return binding.root
    }


}