package com.lelestacia.thelorrytest.ui.screen.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lelestacia.thelorrytest.R
import com.lelestacia.thelorrytest.ui.theme.Gotham

@Composable
fun RestaurantDescription(description: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 36.dp
        )
    ) {
        Text(
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        )
        Text(
            text = description,
            textAlign = TextAlign.Justify,
            fontFamily = Gotham,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp
            )
        )
    }
}