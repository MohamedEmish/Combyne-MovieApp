package com.amosh.movieapp.utils

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amosh.movieapp.R
import com.amosh.movieapp.models.AppMessage
import com.andrognito.flashbar.Flashbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(), CoroutineScope {
    private lateinit var job: Job

    var fragmentContainer: ViewGroup? = null

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        fragmentContainer = container
        return getLayoutRoot(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterCreation(savedInstanceState)
    }

    abstract fun getLayoutRoot(inflater: LayoutInflater): View

    abstract fun onBindingDestroy()

    protected abstract fun afterCreation(bundle: Bundle?)

    fun showMessage(message: AppMessage) {
        if (isVisible)
            Flashbar.Builder(requireActivity())
                    .gravity(Flashbar.Gravity.TOP)
                    .duration(3000L)
                    .backgroundColorRes(message.getMessageColor())
                    .messageColorRes(R.color.white)
                    .messageTypeface(Typeface.DEFAULT)
                    .message(message.message)
                    .build()
                    .show()
    }


    override fun onDestroy() {
        super.onDestroy()
        onBindingDestroy()
        job.cancel()
    }
}