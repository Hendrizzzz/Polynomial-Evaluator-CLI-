package midterms;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A Polynomial designed for the PolynomialEvaluator program.
 * This Polynomial depends on the operations of the Term class.
 * Designed to support only one literal coefficient.
 * Supports method chaining.
 */
public class Polynomial {
    private ArrayList<Term> terms;
    private int degree;
    private char literalCoefficient; // Since this Polynomial only supports one variable


    /**
     * Default Constructor, constructs a polynomial with no elements.
     */
    public Polynomial() {
        this.terms = new ArrayList<>();
        this.degree = 0;
        this.literalCoefficient = 0;
    }


    /**
     * Instantiates a new Polynomial.
     *
     * @param terms the terms
     */
    public Polynomial(ArrayList<Term> terms) {
        this (terms, null);
    }


    /**
     * Constructs a polynomial from another Polynomial
     * @param polynomial the Polynomial to be constructed from
     */
    public Polynomial(Polynomial polynomial) {
        // Defensive copy for Reference objects
        this.terms = new ArrayList<>(polynomial.terms);

        // Primitive types
        this.degree = polynomial.degree;
        this.literalCoefficient = polynomial.literalCoefficient;
    }

    /**
     * Constructs a polynomial given a list of terms with a remainder
     * @param terms the terms of this Polynomial
     * @param remainder the remainder of this Polynomial
     */
    public Polynomial(ArrayList<Term> terms, Polynomial remainder) {
        if (terms == null)
            throw new IllegalArgumentException();

        if (terms.isEmpty()) {
            this.terms = new ArrayList<>();
            this.degree = 0;
            this.literalCoefficient = 0;
            return;
        }

        literalCoefficient = terms.getFirst().getLiteral();

        this.terms = new ArrayList<>();
        ArrayList<Term> toBeAddedTerms = new ArrayList<>(terms); // to avoid sorting the passed terms
        Collections.sort(toBeAddedTerms);
        degree = toBeAddedTerms.getFirst().getExponent(); // the degree of a polynomial is the exponent of the first term (when sorted)

        for (Term term : toBeAddedTerms) // Deep copy
            addTerm(new Term(term));
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
     * Gets literal coefficient.
     *
     * @return the literal coefficient
     */
    public char getLiteralCoefficient() {
        return literalCoefficient;
    }

    /**
     * Gets terms.
     *
     * @return the terms
     */
    public ArrayList<Term> getTerms() {
        return terms;
    }

    /**
     * Sets terms.
     *
     * @param terms the terms
     */
    public void setTerms(ArrayList<Term> terms) {
        this.terms = terms;
    }

    /**
     * Sets degree.
     *
     * @param degree the degree
     */
    public void setDegree(int degree) {
        this.degree = degree;
    }

    /**
     * Sets literal coefficient.
     *
     * @param literalCoefficient the literal coefficient
     */
    public void setLiteralCoefficient(char literalCoefficient) {
        this.literalCoefficient = literalCoefficient;
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
        if (literalIsNotTheSame(newTerm))
            throw new IllegalArgumentException("Inconsistent literal coefficients. ");

        Term currentTerm = this.getTermByExponent(newTerm.getExponent());

        if (currentTerm != null) { // Update if the polynomial already has a term with similar exponent
            currentTerm.setCoefficient(currentTerm.getCoefficient() + newTerm.getCoefficient());
            if (currentTerm.getCoefficient() == 0) // remove if coefficient = 0
                terms.remove(currentTerm);
        }
        else
            terms.add(newTerm);
    }

    // Method to check if this Term and the newTerm have the same literal
    private boolean literalIsNotTheSame(Term newTerm) {
        return newTerm.getLiteral() != this.literalCoefficient;
    }

    // Returns the Term which has the given exponent
    private Term getTermByExponent(int exponent) {
        for (Term term : terms)
            if (term.getExponent() == exponent)
                return term;

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

        return new Polynomial(allTerms);
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
            Term negatedTerm = term.withCoefficient(-1 * term.getCoefficient());
            allTerms.add(negatedTerm);
        }

        return new Polynomial(allTerms);
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
                results.add(thisTerm.getProductWith(otherTerm));

        return new Polynomial(results);
    }


    /**
     * This Polynomial will be divided by another Polynomial
     * @param other the other Polynomial to be divided to this object
     */
    public PolynomialDivisionResult divideBy(Polynomial other) {
        if (other.isEmpty())
            throw new IllegalArgumentException("Divisor is 0. ");

        if (this.isEmpty() || cannotPerformDivision(this, other))
            return new PolynomialDivisionResult(null, this); // return 0, with the remainder of this

        ArrayList<Term> quotients = new ArrayList<>();
        return divisionRecursion(this, other, quotients);
    }


    // Method for doing the repeating process of long division for polynomials;
    // Divide the first term of both, then multiply the quotient to the divisor,
    // then subtract it to the dividend, then repeat.
    private PolynomialDivisionResult divisionRecursion(Polynomial dividend, Polynomial divisor, ArrayList<Term> quotients) {
        if (cannotPerformDivision(dividend, divisor))
            return new PolynomialDivisionResult(new Polynomial(quotients), dividend);

        Term dividendLeadTerm = dividend.terms.getFirst();
        Term divisorLeadTerm = divisor.terms.getFirst();
        Term quotient = dividendLeadTerm.getQuotientWith(divisorLeadTerm);
        quotients.add(quotient);

        ArrayList<Term> queue = new ArrayList<>(); // the terms which to be subtracted in the current dividend

        for (Term term : divisor.terms)
            queue.add(quotient.getProductWith(term));

        // Update the dividend and perform division again
        Polynomial toBeSubtracted = new Polynomial(queue);
        Polynomial newDividend = dividend.decreaseBy(toBeSubtracted);
        return divisionRecursion(newDividend, divisor, quotients);
    }

    // Method to decide if it can still divide
    private boolean cannotPerformDivision(Polynomial dividend, Polynomial divisor) {
        // Cannot perform basic division
        return dividend.degree < divisor.degree || dividend.terms.size() < divisor.terms.size();
    }


    /**
     * Evaluates the Polynomial given the value of its literal/s.
     *
     * @param value the value
     * @return the result
     */
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

    // Helper method that removes the .0 of a double if a perfect integer
    private String formatCoefficient(double coefficient) {
        // Remove '.0' if it's an integer.
        return (coefficient % 1 == 0)
                ? String.valueOf((int) coefficient) // Integer case
                : String.valueOf(coefficient);      // Non-integer case
    }

    // Method to check if a term is not a constant
    private boolean isNotConstant(Term term, double coefficient) {
        return !(coefficient == 1 && term.getExponent() != 0);
    }

}
