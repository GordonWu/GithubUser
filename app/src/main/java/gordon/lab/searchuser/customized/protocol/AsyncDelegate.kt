package gordon.lab.searchuser.customized.protocol

interface AsyncDelegate{
  fun ioJob(func: suspend () -> Unit)
  suspend fun uiJob(func: () -> Unit)
}