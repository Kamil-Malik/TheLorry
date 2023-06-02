package com.lelestacia.thelorrytest.ui.screen.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lelestacia.thelorrytest.R
import com.lelestacia.thelorrytest.domain.model.RestaurantDetail
import com.lelestacia.thelorrytest.util.launchGmmIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantAddressAndOpenMaps(
    address: RestaurantDetail.RestaurantAddress
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = address.fullName,
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp
                ),
                modifier = Modifier.weight(1f)
            )
            ElevatedCard(
                onClick = {
                    clipboardManager.setText(AnnotatedString(address.fullName))
                },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ContentCopy,
                    contentDescription = stringResource(R.string.copy_address),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        val context = LocalContext.current
        Button(
            onClick = { context.launchGmmIntent(address.lat, address.lng) },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(6.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = stringResource(R.string.navigate_with_google_map),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }
    }
}