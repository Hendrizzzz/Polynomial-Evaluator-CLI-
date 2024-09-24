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
        Logger.ReadData();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        showLimitations();
        boolean userWantsMore = true;

        do {
            pressEnter(reader);
            int choice = readChoice(reader);

            try {
                switch (choice) {
                    case 1 -> evaluatePolynomial(reader);
                    case 2 -> addTwoPolynomials(reader);
                    case 3 -> subtractTwoPolynomials(reader);
                    case 4 -> multiplyTwoPolynomials(reader);
                    case 5 -> divideTwoPolynomials(reader);
                    case 6 -> Logger.viewLog(); // History
                    case 7 -> userWantsMore = false; // Quit
                }
            } catch (IllegalArgumentException e) {
                System.out.println(Constants.MULTI_LITERAL_ERROR_MESSAGE);
            }
        } while (userWantsMore);

        Logger.saveData();
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



    private void addTwoPolynomials(BufferedReader reader) {
        // Header
        System.out.println(Constants.GREEN + "[OPTION 2] ADD Two Polynomials. " + Constants.RESET);

        System.out.println(Constants.BOLD + "\nConstructing FIRST Polynomial... " + Constants.RESET);
        Polynomial polynomial1 = constructPolynomial(reader);

        System.out.println(Constants.BOLD + "\nConstructing SECOND Polynomial... " + Constants.RESET);
        Polynomial polynomial2 = constructPolynomial(reader);

        // Display Results
        Polynomial sum = polynomial1.add(polynomial2);
        displayResults(polynomial1, polynomial2, sum, "Addition");
    }

    private void subtractTwoPolynomials(BufferedReader reader) {
        // Header
        System.out.println(Constants.GREEN + "[OPTION 3] SUBTRACT Two Polynomials. " + Constants.RESET);

        System.out.println(Constants.BOLD + "\nConstructing FIRST Polynomial... " + Constants.RESET);
        Polynomial polynomial1 = constructPolynomial(reader);

        System.out.println(Constants.BOLD + "\nConstructing SECOND Polynomial... " + Constants.RESET);
        Polynomial polynomial2 = constructPolynomial(reader);

        // Display Results
        Polynomial difference = polynomial1.subtract(polynomial2);
        displayResults(polynomial1, polynomial2, difference, "Subtraction");
    }

    private void multiplyTwoPolynomials(BufferedReader reader) {
        // Header
        System.out.println(Constants.GREEN + "[OPTION 4] MULTIPLY Two Polynomials. " + Constants.RESET);

        System.out.println(Constants.BOLD + "\nConstructing FIRST Polynomial... " + Constants.RESET);
        Polynomial polynomial1 = constructPolynomial(reader);

        System.out.println(Constants.BOLD + "\nConstructing SECOND Polynomial... " + Constants.RESET);
        Polynomial polynomial2 = constructPolynomial(reader);

        // Display Results
        Polynomial product = polynomial1.multiplyBy(polynomial2);
        displayResults(polynomial1, polynomial2, product, "Multiplication");
    }

    private void divideTwoPolynomials(BufferedReader reader) {
        // Header
        System.out.println(Constants.GREEN + "[OPTION 5] DIVIDE Two Polynomials. " + Constants.RESET);

        System.out.println(Constants.BOLD + "\nConstructing FIRST Polynomial... " + Constants.RESET);
        Polynomial polynomial1 = constructPolynomial(reader);

        System.out.println(Constants.BOLD + "\nConstructing SECOND Polynomial... " + Constants.RESET);
        Polynomial polynomial2 = constructPolynomial(reader);

        // Display Results
        Polynomial quotient = polynomial1.divideBy(polynomial2);
        displayResults(polynomial1, polynomial2, quotient, "Division");
    }

    private Polynomial constructPolynomial(BufferedReader reader) {
        while (true) {
            System.out.print("Would you like to enter the polynomial directly (1) or with assistance (2)? : ");
            int choice = readInteger(1, 2, reader);

            try {
                if (choice == 1) {
                    // Manual polynomial entry
                    System.out.print("Please enter the polynomial (e.g., 5x^2 + 3x + 1): ");
                    String polynomialString = reader.readLine();
                    Polynomial polynomial = convertStringToPolynomial(polynomialString);
                    System.out.println(Constants.GREEN + Constants.BOLD + "Polynomial entered successfully: " + polynomial + Constants.RESET);
                    return polynomial;
                }
                else
                    return constructPolynomialWithAssistance(reader);
            } catch (IOException e) {
                System.err.println(Constants.IOEXCEPTION_ERROR_MESSAGE);
            } catch (Exception e) {
                System.out.println(Constants.RED + Constants.BOLD + "INVALID POLYNOMIAL. Please try again." + Constants.RESET);
            }

        }
    }

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


    private void displayResults(Polynomial polynomial1, Polynomial polynomial2, Polynomial result, String operation) {
        StringBuilder output = new StringBuilder();

        output.append(Constants.GREEN).append("╔══════════════════════════════════════╗\n").append(Constants.RESET);
        output.append(Constants.GREEN).append("           RESULT OF ").append(operation.toUpperCase()).append(Constants.RESET).append("\n");
        output.append(Constants.GREEN).append("╚══════════════════════════════════════╝\n").append(Constants.RESET);

        output.append(Constants.YELLOW).append("First Polynomial: ").append(Constants.RESET);
        output.append(Constants.BOLD).append("   ").append(polynomial1).append(Constants.RESET).append("\n"); // Ensure Polynomial has a proper toString() method

        output.append(Constants.YELLOW).append("Second Polynomial: ").append(Constants.RESET);
        output.append(Constants.BOLD).append("   ").append(polynomial2).append(Constants.RESET).append("\n"); // Ensure Polynomial has a proper toString() method

        output.append(Constants.YELLOW).append("Resulting Polynomial: ").append(Constants.RESET);
        if (result.isEmpty())
            output.append(0).append("\n");
        else
            output.append(Constants.BOLD).append("   ").append(result).append(Constants.RESET).append("\n"); // Ensure Polynomial has a proper toString() method

        if (operation.equals("Division") && result.getRemainder() != null) {
            output.append(Constants.YELLOW).append("Remainder Polynomial: ").append(Constants.RESET);
            output.append(Constants.BOLD).append("   ").append(result.getRemainder()).append(Constants.RESET).append("\n"); // Display the remainder
        }

        output.append(Constants.GREEN).append("╔══════════════════════════════════════╗\n").append(Constants.RESET);
        output.append(Constants.GREEN).append("             END OF RESULT           ").append(Constants.RESET).append("\n");
        output.append(Constants.GREEN).append("╚══════════════════════════════════════╝").append(Constants.RESET);

        System.out.print(output.toString() + "\n");
        Logger.addLog(output.toString(), new Date());
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

        System.out.print("Exponent: ");
        int exponent = readInteger(0, 100, reader);

        return new Term(coefficient, literal, exponent);
    }

    private Polynomial convertStringToPolynomial(String polynomialString) {
        if (polynomialString.isBlank())
            return new Polynomial();

        String formattedPolynomialString = polynomialString.replace(" ", "");
        char literal = getLiteralFromPolynomialString(formattedPolynomialString);
        Polynomial polynomial = new Polynomial();
        int currentTermIndex = 0;
        int currentTermCharIndex = currentTermIndex;

        while (currentTermIndex < formattedPolynomialString.length()) {

            while (notReadingATerm(currentTermIndex, currentTermCharIndex, formattedPolynomialString)) {
                currentTermCharIndex++;
            }

            String termString = formattedPolynomialString.substring(currentTermIndex, currentTermCharIndex);
            Term term = convertStringToTermGivenLiteral(termString, literal);
            polynomial.addTerm(term);

            currentTermIndex = currentTermCharIndex;
            currentTermCharIndex++;
        }

        return polynomial;
    }

    private char getLiteralFromPolynomialString(String formattedPolynomialString) {
        for (int i = 0; i < formattedPolynomialString.length(); i++) {
            char currentChar = formattedPolynomialString.charAt(i);
            if (Character.isLetter(currentChar))
                return currentChar;
        }
        return 0;
    }


    private Term convertStringToTermGivenLiteral(String termString, char literal) {
        double coefficient = 1; // Default coefficient
        int exponent = 0; // Default exponent

        if (termStringHasLiteral(termString)) {
            // Split the string into coefficient and literal parts
            String[] parts = splitString(termString, "(?=[" + literal + "])");

            String coefficientPart;
            String literalPart;

            if (parts.length == 1) {
                literalPart = parts[0]; // Contains "a^2"
                coefficientPart = ""; // No coefficient provided
            } else {
                coefficientPart = parts[0];
                literalPart = parts[1]; // Contains "a^2" or similar
            }

            if (hasCoefficient(coefficientPart))
                coefficient = getCoefficientPart(coefficientPart);
            else if (coefficientIsNegativeOne(coefficientPart))
                coefficient = -1;

            if (literalIsMoreThanOne(literalPart))
                throw new IllegalArgumentException("Invalid Term: Multi-variable terms are not supported.");

            if (hasExponent(literalPart)) {
                String[] varParts = splitString(literalPart, "\\^");
                exponent = getExponentPart(varParts);
            } else
                exponent = 1; // If no explicit exponent, set to 1
        } else
            coefficient = Double.parseDouble(termString);

        return new Term(coefficient, literal, exponent);
    }




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


    private void showLimitations() {
        System.out.println(Constants.YELLOW + "Note that this Polynomial Evaluator only supports 1 literal coefficient. Stay tuned for multi-literals. " + Constants.RESET);
    }

    private int readChoice(BufferedReader reader) {
        showMenu();
        return readInteger(1,7,reader);
    }


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

    private boolean leadingTermIsNegative(int currentTermIndex, String formattedPolynomialString) {
        return currentTermIndex == 0 && formattedPolynomialString.charAt(0) == '-';
    }

    private boolean notReadingATerm(int currentTermIndex, int currentTermCharIndex, String formattedPolynomialString) {
        if (leadingTermIsNegative(currentTermIndex, formattedPolynomialString) && currentTermCharIndex == 0) // Ignore the sign of the leading term if negative
            return true;
        return currentTermCharIndex < formattedPolynomialString.length() &&
                (formattedPolynomialString.charAt(currentTermCharIndex) != '+' &&
                        formattedPolynomialString.charAt(currentTermCharIndex) != '-');
    }

    private int getExponentPart(String[] varParts) {
        return Integer.parseInt(varParts[1]);
    }

    private String[] splitString(String termString, String splitter) {
        return termString.split(splitter);
    }

    private int getCoefficientPart(String coefficientPart) {
        return Integer.parseInt(coefficientPart);
    }

    private boolean hasExponent(String literalPart) {
        return literalPart.contains(Constants.EXPONENT_SYMBOL);
    }

    private boolean coefficientIsNegativeOne(String coefficient) {
        return coefficient.equals(Constants.NEGATIVE_SIGN);
    }

    private boolean termStringHasLiteral(String termString) {
        return termString.matches(Constants.LETTERS);
    }

    private boolean literalIsMoreThanOne(String literalPart) {
        String[] parts = splitString(literalPart, "\\" + Constants.EXPONENT_SYMBOL);
        return parts[0].length() != 1;
    }

    // Method to validate coefficient
    private boolean hasCoefficient(String coefficient) {
        return !coefficient.isEmpty() && !coefficient.equals(Constants.ADDITION_SIGN) &&
                !coefficient.equals(Constants.NEGATIVE_SIGN);
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
