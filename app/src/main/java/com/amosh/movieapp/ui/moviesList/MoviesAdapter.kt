package com.amosh.movieapp.ui.moviesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amosh.combyne_movieapp.fragment.MovieObject
import com.amosh.movieapp.databinding.ItemMovieBinding
import com.amosh.movieapp.utils.Constants
import com.amosh.movieapp.utils.convertFromDateFormatToAnother
import java.text.SimpleDateFormat
import java.util.*

class MoviesAdapter() :
    ListAdapter<MovieObject, MoviesAdapter.ViewHolder>(DIFF_CALLBACK) {

    private val parser = SimpleDateFormat(Constants.SERVER_DATE_FORMAT_SUB, Locale.US)
    private val formatter = SimpleDateFormat(Constants.APP_DATE_FORMAT, Locale.US)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) holder.bindTo(item)
    }

    inner class ViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(item: MovieObject) {
            with(binding) {
                tvTitle.text = item.title
                tvReleaseDate.text =
                    getDateOf((item.releaseDate as? String)?.split("'")?.get(0) ?: "")
                tvSeasons.text = (item.seasons ?: 0.0).toInt().toString()
            }
        }
    }

    private fun getDateOf(releaseDate: String): String = when {
        releaseDate.isEmpty() -> ""
        else -> try {
            releaseDate.convertFromDateFormatToAnother(
                Constants.SERVER_DATE_FORMAT_SUB,
                Constants.APP_DATE_FORMAT
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<MovieObject> =
            object : DiffUtil.ItemCallback<MovieObject>() {
                override fun areItemsTheSame(oldItem: MovieObject, newItem: MovieObject): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: MovieObject,
                    newItem: MovieObject
                ): Boolean =
                    oldItem.id == newItem.id
            }
    }
}