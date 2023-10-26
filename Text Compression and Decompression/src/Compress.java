/*This program runs the compress program that uses the that uses the *HashtableChain class to create a dictionary. The program is able to get an *input file name from the user, compress the file, and write a compressed *file and log file.
 *@author Shayna Bello
 *@author Kimil Thomas
 *since 05-09-2023
 */

import java.util.*;
import java.io.*;

public class Compress {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        if (args.length < 0) {
            System.out.println("Please provide an input file name as a command-line argument.");
            return;
        }

        //variables
        String inputFile = args[0];
        String outputFile = inputFile + ".zzz";
        String logFile = outputFile + ".log";
        long startTime =0; long endTime =0;
        String input = "";
        boolean compressTrue = true;

        try {
            //starts the compression program loop, user can compress multiple files
            while (compressTrue) {
                File file = new File(inputFile); // input file
                while (!file.exists()) {
                    System.out.println("Please enter a valid input file name: ");
                    inputFile = scan.nextLine();
                    file = new File(inputFile);
                }

                //read input file
                input = readFile(inputFile);
                int fileSize = input.length();
                if (fileSize > 5000) {fileSize = fileSize / 4;}
                else if (fileSize > 1000) {fileSize = fileSize /2;}
                else {fileSize = fileSize;}


                //initialize hashtable dictionary
                HashtableChain<String, Integer> dictionary = new HashtableChain<>(fileSize);
                for (int i = 32; i <= 127; i++) {
                    dictionary.put(String.valueOf((char)i), i -32);
                }
                dictionary.put("\n", 96);
                dictionary.put("\r", 97);
                dictionary.put("\t", 98);

                //start compression time
                startTime = System.nanoTime();

                StringBuilder output = new StringBuilder(); // output code
                int dictSize = dictionary.size(); //size of the dictionary
                StringBuilder prefix = new StringBuilder(); // current prefix being scanned

                //run compression algorithm
                for (char ch : input.toCharArray()) {
                    prefix.append(ch);

                    //find longest prefix (p) of the uncoded part of the inputfile (if p is not in dict)
                    if (dictionary.get(prefix.toString()) == null) {

                        // output the code (for longest matching p)
                        output.append(String.format("%32s", Integer.toBinaryString(dictionary.get(prefix.substring(0, prefix.length()-1)))).replace(' ', '0'));

                        // pair is going to be inserted into the dictionary
                        dictionary.put(prefix.toString(), dictSize++);

                        prefix.setLength(0); // reset the prefix
                        prefix.append(ch); // start a new prefix with the current character
                    }
                }
                output.append(String.format("%32s", Integer.toBinaryString(dictionary.get(prefix.toString()))).replace(' ', '0'));
                String stringOutput  = output.toString(); //converts output to string

                //end compression time
                endTime = System.nanoTime();

                //write compressed text to binary outputFile
                FileOutputStream fos = new FileOutputStream(outputFile);
                DataOutputStream dos = new DataOutputStream(fos);
                dos.write(stringOutput.getBytes());
                fos.close();
                dos.close();

                //System.out.println(stringOutput);

                //write log to logFile
                PrintWriter logWriter = new PrintWriter (new FileWriter(logFile));
                File inputF= new File(inputFile);
                long origFileSize = inputF.length(); //gets original file size in bytes
                File outputF = new File(outputFile); //gets compressed file size in bytes
                long compressedFileSize = outputF.length();
                double compressionTime = (endTime - startTime) /1000000000.0;

                logWriter.println("Compression of " + inputFile);
                logWriter.println("Compressed from  " + (origFileSize / 1024.0)+ " Kilobytes to " + (compressedFileSize / 1024.0) + " Kilobytes");
                logWriter.println("Compression took " + compressionTime + " seconds");
                logWriter.println("The dictionary contains " + dictionary.size() + " total entries");
                logWriter.println("The table was rehashed " + HashtableChain.rehashCount + " times");
                logWriter.close();


                // Statment for file print
                System.out.println("\nCompressed data was saved in file: "+ outputFile);
                System.out.println("Log data saved in file: " + logFile);

                //ask user if they want to compress another file
                System.out.print("\nWould you like to compress another file? (y/n)");
                String answer = scan.nextLine();
                if (answer.equals("n")) {
                    compressTrue = false;
                    System.out.println("Goodbye!");
                    break;
                } else if (answer.equals("y")) {
                    System.out.println("Please enter a file name: ");
                    inputFile = scan.nextLine();
                    continue;
                } else {
                    System.out.println("Invalid input.Gooodbye.");
                    compressTrue = false;
                    break;
                }
            }//end while(compressTrue)
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        scan.close();

    }//end main

    //read file and compress method
    static String readFile(String inputFile) {
        String input = "";
        try {
            //String output = "";
            File inFile = new File(inputFile); // input file
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
}//end class
