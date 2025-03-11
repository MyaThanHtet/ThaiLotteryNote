package com.mth.thailotterynote.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mth.thailotterynote.model.User


@Composable
fun EditUserDialog(
    user: User,
    onDismiss: () -> Unit,
    onUserUpdated: (User) -> Unit
) {
    var editedName by remember { mutableStateOf(user.name) }
    var editedTicketNumber by remember { mutableStateOf(user.ticketNumber) }
    var editedIsPaid by remember { mutableStateOf(user.isPaid) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit User") },
        text = {
            Column {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Name") }
                )
                Spacer(modifier = Modifier.height(5.dp))

                OutlinedTextField(
                    value = editedTicketNumber,
                    onValueChange = { editedTicketNumber = it },
                    label = { Text("Ticket Number") }
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Paid")
                    Switch(
                        modifier = Modifier
                            .padding(start = 10.dp),
                        checked = editedIsPaid,
                        onCheckedChange = { editedIsPaid = it }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val updatedUser = user.copy(
                        name = editedName,
                        ticketNumber = editedTicketNumber,
                        isPaid = editedIsPaid
                    )
                    onUserUpdated(updatedUser)
                    onDismiss()
                }
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}