package com.appsfactory.test.ui.album_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.appsfactory.test.databinding.ItemTrackBinding
import com.appsfactory.test.domain.model.track.Track

class TrackAdapter(
    private val onClick: (Track) -> Unit
) : ListAdapter<Track, TrackAdapter.AlbumTrackViewHolder>(TRACK_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumTrackViewHolder {
        val binding = ItemTrackBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlbumTrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumTrackViewHolder, position: Int) {
        val track = getItem(position)
        track?.let { holder.bind(it) }
    }

    inner class AlbumTrackViewHolder(
        private val binding: ItemTrackBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onClick.invoke(getItem(adapterPosition))
            }
        }

        fun bind(track: Track) {
            binding.apply {
                trackName.text = track.name.trim()
                duration.text = track.duration

                val count = adapterPosition + 1
                trackNumber.text = count.toString()
            }
        }
    }

    companion object {
        private val TRACK_COMPARATOR = object : DiffUtil.ItemCallback<Track>() {
            override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
                return oldItem == newItem
            }
        }
    }
}