package com.mth.thailotterynote.ui.composables.widget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PaidUnpaidCircularProgress(
    paidCount: Int,
    totalCount: Int,
    strokeWidth: Dp = 10.dp,
    size: Dp = 90.dp
) {
    val paidPercentage = if (totalCount > 0) paidCount.toFloat() / totalCount.toFloat() else 0f
    val unpaidPercentage = 1f - paidPercentage

    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(top = 16.dp)) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)

            // Unpaid segment
            drawArc(
                color = Color.LightGray,
                startAngle = -90f,
                sweepAngle = 360f * unpaidPercentage,
                useCenter = false,
                style = stroke
            )

            // Paid segment
            drawArc(
                color = Color(0xFF6750A4),
                startAngle = -90f + 360f * unpaidPercentage,
                sweepAngle = 360f * paidPercentage,
                useCenter = false,
                style = stroke
            )
        }

        Text(
            text = "$paidCount/${totalCount-paidCount}",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

@Composable
fun PaidUnpaidLegend() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Canvas(modifier = Modifier.size(10.dp)) {
            drawCircle(color = Color(0xFF6750A4))
        }
        Text(text = "Paid", modifier = Modifier.padding(start = 4.dp, end = 16.dp))

        Canvas(modifier = Modifier.size(10.dp)) {
            drawCircle(color = Color.LightGray)
        }
        Text(text = "Unpaid", modifier = Modifier.padding(start = 4.dp))
    }
}

@Composable
fun PaidUnpaidView(paidCount: Int, totalCount: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        PaidUnpaidCircularProgress(paidCount, totalCount)
        Spacer(modifier = Modifier.height(14.dp))
        PaidUnpaidLegend()
    }
}
