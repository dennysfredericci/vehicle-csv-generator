package com.devoctans;


import net.datafaker.Faker;
import net.datafaker.transformations.Schema;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.UUID;

import static net.datafaker.transformations.Field.field;

public class Application {
    public static void main(String[] args) throws Exception {
        
        CommandLine cmd = parseOptions(args);
        
        String fileName = cmd.getOptionValue("file");
        Integer totalOfRows = Integer.parseInt(cmd.getOptionValue("number"));
        
        generateCsvFile(fileName, totalOfRows);
        
    }
    
    private static void generateCsvFile(String fileName, Integer totalOfRows) throws IOException {
        Faker faker = new Faker();
        
        CsvFileTransformer<Object> transformer = CsvFileTransformer.builder()
                .fileName(fileName)
                .header(false)
                .separator(";")
                .build();
        
        System.out.println("Generating CSV file: " + fileName);
        
        transformer.generate(getSchema(faker), totalOfRows, number -> {
            
            int percentage = 100 * number / totalOfRows;
            
            int progress = 50 * number / totalOfRows; // Progress bar length of 50 characters
            
            StringBuilder progressBar = new StringBuilder("[");
            for (int i = 0; i < 50; i++) {
                if (i < progress) {
                    progressBar.append("=");
                } else {
                    progressBar.append(" ");
                }
            }
            progressBar.append("] ").append(percentage).append("%");
            
            System.out.print("\r" + progressBar.toString());
            
            
        });
        
        System.out.println();
        System.out.println("CSV file " + fileName + " generated successfully !!!!");
        
    }
    
    private static Schema<Object, String> getSchema(Faker faker) {
        return Schema.of(
                field("uuid", () -> UUID.randomUUID().toString()),
                field("vehicleMaker", () -> faker.vehicle().make()),
                field("vehicleModel", () -> faker.vehicle().model()),
                field("vehicleColor", () -> faker.color().name()),
                field("vehicleVin", () -> faker.vehicle().vin()),
                field("vehiclePlate", () -> faker.vehicle().licensePlate()),
                field("vehicleType", () -> faker.vehicle().carType()),
                field("vehicleDoors", () -> faker.vehicle().doors()),
                field("vehicleDriveType", () -> faker.vehicle().driveType()),
                field("vehicleFuelType", () -> faker.vehicle().fuelType()),
                field("vehicleEngine", () -> faker.vehicle().engine()),
                field("vehicleTransmission", () -> faker.vehicle().transmission()),
                field("country", () -> faker.country().name()),
                field("driverFirstname", () -> faker.name().firstName()),
                field("driverLastname", () -> faker.name().lastName()),
                field("driverPhoneNumber", () -> faker.phoneNumber().phoneNumber()),
                field("driverAge", () -> String.valueOf(faker.number().numberBetween(18, 80))),
                field("driverHomeAddress", () -> faker.address().fullAddress())
        );
    }
    
    private static CommandLine parseOptions(String[] args) throws ParseException {
        // Define the options
        Options options = new Options();
        options.addOption(new Option("f", "file", true, "File to save the generated CSV"));
        options.addOption(new Option("n", "number", true, "Number of records to generate"));
        
        // Create the parser
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        
        if (!cmd.hasOption("f") || !cmd.hasOption("n")) {
            printHelp(options);
            System.exit(1);
        }
        
        return cmd;
    }
    
    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("csv-generator", options);
    }
}
