/*

    Featured in this util.FileIO
    ____________________________

    saveData
    readData (ArrayList)
    readData (Array)
    CSVFixer
    clearData
    createBackupAndClearFile (Used for clearData)
    deleteAfter30Days

    Last updated: 12-04-2025
    Updated by: Jonas

*/

package util;

import java.nio.file.Files;
import java.nio.file.Path; // Read meta data from file
import java.nio.file.Paths; // Read meta data from file
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class FileIO { // Custom generic util.FileIO

    // Attributes
    private static final String red = "\u001B[31m";
    private static final String reset = "\u001B[0m";

    // ________________________________________________________

    public void saveData(ArrayList <String> list, String path, String header){

        try {

            FileWriter writer = new FileWriter(path);

            writer.write(header+"\n");

            for(int i = 0; i < list.size(); i++){
                writer.write(list.get(i));

                // Checks for last character basically. Fixes our empty line creation issue.
                if(i < list.size() - 1){
                    writer.write("\n");
                }
            }

            writer.close(); // Closes our writer

        } catch (IOException e) {

            System.out.println("There was a problem.. Problem found: " + e.getMessage());

        } // Try-catch end

    }

    // ________________________________________________________

    public ArrayList <String> readData(String path){

        ArrayList <String> data = new ArrayList<>();

        File file = new File(path);

        try {

            Scanner scan = new Scanner(file);
            scan.nextLine();

            while(scan.hasNextLine()){
                String line = scan.nextLine();
                data.add(line);
            } // While end

        } catch (FileNotFoundException e) {

            System.out.println("Error - File not found...");

        } // Try-catch end

        return data; // Return ArrayList of type String

    }

    // ________________________________________________________

    // Overloading by NOT having same parameters as the original. This allows multiple with same name.

    public String[] readData(String path, int length) {

        String[] data = new String[length];
        File file = new File(path);

        try{
            //new scaner created
            Scanner scan = new Scanner(file);
            scan.nextLine(); //skip header;

            int i = 0;  // Counter

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                data[i]=line;
                i++;
            }

        } catch (FileNotFoundException e){
            System.out.println("File not found: "+ e.getMessage());
        }

        return data;

    }

    // ________________________________________________________

    /*

    How to use:
    ___________

    Overwrite existing file:
    fixCSV("data/seriesData.csv", "data/seriesData.csv");

    Read BUT output as new file:
    fixCSV("data/seriesData.csv", "data/seriesData_fixCSV.csv");

    What it does:
    _____________

    Reads the header LENGTH and checks if each line has the same amount. If the amount is UNDER then
    it will add a ",0" so each line has the exact same length as the header.
    This will allow us to have a pretty setup on Github.

    */

    public void fixCSV(String inputFilePath, String outputFilePath) {

        ArrayList<String> fixedLines = new ArrayList<>();

        try {

            BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
            String header = br.readLine();

            // Checks .csv file for a header
            if (header == null) {

                System.out.println("Empty CSV file.");
                return;

            }

            fixedLines.add(header);

            int headerCount = header.split(",", -1).length;
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1);
                int diff = headerCount - values.length;

                if (diff > 0) {

                    // Add "0" to the end of the array
                    StringBuilder fixedLine = new StringBuilder(line);

                    for (int i = 0; i < diff; i++) {
                        fixedLine.append(",0");
                    }

                    fixedLines.add(fixedLine.toString());

                } else {

                    fixedLines.add(line);

                } // If-else end

            } // While-end

        } catch (IOException e) {

            System.out.println("Error. Please fix and recompile.");

        }

        // Write the fixed lines to outputFilePath
        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath));

            for (String fixedLine : fixedLines) {
                bw.write(fixedLine);
                bw.newLine();
            }

            System.out.println("\nSuccess..\nFixed CSV written to: " + outputFilePath);

        } catch (IOException e) {

            System.out.println("Error. Please fix and recompile.");

        }

    }

    // ________________________________________________________

    /*

    How to use:
    ___________

    util.FileIO io = new util.FileIO();
    io.clearData("data/userData.csv", "data/backup", "_backup", ".csv");

    What it does:
    _____________

    Takes a .csv file. Reads that file and saves it as a _backup file.
    The original .csv file then gets all data EXCEPT the header removed.
    This will allow any .csv file to be totally reset.

    Expected output:
    ________________

    userData.csv (All data removed except the header)
    userData_backup.csv (All original data saved in this file)

    Rainy day:
    __________

    Can make a code that removes the _backup file after x-amount of days. This will ensure
    that all data is removed at some point and not stored on any servers or databases.

    Important:
    __________

    dirPath = "data/backup/
    fileEnding = "_backup"
    fileFormat = ".csv"

    */

    public void clearData(String path, String dirPath, String fileEnding, String fileFormat) {

        Scanner scanner = new Scanner(System.in);

        // Prompt the user twice to make sure they REALLY want to remove the .csv data.
        System.out.println("Please confirm twice that you want to remove all data from this .csv file:");
        String response1 = scanner.nextLine().trim();

        if (!response1.equalsIgnoreCase("yes")) {
            System.out.println("You have typed no. Prompt ending...");
            return; // Exit prompt
        }

        System.out.println(red + "Are you really sure you want to delete all data?: " + reset);
        String response2 = scanner.nextLine().trim();

        if (!response2.equalsIgnoreCase("yes")) {
            System.out.println("You have typed no. Prompt ending...");
            return; // Exit prompt
        }

        // If prompts have been passed, go on and remove the data and make a _backup .csv file
        File originalFile = new File(path);

        if (!originalFile.exists()) {
            System.out.println("Error. File not found. Check path and try again...");
            return; // Exit prompt
        }

        String header = null;
        StringBuilder fileContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile))) {

            String line;
            int lineNumber = 0;

            // Read the header (first line)
            if ((header = reader.readLine()) != null) {
                System.out.println("Header: '" + header + "'");
            }

            // Output in console
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                fileContent.append(line).append("\n");
                System.out.println("Line " + (lineNumber + 1) + ": '" + line + "'");
            }

        } catch (IOException e) {

            System.out.println("Error reading the file.");

        }

        // Backup and clearing data
        if (header != null && !header.trim().isEmpty()) {

            createBackupAndClearFile(path, header, fileContent.toString(), dirPath, fileEnding, fileFormat);

        } else {

            System.out.println("Original CSV file is empty or header is invalid.");

        } // If-else end

    }

    // ________________________________________________________

    /*

    Important:
    __________

    dirPath = "data/backup/
    fileEnding = "_backup"
    fileFormat = ".csv"

    ** DO NOT REMOVE**

         Used for clearData()

    ** DO NOT REMOVE**

    */

    private void createBackupAndClearFile(String path, String header, String fileContent, String dirPath, String fileEnding, String fileFormat) {

        File originalFile = new File(path);
        String originalFileName = new File(path).getName().replace(fileFormat, "");
        String backupDir = dirPath;
        String backupBaseName = backupDir + originalFileName + fileEnding + "_";
        String backupFilePath = backupBaseName + "1" + fileFormat;

        // Ensure unique filename by appending a number if needed
        int count = 1;
        while (new File(backupFilePath).exists()) {
            count++;
            backupFilePath = backupBaseName + count + fileFormat;
        }

        try {
            // Ensure backup directory exists
            File backupDirectory = new File(backupDir);
            if (!backupDirectory.exists()) {
                backupDirectory.mkdirs();
            }

            try (BufferedWriter backupWriter = new BufferedWriter(new FileWriter(backupFilePath));
                 BufferedWriter originalWriter = new BufferedWriter(new FileWriter(originalFile, false))) {

                backupWriter.write(header);
                backupWriter.newLine();
                backupWriter.write(fileContent);

                originalWriter.write(header);
                originalWriter.newLine();

                System.out.println("Backup created: " + backupFilePath);
                System.out.println("Original file cleared, only header retained.");
            }

        } catch (IOException e) {
            System.out.println("Error while processing the file.");
        }
    }

    // ________________________________________________________

    /*

    How to use:
    ___________

    util.FileIO io = new util.FileIO();
    io.deleteAfter30Days("data/backup/", "_backup.csv")

    Expected output:
    ________________

    Files >= 30 days == REMOVED
    Files < 30 days == NOT REMOVED BUT DISPLAYED
    ERROR == Display error and why it's failing

    Cost / Benefit:
    _______________

    Should only be prompted like once per 7 days.

    Rainy day:
    __________

    Make the method run automaticly after 7 days.

    */

    public void deleteAfter30Days(String folderPath, String endsWith){

        // File object with our directory (NOT OUR .csv FILE)
        File file = new File(folderPath);

        // Checks if our path exist and is a directory first
        if(file.exists() && file.isDirectory()){

            // Array to hold our files with _backup.csv ending
            File[] files = file.listFiles((dir, name) -> name.endsWith(endsWith));

            if(files != null && files.length > 0){

                // For-each to loop through files
                for(File f : files){

                    // Try catch to catch exceptions
                    try{

                        Path p = Paths.get(f.getAbsolutePath());
                        BasicFileAttributes attributes = Files.readAttributes(p, BasicFileAttributes.class);
                        long creationTimeMilliseconds = attributes.creationTime().toMillis();
                        long currentTimeMilliseconds = System.currentTimeMillis();

                        // Convert our time from milliseconds to days
                        long ageInMilliseconds = currentTimeMilliseconds - creationTimeMilliseconds;
                        long ageInDays = TimeUnit.MILLISECONDS.toDays(ageInMilliseconds);

                        if(ageInDays >= 30){

                            boolean deleted = f.delete();

                            if(deleted){

                                System.out.println("File deleted: " + f.getName());

                            } else {

                                System.out.println("Failed to removed file: " + f.getName());

                            } // If-end (4)

                        } else {

                            System.out.println("Not yet 30 days: " + f.getName() + " | Age: " + ageInDays + " days old.");

                        } // If-end (3)

                    } catch (Exception e) {

                        System.out.println("Error. Please try again.");

                    } // Try-catch end

                } // For-each end

            } else {

                System.out.println("No " + endsWith + " files found in this directory..");

            } // If-end (2)

        } else {

            System.out.println("Invalid directory path.. Please try again.");

        } // If-end (MAIN)

    } // Class end

} // util.FileIO end
