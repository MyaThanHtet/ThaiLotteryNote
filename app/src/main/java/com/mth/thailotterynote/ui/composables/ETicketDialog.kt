package com.mth.thailotterynote.ui.composables

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.mth.thailotterynote.model.User
import java.io.OutputStream
import java.util.Hashtable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ETicketDialog(user: User, onDismiss: () -> Unit) {

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    BasicAlertDialog(onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TicketContent(user) { capturedBitmap ->
                    bitmap = capturedBitmap
                }
                Spacer(modifier = Modifier.height(2.dp))
                //save button
                Box(
                    modifier = Modifier
                        .height(28.dp)
                        .fillMaxWidth()
                        .background(Color(0xFF43A047)),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = {
                        if (checkAndRequestStoragePermission(context as Activity)) {
                            bitmap?.let {
                                saveBitmapToGallery(context, it)
                                Toast.makeText(
                                    context,
                                    "Image saved successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onDismiss()
                            }

                        }

                    }) {
                        Text("Save", color = Color(0xFFFFFFFF))
                    }
                }

            }
        }
    )
}

fun saveBitmapToGallery(context: Context, bitmap: Bitmap) {
    val filename = "lottery_ticket_${System.currentTimeMillis()}.png"
    val fos: OutputStream?
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/LotteryTickets")
    }

    val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    fos = imageUri?.let { resolver.openOutputStream(it) }

    fos?.use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }
}

fun checkAndRequestStoragePermission(activity: Activity): Boolean {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        if (ContextCompat.checkSelfPermission(
                activity, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1001
            )
            false
        } else {
            true
        }
    } else {
        true
    }
}

fun generateQrCode(ticketNumber: String, name: String, widthX: Int, heightX: Int): Bitmap {
    val hints = Hashtable<EncodeHintType, Any>()
    hints[EncodeHintType.CHARACTER_SET] = "UTF-8"

    val bitMatrix =
        QRCodeWriter().encode(
            "$name : $ticketNumber",
            BarcodeFormat.QR_CODE,
            widthX,
            heightX,
            hints
        )
    val width = bitMatrix.width
    val height = bitMatrix.height
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    for (x in 0 until width) {
        for (y in 0 until height) {
            bmp.setPixel(x, y, if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
        }
    }
    return bmp
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ETicketPreview() {
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
        ETicketDialog(
            user = user,
            onDismiss = {}
        )
    }
}