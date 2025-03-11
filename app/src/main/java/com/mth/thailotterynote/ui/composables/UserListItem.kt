package com.mth.thailotterynote.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mth.thailotterynote.model.User

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserListItem(
    user: User,
    onDelete: (User) -> Unit,
    onUserItemClick: (User) -> Unit,
    onEdit: (User) -> Unit
) {

    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        DeleteConfirmationDialog(
            onConfirm = { onDelete(user); showDialog.value = false },
            onDismiss = { showDialog.value = false }
        )
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                onClick = { onUserItemClick(user) },
                onLongClick = { showDialog.value = true }
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),

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

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.End
            ) {

                Text(text = user.date, fontSize = 12.sp)

                Text(
                    text = if (user.isPaid) "Paid" else "Unpaid",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .combinedClickable(onClick = {
                            onEdit(user)
                        })

                )
            }

        }
    }
}

@Composable
fun DeleteConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Deletion") },
        text = { Text("Are you sure you want to delete this item?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
