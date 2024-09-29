package midterms;


/**
 * A Polynomial with a Quotient and Result
 */
public class PolynomialDivisionResult extends Polynomial {
    private Polynomial quotient;
    private Polynomial remainder;


    /**
     * Instantiates a new Polynomial division result.
     *
     * @param quotient  the quotient
     * @param remainder the remainder
     */
    public PolynomialDivisionResult(Polynomial quotient, Polynomial remainder) {
        this.quotient = quotient;
        this.remainder = remainder;
    }

    /**
     * Gets quotient.
     *
     * @return the quotient
     */
    public Polynomial getQuotient() {
        return quotient;
    }

    /**
     * Sets quotient.
     *
     * @param quotient the quotient
     */
    public void setQuotient(Polynomial quotient) {
        this.quotient = quotient;
    }

    /**
     * Gets remainder.
     *
     * @return the remainder
     */
    public Polynomial getRemainder() {
        return remainder;
    }

    /**
     * Sets remainder.
     *
     * @param remainder the remainder
     */
    public void setRemainder(Polynomial remainder) {
        this.remainder = remainder;
    }


    /**
     * @return the String representation for this Quotient of Polynomial
     */
    @Override
    public String toString() {
        if (quotient == null && remainder == null)
            return "0";

        if (quotient == null)
            return "Result: 0\nRemainder: " + remainder;

        if (remainder == null)
            return quotient.toString();

        if (remainder.isEmpty())
            return "Result: " + quotient;

        return "Result: " + quotient + "\nRemainder: " + remainder;
    }
}
