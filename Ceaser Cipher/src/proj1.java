        /*This program emulates a caesar cipher. It asks the users for any number of positive or negative shift keys. Then the user inputs a phrase or message and that message is then encoded by the inputted cipher keys and then decoded back to the original text by the decrypter.
        *
        * @author Kimil Thomas
        * @author Shayna Bello
        * @since 02-23-23
        *
        */
        import java.util.*;
        import java.io.*;
        import java.lang.*;
public class proj1
{
    public static void main(String[] args) {

        // Scanner initialization
        Scanner input =  new Scanner(System.in);

        // Variable initialization
        String message = ("");
        String choice = ("");
        String encryptedText = ("");
        String decryptedText = ("");

        // Start program for while loop
        do{

            //Input for value keys
            System.out.println("Enter the individual key values(positive or negative integers, one after another in the same line with a blank between two values: ");
            String inputKeys = input.nextLine();

            // Loop to convert the input string of Key values into an array
            String[] strKeys = inputKeys.split(" "); // string array for the keys
            int[] keyValues = new int[strKeys.length];
            for(int i = 0; i < strKeys.length; i++){
                keyValues[i] = Integer.valueOf(strKeys[i]);
            }

            //Input for encoded string
            System.out.println("Enter a string to be encoded: ");
            message = input.nextLine();

            // Method call for encryption
            encryptedText = encryption(keyValues,message);

            // Print out of Encoded Message
            System.out.println("\nEncoded Message: " + encryptedText);

            // Method Call for decryptedText
            decryptedText = decryption(keyValues,encryptedText);

            // Output for decoded message
            System.out.println("Decoded Message: " + decryptedText);

            //Determines whether user wants to end program
            System.out.println("\nDo you want to run the program again? (y for yes and n for no?): ");
            choice  = input.nextLine();
            if (choice.equalsIgnoreCase("y")){
                continue;
            }else if(choice.equalsIgnoreCase("n")){
                break;
            }

        }while(choice.equalsIgnoreCase("y"));
        input.close();
    }// End of main

    // Method for creating the ASCII string
    public static String ascii(int[] keyValues){
        // Method vafvcifdriables
        char index;
        String asciiString = "";
        // Loop to Create ASCII
        for(int j = 32; j <= 126; j++){
            index = (char)j;
            asciiString +=index;
        }
        return asciiString;
    }

    // Method for Encryption with parameters for key shifts and the string of the user inputted.
    public static String encryption(int[] keys, String message){
        // Variables
        String cipheredText = ("");     // String initialization for ciphered text return
        int position = 0;               // int initialization for ASCII cipher position
        int cipherValue, keyShift;      // int initialization for the cipherValue, the keyShift
        int keyIndex = 0;               // int for keyIndex shift through the key Array
        String caseAscii = ascii(keys);         // String initialization for ASCII with call to ASCII method

        // For loop for message encryption
        for(int i = 0; i < message.length(); i++){
            position = caseAscii.indexOf(message.charAt(i));
            keyShift = keys[keyIndex];              // keyshift initialization
            cipherValue = Math.abs((keyShift + position)) % caseAscii.length(); // cipherValue with caeser equation
            char replaceVal = caseAscii.charAt(cipherValue);
            cipheredText += replaceVal;

            // If statement to loop keyindex
            if( keyIndex+1 >= keys.length){
                keyIndex = 0;
            }
            keyIndex++;

        }
        // Completed CipherText returned
        return cipheredText;
    }

    // Method for Decryption with paramters for key shifts and the string of the given cipheredtext
    public static String decryption(int[] keys, String cipheredText){
        // Variables
        String decryptedText = ("");    // String initialization for decyrpting text
        int position = 0;               // int initialization for ASCII cipher position
        int decryptedValue, keyShift;   // int initialization for the decryptedValue and the keyShift
        int keyIndex = 0;               // int for keyIndex shift through the key array
        String caseAscii = ascii(keys); // String initialization for ASCII with call to ASCII method

        // For loop to decrypt cipheredText
        for(int i = 0; i < cipheredText.length(); i++){
            position = caseAscii.indexOf(cipheredText.charAt(i));
            keyShift = keys[keyIndex];      // keyShift initialization
            decryptedValue = (Math.abs(keyShift - (position+ caseAscii.length() ))) % caseAscii.length();   // decryptedValue with caesar equation
            char replaceVal = caseAscii.charAt(decryptedValue);
            decryptedText += replaceVal;


            // If statement to loop keyindex
            if( keyIndex+1 == keys.length){
                keyIndex = 0;
            }
            keyIndex++;


        }
        // Completed CipherText returned
        return decryptedText;
    }


} // end of class


