package com.lelestacia.thelorrytest

import com.lelestacia.thelorrytest.data.model.GenericType
import com.lelestacia.thelorrytest.data.model.GenericTypeError
import com.lelestacia.thelorrytest.data.model.RestaurantDTO
import com.lelestacia.thelorrytest.data.model.RestaurantListDTO
import com.lelestacia.thelorrytest.data.remote.RestaurantAPI
import com.lelestacia.thelorrytest.data.repository.IRestaurantRepository
import com.lelestacia.thelorrytest.data.repository.RestaurantRepository
import com.lelestacia.thelorrytest.domain.mapper.asRestaurant
import com.lelestacia.thelorrytest.util.ErrorParserUtil
import com.lelestacia.thelorrytest.util.Resource
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
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
import org.junit.runners.BlockJUnit4ClassRunner
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(BlockJUnit4ClassRunner::class)
class RepositoryTest {

    @MockK
    lateinit var restaurantAPI: RestaurantAPI
    private lateinit var restaurantRepository: IRestaurantRepository

    private val testDispatcher = StandardTestDispatcher()
    private val moshi: Moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val errorJsonAdapter: JsonAdapter<GenericTypeError> = moshi
        .adapter(GenericTypeError::class.java).lenient()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        restaurantRepository = RestaurantRepository(
            restaurantAPI = restaurantAPI,
            errorParserUtil = ErrorParserUtil(),
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Result should be Correct`() = runTest(testDispatcher) {
        val expectedResult = GenericType(
            status = true,
            message = "Food has been fetched successfully",
            data = RestaurantListDTO(
                restaurants = listOf(
                    RestaurantDTO(
                        id = 1,
                        title = "Tom's Kitchen",
                        image = "https://img.freepik.com/free-photo/top-view-table-full-delicious-food-composition_23-2149141352.jpg?w=1380&t=st=1685077948~exp=1685078548~hmac=dc6e59db72bc9f6dd6c9658f7e049882c1057ba1c3d1fde2f1311dec21681706"
                    ),
                    RestaurantDTO(
                        id = 2,
                        title = "Bangkok Taste Thai Cuisine",
                        image = "https://img.freepik.com/free-photo/top-view-table-full-delicious-food_23-2149141313.jpg?t=st=1685055169~exp=1685055769~hmac=f0546d7234bba701fee3392f7c16d57c322806cfc881e19100ddeb51977ca365"
                    ),
                    RestaurantDTO(
                        id = 3,
                        title = "Hokkaido’s House",
                        image = "https://img.freepik.com/premium-photo/traditional-spanish-breakfast-tostada-with-different-toppings-dark-background_79782-3251.jpg?w=1380"
                    )
                )
            )
        )
        coEvery { restaurantAPI.getRestaurantsListByCategory("") } answers { expectedResult }
        val actualResult = restaurantRepository.getRestaurantsListByCategory("").toList()
        coVerify(exactly = 1) { restaurantAPI.getRestaurantsListByCategory("") }
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
    fun `HTTP error message should be correct`() = runTest(testDispatcher) {
        val expectedResult = GenericTypeError(
            status = true,
            message = "No restaurant is available for this category",
            error = GenericTypeError.ErrorAPI(
                message = "This category is under maintenance, please try in another time."
            )
        )
        val json: String = errorJsonAdapter.toJson(expectedResult)
        coEvery { restaurantAPI.getRestaurantsListByCategory("") } throws HttpException(
            Response.error<ResponseBody>(
                400,
                json.toResponseBody()
            )
        )
        val actualResult = restaurantRepository.getRestaurantsListByCategory("").toList()
        coVerify(exactly = 1) { restaurantAPI.getRestaurantsListByCategory("") }
        assertTrue(
            "First result should be Loading",
            actualResult.first() is Resource.Loading
        )
        assertTrue(
            "Second result should be Success",
            actualResult.last() is Resource.Error
        )
        assertEquals(
            "Error message should match expected error message",
            expectedResult.message,
            (actualResult.last() as Resource.Error).message
        )
    }

    @Test
    fun `Connection error message should be correct`() = runTest(testDispatcher) {
        val expectedMessage = "Please check your connection and try again."
        coEvery { restaurantAPI.getRestaurantsListByCategory("") } throws UnknownHostException(expectedMessage)
        val actualResult = restaurantRepository.getRestaurantsListByCategory("").toList()
        coVerify(exactly = 1) { restaurantAPI.getRestaurantsListByCategory("") }
        assertTrue(
            "First result should be Loading",
            actualResult.first() is Resource.Loading
        )
        assertTrue(
            "Second result should be Success",
            actualResult.last() is Resource.Error
        )
        assertEquals(
            "Error message should match expected error message",
            expectedMessage,
            (actualResult.last() as Resource.Error).message
        )
    }
}