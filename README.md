# OOPP Project: Splitty

## Project Overview
This repository contains the Splitty application for the OOP Project. It is a JavaFX and Spring Boot application designed to help users split bills with their friends.

## Features
- All basic requirements for the application have been implemented.
- Special Admin Feature: Accessible by adding a `--admin` command line argument to the client.
- Custom Splitty Server: Accessible by editing the server url in the config.properties file.
- Language Switch: Accessible by changing LANGUAGE to either en or nl in the config.properties file.

## Installation Requirements
- Java 21
- Gradle for dependency management and building the project
- JavaFX SDK (ensure you have JavaFX SDK 21.0.2 or later)

## Setup Instructions
1. Clone this repository to your local machine.
2. Ensure Java 21 is installed and properly set up on your system.
3. Download the JavaFX SDK 21.0.2 (or later) from the official website and note its installation path.
4. Import the project into your preferred IDE that supports Gradle projects.

## Running the Application
1. **Run the Spring Boot Server:**
    - Navigate to the `app.server.Main` class and run it as a Java application.
    - Note the admin password displayed on the first line of the server stdout, if you wish to use the admin features.

2. **Run the JavaFX Client:**
    - Navigate to the `app.client.Main` class.
    - Configure the VM arguments to include the module path to your JavaFX SDK lib folder and add the required modules. Replace the path with your actual JavaFX SDK location:
      ```
      --module-path="C:\Path\To\Your\javafx-sdk-21.0.2\lib" --add-modules=javafx.controls,javafx.fxml
      ```
    - To access the admin features, add the `--admin` command line argument when running the client.

## Usage Guide
- You will initially be greeted with the start screen where you can create a new event or join an existing event using its invite code.
- On the overview page of the event is where you can perform activites like add/edit/delete participant add/edit/delete expense.
- The outstanding debt for each participant is also displayed on the overview page.
