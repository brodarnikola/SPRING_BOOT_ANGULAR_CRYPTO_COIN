package com.example.mvi_compose.ui.users

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.mvi_compose.network.data.movie.Trailer
import com.example.mvi_compose.repositories.UserRepo
import com.example.mvi_compose.network.data.movie.Movie
import com.example.mvi_compose.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: UserRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<MovieDetailsState, MovieDetailsEvents>() {


    private var movieId: Long = 0L

    init {
        movieId = savedStateHandle.get<Long>("movieId") ?: 0L
        onEvent(MovieDetailsEvents.FetchTrailers)
        onEvent(MovieDetailsEvents.GetLikeState)
    }

    override fun initialState(): MovieDetailsState {
        return MovieDetailsState()
    }

    override fun onEvent(event: MovieDetailsEvents) {
        when (event) {
            is MovieDetailsEvents.FetchTrailers -> {
                fetchMovieTrailers()
            }

            is MovieDetailsEvents.GetLikeState -> {
                getLikeState()
            }
        }
    }

    private fun fetchMovieTrailers() {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.update {
                    it.copy(isLoading = true)
                }
                delay(2000)
//                val movie = repository.getMovieById(movieId = movieId)
//
//                Log.d("MOVIE_ID", "Movie id is 44: ${movieId.toInt()}")
//                when (val result = repository.fetchMovieTrailers(movieId.toInt())) {
//
//                    is NetworkResult.Error -> {
//
//                    }
//
//                    is NetworkResult.Exception -> {
//
//                    }
//
//                    is NetworkResult.Success -> {
//
//                        val newResult = TrailerResponseMapper()
//                        val finalList = newResult.copy(results = result.data.results.toImmutableList())
//
//                        Log.d("MOVIE_ID", "Movie id is 55: ${result}")
//                        Log.d("MOVIE_ID", "Movie id is 66: ${result.data.results}")
//                        withContext(Dispatchers.Main) {
//                            _state.update {
//                                it.copy(
//                                    isLoading = false,
////                                    trailerExternalIntent = null,
//                                    trailers = finalList.results, // result.data.results,
//                                    movie = movie
//                                )
//                            }
//                        }
//                    }
//                }

            } catch (e: Exception) {
                Log.d("MOVIE_ID", "Movie id is 101: ${e.localizedMessage}")
                _state.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            }
        }
    }

    private fun getLikeState() {
        viewModelScope.launch(Dispatchers.IO) {
//            val movieData = repository.getMovieById(movieId)
//            withContext(Dispatchers.Main) {
//                _state.update {
//                    var movieDetailsState: MovieDetailsState = it
//                    if(it.movie != null)
//                        movieDetailsState = it.copy(isLiked = if(movieData.isLiked != null ) movieData.isLiked == true else false)
//                    movieDetailsState
//              }
//            }
        }
    }

}


sealed class MovieDetailsEvents {
    object FetchTrailers : MovieDetailsEvents()
    object GetLikeState : MovieDetailsEvents()
}

data class MovieDetailsState(
    val movie: Movie? = null,
    val isLoading: Boolean = false,
    val trailers: ImmutableList<Trailer> = persistentListOf(), // List<Trailer>? = null,
    val isLiked: Boolean = false,
//    val trailerExternalIntent: Intent? = null,
    val errorMessage: String? = null
)