package com.amosh.movieapp.ui.addMoview

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import androidx.fragment.app.viewModels
import com.amosh.combyne_movieapp.type.CreateMovieFieldsInput
import com.amosh.movieapp.databinding.FragmentAddMovieBinding
import com.amosh.movieapp.models.AppMessage
import com.amosh.movieapp.models.AppResponseState
import com.amosh.movieapp.ui.MoviesEvent
import com.amosh.movieapp.ui.MoviesViewModel
import com.amosh.movieapp.utils.*
import com.apollographql.apollo.api.Input
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddMovieFragment : BaseFragment(), DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentAddMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by viewModels()

    private val cal: Calendar by lazy {
        Calendar.getInstance()
    }

    private val datePickerDialog by lazy {
        DatePickerDialog(
            requireContext(),
            this,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = cal.timeInMillis
        }
    }

    override fun getLayoutRoot(inflater: LayoutInflater): View {
        _binding = FragmentAddMovieBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onBindingDestroy() {
        _binding = null
    }

    override fun afterCreation(bundle: Bundle?) {
        with(binding) {
            etDate.apply {
                clickableOnly()
                setOnClickListener {
                    KeyboardUtils.hide(requireActivity())
                    showDatePicker()
                }
            }
            btnSave.setOnClickListener {
                KeyboardUtils.hide(requireActivity())
                if (isDataValid()) {
                    viewModel.setEvent(
                        MoviesEvent.AddMovie(
                            CreateMovieFieldsInput(
                                aCL = Input.absent(),
                                title = etShowTitle.text?.toString()?.trim() ?: "",
                                releaseDate = Input.fromNullable(getDate()),
                                seasons = Input.fromNullable(
                                    etSeasons.text?.toString()?.toDoubleOrNull()
                                )
                            )
                        )
                    )
                } else {
                    showMessage(
                        AppMessage(
                            AppMessage.WARNING,
                            "Please fill all data"
                        )
                    )
                }
            }
        }
        initObservables()
    }

    private fun initObservables() {
        with(viewModel) {
            addMovies.observe(viewLifecycleOwner, { response ->
                when (response) {
                    is AppResponseState.Error -> {
                        binding.progressOverlay.root.makeGone()
                        showMessage(
                            AppMessage(
                                AppMessage.FAILURE,
                                response.message
                            )
                        )
                    }
                    is AppResponseState.Loading -> {
                        binding.progressOverlay.root.makeVisible()
                    }
                    is AppResponseState.Success -> {
                        binding.progressOverlay.root.makeGone()
                        showMessage(
                            AppMessage(
                                AppMessage.SUCCESS,
                                "Added Successfully"
                            )
                        )
                        requireActivity().onBackPressed()
                    }
                }
            })
        }
    }

    private fun isDataValid(): Boolean {
        with(binding) {
            val title = etShowTitle.text?.toString()?.trim() ?: ""
            val date = etDate.text?.toString()?.trim() ?: ""
            val seasons = etSeasons.text?.toString()?.trim()?.toInt() ?: 0
            return AddMovieUtil.validateNewMovieInput(
                title = title,
                date = date,
                seasons = seasons
            )
        }
    }

    private fun showDatePicker() = datePickerDialog.show()

    private fun getDate(): String =
        (binding.etDate.text?.toString()?.trim() ?: "").convertFromDateFormatToAnother(
            Constants.APP_DATE_FORMAT,
            Constants.SERVER_DATE_FORMAT
        )

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val date = "$dayOfMonth/${month + 1}/$year"
        binding.etDate.setText(
            date.convertFromDateFormatToAnother(
                currentFormat = Constants.CALENDER_DATE_FORMAT,
                requiredFormat = Constants.APP_DATE_FORMAT
            )
        )
    }
}