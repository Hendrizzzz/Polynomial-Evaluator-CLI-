package midterms;

/**
 * A Term designed for the PolynomialEvaluator program. This term doesn't support multivariable.
 */
public class Term implements Comparable<Term>{
    private double coefficient;
    private char literal;
    private int exponent;

    /**
     * Constructs a Term given its datafields.
     * @param coefficient the coefficient to be set in this Term.
     * @param literal the literal to be set in this Term.
     * @param exponent the exponent to be set in this Term.
     */
    public Term(double coefficient, char literal, int exponent) {
        this.coefficient = coefficient;
        this.literal = literal;
        this.exponent = exponent;
    }

    /**
     * Constructs a new Term based on the given Term
     * @param term the Term to be copied
     */
    public Term(Term term) {
        this.coefficient = term.coefficient;
        this.literal = term.literal;
        this.exponent = term.exponent;
    }

    /**
     * Constructs a new Term with the same datafields except for coefficient
     * @param newCoefficient the new coefficient to be set
     * @return a new Term
     */
    public Term withCoefficient(double newCoefficient) {
        return new Term(newCoefficient, this.literal, this.exponent);
    }


    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public void setLiteral(char literal) {
        this.literal = literal;
    }

    public void setExponent(int exponent) {
        this.exponent = exponent;
    }


    /**
     * Gets coefficient.
     *
     * @return the coefficient
     */
    public double getCoefficient() {
        return coefficient;
    }


    /**
     * Gets literal.
     *
     * @return the literal
     */
    public char getLiteral() {
        return literal;
    }


    /**
     * Gets exponent.
     *
     * @return the exponent
     */
    public int getExponent() {
        return exponent;
    }



    /**
     * Adds this Term to the other Term then returns a new Term with the sum.
     *
     * @param other the other term to be added to this Term
     * @throws IllegalArgumentException if the exponent or the literal of the other Term is different from this Term
     */
    public Term getSumWith(Term other) {
        if (notTheSameExponent(other) || notSameLiteral(other))
            throw new IllegalArgumentException("Terms have different literal coefficients and exponents. ");

        double sumCoefficient = this.coefficient + other.coefficient;
        
        return new Term(sumCoefficient, this.literal, this.exponent);
    }


    /**
     * Increase this Term by the other Term
     *
     * @param other the other term to be added to this Term
     * @throws IllegalArgumentException if the exponent or the literal of the other Term is different from this Term
     */
    public void increaseBy(Term other) {
        if (notTheSameExponent(other) || notSameLiteral(other))
            throw new IllegalArgumentException("Terms have different literal coefficients and exponents. ");

        this.coefficient = this.coefficient + other.coefficient;
    }


    /**
     * Subtracts this Term to the other Term then returns a new Term with the difference.
     *
     * @param other the other term to be added to this Term
     * @throws IllegalArgumentException if the exponent or the literal of the other Term is different from this Term
     */
    public Term getDifferenceWith(Term other) {
        if (notTheSameExponent(other) || notSameLiteral(other))
            throw new IllegalArgumentException("Terms have different literal coefficients and exponents. ");

        double differenceCoefficient = this.coefficient - other.coefficient;
        
        return new Term(differenceCoefficient, this.literal, this.exponent);
    }


    /**
     * Subtracts this Term to the other Term then returns a new Term with the difference.
     *
     * @param other the other term to be added to this Term
     * @throws IllegalArgumentException if the exponent or the literal of the other Term is different from this Term
     */
    public void decreaseBy(Term other) {
        if (notTheSameExponent(other) || notSameLiteral(other))
            throw new IllegalArgumentException("Terms have different literal coefficients and exponents. ");

        this.coefficient = this.coefficient - other.coefficient;
    }

    // Checks if two terms don't have the same literal
    private boolean notSameLiteral(Term other) {
        return this.literal != other.literal;
    }


    // Method to check if this Term and the other Term has the same exponent
    private boolean notTheSameExponent(Term other) {
        // Nothing to simplify, cannot add or subtract two terms with different exponents
        return this.exponent != other.exponent;
    }


    /**
     * Multiplies this Term to the other Term.
     * Returns void because you can multiply two any kinds of Terms.
     * @param other the other Term to be multiplied to this Term.
     * @throws IllegalArgumentException if the literal of the other Term is different from this Term
     */
    public Term getProductWith(Term other) {
        if (notSameLiteral(other))
            throw new IllegalArgumentException("Terms have different literal coefficients. ");

        int newExponent = this.exponent + other.exponent;
        double newCoefficient = this.coefficient * other.coefficient;
        
        return new Term(newCoefficient, this.literal, newExponent);
    }


    /**
     * Divides this Term to the other Term.
     * @param other The divisor
     * @throws IllegalArgumentException if the literal of the other Term is different from this Term
     */
    public Term getQuotientWith(Term other) {
        if (notSameLiteral(other))
            throw new IllegalArgumentException("Terms have different literal coefficients");

        int newExponent = this.exponent - other.exponent;
        double newCoefficient = this.coefficient / other.coefficient;
        
        return new Term(newCoefficient, this.literal, newExponent);
    }


    /**
     * Compares this Term object to the specified object for equality.
     * Two Terms are considered equal if they have the same coefficient,
     * literal, and exponent values.
     *
     * @param o the object to be compared for equality
     * @return true if this Term is equal to the specified object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Term term = (Term) o;

        if (Double.compare(coefficient, term.coefficient) != 0) return false;
        if (literal != term.literal) return false;
        return exponent == term.exponent;
    }


    /**
     * Generates a hash code for the Term object based on its properties.
     *
     * @return an integer hash code for this Term.
     */
    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(coefficient);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) literal;
        result = 31 * result + exponent;
        return result;
    }


    /**
     * Returns a string representation of the Term object.
     *
     * @return a formatted string that represents the term.
     */
    @Override
    public String toString() {
        String literalPart = switch (exponent) {
            case 0 -> "";
            case 1 -> String.valueOf(literal);
            case -1 -> "-" + literal;
            default -> literal + "^" + exponent;
        };

        if (coefficientIsInteger(coefficient))
            return formattedTerm(coefficient, literalPart);
        else
            return coefficient + literalPart;
    }


    /**
     * Formats the term based on its coefficient and literal part.
     *
     * @param coefficient the coefficient of the term
     * @param literalPart the string representation of the literal part
     * @return a formatted string representation of the term
     */
    private String formattedTerm(double coefficient, String literalPart) {
        switch ((int) coefficient) {
            case 0 -> { return "0"; }
            case 1 -> {
                if (exponent != 0) return literalPart; }
            case -1 -> {
                if (exponent != 0) return "-" + literalPart; }
        }
        return (int) coefficient + literalPart;
    }


    /**
     * Checks if the coefficient is an integer.
     *
     * @param coefficient the coefficient to check
     * @return true if the coefficient is an integer, false otherwise
     */
    private boolean coefficientIsInteger(double coefficient) {
        return coefficient % 1 == 0;
    }


    /**
     * Compares this Term object with another Term for sorting purposes.
     * It sorts Terms primarily by their exponent in descending order
     * and secondarily by their coefficients in descending order.
     *
     * @param o the Term to be compared
     * @return a negative integer, zero, or a positive integer as this Term
     *         is less than, equal to, or greater than the specified Term
     */
    @Override
    public int compareTo(Term o) {
        if (this.exponent < o.exponent)
            return 1;
        else if (this.exponent > o.exponent)
            return -1;
        else
            if (this.coefficient == o.coefficient) // compare their coefficients if their exponents are equal
                return 0;
            else
                return this.coefficient <= o.coefficient ? 1 : -1;

    }

    /**
     * Evaluates the term for a given value.
     *
     * @param value the value of the literal to evaluate the term
     * @return the result of the term evaluated at the given value
     */
    public double evaluate(double value) {
        return coefficient * (Math.pow(value, exponent));
    }
}
