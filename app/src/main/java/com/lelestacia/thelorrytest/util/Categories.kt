package com.lelestacia.thelorrytest.util

enum class Categories(
    val displayName: String,
    val queryName: String
) {
    ASIAN(
        displayName = "Asian",
        queryName = "asian"
    ),
    WESTERN(
        displayName = "Western",
        queryName = "western"
    ),
    JAPANESE(
        displayName = "Japanese",
        queryName = "japanese"
    ),
    FAST_FOOD(
        displayName = "Fast Food",
        queryName = "fast_food"
    ),
    KOREAN(
        displayName = "Korean",
        queryName = "korean"
    ),
    THAILAND(
        displayName = "Thailand",
        queryName = "thai"
    )
}

fun getCategoriesAsList(): List<Categories> {
    val categories: MutableList<Categories> = mutableListOf()
    categories.add(Categories.ASIAN)
    categories.add(Categories.JAPANESE)
    categories.add(Categories.WESTERN)
    categories.add(Categories.FAST_FOOD)
    categories.add(Categories.KOREAN)
    categories.add(Categories.THAILAND)
    return categories.toList()
}
