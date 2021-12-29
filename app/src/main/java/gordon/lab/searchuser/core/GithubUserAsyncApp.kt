package gordon.lab.searchuser.core

import gordon.lab.searchuser.customized.protocol.AsyncDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GithubUserAsyncApp: AsyncDelegate {
     override fun ioJob(func: suspend () -> Unit) {
         CoroutineScope(Dispatchers.IO).launch {
           func()
       }
     }

    override suspend fun uiJob(func: () -> Unit)= withContext(Dispatchers.Main) {
       func()
    }
}