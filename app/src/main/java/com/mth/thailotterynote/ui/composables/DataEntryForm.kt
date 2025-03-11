package com.mth.thailotterynote.ui.composables

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mth.thailotterynote.model.User
import com.mth.thailotterynote.viewmodel.DataEntryViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataEntryForm(
    navController: NavHostController,
    viewModel: DataEntryViewModel = hiltViewModel()
) {
    var inputError by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showSnackBar by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var selectedCurrency by remember { mutableStateOf("Select Currency") }
    var ticketCount by remember { mutableIntStateOf(1) }
    val ticketNumbers = remember { mutableStateListOf("") }
    var dayExpanded by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf("Day") }
    var monthExpanded by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf("Month") }
    var yearExpanded by remember { mutableStateOf(false) }
    var selectedYear by remember { mutableStateOf("Year") }
    var userName by remember { mutableStateOf("") }
    var isPaid by remember { mutableStateOf(false) }
    var price by remember { mutableStateOf("") }
    var showProgressBar by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Data Entry Form") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (userName.isBlank() ||
                            ticketNumbers.any { it.isBlank() } ||
                            selectedCurrency == "Select Currency" ||
                            selectedDay == "Day"||
                            selectedMonth == "Month"||
                            selectedYear == "Year"||
                            price.isBlank()
                            ) {
                            inputError = "Please fill in all required fields."
                            coroutineScope.launch {
                                Toast.makeText(context, inputError, Toast.LENGTH_SHORT).show()
                            }
                            return@IconButton
                        }

                        inputError = null

                        val currentDate = LocalDate.now()
                        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
                        val formattedDate = currentDate.format(formatter)

                        coroutineScope.launch {

                            showProgressBar = true
                            try {
                                ticketNumbers.forEach { ticketNumber ->
                                    val user = User(
                                        name = userName,
                                        date = formattedDate,
                                        ticketNumber = ticketNumber,
                                        currency = listOf(selectedCurrency),
                                        isPaid = isPaid,
                                        lotteryDate = "$selectedDay $selectedMonth $selectedYear",
                                        price = price.toInt()
                                    )
                                    viewModel.saveUserData(user)
                                    delay(100)
                                }
                            } finally {
                                showProgressBar = false
                            }

                            showSnackBar = true
                            navController.popBackStack()
                        }


                    }) {
                        Icon(Icons.Filled.Done, contentDescription = "More")
                    }
                },
            )
        }
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {
            if (showProgressBar) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("Enter name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Select Due Date",
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    // Day Selection
                    Box {
                        OutlinedButton(onClick = { dayExpanded = true }) {
                            Text(selectedDay)
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = dayExpanded,
                            onDismissRequest = { dayExpanded = false }
                        ) {
                            for (day in 1..31) {
                                DropdownMenuItem({ Text(day.toString()) }, onClick = {
                                    selectedDay = day.toString()
                                    dayExpanded = false
                                }, modifier = Modifier)
                            }
                        }
                    }

                    // Month Selection
                    Box {
                        OutlinedButton(onClick = { monthExpanded = true }) {
                            Text(selectedMonth)
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = monthExpanded,
                            onDismissRequest = { monthExpanded = false }
                        ) {
                            val months = listOf(
                                "JANUARY",
                                "FEBRUARY",
                                "MARCH",
                                "APRIL",
                                "MAY",
                                "JUNE",
                                "JULY",
                                "AUGUST",
                                "SEPTEMBER",
                                "OCTOBER",
                                "NOVEMBER",
                                "DECEMBER"
                            )
                            months.forEach { month ->
                                DropdownMenuItem({ Text(month) }, onClick = {
                                    selectedMonth = month
                                    monthExpanded = false
                                }, modifier = Modifier)
                            }
                        }
                    }

                    // Year Selection

                    Box {
                        OutlinedButton(onClick = { yearExpanded = true }) {
                            Text(selectedYear)
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = yearExpanded,
                            onDismissRequest = { yearExpanded = false }
                        ) {
                            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                            for (year in currentYear..currentYear + 10) { // Show next 10 years

                                DropdownMenuItem({ Text(year.toString()) }, onClick = {
                                    selectedYear = year.toString()
                                    yearExpanded = false
                                }, modifier = Modifier)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))



                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(selectedCurrency)
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        val currencies = listOf(
                            "MMK",
                            "THB",
                        )
                        currencies.forEach { currency ->
                            DropdownMenuItem({ Text(currency) }, onClick = {
                                selectedCurrency = currency
                                expanded = false
                            }, modifier = Modifier)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = {
                            Text(
                                "Enter Price per Ticket"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )
                }


                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.material3.Checkbox(
                        checked = isPaid,
                        onCheckedChange = { isPaid = it }
                    )
                    Text(text = if (isPaid) "Paid" else "Unpaid")
                }
                Spacer(modifier = Modifier.height(8.dp))

                TicketNumberFields(ticketNumbers)

                Button(onClick = {
                    ticketCount++
                    ticketNumbers.add("")
                }) {
                    Text("Add more tickets +")
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DataEntryFormPreview() {
    val navController = rememberNavController()
    MaterialTheme {
        DataEntryForm(navController)
    }
}

@Composable
fun TicketNumberFields(ticketNumbers: MutableList<String>) {
    val focusRequesters = List(ticketNumbers.size) { FocusRequester() }

    Column {
        for (i in ticketNumbers.indices) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {


                OutlinedTextField(
                    value = ticketNumbers[i],
                    onValueChange = {
                        if (it.length <= 6) {
                            ticketNumbers[i] = it
                        }
                    },
                    label = { Text("Enter Ticket Number ${i + 1}") },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequesters[i])
                        .onFocusEvent { focusState ->
                            if (!focusState.hasFocus && ticketNumbers[i].isEmpty()) {
                                ticketNumbers[i] = ""
                            }
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (i < ticketNumbers.size - 1) ImeAction.Next else ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            if (i < ticketNumbers.size - 1) {
                                focusRequesters[i + 1].requestFocus()
                            }
                        }
                    )
                )

                if (ticketNumbers.size > 1 && i != 0) {
                    IconButton(onClick = {
                        ticketNumbers.removeAt(i)
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove Ticket")
                    }
                }


            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}