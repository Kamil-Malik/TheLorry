package com.lelestacia.thelorrytest.repository

import android.content.Context
import com.lelestacia.thelorrytest.R
import com.lelestacia.thelorrytest.data.model.CommentDTO
import com.lelestacia.thelorrytest.data.model.GenericTypeError
import com.lelestacia.thelorrytest.data.model.PostCommentErrorDTO
import com.lelestacia.thelorrytest.data.model.RestaurantDTO
import com.lelestacia.thelorrytest.data.remote.RestaurantAPI
import com.lelestacia.thelorrytest.data.repository.IRestaurantRepository
import com.lelestacia.thelorrytest.data.repository.RestaurantRepository
import com.lelestacia.thelorrytest.domain.mapper.asComment
import com.lelestacia.thelorrytest.domain.mapper.asDetailRestaurant
import com.lelestacia.thelorrytest.domain.mapper.asRestaurant
import com.lelestacia.thelorrytest.domain.model.PostComment
import com.lelestacia.thelorrytest.util.CommentsResponse
import com.lelestacia.thelorrytest.util.ErrorParserUtil
import com.lelestacia.thelorrytest.util.PostCommentErrorParserUtil
import com.lelestacia.thelorrytest.util.PostCommentErrorResponse
import com.lelestacia.thelorrytest.util.PostCommentResponse
import com.lelestacia.thelorrytest.util.Resource
import com.lelestacia.thelorrytest.util.RestaurantDetailResponse
import com.lelestacia.thelorrytest.util.RestaurantNotFoundErrorResponse
import com.lelestacia.thelorrytest.util.RestaurantsNotAvailableResponse
import com.lelestacia.thelorrytest.util.RestaurantsResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class RepositoryTest {

    @MockK
    lateinit var restaurantAPI: RestaurantAPI
    private lateinit var restaurantRepository: IRestaurantRepository

    private val context: Context = RuntimeEnvironment.getApplication().applicationContext
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
    private val moshi: Moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val genericErrorJsonAdapter: JsonAdapter<GenericTypeError> = moshi
        .adapter(GenericTypeError::class.java).lenient()
    private val postCommentErrorJsonAdapter: JsonAdapter<PostCommentErrorDTO> = moshi
        .adapter(PostCommentErrorDTO::class.java)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        restaurantRepository = RestaurantRepository(
            restaurantAPI = restaurantAPI,
            errorParserUtil = ErrorParserUtil(
                context = context
            ),
            postCommentErrorParser = PostCommentErrorParserUtil(
                context = context
            ),
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `List of Restaurant should be Correct and Success`() = runTest {
        val expectedResult = RestaurantsResponse
        coEvery { restaurantAPI.getRestaurantsListByCategory("") } answers { expectedResult }
        val actualResult = restaurantRepository.getRestaurantsListByCategory("").toList()
        assertTrue(
            "First result should be Loading",
            actualResult.first() is Resource.Loading
        )
        assertTrue(
            "Second result should be Success",
            actualResult.last() is Resource.Success
        )
        assertEquals(
            "Second result should match the expected result",
            expectedResult.data.restaurants.map(RestaurantDTO::asRestaurant),
            (actualResult.last() as Resource.Success).data
        )
    }

    @Test
    fun `List of Restaurant HTTP error message should be correct`() = runTest {
        val expectedResult = RestaurantsNotAvailableResponse
        val json: String = genericErrorJsonAdapter.toJson(expectedResult)
        coEvery { restaurantAPI.getRestaurantsListByCategory("") } throws HttpException(
            Response.error<ResponseBody>(
                400,
                json.toResponseBody()
            )
        )
        val actualResult = restaurantRepository.getRestaurantsListByCategory("").toList()
        assertTrue(
            "First result should be Loading",
            actualResult.first() is Resource.Loading
        )
        assertTrue(
            "Second result should be Error",
            actualResult.last() is Resource.Error
        )
        assertEquals(
            "Error message should match expected error message",
            expectedResult.message,
            (actualResult.last() as Resource.Error).message
        )
    }

    @Test
    fun `List of Restaurant Connection error message should be correct`() = runTest {
        val expectedMessage = context.getString(R.string.no_connection)
        coEvery { restaurantAPI.getRestaurantsListByCategory("") } throws UnknownHostException(
            expectedMessage
        )
        val actualResult = restaurantRepository.getRestaurantsListByCategory("").toList()
        assertTrue(
            "First result should be Loading",
            actualResult.first() is Resource.Loading
        )
        assertTrue(
            "Second result should be Error",
            actualResult.last() is Resource.Error
        )
        assertEquals(
            "Error message should match expected error message",
            expectedMessage,
            (actualResult.last() as Resource.Error).message
        )
    }

    @Test
    fun `Detail Restaurant should be Correct and Success`() = runTest {
        val expectedResult = RestaurantDetailResponse
        coEvery { restaurantAPI.getRestaurantDetailsByID(1) } answers { expectedResult }
        val actualResult = restaurantRepository.getRestaurantDetailsByID(1)
        assertTrue(
            "First result should be Loading",
            actualResult.first() is Resource.Loading
        )
        assertTrue(
            "Second result should be Success",
            actualResult.last() is Resource.Success
        )
        assertEquals(
            "Second result should match the expected result",
            expectedResult.data.asDetailRestaurant(),
            (actualResult.last() as Resource.Success).data
        )
    }

    @Test
    fun `Detail Restaurant HTTP error message should be correct`() = runTest {
        val expectedResult = RestaurantNotFoundErrorResponse
        val json: String = genericErrorJsonAdapter.toJson(expectedResult)
        coEvery { restaurantAPI.getRestaurantDetailsByID(1) } throws HttpException(
            Response.error<ResponseBody>(
                400,
                json.toResponseBody()
            )
        )
        val actualResult = restaurantRepository.getRestaurantDetailsByID(1).toList()
        assertTrue(
            "First result should be Loading",
            actualResult.first() is Resource.Loading
        )
        assertTrue(
            "Second result should be Error",
            actualResult.last() is Resource.Error
        )
        assertEquals(
            "Error message should match expected error message",
            expectedResult.message,
            (actualResult.last() as Resource.Error).message
        )
    }

    @Test
    fun `Detail Restaurant Connection error message should be correct`() = runTest {
        val expectedMessage = context.getString(R.string.no_connection)
        coEvery { restaurantAPI.getRestaurantDetailsByID(1) } throws UnknownHostException(
            expectedMessage
        )
        val actualResult = restaurantRepository.getRestaurantDetailsByID(1).toList()
        assertTrue(
            "First result should be Loading",
            actualResult.first() is Resource.Loading
        )
        assertTrue(
            "Second result should be Error",
            actualResult.last() is Resource.Error
        )
        assertEquals(
            "Error message should match expected error message",
            expectedMessage,
            (actualResult.last() as Resource.Error).message
        )
    }

    @Test
    fun `Comments should be Correct and Success`() = runTest {
        val expectedResult = CommentsResponse
        coEvery { restaurantAPI.getCommentsByRestaurantID(1, 1) } answers { expectedResult }
        val actualResult = restaurantRepository.getCommentsByRestaurantID(1, 1)
        assertTrue(
            "First result should be Loading",
            actualResult.first() is Resource.Loading
        )
        assertTrue(
            "Second result should be Success",
            actualResult.last() is Resource.Success
        )
        assertEquals(
            "Second result should match the expected result",
            expectedResult.data.comments.map(CommentDTO::asComment),
            (actualResult.last() as Resource.Success).data
        )
    }

    @Test
    fun `Comments HTTP error message should be correct`() = runTest {
        val expectedResult = RestaurantNotFoundErrorResponse
        val json: String = genericErrorJsonAdapter.toJson(expectedResult)
        coEvery { restaurantAPI.getCommentsByRestaurantID(1, 1) } throws HttpException(
            Response.error<ResponseBody>(
                400,
                json.toResponseBody()
            )
        )
        val actualResult = restaurantRepository.getCommentsByRestaurantID(1, 1).toList()
        assertTrue(
            "First result should be Loading",
            actualResult.first() is Resource.Loading
        )
        assertTrue(
            "Second result should be Error",
            actualResult.last() is Resource.Error
        )
        assertEquals(
            "Error message should match expected error message",
            expectedResult.message,
            (actualResult.last() as Resource.Error).message
        )
    }

    @Test
    fun `Comments Connection error message should be correct`() = runTest {
        val expectedMessage = context.getString(R.string.no_connection)
        coEvery { restaurantAPI.getCommentsByRestaurantID(1, 1) } throws UnknownHostException(
            expectedMessage
        )
        val actualResult = restaurantRepository.getCommentsByRestaurantID(1, 1).toList()
        assertTrue(
            "First result should be Loading",
            actualResult.first() is Resource.Loading
        )
        assertTrue(
            "Second result should be Error",
            actualResult.last() is Resource.Error
        )
        assertEquals(
            "Error message should match expected error message",
            expectedMessage,
            (actualResult.last() as Resource.Error).message
        )
    }

    @Test
    fun `Post Comments should be Correct and Success`() = runTest {
        val expectedResult = PostCommentResponse
        coEvery { restaurantAPI.sendCommentToRestaurantByID(expectedResult.data) } answers { expectedResult }
        val actualResult = restaurantRepository.sendCommentToRestaurantByID(
            PostComment(
                restaurantID = expectedResult.data.id,
                message = expectedResult.data.message
            )
        )
        assertTrue(
            "First result should be Loading",
            actualResult.first() is Resource.Loading
        )
        assertTrue(
            "Second result should be Success",
            actualResult.last() is Resource.Success
        )
        assertEquals(
            "Second result should match the expected result",
            expectedResult.message,
            (actualResult.last() as Resource.Success).data
        )
    }

    @Test
    fun `Post Comments HTTP error message should be correct`() = runTest {
        val expectedResult = PostCommentErrorResponse
        val json: String = postCommentErrorJsonAdapter.toJson(expectedResult)
        coEvery { restaurantAPI.sendCommentToRestaurantByID(PostCommentResponse.data) } throws HttpException(
            Response.error<ResponseBody>(
                400,
                json.toResponseBody()
            )
        )
        val actualResult = restaurantRepository.sendCommentToRestaurantByID(
            PostComment(
                PostCommentResponse.data.id,
                PostCommentResponse.data.message
            )
        ).toList()
        assertTrue(
            "First result should be Loading",
            actualResult.first() is Resource.Loading
        )
        assertTrue(
            "Second result should be Error",
            actualResult.last() is Resource.Error
        )
        assertEquals(
            "Error message should match expected error message",
            expectedResult.errors[0].messages,
            (actualResult.last() as Resource.Error).message
        )
    }

    @Test
    fun `Post Comments Connection error message should be correct`() = runTest {
        val expectedMessage = context.getString(R.string.no_connection)
        coEvery { restaurantAPI.sendCommentToRestaurantByID(PostCommentResponse.data) } throws UnknownHostException(
            expectedMessage
        )
        val actualResult = restaurantRepository.sendCommentToRestaurantByID(
            PostComment(
                restaurantID = PostCommentResponse.data.id,
                message = PostCommentResponse.data.message
            )
        ).toList()
        assertTrue(
            "First result should be Loading",
            actualResult.first() is Resource.Loading
        )
        assertTrue(
            "Second result should be Error",
            actualResult.last() is Resource.Error
        )
        assertEquals(
            "Error message should match expected error message",
            expectedMessage,
            (actualResult.last() as Resource.Error).message
        )
    }
}