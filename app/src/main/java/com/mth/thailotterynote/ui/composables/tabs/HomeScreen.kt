package com.mth.thailotterynote.ui.composables.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mth.thailotterynote.R
import com.mth.thailotterynote.model.User
import com.mth.thailotterynote.ui.composables.ETicketDialog
import com.mth.thailotterynote.ui.composables.EditUserDialog
import com.mth.thailotterynote.ui.composables.UserListItem
import com.mth.thailotterynote.viewmodel.HomeViewModel


@Composable
fun HomeScreen(
    navHostController: NavHostController, viewModel: HomeViewModel = hiltViewModel()
) {
    val userList by viewModel.userList.collectAsState(emptyList())
    //val users: LazyPagingItems<User> = viewModel.users.collectAsLazyPagingItems()

    var showETicketDialog by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var editingUser by remember { mutableStateOf<User?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(modifier = Modifier.padding(bottom = 100.dp), onClick = {
                navHostController.navigate("data_entry_form")
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add User")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
           /* LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 80.dp)
            ) {
                items(users.itemCount) { index ->
                    val user = users[index]
                    if (user != null) {
                        UserListItem(
                            user = user,
                            onDelete = { deletedUser ->
                                viewModel.deleteUser(deletedUser)
                            },
                            onUserItemClick = { clickedUser ->
                                selectedUser = clickedUser
                                showETicketDialog = true
                            },
                            onEdit = { editedUser ->
                                editingUser = editedUser
                                showEditDialog = true
                            }
                        )
                    } else {
                        EmptyDataScreen()
                    }
                }
            }*/

             if (userList.isEmpty()) {
                 EmptyDataScreen()
             } else {
                 LazyColumn(
                     modifier = Modifier
                         .fillMaxWidth()
                         .weight(1f)
                         .padding(bottom = 80.dp)
                 ) {
                     items(userList.reversed()) { userList ->
                         UserListItem(
                             user = userList,
                             onDelete = { deletedUser ->
                                 viewModel.deleteUser(deletedUser)
                             },
                             onUserItemClick = { clickedUser ->
                                 selectedUser = clickedUser
                                 showETicketDialog = true
                             },
                             onEdit = {
                                 editedUser->
                                 editingUser = editedUser
                                 showEditDialog = true
                             }
                         )
                     }

                 }


             }


        }
    }
    if (showETicketDialog && selectedUser != null) {
        ETicketDialog(
            user = selectedUser!!,
            onDismiss = { showETicketDialog = false },
        )
    }

    if (showEditDialog && editingUser != null) {
        EditUserDialog(
            user = editingUser!!,
            onDismiss = {
                showEditDialog = false
                editingUser = null
            },
            onUserUpdated = { updatedUser ->
                viewModel.updateUser(updatedUser)
                editingUser = null
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    /*val sampleUserList = listOf(
        User(
            615608,
            "MG MYA THAN HTET",
            "25/12/2025",
            "123456",
            listOf("MMK", "THB"),
            true,
            "25/12/2025"
        ),
        User(
            615600,
            "MG MYA THAN HTET",
            "25/12/2025",
            "123456",
            listOf("MMK", "THB"),
            true,
            "25/12/2025"
        ),
        User(
            615609,
            "MG MYA THAN HTET",
            "25/12/2025",
            "123456",
            listOf("MMK", "THB"),
            true,
            "25/12/2025"
        )
    )*/


    MaterialTheme {
        HomeScreen(navController)
    }
}

@Composable
fun EmptyDataScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_data)).value,
            /* iterations = LottieConstants.IterateForever,*/
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
        )
    }
}