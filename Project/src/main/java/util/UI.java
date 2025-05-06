package util;/*

    Featured in this UI
    _______________________

    Prompts:                        Displays:                   Random:
    Numeric                         Msg                         Single (min - max)
    Binary                          List
    Text
    Double
    Char
    CharAZ
    Email
    Date
    PhoneNumber
    PasswordConfirmation
    Gender
    Choice (ArrayList)
    rollDice
    RandomLetterAZ
    TextColor (console)
    BackgroundColor (console)
    TextFormat (console)

    Last updated: 05-04-2025
    Updated by: Jonas

*/

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class UI { // Our own custom generic util.TextUI class

    // Attributes
    Scanner scanner = new Scanner(System.in);
    Random random = new Random();

    // ________________________________________________________

    public void displayList(ArrayList <String> list, String msg){

        for(int i = 0; i < list.size(); i++){
            System.out.println(i+1 + ". " + list.get(i));
        }

    }

    // ________________________________________________________

    public int randomSingle(int numberOne, int numberTwo){

        int randomNumber = random.nextInt(numberOne, numberTwo);
        return randomNumber;

    }

    // ________________________________________________________

    public String promptRandomLetterAZ(String input){

        try {

            if(input.matches("\\d+")){ // Invalid characters handle
                text("Please only use valid inputs.");
            }

            char singleLetter = (char) ('A' + random.nextInt(26)); // A-Z (26)
            return String.valueOf(singleLetter);

        } catch (IllegalArgumentException e) {

            text("Invalid input. Try again.");
            return promptRandomLetterAZ(input);

        }

    }

    // ________________________________________________________

    public int rollDice(int amountOfDice){

        int totalSum = 0;
        int[] dice = new int[amountOfDice];

        for(int i = 0; i < dice.length; i++){
            dice[i] = random.nextInt(1, 7);
            totalSum += dice[i];
        }

        return totalSum;

    }

    // ________________________________________________________

    public String text(String msg){

        return msg;

    }

    // ________________________________________________________

    public String promptGender(String msg){

        boolean valid = false;
        String input = "";

        while(!valid){

            input = msg;

            if(input.equalsIgnoreCase("Male") || input.equalsIgnoreCase("Female")){

                valid = true;

            } else {

                return text("Invalid input");

            } // If-else end

        } // While end

        return input;
    }

    // ________________________________________________________

    public boolean promptBinary(String msg){

        String choice = msg.toLowerCase();

        // Added most common user replies so we don't have to rely on y/n only.

        switch (choice){
            case "y", "yes", "yea", "yup", "yeah", "ya", "yessir", "yur":
                return true;
            case "n", "no", "na", "nah", "nope":
                return false;
            default:
                text("Invalid input");
                return promptBinary(msg);
        }

        /*

        Allow users to suggest new keywords that have failed? Like if I type "nopers"
        and I genuinely think it's correct and a valid response.. Should I as user
        be allowed to press "Keyword failed.. Think it's correct? Suggest it for future updates.." ?

        I think it would be a cool little feature.

        */

    }

    // ________________________________________________________

    public int promptNumeric(String msg){

        int numInput = 0;
        boolean valid = false;

        while (!valid){

            String input = msg;

            try {

                numInput = Integer.parseInt(input);
                valid = true;

            } catch (NumberFormatException e){

                text("Error. Please write a valid number..");

            } // Try-catch end

        } // While loop end

        return numInput;

    }

    // ________________________________________________________

    public double promptDecimal(String msg){

        double numInput = 1.0;
        boolean valid = false;

        while (!valid){

            String input = msg;

            try{

                numInput = Double.parseDouble(input);
                valid = true;

            } catch (NumberFormatException e){

                text("Please enter a valid decimal number..");

            } // Try-catch end

        } // While end

        return numInput;

    }

    // ________________________________________________________

    public String promptEmail(String msg){

        String emailInput;
        boolean valid = false;

        while(!valid){

            emailInput = msg;

            if(emailInput.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")){

                valid = true;
                return emailInput;

            } else {

                text("Invalid format. Please try again..");

            } // If end

        } // While end

        // Un-reachable cuz of if-else statement
        return null;

    }

    // ________________________________________________________

    public LocalDate promptDate(String msg){

        /*

        How to use
        __________

        LocalDate testDate = ui.promptDate("Please enter a date..");

        */

        String dateInput;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy"); // MM not mm | mm apparently is minutes
        boolean valid = false;

        while(true){

            dateInput = msg;

            try{

                return LocalDate.parse(dateInput, dtf);

            } catch (DateTimeParseException e) {

                text("Invalid input. Try again.. Hint: dd-mm-yyyy");

            } // Try-catch end

        } // While end

    }

    // ________________________________________________________

    public String promptPhoneNumber(String msg){

        String numberInput;

        while(true){

            numberInput = msg;

            if(numberInput.matches("^\\+?[0-9]{8,8}$")){ // 8,8 allows danish only phone numbers

                return numberInput;

            } else {

               text("Invalid number.. Try again!");

            }

        }

    }

    // ________________________________________________________

    public boolean promptPasswordConfirmation(String msg){

        /*

        How to use | Example
        ____________________

        String playerPassword = ui.promptText("Please enter a password..");
        boolean passwordTest = false;

        // Allows us to loop over the password part
        while(!passwordTest){

            passwordTest = ui.promptPasswordConfirmation(playerPassword);

            if(!passwordTest){
                ui.displayMsg("\nPasswords don't match.. Try again.\n");
            }

        }

        */

        String passwordConfirmation = text("Please confirm your password:");
        return msg.equals(passwordConfirmation);

    }

    // ________________________________________________________

    public char promptChar(String msg){

        // Basically just takes the first character of a given String and stores it as a char

        char charInput = '0';
        boolean valid = false;

        while (!valid){

            String input = msg;

            if(input.length() > 0){

                charInput = input.charAt(0);
                valid = true;

            } else {

                text("Please enter a valid character..");
            }

        }

        return charInput;

    }

    // ________________________________________________________

    public char promptCharAZ(String msg){

        // Only allow single letters.

        char charInput = '0';
        boolean valid = false;

        while (!valid){

            String input = msg;

            if(input.length() == 1){

                charInput = Character.toUpperCase(input.charAt(0));

                if(charInput >= 'A' && charInput <= 'Z'){

                    valid = true;

                } else {

                    text("Wrong input. Please only use A - Z..");

                } // if end (INNER)

            } else {

                text("Please only enter a single character..");

            } // if end (OUTTER)

        } // While-end

        return charInput;

    }

    // ________________________________________________________

    public ArrayList <String> promptChoice(ArrayList <String> options, int limit, String msg){

        displayList(options, "");
        ArrayList <String> choices = new ArrayList<>();

        while(choices.size() < limit){
            int choice = promptNumeric(msg);
            choices.add(options.get(choice-1));
        }

        return choices;

    }

    // ________________________________________________________

    /*

    How to use:
    ___________

    ui.displayMsg(ui.promptTextColor("red") + "Netflix" + ui.promptTextColor("reset"));

    */

    public String promptTextColor(String msg){

        switch(msg.toLowerCase()){

            case "black":
                return "\u001B[30m";
            case "red":
                return "\u001B[31m";
            case "green":
                return "\u001B[32m";
            case "yellow":
                return "\u001B[33m";
            case "blue":
                return "\u001B[34m";
            case "purple":
                return "\u001B[35m";
            case "cyan":
                return "\u001B[36m";
            case "white":
                return "\u001B[37m";
            case "grey":
                return "\u001B[90m";
            case "bright red":
                return "\u001B[91m";
            case "bright green":
                return "\u001B[92m";
            case "bright yellow":
                return "\u001B[93m";
            case "bright blue":
                return "\u001B[94m";
            case "bright purple":
                return "\u001B[95m";
            case "bright cyan":
                return "\u001B[96m";
            case "bright white":
                return "\u001B[97m";
            case "reset":
                return "\u001B[0m";
            default:
                return "|invalid color|";

        } // Switch-case end

    }

    // ________________________________________________________

    /*

    How to use:
    ___________

    ui.displayMsg(ui.promptTextColor("red") + ui.promptBackgroundColor("white") + "Netflix" + ui.promptTextColor("reset"));

    */

    public String promptBackgroundColor(String msg){

        switch(msg.toLowerCase()){

            case "black":
                return "\u001B[40m";
            case "red":
                return "\u001B[41m";
            case "green":
                return "\u001B[42m";
            case "yellow":
                return "\u001B[43m";
            case "blue":
                return "\u001B[44m";
            case "purple":
                return "\u001B[45m";
            case "cyan":
                return "\u001B[46m";
            case "white":
                return "\u001B[47m";
            case "grey":
                return "\u001B[100m";
            case "bright red":
                return "\u001B[101m";
            case "bright green":
                return "\u001B[102m";
            case "bright yellow":
                return "\u001B[103m";
            case "bright blue":
                return "\u001B[104m";
            case "bright purple":
                return "\u001B[105m";
            case "bright cyan":
                return "\u001B[106m";
            case "bright white":
                return "\u001B[107m";
            case "reset":
                return "\u001B[0m";
            default:
                return "|invalid background color|";

        } // Switch-case end

    }

    // ________________________________________________________

    public String promptTextFormat(String msg){

        switch(msg.toLowerCase()){

            case "bold":
                return "\u001B[1m";
            case "italic":
                return "\u001B[3m";
            case "underline":
                return "\u001B[4m";
            case "cross out":
                return "\u001B[9m";
            case "cross out reset":
                return "\u001B[29m";
            case "outline":
                return "\u001B[51m";
            case "outline reset":
                return "\u001B[54m";
            default:
                return "|Invalid input|";

        } // Switch-case end

    }


} // util.TextUI end
