import LogsPackage.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class PolynomialEvaluator {

    private void showMenu() {
        System.out.print(Constants.BOLD + """
        
            ***************************************
            *      🌟 POLYNOMIAL EVALUATOR 🌟     *
            ***************************************
            Please choose an option below:
            ---------------------------------------
            |  1. ✅ EVALUATE a Polynomial        |
            |  2. ➕ ADD 2 Polynomials            |
            |  3. ➖ SUBTRACT 2 Polynomials       |
            |  4. ✖️ MULTIPLY 2 Polynomials       |
            |  5. ➗ DIVIDE 2 Polynomials         |
            |  6. 📜 HISTORY                      |
            |  7. ❌ QUIT                         |
            ---------------------------------------
            
            Enter your choice (1-7): \s""" + Constants.RESET);
    }

    private void run() {
        // Read past data
        Logger.ReadData();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        showLimitations();

        runPolynomialOperations(reader);

        // Save data from this program execution
        Logger.saveData();
    }

    private void runPolynomialOperations(BufferedReader reader) {
        boolean userWantsMore = true;

        do {
            int choice = readChoice(reader);

            try {
                switch (choice) {
                    case 1 -> evaluatePolynomial(reader);
                    case 2 -> performOperation(2, "ADD", "ADDITION", reader);
                    case 3 -> performOperation(3, "SUBTRACT", "SUBTRACTION", reader);
                    case 4 -> performOperation(4, "MULTIPLY", "MULTIPLICATION", reader);
                    case 5 -> performOperation(5, "DIVIDE", "DIVISION", reader);
                    case 6 -> Logger.viewLog(); // History
                    case 7 -> userWantsMore = false; // Quit
                }
            } catch (IllegalArgumentException e) {
                System.out.println(Constants.MULTI_LITERAL_ERROR_MESSAGE);
            }

            System.out.println("Saving data...");
            pressEnter(reader);
        } while (userWantsMore);
    }


    private void evaluatePolynomial(BufferedReader reader) {
        // Header
        System.out.println(Constants.GREEN + "[OPTION 1] : EVALUATE a Polynomial." + Constants.RESET);
        System.out.println();

        // Construct the Polynomial
        Polynomial polynomial = constructPolynomial(reader);

        // Get the value for the literal coefficient
        System.out.print("Enter a value for the coefficient '" + polynomial.getLiteralCoefficient() + "': ");
        double value = readFloatingPointValue(reader);

        displayResults(polynomial, value);
    }


    private void performOperation(int optionNumber, String process, String operation, BufferedReader reader) {
        // Header
        System.out.println(Constants.GREEN +
                "[OPTION " + optionNumber + "] " + process + " Two Polynomials. "
                + Constants.RESET);

        System.out.println(Constants.BOLD + "\nConstructing FIRST Polynomial... " + Constants.RESET);
        Polynomial polynomial1 = constructPolynomial(reader);

        System.out.println(Constants.BOLD + "\nConstructing SECOND Polynomial... " + Constants.RESET);
        Polynomial polynomial2 = constructPolynomial(reader);

        // Display Results
        Polynomial result = getResult(polynomial1, polynomial2, operation);
        displayResults(polynomial1, polynomial2, result, operation);
    }

    private Polynomial getResult(Polynomial polynomial1, Polynomial polynomial2, String operation) {
        return switch (operation) {
            case "ADDITION" -> polynomial1.addTo(polynomial2);
            case "SUBTRACTION" -> polynomial1.decreaseBy(polynomial2);
            case "MULTIPLICATION" -> polynomial1.multiplyBy(polynomial2);
            default -> polynomial1.divideBy(polynomial2);
        };
    }


    private Polynomial constructPolynomial(BufferedReader reader) {
        while (true) {
            System.out.print("Would you like to enter the polynomial directly (1) or with assistance (2)? : ");
            int choice = readInteger(1, 2, reader);

            try {
                if (choice == 1)
                    return constructPolynomialDirectly(reader);
                else
                    return constructPolynomialWithAssistance(reader);
            } catch (IOException e) {
                System.err.println(Constants.IOEXCEPTION_ERROR_MESSAGE);
            } catch (Exception e) {
                System.out.println(Constants.RED + Constants.BOLD + "INVALID POLYNOMIAL. Please try again." + Constants.RESET);
            }

        }
    }


    private Polynomial constructPolynomialDirectly(BufferedReader reader) throws IOException {
        // Manual polynomial entry
        System.out.print("Please enter the polynomial (e.g., 5x^2 + 3x + 1): ");
        String polynomialString = reader.readLine();
        Polynomial polynomial = convertStringToPolynomial(polynomialString);
        System.out.println(Constants.GREEN + Constants.BOLD + "Polynomial entered successfully: " + polynomial + Constants.RESET);
        return polynomial;
    }


    // Method for constructing polynomial with user assistance
    private Polynomial constructPolynomialWithAssistance(BufferedReader reader) throws IOException {
        System.out.print("How many terms are there in the polynomial? ");
        int termCount = readInteger(1, 100, reader);

        System.out.print("What is the literal coefficient (e.g., x)? ");
        char literal = readLiteral(reader);

        Polynomial polynomial = new Polynomial();
        for (int i = 0; i < termCount; i++) {
            System.out.println(Constants.BOLD + "Constructing Term " + (i + 1) + "..." + Constants.RESET);
            Term term = constructTermGivenLiteral(literal, reader);
            polynomial.addTerm(term);
        }

        System.out.println(Constants.GREEN + Constants.BOLD + "Polynomial constructed successfully: " + polynomial + Constants.RESET);
        return polynomial;
    }



    private Term constructTermGivenLiteral(char literal, BufferedReader reader) {
        System.out.print("Coefficient: ");
        double coefficient = readFloatingPointValue(reader);

        System.out.println("Literal: " + literal);

        System.out.print("Exponent: ");
        int exponent = readInteger(0, 100, reader);

        return new Term(coefficient, literal, exponent);
    }


    private Polynomial convertStringToPolynomial(String polynomialString) {
        if (polynomialString == null || polynomialString.isBlank())
            return new Polynomial(); // Return empty polynomial

        // Remove whitespaces
        String formattedPolynomialString = polynomialString.replace(" ", "");

        // Determine the literal
        char literal = getLiteralFromPolynomialString(formattedPolynomialString);

        Polynomial polynomial = new Polynomial();

        int currentTermIndex = 0;
        int currentTermCharIndex = 0;
        // Have 2 pointers, one from the starting index of a term and one on the ending of the same term
        // Then read each term one by one then passed that to the termString to Term method
        while (currentTermIndex < formattedPolynomialString.length()) {
            currentTermCharIndex = findNextTermEnd(currentTermCharIndex, formattedPolynomialString);
            String termString = formattedPolynomialString.substring(currentTermIndex, currentTermCharIndex);
            Term term = convertStringToTermGivenLiteral(termString, literal);
            polynomial.addTerm(term);
            currentTermIndex = currentTermCharIndex; // Move to the next term
            currentTermCharIndex++;
        }

        return polynomial;
    }


    private int findNextTermEnd(int startIndex, String polynomialString) {
        int index = startIndex;

        while (index < polynomialString.length() && notReadingATerm(startIndex, index, polynomialString)) {
            index++;
        }

        return index; // Return the index of the last character of the term
    }



    // Identify the literal from the StringPolynomial
    private char getLiteralFromPolynomialString(String formattedPolynomialString) {
        for (int i = 0; i < formattedPolynomialString.length(); i++) {
            char currentChar = formattedPolynomialString.charAt(i);
            if (Character.isLetter(currentChar))
                return currentChar;
        }
        return 0;
    }


    // Method that returns a Term object given its String form and the literal
    private Term convertStringToTermGivenLiteral(String termString, char literal) {
        double coefficient = 1; // Default coefficient
        int exponent = 0; // Default exponent

        if (termStringHasLiteral(termString)) {
            String[] parts = splitString(termString, "(?=[" + literal + "])");

            String coefficientPart = (parts.length > 1) ? parts[0] : "";
            String literalPart = (parts.length > 1) ? parts[1] : parts[0]; // Either the literal part or the entire term

            coefficient = parseCoefficient(coefficientPart);
            exponent = parseExponent(literalPart, literal);
        } else
            coefficient = Double.parseDouble(termString);

        return new Term(coefficient, literal, exponent);
    }

    // Given a coefficient part (i.e -, 4 or none at all) it returns the coefficient
    private double parseCoefficient(String coefficientPart) {
        if (hasCoefficient(coefficientPart))
            return getCoefficientPart(coefficientPart);
        else if (coefficientIsNegativeOne(coefficientPart))
            return -1;

        return 1; // Default coefficient if none specified
    }

    // Helper method
    // Given a literal part (i.e x^3) and the literal, it returns the exponent
    private int parseExponent(String literalPart, char literal) {
        if (literalIsMoreThanOne(literalPart))
            throw new IllegalArgumentException("Invalid Term: Multi-variable terms are not supported.");

        if (hasExponent(literalPart)) {
            String[] varParts = splitString(literalPart, "\\^");
            return getExponentPart(varParts);
        }

        return 1; // Default exponent if none specified
    }


    // Method that adds a space and a pause during the program in every after operation
    public static void pressEnter(BufferedReader reader) {
        System.out.println("Press <Enter> to continue... ");
        try {
            reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 5; i++)
            System.out.println();
    }


    // Method that shows the limitations of this program
    private void showLimitations() {
        System.out.println(Constants.YELLOW + "Note that this Polynomial Evaluator only supports 1 literal coefficient. Stay tuned for multi-literals. " + Constants.RESET);
    }

    // helper method
    private int readChoice(BufferedReader reader) {
        showMenu();
        return readInteger(1,7,reader);
    }


    // Method that reads literal (char)
    private char readLiteral(BufferedReader reader) {
        while (true) {
            try {
                String charString = reader.readLine();
                if (charString.length() > 1)
                    System.out.println(Constants.RED + Constants.BOLD + "INVALID INPUT. Literals should only be 1. Try again. " + Constants.RESET + "\n-> ");
                else {
                    char literal = charString.charAt(0);
                    if (Character.isLetter(literal))
                        return literal;
                    else
                        System.out.print(Constants.BOLD + Constants.RED + "INVALID INPUT. Not a valid symbolic representation for literal coefficient. Try again. " + Constants.RESET + "\n-> ");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    // Method that reads and returns double
    private double readFloatingPointValue(BufferedReader reader) {
        while (true) {
            try {
                return Double.parseDouble(reader.readLine());
            } catch (IOException e) {
                throw new RuntimeException();
            } catch (NumberFormatException e ) {
                System.out.print(Constants.RED + Constants.BOLD + "INVALID INPUT. Must be a numeric Try again. " + Constants.RESET + "\n-> ");
            }
        }
    }


    // Method that reads and returns a validated integer given a minimum and maximum allowed integer
    private int readInteger(int min, int max, BufferedReader reader) {
        boolean inputIsNotValid = true;
        int choice = 0;

        while (inputIsNotValid) {
            try {
                choice = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NumberFormatException e) {
                System.out.print(Constants.RED + Constants.BOLD + "INVALID INPUT. Input is not an integer. Try again. " + Constants.RESET + "\n-> ");
                continue;
            }

            // Verify choice
            if (choice < min || choice > max)
                System.out.print(Constants.RED + Constants.BOLD + "INVALID INPUT. Please enter a number from " + min + " to " + max + " only. Try again. "+ Constants.RESET + "\n-> ");
            else
                inputIsNotValid = false;
        }

        return choice;
    }

    // Helper method
    private boolean leadingTermIsNegative(int currentTermIndex, String formattedPolynomialString) {
        return currentTermIndex == 0 && formattedPolynomialString.charAt(0) == '-';
    }

    // Helper method for the two-pointer to avoid having 20+lines of code in a method
    private boolean notReadingATerm(int currentTermIndex, int currentTermCharIndex, String formattedPolynomialString) {
        if (leadingTermIsNegative(currentTermIndex, formattedPolynomialString) && currentTermCharIndex == 0) // Ignore the sign of the leading term if negative
            return true;
        return currentTermCharIndex < formattedPolynomialString.length() &&
                (formattedPolynomialString.charAt(currentTermCharIndex) != '+' &&
                        formattedPolynomialString.charAt(currentTermCharIndex) != '-');
    }

    // Method to get the exponent part given an array of a literal part
    // the first element should be the literal
    private int getExponentPart(String[] varParts) {
        return Integer.parseInt(varParts[1]);
    }

    // Method to split a given string with a given splitter
    private String[] splitString(String termString, String splitter) {
        return termString.split(splitter);
    }

    private int getCoefficientPart(String coefficientPart) {
        return Integer.parseInt(coefficientPart);
    }

    // Method to check if the literal part (i.e x^3 or a) has an exponent
    private boolean hasExponent(String literalPart) {
        return literalPart.contains(Constants.EXPONENT_SYMBOL);
    }

    // Pre condition : coefficient is either one or negative 1 "-a" or "a"
    private boolean coefficientIsNegativeOne(String coefficient) {
        return coefficient.equals(Constants.NEGATIVE_SIGN);
    }

    // Method to check if the term String has a literal
    private boolean termStringHasLiteral(String termString) {
        return termString.matches(Constants.LETTERS);
    }

    // method to check if there are more than one literal in the given String
    // literalPart = i.e. x^3
    private boolean literalIsMoreThanOne(String literalPart) {
        String[] parts = splitString(literalPart, "\\" + Constants.EXPONENT_SYMBOL);
        return parts[0].length() != 1;
    }

    // Method to check if it is a coefficient
    private boolean hasCoefficient(String coefficient) {
        return !coefficient.isEmpty() && !coefficient.equals(Constants.ADDITION_SIGN) &&
                !coefficient.equals(Constants.NEGATIVE_SIGN);
    }


    /**
     * This method is used to display results with format for the evaluation of Polynomial
     * @param polynomial evaluated polynomial
     * @param value the result.
     */
    private void displayResults(Polynomial polynomial, double value) {
        StringBuilder log = new StringBuilder();

        // Add a stylized header
        log.append(Constants.GREEN).append("╔══════════════════════════════════════╗\n").append(Constants.RESET);
        log.append(Constants.GREEN).append("          EVALUATING POLYNOMIAL         ").append(Constants.RESET).append("\n");
        log.append(Constants.GREEN).append("╚══════════════════════════════════════╝\n").append(Constants.RESET);

        // Log the polynomial being evaluated
        log.append(Constants.YELLOW).append("Polynomial: ").append(Constants.RESET)
                .append(Constants.BOLD).append(polynomial).append(Constants.RESET).append("\n");

        // Log the value of the coefficient
        log.append(Constants.YELLOW).append("Value for Coefficient '").append(Constants.RESET)
                .append(Constants.BOLD).append(polynomial.getLiteralCoefficient())
                .append(Constants.RESET).append("': ").append(Constants.CYAN)
                .append(String.format("%.3f", value)).append(Constants.RESET).append("\n");

        // Log the result after evaluation
        log.append(Constants.YELLOW).append("Calculated Result: ").append(Constants.RESET)
                .append(Constants.CYAN).append(String.format("%.3f", polynomial.evaluate(value)))
                .append(Constants.RESET).append("\n");

        // Add a divider line at the end
        log.append(Constants.GREEN).append("╔══════════════════════════════════════╗\n").append(Constants.RESET);
        log.append(Constants.GREEN).append("            END OF EVALUATION         ").append(Constants.RESET).append("\n");
        log.append(Constants.GREEN).append("╚══════════════════════════════════════╝").append(Constants.RESET);

        // Print the log and add it to the logger
        System.out.println(log);
        Logger.addLog(log.toString(), new Date());
    }


    /**
     * This method is used to display the results for two Polynomials' Operation
     * @param polynomial1 the first polynomial
     * @param polynomial2 the second polynomial
     * @param result the resulting polynomial
     * @param operation the operation used
     */
    private void displayResults(Polynomial polynomial1, Polynomial polynomial2, Polynomial result, String operation) {
        StringBuilder output = new StringBuilder();

        output.append(Constants.GREEN).append("╔══════════════════════════════════════╗\n").append(Constants.RESET);
        output.append(Constants.GREEN).append("         RESULT OF ").append(operation).append(Constants.RESET).append("\n");
        output.append(Constants.GREEN).append("╚══════════════════════════════════════╝\n").append(Constants.RESET);

        output.append(Constants.YELLOW).append("First Polynomial: ").append(Constants.RESET);
        output.append(Constants.BOLD).append("   ").append(polynomial1).append(Constants.RESET).append("\n"); // Ensure Polynomial has a proper toString() method

        output.append(Constants.YELLOW).append("Second Polynomial: ").append(Constants.RESET);
        output.append(Constants.BOLD).append("   ").append(polynomial2).append(Constants.RESET).append("\n"); // Ensure Polynomial has a proper toString() method

        output.append(Constants.YELLOW).append("Resulting Polynomial: ").append(Constants.RESET);
        output.append(Constants.BOLD).append("   ").append(result).append(Constants.RESET).append("\n"); // Ensure Polynomial has a proper toString() method

        if (operation.equals("Division") && result.getRemainder() != null) {
            output.append(Constants.YELLOW).append("Remainder Polynomial: ").append(Constants.RESET);
            output.append(Constants.BOLD).append("   ").append(result.getRemainder()).append(Constants.RESET).append("\n"); // Display the remainder
        }

        output.append(Constants.GREEN).append("╔══════════════════════════════════════╗\n").append(Constants.RESET);
        output.append(Constants.GREEN).append("             END OF RESULT           ").append(Constants.RESET).append("\n");
        output.append(Constants.GREEN).append("╚══════════════════════════════════════╝").append(Constants.RESET);

        System.out.print(output + "\n");
        Logger.addLog(output.toString(), new Date());
    }



    public static void main(String[] args) {
        PolynomialEvaluator myProgram;

        try {
            myProgram = new PolynomialEvaluator();
            myProgram.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static class Constants {

        // ANSI Color Codes
        public static final String RESET = "\u001B[0m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String CYAN = "\u001B[36m";

        // Text Formatting
        public static final String BOLD = "\u001B[1m";

        // Symbols and Signs
        public static final String LETTERS = ".*[a-zA-Z].*";
        public static final CharSequence EXPONENT_SYMBOL = "^";
        public static final String NEGATIVE_SIGN = "-";
        public static final String ADDITION_SIGN = "+";

        // Error Message
        public static final String IOEXCEPTION_ERROR_MESSAGE =
                RED + BOLD + "An error occurred while reading input. Please try again." + RESET;
        public static final String MULTI_LITERAL_ERROR_MESSAGE = Constants.BOLD + Constants.RED + "\nFAILED. This program doesn't support multi-literals. Try again. \n" + Constants.RESET;
    }

}
