package ghazimoradi.soheil.weather.utils.events

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance

object EventBus {
    private val events = MutableSharedFlow<Any>()
    val usableEvent = events.asSharedFlow()

    suspend fun publish(event: Any) = events.emit(event)

    suspend inline fun <reified T> subscribe(crossinline onEvents: (T) -> Unit) {
        usableEvent.filterIsInstance<T>().collectLatest {
            currentCoroutineContext().ensureActive()
            onEvents(it)
        }
    }
}