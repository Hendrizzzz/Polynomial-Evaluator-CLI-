import java.util.Objects;


/**
 * An Immutable Term designed for the PolynomialEvaluator program. This term doesn't support multivariable.
 */
public class Term implements Comparable<Term>{
    private final double coefficient;
    private final char literal;
    private final int exponent;

    /**
     * Constructs a Term given its all datafields.
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


    // Getter methods
    public double getCoefficient() {
        return coefficient;
    }

    public char getLiteral() {
        return literal;
    }

    public int getExponent() {
        return exponent;
    }



    /**
     * Adds this Term to the other Term then returns a new Term with the sum.
     *
     * @param other the other term to be added to this Term
     * @return A new Term, if you can't add then returns a null value
     */
    public Term add(Term other) {
        if (notTheSameExponent(other) || notSameLiteral(other))
            return null;
        double newNumCoefficient = this.coefficient + other.coefficient;
        return new Term(newNumCoefficient, this.literal, this.exponent);
    }


    /**
     * Subtracts this Term to the other Term then returns a new Term with the difference.
     *
     * @param other the other term to be added to this Term
     * @return A new Term, if you can't subtract then returns a null value
     */
    public Term subtract(Term other) {
        if (notTheSameExponent(other) || notSameLiteral(other))
            return null;
        double newCoefficient = this.coefficient + other.coefficient;
        return new Term(newCoefficient, this.literal, this.exponent);
    }

    // Won't be used in this program
    private boolean notSameLiteral(Term other) {
        return this.literal != other.literal;
    }


    private boolean notTheSameExponent(Term other) {
        // Nothing to simplify, cannot add or subtract two terms with different exponents
        return this.exponent != other.exponent;
    }


    /**
     * Multiplies this Term to the other Term.
     * Returns void because you can multiply two any kinds of Terms.
     * @param other the other Term to be multiplied to this Term.
     */
    public Term multiply(Term other) {
        if (notSameLiteral(other))
            return null;
        int resultingExponent = this.exponent + other.exponent;
        double resultingCoefficient = this.coefficient * other.coefficient;
        return new Term(resultingCoefficient, this.literal, resultingExponent);
    }


    /**
     * Divides this Term to the other Term.
     * @param other The divisor
     * @return the quotient Term
     */
    public Term divideBy(Term other) {
        if (notSameLiteral(other))
            return null;
        int resultingExponent = this.exponent - other.exponent;
        double resultingCoefficient = this.coefficient / other.coefficient;
        return new Term(resultingCoefficient, this.literal, resultingExponent);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Term term = (Term) o;

        if (Double.compare(coefficient, term.coefficient) != 0) return false;
        if (literal != term.literal) return false;
        return exponent == term.exponent;
    }

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

    @Override
    public String toString() {
        if (coefficient == 0)
            return "0";

        String stringCoefficient = (this.coefficient % 1 == 0)
                ? String.valueOf((int) this.coefficient) // Integer case
                : String.valueOf(this.coefficient);      // Non-integer case

        if (exponent == 0) {
            if (this.coefficient == 1)
                return "1";
            else if (coefficient == -1)
                return "-1";
        }

        if (this.coefficient == 1)
            stringCoefficient = "";
        else if (this.coefficient == -1)
            stringCoefficient = "-";

        if (exponent < 0)
            return (stringCoefficient.isEmpty() ? 1 : stringCoefficient) + "/" + literal + "^" + -exponent; // bring the exponent down
        else if (exponent == 1)
            return stringCoefficient + literal;
        return stringCoefficient + literal + "^" + exponent;
    }


    /**
     * @param o the object to be compared.
     * @return the difference in numerically
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

    public double evaluate(double value) {
        return coefficient * (Math.pow(value, exponent));
    }
}
