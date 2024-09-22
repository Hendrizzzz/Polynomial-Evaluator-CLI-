import java.util.Objects;


/**
 * A Term designed for the PolynomialEvaluator program. This term doesn't support multivariable.
 * But few multivariable-support methods are still included.
 */
public class Term implements Comparable<Term>{
    private final double coefficient;
    private final String variable;
    private final int exponent;

    public Term(double coefficient, String variable, int exponent) {
        this.coefficient = coefficient;
        this.variable = variable;
        this.exponent = exponent;
    }

    public Term(Term term) {
        this.coefficient = term.coefficient;
        this.variable = term.variable;
        this.exponent = term.exponent;
    }


    public double getCoefficient() {
        return coefficient;
    }


    public String getVariable() {
        return variable;
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
        if (notTheSameExponent(other))
            return null;
        double newNumCoefficient = this.coefficient + other.coefficient;
        return new Term(newNumCoefficient, this.variable, this.exponent);
    }


    /**
     * Subtracts this Term to the other Term then returns a new Term with the difference.
     *
     * @param other the other term to be added to this Term
     * @return A new Term, if you can't subtract then returns a null value
     */
    public Term subtract(Term other) {
        if (notTheSameExponent(other))
            return null;
        double newCoefficient = this.coefficient + other.coefficient;
        return new Term(newCoefficient, this.variable, this.exponent);
    }

    private boolean notSameExponentOrVariable(Term other) {
        return notTheSameExponent(other) || notTheSameVariable(other);
    }

    private boolean notTheSameExponent(Term other) {
        // Nothing to simplify, cannot add or subtract two terms with different exponents
        return this.exponent != other.exponent;
    }

    private boolean notTheSameVariable(Term other) {
        // Nothing to simplify, cannot add or subtract two terms with different literal coefficients
        return !this.variable.equals(other.variable);
    }


    /**
     * Multiplies this Term to the other Term.
     * Returns void because you can multiply two any kinds of Terms.
     * @param other the other Term to be multiplied to this Term.
     */
    public Term multiply(Term other) {
        int resultingExponent = this.exponent + other.exponent;
        double resultingCoefficient = this.coefficient * other.coefficient;
        return new Term(resultingCoefficient, this.variable, resultingExponent);
    }


    /**
     * Divides this Term to the other Term.
     * @param other The divisor
     * @return the quotient Term
     */
    public Term divideBy(Term other) {
        int resultingExponent = this.exponent - other.exponent;
        double resultingCoefficient = this.coefficient / other.coefficient;
        return new Term(resultingCoefficient, this.variable, resultingExponent);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Term term = (Term) o;

        if (Double.compare(coefficient, term.coefficient) != 0) return false;
        if (exponent != term.exponent) return false;
        return Objects.equals(variable, term.variable);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(coefficient);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (variable != null ? variable.hashCode() : 0);
        result = 31 * result + exponent;
        return result;
    }

    @Override
    public String toString() {
        if (exponent == 0)
            return String.valueOf(coefficient);

        // This may not be used in this program
        if (exponent < 0)
            return coefficient + "/" + variable + "^" + -exponent; // bring the exponent down

        return coefficient + variable + "^" + exponent;
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

    public Term withCoefficient(double newCoefficient) {
        return new Term(newCoefficient, this.variable, this.exponent);
    }
}
