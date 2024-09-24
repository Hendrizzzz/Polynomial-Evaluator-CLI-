import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An Immutable Polynomial designed for the PolynomialEvaluator program.
 * This Polynomial depends on the operations of the Term class.
 * Designed to support only one literal coefficient.
 * Supports method chaining.
 */
public class Polynomial {
    private final ArrayList<Term> terms;
    private int degree;
    private Polynomial remainder;
    private char literalCoefficient; // For checking, since this only supports one variable

    {
        degree = 0;
    }

    /**
     * Default Constructor, constructs a polynomial with no elements.
     */
    public Polynomial() {
        this.terms = new ArrayList<>();
    }


    /**
     * Instantiates a new Polynomial.
     *
     * @param terms the terms
     */
    public Polynomial(ArrayList<Term> terms) {
        this.terms = new ArrayList<>();
        ArrayList<Term> toBeAddedTerms = new ArrayList<>(List.copyOf(terms));
        Collections.sort(toBeAddedTerms);
        if (!toBeAddedTerms.isEmpty())
            literalCoefficient = toBeAddedTerms.get(0).getLiteral();

        for (Term term : toBeAddedTerms)
            addTerm(term);

    }


    /**
     * Constructs a polynomial from another Polynomial
     * @param polynomial the Polynomial to be constructed from
     */
    public Polynomial(Polynomial polynomial) {
        this.terms = polynomial.terms;
        this.degree = polynomial.degree;
        this.remainder = polynomial.remainder;
        this.literalCoefficient = polynomial.literalCoefficient;
    }

    /**
     * Constructs a polynomial given a list of terms with a remainder
     * @param terms the terms of this Polynomial
     * @param remainder the remainder of this Polynomial
     */
    public Polynomial(ArrayList<Term> terms, Polynomial remainder) {
        this(terms);
        if (remainder.isEmpty())
            this.remainder = null;
        else
            this.remainder = new Polynomial(remainder);
    }

    /**
     * Constructs a Polynomial with a first term.
     * @param leadTerm the first term of the polynomial
     * @param remainder the remainder of the polynomial
     */
    public Polynomial(Term leadTerm, Polynomial remainder) {
        this.terms = new ArrayList<>();
        this.terms.add(new Term(leadTerm));
        this.remainder = new Polynomial(remainder);
    }


    /**
     * Checks if this Polynomial is not empty.
     * @return true if the Polynomial has no terms yet, else false.
     */
    public boolean isEmpty() {
        return this.terms.isEmpty();
    }


    /**
     * Getter method of the degree datafield
     * @return the degree of the polynomial
     */
    public int getDegree() {
        return degree;
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
     * Gets literal coefficient.
     *
     * @return the literal coefficient
     */
    public char getLiteralCoefficient() {
        return literalCoefficient;
    }


    /**
     * Adds a Term in the Polynomial
     *
     * @param coefficient the coefficient of the term to be added
     * @param exponent the exponent of the term to be added
     * @param literal the literal of the term to be added
     */
    public void addTerm(double coefficient, char literal, int exponent) {
        if (coefficient == 0) return;
        addTerm(new Term(coefficient, literal, exponent));
    }


    /**
     * Adds a Term in the Polynomial. Chains it to the other method.
     *
     * @param newTerm the term to be added in the Polynomial
     */
    public void addTerm(Term newTerm) {
        if (isEmpty())
            this.literalCoefficient = newTerm.getLiteral();

        if (literalIsNotTheSame(newTerm))
            throw new IllegalArgumentException("Inconsistent literal coefficients. ");

        Term oldTerm = this.getTermByExponent(newTerm.getExponent());

        if (oldTerm != null) { // Update if the polynomial already has a term with similar exponent
            terms.remove(oldTerm);
            Term updatedTerm = oldPlusNewTerm(oldTerm, newTerm);
            if (updatedTerm.getCoefficient() != 0)
                terms.add(updatedTerm);
        }
        else
            terms.add(newTerm);

        // Update
        Collections.sort(terms);
        if (!isEmpty())
            degree = terms.getFirst().getExponent(); // the degree of a polynomial is the exponent of the first term (when sorted)
    }

    private Term oldPlusNewTerm(Term oldTerm, Term newTerm) {
        return oldTerm.withCoefficient(oldTerm.getCoefficient() + newTerm.getCoefficient());
    }

    private boolean literalIsNotTheSame(Term newTerm) {
        return !isEmpty() && newTerm.getLiteral() != this.literalCoefficient;
    }

    private Term getTermByExponent(int exponent) {
        for (Term term : terms) {
            if (term.getExponent() == exponent)
                return term;
        }
        return null;
    }


    /**
     * This Polynomial will be increased by another Polynomial.
     * Collect all terms and make a new Polynomial with it.
     * @param other the other Polynomial to be added to this object
     */
    public Polynomial addTo(Polynomial other) {
        // Collect all terms and make a new Polynomial with it.
        ArrayList<Term> allTerms = new ArrayList<>(this.terms);
        allTerms.addAll(other.terms);

        return new Polynomial(allTerms); // The constructor will organize this list of terms
    }


    /**
     * This Polynomial will be decreased by another Polynomial.
     * Collect all terms and make a new Polynomial with it.
     * @param other the other Polynomial to be subtracted to this object
     */
    public Polynomial decreaseBy(Polynomial other) {
        // Collect all terms and make a new Polynomial with it.
        ArrayList<Term> allTerms = new ArrayList<>(this.terms);

        // The terms of the subtrahend should be multiplied to -1 before adding
        for (Term term : other.terms) {
            Term negatedTerm = term.withCoefficient(-term.getCoefficient());
            allTerms.add(negatedTerm);
        }

        return new Polynomial(allTerms); // The constructor will organize this list of terms
    }


    /**
     * This Polynomial will be multiplied by another Polynomial
     * @param other the other Polynomial to be multiplied to this object
     */
    public Polynomial multiplyBy(Polynomial other) {
        ArrayList<Term> results = new ArrayList<>();

        // Distribute each term, collect all products and make a new Polynomial of that list
        for (Term thisTerm : this.terms)
            for (Term otherTerm : other.terms)
                results.add(thisTerm.multiply(otherTerm));

        return new Polynomial(results); // The constructor will organize this list of terms
    }


    /**
     * This Polynomial will be divided by another Polynomial
     * @param other the other Polynomial to be divided to this object
     */
    public Polynomial divideBy(Polynomial other) {
        if (this.isEmpty() || other.isEmpty())
            throw new IllegalArgumentException("One of the Polynomials is empty. ");

        // Return 0 with the remainder of this Polynomial (dividend)
        if (cannotPerformDivision(this, other)) {
            Term quotient = new Term(0, 'x', 0);
            return new Polynomial(quotient, this);
        }

        ArrayList<Term> quotients = new ArrayList<>();
        return divisionRecursion(this, other, quotients);
    }


    private Polynomial divisionRecursion(Polynomial dividend, Polynomial divisor, ArrayList<Term> quotients) {
        if (cannotPerformDivision(dividend, divisor))
            return new Polynomial(quotients, dividend);

        Term dividendLeadTerm = dividend.terms.getFirst();
        Term divisorLeadTerm = divisor.terms.getFirst();
        Term quotient = dividendLeadTerm.divideBy(divisorLeadTerm);
        quotients.add(quotient);

        ArrayList<Term> queue = new ArrayList<>(); // the terms which to be subtracted in the current dividend

        for (Term term : divisor.terms)
            queue.add(quotient.multiply(term));

        // Update the dividend and perform division again
        Polynomial toBeSubtracted = new Polynomial(queue);
        Polynomial newDividend = dividend.decreaseBy(toBeSubtracted);
        return divisionRecursion(newDividend, divisor, quotients);
    }

    private boolean cannotPerformDivision(Polynomial dividend, Polynomial divisor) {
        // Cannot perform basic division
        return dividend.degree < divisor.degree || dividend.terms.size() < divisor.terms.size();
    }


    public double evaluate(double value) {
        double result = 0;

        for (Term term : terms)
            result += term.evaluate(value);

        return result;
    }


    /**
     * @return a String representation of this Polynomial.
     */
    @Override
    public String toString() {
        if (terms.isEmpty())
            return "0";

        StringBuilder polynomial = new StringBuilder();

        // Handle the first term (no need for positive sign before the first term).
        Term firstTerm = terms.get(0);
        polynomial.append(firstTerm.toString());

        for (int i = 1; i < terms.size(); i++) {
            Term currentTerm = terms.get(i);
            polynomial.append(formatTerm(currentTerm));
        }

        return polynomial.toString();
    }

    // Helper method to format each term properly.
    private String formatTerm(Term term) {
        double coefficient = term.getCoefficient();
        StringBuilder termString = new StringBuilder();

        // Don't include the '+' sign for the leading-term
        termString.append(coefficient < 0 ? " - " : " + ");
        coefficient = Math.abs(coefficient);

        String stringCoefficient = formatCoefficient(coefficient);

        // Don't append "1" for coefficients of 1 (except for constants).
        if (isNotConstant(term, coefficient))
            termString.append(stringCoefficient);

        // Handle the literal and exponent part.
        if (term.getExponent() == 1)
            termString.append(term.getLiteral()); // Just append 'x' for exponent 1.
        else if (term.getExponent() != 0)
            termString.append(term.getLiteral()).append("^").append(term.getExponent());

        return termString.toString();
    }

    private String formatCoefficient(double coefficient) {
        // Remove '.0' if it's an integer.
        return (coefficient % 1 == 0)
                ? String.valueOf((int) coefficient) // Integer case
                : String.valueOf(coefficient);      // Non-integer case
    }

    private boolean isNotConstant(Term term, double coefficient) {
        return !(coefficient == 1 && term.getExponent() != 0);
    }

}
