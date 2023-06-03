package com.lelestacia.thelorrytest.util

import com.lelestacia.thelorrytest.data.model.CommentDTO
import com.lelestacia.thelorrytest.data.model.CommentsDTO
import com.lelestacia.thelorrytest.data.model.GenericType
import com.lelestacia.thelorrytest.data.model.GenericTypeError
import com.lelestacia.thelorrytest.data.model.PostCommentDTO
import com.lelestacia.thelorrytest.data.model.PostCommentErrorDTO
import com.lelestacia.thelorrytest.data.model.RestaurantDTO
import com.lelestacia.thelorrytest.data.model.RestaurantDetailDTO
import com.lelestacia.thelorrytest.data.model.RestaurantsDTO

val RestaurantDetailResponse = GenericType(
    status = true,
    message = "Food details has been fetched successfully",
    data = RestaurantDetailDTO(
        title = "Tom's Kitchen",
        images = listOf(
            RestaurantDetailDTO.ImageUrlDTO(
                url = "https://img.freepik.com/free-photo/top-view-table-full-delicious-food-composition_23-2149141352.jpg?w=1380&t=st=1685077948~exp=1685078548~hmac=dc6e59db72bc9f6dd6c9658f7e049882c1057ba1c3d1fde2f1311dec21681706"
            ),
            RestaurantDetailDTO.ImageUrlDTO(
                url = "https://img.freepik.com/free-photo/top-view-table-full-delicious-food_23-2149141313.jpg?t=st=1685055169~exp=1685055769~hmac=f0546d7234bba701fee3392f7c16d57c322806cfc881e19100ddeb51977ca365"
            ),
            RestaurantDetailDTO.ImageUrlDTO(
                url = "https://img.freepik.com/premium-photo/traditional-spanish-breakfast-tostada-with-different-toppings-dark-background_79782-3251.jpg?w=1380"
            ),
            RestaurantDetailDTO.ImageUrlDTO(
                url = "https://img.freepik.com/free-photo/fruit-salad-spilling-floor-was-mess-vibrant-colors-textures-generative-ai_8829-2895.jpg?w=826&t=st=1685078122~exp=1685078722~hmac=6db09ebf5256817a8f3e8f2043f9ba9275fe8614bb954eb35070c7ba2267c2c5"
            ),
            RestaurantDetailDTO.ImageUrlDTO(
                url = "https://img.freepik.com/free-psd/delicous-asian-food-social-media-template_505751-2982.jpg?w=1380&t=st=1685080108~exp=1685080708~hmac=b10d601c91849abd3165ddf1fa919da2a5aa399bab5e889eb6ec6b40d08d921a"
            )
        ),
        rating = 4,
        address = RestaurantDetailDTO.RestaurantAddressDTO(
            fullName = "Jl. RC. Veteran Raya No.9, Bintaro, Kec. Pesanggrahan, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12330",
            lat = "-6.2830295",
            lng = "106.7940221"
        ),
        description = "The Stick is a delightful food place that specializes in grilled beef steaks. Located in a cozy setting, it offers a mouthwatering selection of perfectly cooked steaks served on a dark wooden surface. The aroma of sizzling meat fills the air, enticing diners with its irresistible appeal. Whether you prefer your steak rare, medium, or well-done, The Stick ensures a culinary experience that satisfies even the most discerning meat lovers. Indulge in their succulent steaks, accompanied by a range of delectable sides and sauces, for a truly memorable dining experience."
    )
)

val RestaurantNotFoundErrorResponse = GenericTypeError(
    status = true,
    message = "Restaurant not found",
    error = GenericTypeError.ErrorAPI(
        message = "Restaurant not found"
    )
)

val RestaurantsResponse = GenericType(
    status = true,
    message = "Food has been fetched successfully",
    data = RestaurantsDTO(
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

val RestaurantsNotAvailableResponse = GenericTypeError(
    status = true,
    message = "No restaurant is available for this category",
    error = GenericTypeError.ErrorAPI(
        message = "This category is under maintenance, please try in another time."
    )
)

val CommentsResponse = GenericType(
    status = true,
    message = "Comments have been returned successfully",
    data = CommentsDTO(
        comments = listOf(
            CommentDTO(
                id = 5210,
                userName = "Tom John",
                body = "Best food in town! Highly recommended!",
                profilePicture = "https://img.freepik.com/free-vector/businessman-character-avatar-isolated_24877-60111.jpg?w=1380&t=st=1685080702exp=1685081302hmac=25064c0fcde5709896af5d58a8c55d5da65692c5f30292965d9f914bdaa34959"
            ),
            CommentDTO(
                id = 5211,
                userName = "Samantha Smith",
                body = "Delicious flavors and excellent service. A must-visit for food enthusiasts!",
                profilePicture = "https://img.freepik.com/free-photo/happy-businesswoman-using-smartphone-outdoors_171337-189.jpg?size=626&ext=jpg",
            ),
            CommentDTO(
                id = 5212,
                userName = "Alex Thompson",
                body = "I keep coming back for their amazing dishes. The quality and taste are unmatched!",
                profilePicture = "https://img.freepik.com/free-photo/stylish-bearded-man-office-attire_273609-1559.jpg?size=626&ext=jpg",
            )
        )
    )
)


val PostCommentResponse = GenericType(
    status = true,
    message = "Comment has been submitted successfully",
    data = PostCommentDTO(
        id = 1,
        message = "The food is great"
    )
)

val PostCommentErrorResponse = PostCommentErrorDTO(
    errors = listOf(
        PostCommentErrorDTO.PostCommentErrorMessageDTO(
            messages = "Something went wrong"
        )
    )
)

const val Lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."