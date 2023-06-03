package com.lelestacia.thelorrytest.ui.screen.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lelestacia.thelorrytest.R
import com.lelestacia.thelorrytest.ui.theme.starColor

@Composable
fun RestaurantRating(rating: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 24.dp
        )
    ) {
        Text(
            text = stringResource(R.string.rating),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
        )
        val ratingValue: Int = rating
        val leftOver = 5 - ratingValue
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            for (i in 0..rating) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = starColor
                )
            }
            if (leftOver != 0) {
                Icon(
                    imageVector = Icons.Filled.StarBorder,
                    contentDescription = null,
                    tint = starColor
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = stringResource(R.string.rating_value, rating.toFloat()),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = starColor
            )
        }
    }
}