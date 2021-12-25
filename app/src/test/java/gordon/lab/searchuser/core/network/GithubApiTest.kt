package gordon.lab.searchuser.core.network

import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit
import org.junit.After
import java.io.IOException

