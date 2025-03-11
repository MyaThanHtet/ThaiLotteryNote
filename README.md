# ThaiLottery

This is a simple Android application built with Kotlin and Jetpack Compose to help users track and manage their Thai Lottery tickets.

## Features
* **User Management:**
    * Add, edit, and delete user ticket entries.
    * Store user details like name, ticket number, price, and payment status.
* **Lottery Result Checking:**
    * Fetch the latest Thai Lottery results from an external API: [API Endpoint](https://lotto.api.rayriffy.com/latest).
    * Automatically check user tickets against the winning numbers.
    * Display winning ticket information, including prize details.
    * Support for main prizes and running number prizes.
* **Dashboard History:**
    * Track lottery sales data.
    * Display total sold tickets, paid/unpaid counts, and total prices for each lottery date.
* **User-Friendly Interface:**
    * Built with Jetpack Compose for a modern and responsive UI.
    * Displays ETicket information.
* **Offline Data Storage:**
    * Uses Room persistence library for local data storage.

      
## Technologies Used

* **Kotlin:** Programming language.
* **Jetpack Compose:** Modern UI toolkit.
* **Room Persistence Library:** Local database.
* **Hilt:** Dependency injection.
* **Retrofit:** Network communication.
* **Coroutines and Flow:** Asynchronous programming.
* **Lottie:** Animations.

## Architecture

The app follows a clean architecture pattern with the following layers:

* **UI Layer (Jetpack Compose):** Handles the user interface and interacts with the ViewModel.
* **ViewModel Layer:** Manages the UI state and interacts with the Repository.
* **Repository Layer:** Acts as an abstraction layer for data access, interacting with the local database and network.
* **Data Layer (Room, Retrofit):** Handles data persistence and network communication.


## API Integration

The app uses an external API to fetch Thai Lottery results. You'll need to configure the API endpoint in the `ApiService` interface.

## Database

The app uses Room to store user ticket entries and dashboard history. The database schema includes tables for `User` and `DashboardHistory`.


## Getting Started

To get started with the project, follow these steps:

1. Clone the repository: `git clone <https://github.com/MyaThanHtet/ThaiLotteryNote.git>`
2. Open the project in Android Studio.
3. Build and run the application on an emulator or physical device.

## Contributing

Contributions are welcome! If you find any bugs or have suggestions for improvements, please open an issue or submit a pull request.


## Future Improvements

* Add notifications for lottery results.
* Implement data backup and restore functionality.
* Enhance UI/UX based on user feedback.
* Add more detailed statistics to the dashboard.
* Add filtering and sorting options for the user list.
* Implement more robust error handling.
* Implement testing.

