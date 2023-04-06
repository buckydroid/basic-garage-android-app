Garage App is an Android application that allows the user to create a list of vehicles they own or wish to purchase. The app also provides login/signup support for the user. The user can add a new car, select the vehicle manufacturer, and the vehicle model. The app fetches the list of vehicle makes and models from the US Department of Transportation's open API. The user can add an image for the car from the phone gallery or camera and delete the car. The app saves all the data locally using a local database (Room).

Design Choices and Decisions:

Language: The app is developed using Java and XML.
Database: Room is used as a local database for the app. The reason for choosing Room is that it provides a higher level of abstraction and allows for better management of the SQLite database. Room also provides compile-time verification of SQL queries.
Network Calls: Retrofit is used for making network calls to fetch data from the API. The reason for choosing Retrofit is that it provides an easy-to-use interface for making network calls and supports caching.
UI: The app's UI is designed using XML. The app uses RecyclerView for displaying the list of cars. The reason for choosing RecyclerView is that it provides better performance compared to ListView, and it supports a more complex layout.

Functionality:

Dashboard: The dashboard has two sections: Add a new car and List of added cars.
Add a new car: The user can select the vehicle manufacturer and vehicle model using two dropdown views. The app fetches the list of vehicle makes and models from the API. After selecting the car make, the app calls the API to fetch the list of vehicle models for that make and populates the dropdown view for car models. The user can tap the 'Add Car' button to save the car locally and refresh the list of cars to display the newly added car in the list.
List of added cars: Each cell in the list has the car image and labels for make and model. The user can tap the 'Add Car Image' button in each cell to add an image for the car from the phone gallery or camera. The user can tap the 'Delete Car' button in each cell to delete the car. The app refreshes the list view after all update/delete operations on the vehicle list.
User Logout: The user can log out of the app by tapping the 'Logout' button.

APIs Used:

API #1: GET API for fetching all vehicle makes: https://vpic.nhtsa.dot.gov/api/vehicles/getallmakes?format=json
API #2: GET API for fetching all vehicle models by Make ID: https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMakeld/<make_id>?format=json




