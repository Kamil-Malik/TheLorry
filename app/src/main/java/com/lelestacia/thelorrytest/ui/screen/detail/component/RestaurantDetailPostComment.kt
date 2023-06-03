package com.lelestacia.thelorrytest.ui.screen.detail.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lelestacia.thelorrytest.R
import com.lelestacia.thelorrytest.ui.theme.Gotham
import com.lelestacia.thelorrytest.ui.theme.TheLorryTestTheme
import com.lelestacia.thelorrytest.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailPostComment(
    userComment: String,
    onUserCommentChanged: (String) -> Unit,
    onSendUserComment: () -> Unit,
    sendCommentStatus: Resource<String>
) {
    val shouldBeEnabled = sendCommentStatus != Resource.Loading
    Box(
        modifier = Modifier.background(
            MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                vertical = 12.dp,
                horizontal = 16.dp
            )
        ) {
            BasicTextField(
                value = userComment,
                enabled = shouldBeEnabled,
                onValueChange = {
                    if (it.length > 80) return@BasicTextField
                    else onUserCommentChanged(it)
                },
                decorationBox = {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            )
                    ) {
                        Text(
                            text = userComment.ifEmpty {
                                stringResource(id = R.string.write_a_comment)
                            },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = Gotham,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                        )
                    }
                },
                modifier = Modifier
                    .heightIn(min = 34.dp)
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.25F
                        ),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .background(MaterialTheme.colorScheme.surface)
                    .animateContentSize(),

                )
            ElevatedCard(
                onClick = {
                    if (shouldBeEnabled) {
                        onSendUserComment.invoke()
                    }
                },
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.size(34.dp)
            ) {
                if (shouldBeEnabled) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        modifier = Modifier.padding(8.dp)
                    )

                } else {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewRestaurantDetailPostComment() {
    TheLorryTestTheme {
        Surface {
            var userComment by remember {
                mutableStateOf("")
            }
            RestaurantDetailPostComment(
                userComment = userComment,
                onUserCommentChanged = {
                    userComment = it
                },
                onSendUserComment = {},
                sendCommentStatus = Resource.None
            )
        }
    }
}