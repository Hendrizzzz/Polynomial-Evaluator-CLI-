package midterms;

import midterms.LogsPackage.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

/**
 * The {@code PolynomialEvaluator} class allows users to evaluate polynomials
 * and perform basic operations like addition, subtraction, multiplication,
 * and division.
 */
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

    /**
     * Executes the main operations of the polynomial evaluator.
     * It reads past data, runs polynomial operations, and saves data
     * from the current execution.
     */
    private void run() {
        // Read past data
        Logger.ReadData();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        showLimitations();

        runPolynomialOperations(reader);

        // Save data from this program execution
        Logger.saveData();
    }


    /**
     * Runs polynomial operations based on user input.
     *
     * @param reader a BufferedReader to read user input
     */
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


    /**
     * Evaluates a polynomial based on user input.
     *
     * @param reader a BufferedReader to read user input
     */
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


    /**
     * Performs the specified polynomial operation based on user input.
     *
     * @param optionNumber the option number for the operation
     * @param process a string representing the operation process
     * @param operation a string representing the type of operation
     * @param reader a BufferedReader to read user input
     */
    private void performOperation(int optionNumber, String process, String operation, BufferedReader reader) {
        // Header
        System.out.println(Constants.GREEN +
                "[OPTION " + optionNumber + "] " + process + " Two Polynomials. "
                + Constants.RESET);

        System.out.println(Constants.FIRST_POLYNOMIAL_CONSTRUCTION_MESSAGE);
        Polynomial polynomial1 = constructPolynomial(reader);

        System.out.println(Constants.SECOND_POLYNOMIAL_CONSTRUCTION_MESSAGE);
        Polynomial polynomial2 = constructPolynomial(reader);

        // Display Results
        Polynomial result = getResult(polynomial1, polynomial2, operation);
        displayResults(polynomial1, polynomial2, result, operation);
    }

    /**
     * Calculates the result of performing the specified operation on two polynomials.
     *
     * @param polynomial1 the first polynomial
     * @param polynomial2 the second polynomial
     * @param operation a string representing the operation to perform
     * @return the resulting polynomial after performing the operation
     */
    private Polynomial getResult(Polynomial polynomial1, Polynomial polynomial2, String operation) {
        return switch (operation) {
            case "ADDITION"         -> polynomial1.addTo(polynomial2);
            case "SUBTRACTION"      -> polynomial1.decreaseBy(polynomial2);
            case "MULTIPLICATION"   -> polynomial1.multiplyBy(polynomial2);
            default                 -> polynomial1.divideBy(polynomial2);
        };
    }


    /**
     * Constructs a polynomial based on user input.
     *
     * @param reader a BufferedReader to read user input
     * @return the constructed Polynomial
     */
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

    /**
     * Constructs a polynomial directly from user input. (i.e. -x -5x^2 + 3)
     *
     * @param reader a BufferedReader to read user input
     * @return the constructed Polynomial
     * @throws IOException if an I/O error occurs during input
     */
    public Polynomial constructPolynomialDirectly(BufferedReader reader) throws IOException {
        // Manual polynomial entry
        System.out.print("Please enter the polynomial (e.g., 5x^2 + 3x + 1): ");
        String polynomialString = reader.readLine();
        Polynomial polynomial = constructPolynomialFromString(polynomialString);
        System.out.println(Constants.GREEN + Constants.BOLD + "Polynomial entered successfully: " + polynomial + Constants.RESET);
        return polynomial;
    }


    /**
     * Constructs a polynomial with assistance.
     *
     * @param reader a BufferedReader to read user input
     * @return the constructed Polynomial
     * @throws IOException if an I/O error occurs during input
     */
    private Polynomial constructPolynomialWithAssistance(BufferedReader reader) throws IOException {
        System.out.print("How many terms are there in the polynomial? ");
        int termCount = readInteger(1, 100, reader);

        System.out.print("What is the literal coefficient (e.g., x)? ");
        char literal = readLiteral(reader);

        ArrayList<Term> terms = new ArrayList<>();
        for (int i = 0; i < termCount; i++)
            addNewTerm(terms, literal, reader, i);

        Polynomial polynomial = new Polynomial(terms);
        System.out.println(Constants.GREEN + Constants.BOLD + "Polynomial constructed successfully: " + polynomial + Constants.RESET);
        return polynomial;
    }


    private void addNewTerm(ArrayList<Term> terms, char literal, BufferedReader reader, int i) {
        System.out.println(Constants.BOLD + "Constructing Term " + (i + 1) + "..." + Constants.RESET);
        Term term = constructTermGivenLiteral(literal, reader);
        terms.add(term);
    }


    /**
     * Constructs a term given the literal coefficient and user input.
     *
     * @param literal the literal coefficient (e.g., x)
     * @param reader a BufferedReader to read user input
     * @return the constructed Term
     */
    private Term constructTermGivenLiteral(char literal, BufferedReader reader) {
        System.out.print("Coefficient: ");
        double coefficient = readFloatingPointValue(reader);

        System.out.println("Literal: " + literal);

        System.out.print("Exponent: ");
        int exponent = readInteger(0, 100, reader);

        return new Term(coefficient, literal, exponent);
    }


    /**
     * Converts a string representation of a polynomial into a Polynomial object.
     *
     * @param polynomialString the string representation of the polynomial
     * @return the constructed Polynomial
     */
    public Polynomial constructPolynomialFromString(String polynomialString) {
        if (polynomialString == null || polynomialString.isBlank())
            return new Polynomial(); // Return empty polynomial

        if (polynomialString.length() == 1 && !Character.isDigit(polynomialString.charAt(0)))
            throw new IllegalArgumentException("INVALID POLYNOMIAL");

        String formattedPolynomialString = polynomialString.replace(" ", "");
        char literal = getLiteralFromPolynomialString(formattedPolynomialString);

        ArrayList<Term> terms = new ArrayList<>();

        // Have 2 pointers, one from the starting index of a term and one on the ending of the same term
        // Then read each term one by one then passed that to the termString to Term method
        int currentTermIndex = 0;
        int currentTermCharIndex = 0;
        while (currentTermIndex < formattedPolynomialString.length()) {
            currentTermCharIndex = findNextTermEndIndex(++currentTermCharIndex, formattedPolynomialString); // set it to the last index + 1 of the current term
            String termString = formattedPolynomialString.substring(currentTermIndex, currentTermCharIndex); // extract that from the polynomial
            Term term = convertStringToTermGivenLiteral(termString, literal);
            terms.add(term);
            currentTermIndex = currentTermCharIndex; // Move to the next term
        }

        return new Polynomial(terms);
    }



    /**
     * Finds the end index of the next term in the polynomial string.
     *
     * @param startIndex the starting index to search from
     * @param polynomialString the polynomial string
     * @return the index of the last character of the term
     */
    private int findNextTermEndIndex(int startIndex, String polynomialString) {
        int index = startIndex;

        while (index < polynomialString.length() && isCharacterNotOperator(polynomialString.charAt(index))) {
            index++;
        }

        return index; // Return the index of the last character of the term
    }

    private boolean isCharacterNotOperator(char character) {
        return character != '-' && character != '+';
    }


    // Identifies the literal from the StringPolynomial
    private char getLiteralFromPolynomialString(String formattedPolynomialString) {
        for (int i = 0; i < formattedPolynomialString.length(); i++) {
            char currentChar = formattedPolynomialString.charAt(i);
            if (Character.isLetter(currentChar))
                return currentChar;
        }
        return 0;
    }


    // Method that returns a Term object given its String form and the literal
    public Term convertStringToTermGivenLiteral(String termString, char literal) {
        double coefficient; // Default coefficient
        int exponent = 0; // Default exponent

        // Split the term string based on the literal character, preserving the literal character in the split parts.
        // If the term string doesn't contain the literal, the entire string is considered the coefficient part.
        if (termStringHasLiteral(termString)) {
            String[] parts = splitString(termString, "(?=[" + literal + "])");

            // Extract the coefficient part (if present) and the literal part.
            String coefficientPart = (parts.length > 1) ? parts[0] : "";
            String literalPart = (parts.length > 1) ? parts[1] : parts[0]; // Either the literal part or the entire term

            coefficient = parseCoefficient(coefficientPart);
            exponent = parseExponent(literalPart);
        } else
            // If the term doesn't contain a literal, the entire string is the coefficient.
            coefficient = Double.parseDouble(termString);

        return new Term(coefficient, literal, exponent);
    }

    // Given a coefficient part (i.e. -, 4 or none at all), it returns the coefficient
    private double parseCoefficient(String coefficientPart) {
        if (hasCoefficient(coefficientPart))
            return getCoefficientPart(coefficientPart);
        else if (isCoefficientNegativeOne(coefficientPart))
            return -1;

        return 1; // Default coefficient if none specified
    }

    // Helper method
    // Given a literal part (i.e., x^3), it returns the exponent
    private int parseExponent(String literalPart) {
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
    public char readLiteral(BufferedReader reader) {
        while (true) {
            try {
                String charString = reader.readLine();
                if (charString.length() > 1) {
                    System.out.println(Constants.RED + Constants.BOLD + "INVALID INPUT. Literals should only be 1. Try again. " + Constants.RESET + "\n-> ");
                    continue;
                }

                char literal = charString.charAt(0);
                if (Character.isLetter(literal))
                    return literal;
                else
                    System.out.print(Constants.BOLD + Constants.RED + "INVALID INPUT. Not a valid symbolic representation for literal coefficient. Try again. " + Constants.RESET + "\n-> ");
            } catch (IOException e) {
                System.out.println(Constants.IOEXCEPTION_ERROR_MESSAGE);
            }
        }
    }


    // Method that reads and returns double
    private double readFloatingPointValue(BufferedReader reader) {
        while (true) {
            try {
                return Double.parseDouble(reader.readLine());
            } catch (IOException e) {
                System.out.println(Constants.IOEXCEPTION_ERROR_MESSAGE);
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

    // Method to check if the literal part (i.e., x^3 or a) has an exponent
    private boolean hasExponent(String literalPart) {
        return literalPart.contains(Constants.EXPONENT_SYMBOL);
    }

    // Pre-condition: coefficient is either one or negative 1 "-a" or "a"
    private boolean isCoefficientNegativeOne(String coefficient) {
        return coefficient.equals(Constants.NEGATIVE_SIGN);
    }

    // Method to check if the term String has a literal
    private boolean termStringHasLiteral(String termString) {
        return termString.matches(Constants.LETTERS);
    }

    // method to check if there are more than one literal in the given String
    // literalPart = i.e., x^3
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

        if (operation.equals("DIVISION")) {
            PolynomialDivisionResult polynomialDivisionResult = (PolynomialDivisionResult) result;
            output.append(Constants.BOLD).append("   ").append(polynomialDivisionResult.toString()).append(Constants.RESET).append("\n");
        } else {
            output.append(Constants.YELLOW).append("Resulting Polynomial: ").append(Constants.RESET);
            output.append(Constants.BOLD).append("   ").append(result).append(Constants.RESET).append("\n"); // Ensure Polynomial has a proper toString() method
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


    // Inner class to hide the constants
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
        public static final String IOEXCEPTION_ERROR_MESSAGE = RED + BOLD + "An error occurred while reading input. Please try again." + RESET;
        public static final String MULTI_LITERAL_ERROR_MESSAGE = BOLD + RED + "\nFAILED. This program doesn't support multi-literals. Try again. \n" + RESET;
        public static final String FIRST_POLYNOMIAL_CONSTRUCTION_MESSAGE = BOLD + "\nConstructing FIRST Polynomial... " + RESET;
        public static final String SECOND_POLYNOMIAL_CONSTRUCTION_MESSAGE = BOLD + "\nConstructing SECOND Polynomial... " + RESET;
    }

}
