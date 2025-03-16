# Vehicle CSV Generator

A Java application that generates CSV files containing random vehicle and driver data using the DataFaker library.

## Features

- Generates CSV files with customizable number of records
- Includes vehicle details such as:
  - Make, model, color
  - VIN number
  - License plate
  - Vehicle type
  - Fuel type
  - Engine details
  - Transmission
- Includes driver information:
  - Name
  - Phone number
  - Age
  - Address
- Progress bar visualization during generation
- Configurable CSV separator
- Command-line interface

## Usage

Run the application with the following command-line arguments:

```bash
java -jar vehicle-csv-generator.jar -f <filename> -n <number_of_records>
```

### Arguments

- `-f, --file`: The output CSV file name
- `-n, --number`: The number of records to generate

### Example

```bash
java -jar vehicle-csv-generator.jar -f vehicles.csv -n 1000
```

## CSV Output Format

The generated CSV file contains the following columns:

- uuid
- vehicleMaker
- vehicleModel
- vehicleColor
- vehicleVin
- vehiclePlate
- vehicleType
- vehicleDoors
- vehicleDriveType
- vehicleFuelType
- vehicleEngine
- vehicleTransmission
- country
- driverFirstname
- driverLastname
- driverPhoneNumber
- driverAge
- driverHomeAddress

## Dependencies

- DataFaker - For generating random data
- Apache Commons CLI - For command-line argument parsing

## Building

This is a Maven project. To build it, run:

```bash
mvn clean package
```
