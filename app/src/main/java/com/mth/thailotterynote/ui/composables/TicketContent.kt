package com.mth.thailotterynote.ui.composables

import android.graphics.Bitmap
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.mth.thailotterynote.R
import com.mth.thailotterynote.model.User
import kotlinx.coroutines.delay

@Composable
fun TicketContent(user: User, onCaptured: (Bitmap) -> Unit) {
    val context = LocalContext.current
    val view = remember { FrameLayout(context) }
    val composeView = remember { ComposeView(context) }

    LaunchedEffect(Unit) {
        delay(500)
        val bitmap = captureViewToBitmap(composeView)
        onCaptured(bitmap)
    }
    AndroidView(
        factory = {
            view.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                addView(composeView.apply {
                    setContent {
                        TicketContentInternal(user)
                    }
                })
            }
        }
    )
}

@Composable
fun TicketContentInternal(user: User) {
    val qrCode = generateQrCode(user.ticketNumber, user.name, 100, 100)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .background(Color.White)

    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            // User Name Box
            Box(
                modifier = Modifier
                    .background(Color(0xFFD9D9D9))
                    .padding(8.dp)
                    .weight(1f)
                    .height(25.dp)
            ) {
                Text(
                    text = user.name.uppercase(),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5A5757),
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Ticket Number Box
            Box(
                modifier = Modifier
                    .background(Color(0xFFFFC107))
                    .padding(8.dp)
                    .height(25.dp)
            ) {
                val letterSpacing = with(LocalDensity.current) {
                    14.toSp()
                }
                Text(
                    text = user.ticketNumber,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = Color.Black,
                    letterSpacing = letterSpacing
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Placeholder Box
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .weight(0.2f)
                    .background(Color(0xFF81D4FA))
            )
        }

        // QR Code and Lottery Image
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.padding(top = 50.dp, start = 30.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.clover),
                        contentDescription = "Casino",
                        modifier = Modifier.size(60.dp)
                    )
                    Text(
                        text = "Thai Lottery E-Ticket",
                        fontWeight = FontWeight.Normal,
                        fontSize = 8.sp,
                        color = Color(0xFF5A5757),
                    )
                }
            }

            Box(modifier = Modifier.padding(top = 50.dp, start = 70.dp)) {
                Image(
                    painter = BitmapPainter(qrCode.asImageBitmap()),
                    contentDescription = "QR",
                    modifier = Modifier.size(80.dp)
                )
            }
        }

        // Lottery Date
        Text(
            text = user.lotteryDate,
            fontSize = 8.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.End,
            color = Color(0xFF5A5757),
            modifier = Modifier
                .width(280.dp)
                .align(Alignment.BottomStart)
                .background(Color(0xFFEDE7F6))
                .padding(start = 10.dp, end = 20.dp)

        )
    }
}

fun captureViewToBitmap(view: View): Bitmap {
    val width = view.width
    val height = view.height
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TicketContentPreview() {
    val user = User(
        615609,
        "MG MYA THAN HTET",
        "25/12/2025",
        "123456",
        listOf("MMK", "THB"),
        true,
        "25/12/2025",
        0
    )
    MaterialTheme {
        TicketContent(user = user, onCaptured = {})
    }
}