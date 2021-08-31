package com.amosh.movieapp.ui.mainScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import com.amosh.movieapp.R
import com.amosh.movieapp.databinding.FragmentMainScreenBinding
import com.amosh.movieapp.utils.BaseFragment

class MainScreenFragment: BaseFragment() {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    override fun getLayoutRoot(inflater: LayoutInflater): View {
        _binding = FragmentMainScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onBindingDestroy() {
        _binding = null
    }

    override fun afterCreation(bundle: Bundle?) {
        with(binding) {
            showMoviesList.setOnClickListener {
                findNavController().navigate(
                    R.id.action_mainScreenFragment_to_moviesListFragment
                )
            }

            addMovie.setOnClickListener {
                findNavController().navigate(
                    R.id.action_mainScreenFragment_to_addMovieFragment
                )
            }
        }
    }
}