package com.amosh.movieapp.ui

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import com.amosh.movieapp.R
import com.amosh.movieapp.databinding.ActivityMainBinding
import com.amosh.movieapp.models.AppMessage
import com.amosh.movieapp.models.AppMessage.Companion.INFO
import com.amosh.movieapp.ui.mainScreen.MainScreenFragment
import com.amosh.movieapp.utils.makeGone
import com.amosh.movieapp.utils.makeVisible
import com.amosh.movieapp.utils.setIsVisible
import com.andrognito.flashbar.Flashbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(this, R.id.navHostFragment)

        with(navController) {
            setGraph(
                R.navigation.nav_graph
            )
            addOnDestinationChangedListener(this@MainActivity)
        }

        binding.toolbarBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        val navigation = navController
        when ((navigation.currentDestination as FragmentNavigator.Destination).className) {
            (MainScreenFragment::class.java).canonicalName -> doubleToExit()
            else -> navigation.navigateUp()
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        binding.toolbarBack.setIsVisible(destination.id != R.id.mainScreenFragment)
    }

    private fun doubleToExit() {
        if (doubleBackToExitPressedOnce) {
            finish()
            return
        }
        doubleBackToExitPressedOnce = true
        showMessage(
            AppMessage(
                id = INFO,
                message = getString(R.string.back_btn_message)
            )
        )
        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showMessage(message: AppMessage) {
        Flashbar.Builder(this)
            .gravity(Flashbar.Gravity.TOP)
            .duration(3000L)
            .backgroundColorRes(message.getMessageColor())
            .messageColorRes(R.color.white)
            .messageTypeface(Typeface.DEFAULT)
            .message(message.message)
            .build()
            .show()
    }
}