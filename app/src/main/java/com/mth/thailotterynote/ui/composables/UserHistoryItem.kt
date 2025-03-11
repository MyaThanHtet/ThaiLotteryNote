package com.mth.thailotterynote.ui.composables


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mth.thailotterynote.model.User

@Composable
fun UserHistoryItem(
    user: User,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                val letterSpacing = with(LocalDensity.current) {
                    20.toSp()
                }
                Text(
                    text = user.name.uppercase(),
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    text = user.ticketNumber, fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = letterSpacing
                )

            }

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.End
            ) {

                Text(text = user.date, fontSize = 12.sp)
                Text(
                    text = if (user.currency.contains("MMK")) {
                        "${user.price} MMK"
                    } else if (user.currency.contains("THB")) {
                        "${user.price} THB"
                    } else {
                        "${user.price}"
                    },
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
            }

        }
    }
}

