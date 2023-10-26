/*This program runs the decompressing program that uses a string array to create a dictionary. The program is able to get an *input file name from the user, decompress the file, and write a binary compressed *file and log file.
 *@author Shayna Bello
 *@author Kimil Thomas
 *since 05-11-2023
 */
import java.util.*;
import java.io.*;


public class Decompress {
    public static int fileSize;

    public static int dictSizeDouble;
    public static void main(String[] args) {
        //variables
        String inputFile = args[0];
        String input = "";
        String outputString = "";
        String outputFile = inputFile.replaceAll("\\.zzz$","");
        String logFile = outputFile + ".log";
        long startTime, endTime = 0;

        //get input file from command line or user input
        Scanner scan = new Scanner(System.in);
        inputFile = args[0];

        boolean decompressTrue = true;

        try{
            // starts the decompression loop, user can decompress multiple files
            while(decompressTrue){
                File file = new File(inputFile);
                while (!file.exists()) {
                    System.out.println("Please enter a valid input file name: ");
                    inputFile = scan.nextLine();
                    file = new File(inputFile);
                }

                //initialize dictionary
                String[] dictionary = new String[99];
                int dictSize = dictionary.length;
                int dictCount = 0;
                int origDictSize = dictSize;
                for (int i = 32; i <= 127; i++) {
                    dictionary[i -32] = String.valueOf((char)i);
                }
                dictionary[96] = "\n";
                dictionary[97] = "\r";
                dictionary[98] = "\t";

                //read input file
                input = readFile(inputFile);

                // convert inputString to input array
                String[] inputArray = new String[input.length()];
                for (int i = 0; i < input.length(); i++) {
                    inputArray[i] = String.valueOf(input.charAt(i));
                }

                //decompress file and get decompress time
                startTime = System.nanoTime();
                outputString = decompressFile(input, outputFile, dictionary);
                endTime = System.nanoTime();
                double decompressionTime = (endTime - startTime) / 100000000;

                //write compressed file and log file
                writeFile(outputString, outputFile);
                writeLogFile(logFile, inputFile, outputFile,decompressionTime);

                // Statment for file print
                System.out.println("\nCompressed data was saved in file: "+ outputFile);
                System.out.println("Log data saved in file: " + logFile);

                //Loop to continue Decompress file
                System.out.print("Would you like to decompress another file? (y/n): ");
                String answer = scan.nextLine();
                if (answer.equals("n")) {
                    System.out.println("Goodbye!");
                    break;
                }
                else if (answer.equals("y")) {
                    System.out.print("Please enter a filename: ");
                    inputFile = scan.nextLine();
                    continue;
                }
                else {
                    System.out.println("Invalid input. Goodbye!");
                    break;
                }
            }//end while
        } catch(Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
        scan.close();
    }//end main

    //read file method
    static String readFile(String inputFile) {
        String input = "";
        try {
            //String output = "";
            File inFile = new File(inputFile); // input file
            fileSize = inputFile.length();
            InputStream inputStream = new FileInputStream(inFile);
            StringBuilder inputBuilder = new StringBuilder();

            int c;
            while ((c = inputStream.read()) != -1) {
                char character = (char) c;
                inputBuilder.append(character);
            }

            input = inputBuilder.toString(); // input string
            inputStream.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return input;
    }//end readFile

    //write decompress file method
    static String decompressFile(String input, String outputFile, String[] dictionary) {
        // Method Variables
        StringBuilder output = new StringBuilder();
        long max32Bit = 4294967295L;

        //convert the input string to a list of integers
        List<Integer> compressedData = new ArrayList<>();
        for (int i = 0; i < input.length(); i += 32) {
            if(i+32 <= input.length()){
                compressedData.add(Integer.parseInt(input.substring(i,i+32),2));
            } else {
                compressedData.add(Integer.parseInt(input.substring(i),2));
            }
        }

        // initialize variables for the previous code and the output string
        int previousCode = compressedData.get(0);
        String previousText = dictionary[previousCode];
        output.append(previousText);

        //iterate over the remaining codes
        for (int i = 1; i < compressedData.size(); i++) {
            int currentCode = compressedData.get(i);
            String currentText;

            // check if the current code is in the dictionary
            if (currentCode < dictionary.length && dictionary[currentCode] != null) {
                currentText = dictionary[currentCode];

            } else {
                // special case for when the current code is not in the dictionary
                currentText = previousText + previousText.charAt(0);
                dictSizeDouble++;

            }
            // output the current text and update the dictionary
            output.append(currentText);

            if (dictionary.length < max32Bit) {
                dictionary =  Arrays.copyOf(dictionary, dictionary.length+1);
            }
            dictionary[dictionary.length-1] = previousText + currentText.charAt(0);
            previousText =  currentText;

        }
        // return the decompressed string
        return output.toString();
    }//end of Decompress Method


    //writeFile method
    static void writeFile(String outputString, String outputFile) {
        try {
            PrintWriter writer = new PrintWriter (new FileWriter(outputFile));
            writer.write(outputString);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }//end writeFile


    //write log file method for this writeLogFile(logFile, inputFile, outputFile, dictionary, compressionTime);
    static void writeLogFile(String logFile, String inputFile, String outputFile, double decompressionTime) {
        try {
            PrintWriter logWriter = new PrintWriter (new FileWriter(logFile));
            File inputF= new File(inputFile);
            long origFileSize = inputF.length(); //gets original file size in bytes
            File outputF = new File(outputFile); //gets compressed file size in bytes
            long compressedFileSize = outputF.length();

            logWriter.println("Decompression of " + inputFile);
            logWriter.println("Decompression took " + decompressionTime + " seconds");
            logWriter.println("The table was doubled " + dictSizeDouble+ " times"); //divide final dict size by initial dict sizei
            logWriter.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}//end class
