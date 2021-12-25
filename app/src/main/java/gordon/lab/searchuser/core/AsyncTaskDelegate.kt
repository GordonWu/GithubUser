package gordon.lab.searchuser.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AsyncTaskDelegate @Inject constructor() {
   fun ioJob(func: suspend ()->Unit) {
      CoroutineScope(Dispatchers.IO).launch {
         func()
      }
   }
   suspend fun uiJob(func: ()->Unit) = withContext(Dispatchers.Main) {
      func()
   }
}