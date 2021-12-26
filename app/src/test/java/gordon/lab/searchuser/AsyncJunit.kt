package gordon.lab.searchuser

import gordon.lab.searchuser.core.AsyncDelegate
import kotlinx.coroutines.runBlocking

class AsyncJunit:AsyncDelegate() {

    override fun ioJob(func: suspend () -> Unit)  {
        runBlocking { func() }
    }
    override suspend fun uiJob(func: () -> Unit) {
        func()
    }
}
