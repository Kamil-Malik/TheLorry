package com.lelestacia.thelorrytest.domain.model

data class RestaurantDetail(
    val title: String,
    val images: List<ImageUrl>,
    val rating: Int,
    val address: RestaurantAddress,
    val description: String
) {
    data class ImageUrl(
        val url: String
    )

    data class RestaurantAddress(
        val fullName: String,
        val lat: String,
        val lng: String
    )
}

val TomKitchen = RestaurantDetail(
    title = "Tom's Kitchen",
    images = listOf(
        RestaurantDetail.ImageUrl(
            url = "https://img.freepik.com/free-photo/top-view-table-full-delicious-food-composition_23-2149141352.jpg?w=1380&t=st=1685077948~exp=1685078548~hmac=dc6e59db72bc9f6dd6c9658f7e049882c1057ba1c3d1fde2f1311dec21681706"
        ),
        RestaurantDetail.ImageUrl(
            url = "https://img.freepik.com/free-photo/top-view-table-full-delicious-food_23-2149141313.jpg?t=st=1685055169~exp=1685055769~hmac=f0546d7234bba701fee3392f7c16d57c322806cfc881e19100ddeb51977ca365"
        ),
        RestaurantDetail.ImageUrl(
            url = "https://img.freepik.com/premium-photo/traditional-spanish-breakfast-tostada-with-different-toppings-dark-background_79782-3251.jpg?w=1380"
        ),
        RestaurantDetail.ImageUrl(
            url = "https://img.freepik.com/free-photo/fruit-salad-spilling-floor-was-mess-vibrant-colors-textures-generative-ai_8829-2895.jpg?w=826&t=st=1685078122~exp=1685078722~hmac=6db09ebf5256817a8f3e8f2043f9ba9275fe8614bb954eb35070c7ba2267c2c5"
        ),
        RestaurantDetail.ImageUrl(
            url = "https://img.freepik.com/free-psd/delicous-asian-food-social-media-template_505751-2982.jpg?w=1380&t=st=1685080108~exp=1685080708~hmac=b10d601c91849abd3165ddf1fa919da2a5aa399bab5e889eb6ec6b40d08d921a"
        )
    ),
    rating = 4,
    address = RestaurantDetail.RestaurantAddress(
        fullName = "Jl. RC. Veteran Raya No.9, Bintaro, Kec. Pesanggrahan, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta 12330",
        lat = "-6.2830295",
        lng = "106.7940221"
    ),
    description = "The Stick is a delightful food place that specializes in grilled beef steaks. Located in a cozy setting, it offers a mouthwatering selection of perfectly cooked steaks served on a dark wooden surface. The aroma of sizzling meat fills the air, enticing diners with its irresistible appeal. Whether you prefer your steak rare, medium, or well-done, The Stick ensures a culinary experience that satisfies even the most discerning meat lovers. Indulge in their succulent steaks, accompanied by a range of delectable sides and sauces, for a truly memorable dining experience."
)
