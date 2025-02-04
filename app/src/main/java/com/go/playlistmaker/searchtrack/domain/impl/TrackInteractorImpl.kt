package com.go.playlistmaker.searchtrack.domain.impl

import com.go.playlistmaker.searchtrack.domain.api.TrackInteractor
import com.go.playlistmaker.searchtrack.domain.api.TrackRepository
import com.go.playlistmaker.searchtrack.domain.models.Track
import com.go.playlistmaker.searchtrack.util.Resource
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {
    private var executor = Executors.newCachedThreadPool()

    override fun findMusic(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            when (val resource = repository.findMusic(expression)) {
                is Resource.Success -> {
                    consumer.consume(resource.data.orEmpty(), null)
                }

                is Resource.Error -> {
                    consumer.consume(emptyList(), resource.message)
                }
            }
        }
    }

    override fun findMusicHistory(consumer: TrackInteractor.TrackConsumerHistory) {
        executor.execute {
            consumer.consume(repository.findMusicHistory())
        }
    }

    override fun addMusicHistory(track: Track) {
        repository.addMusicHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}