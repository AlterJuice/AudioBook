package com.alterjuice.test.audiobook.utils.other

import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext



typealias JobBlock = suspend CoroutineScope.() -> Unit
typealias JobArgsBlock<T> =  suspend CoroutineScope.(T) -> Unit



/**
 * A specialized version of [RestartableJobArgs] that uses a fixed null value as the job argument.
 * This class provides functionality to start, restart, and join coroutine jobs with scoped contexts.
 *
 * @param exceptionHandler Optional [CoroutineExceptionHandler] to handle exceptions within the coroutine.
 * @param launchBlock A block of code to execute in the job.
 *
 *
 * @see RestartableJobArgs
 */
class RestartableJob(
    exceptionHandler: CoroutineExceptionHandler? = null,
    launchBlock: JobArgsBlock<Nothing?>,
) : RestartableJobArgs<Nothing?>(exceptionHandler, launchBlock) {

    /**
     * Restarts the coroutine job in the given [scope] and [context], with an optional [completionHandler].
     *
     * @param scope The [CoroutineScope] to launch the job in.
     * @param context The [CoroutineContext] to be used; defaults to [EmptyCoroutineContext].
     * @param completionHandler A [CompletionHandler] to invoke when the job completes.
     * @return The restarted [Job].
     */
    fun restart(
        scope: CoroutineScope,
        context: CoroutineContext = EmptyCoroutineContext,
        coroutineName: CoroutineName = CoroutineLocalName(),
        start: CoroutineStart = CoroutineStart.DEFAULT,
        completionHandler: CompletionHandler? = null,
    ) = restart(scope, context, start, coroutineName = coroutineName, value = null, completionHandler = completionHandler)
}

/**
 * A utility class to manage restartable coroutine jobs with arguments.
 * This class allows starting, restarting, joining, and canceling coroutine jobs.
 *
 * @param T The type of the argument passed to the job block.
 * @param exceptionHandler Optional [CoroutineExceptionHandler] to handle exceptions within the coroutine.
 * @param launchBlock A block of code to execute in the job with an argument of type [T].
 */
open class RestartableJobArgs<T>(
    private val exceptionHandler: CoroutineExceptionHandler? = null,
    private val launchBlock: JobArgsBlock<T>,
) {

    private var job: Job? = null

    val isActive get() = job?.isActive == true
    val isStarted get() = job != null
    val isFinished get() = job?.let { it.isCompleted || it.isCancelled } == true

    /**
     * Starts or restarts the coroutine job with the given [scope], [context], and [value].
     * Cancels any existing job before starting the new one.
     *
     * @param scope The [CoroutineScope] to launch the job in.
     * @param context The [CoroutineContext] to be used; defaults to [EmptyCoroutineContext].
     * @param value The argument of type [T] to pass to the job block.
     * @param completionHandler A [CompletionHandler] to invoke when the job completes.
     * @return The newly started [Job].
     */
    fun restart(
        scope: CoroutineScope,
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        coroutineName: CoroutineName = CoroutineLocalName(),
        value: T,
        completionHandler: CompletionHandler? = null,
    ): Job {
        cancel()
        val ctx = when (val excHandler = exceptionHandler) {
            null -> context + SupervisorJob()
            else -> excHandler + context + SupervisorJob()
        }
        return scope.launch(coroutineName + ctx, start) { launchBlock.invoke(this, value) }.also { j ->
            completionHandler?.let { j.invokeOnCompletion(it) }
            job = j
        }
    }

    /**
     * Suspends until the currently running job completes.
     */
    suspend fun join() = job?.join()

    /**
     * Cancels the currently running job, if any.
     */
    fun cancel() {
        job?.cancel()
        job = null
    }
}

/**
 * Creates a coroutine name with tag of current stack information;
 * For debug purposes;
 * */
inline fun Any.CoroutineLocalName(alternative: String = ""): CoroutineName {
    // return CoroutineName(object {}.javaClass.enclosingMethod?.name ?: this::class.java.simpleName)
    return CoroutineName(alternative)
}




