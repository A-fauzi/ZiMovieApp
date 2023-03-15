package com.afauzi.zimovieapp.presentation.adapter.stateadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afauzi.zimovieapp.databinding.ItemLoadStateBinding

class StateLoadAdapterMoviePaging(private val retry: () -> Unit): LoadStateAdapter<StateLoadAdapterMoviePaging.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemLoadStateBinding, private val retry: () -> Unit):RecyclerView.ViewHolder(binding.root){
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.progressbar.visibility = View.GONE
                binding.textViewError.visibility = View.VISIBLE
                binding.textViewError.text = loadState.error.localizedMessage
            } else {
                binding.progressbar.visibility = View.VISIBLE
            }

            binding.progressbar.isVisible = (loadState is LoadState.Loading)
            binding.buttonRetry.isVisible = (loadState is LoadState.Error)
            binding.textViewError.isVisible = (loadState is LoadState.Error)

            binding.buttonRetry.setOnClickListener {
                binding.textViewError.visibility = View.GONE
                retry()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder = ViewHolder(
        ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false), retry
    )
}