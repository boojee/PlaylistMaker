package com.go.playlistmaker.domain.impl

import com.go.playlistmaker.domain.api.TrackInteractor
import com.go.playlistmaker.domain.api.TrackRepository
import com.go.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {
    private var executor = Executors.newCachedThreadPool()

    override fun findMusic(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.findMusic(expression))
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