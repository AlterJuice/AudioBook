package com.alterjuice.test.audiobook.errors

sealed class GlobalError(message: String? = null, cause: Throwable? = null) : AppError(message, cause){

    class ConnectionError(val type: Issue): GlobalError("Connection issue: $type") {
        enum class Issue {
            NO_INTERNET,
            BAD_INTERNET;
            operator fun invoke() = ConnectionError(this)
        }
    }


    data class ServerError(val code: Int) : GlobalError("Server error")
    class PlaybackError(message: String) : GlobalError("Playback error: ${message}")
    class Unknown(t: Throwable? = null) : GlobalError("Unknown", cause = t)
}