package gordon.lab.searchuser.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class AsyncDelegate{

    open fun ioJob(func: suspend () -> Unit) {
       CoroutineScope(Dispatchers.IO).launch {
           func()
       }
   }

   open suspend fun uiJob(func: () -> Unit) = withContext(Dispatchers.Main) {
       func()
   }
}