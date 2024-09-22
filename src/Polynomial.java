import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An Immutable Polynomial designed for the PolynomialEvaluator program.
 * This Polynomial depends on the operations of the Term class.
 * Designed to support only one literal coefficient.
 * The divide method doesn't support method chaining.
 */
public class Polynomial {
    private final ArrayList<Term> terms;
    private int degree;
    private ArrayList<Term> remainder;

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
     *
     * @param terms
     */
    public Polynomial(ArrayList<Term> terms) {
        this.terms = new ArrayList<>();
        List<Term> tobeAddedTerms = List.copyOf(terms);
        for (Term term : tobeAddedTerms)
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
    }

    /**
     * Constructs a polynomial given a list of terms with a remainder
     * @param terms the terms of this Polynomial
     * @param remainder the remainder of this Polynomial
     */
    public Polynomial(ArrayList<Term> terms, ArrayList<Term> remainder) {
        this(terms);
        this.remainder = remainder;
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
     * Adds a Term in the Polynomial
     *
     * @param coefficient the coefficient of the term to be added
     * @param exponent the exponent of the term to be added
     * @param variable the variable of the term to be added
     */
    public void addTerm(double coefficient, String variable, int exponent) {
        if (coefficient == 0) return;
        addTerm(new Term(coefficient, variable, exponent));
    }


    /**
     * Adds a Term in the Polynomial. Chains it to the other method.
     *
     * @param newTerm the term to be added in the Polynomial
     */
    public void addTerm(Term newTerm) {
        if (newTerm.getCoefficient() == 0) return;

        Term oldTerm = this.getTermByExponent(newTerm.getExponent());

        if (oldTerm != null) { // Update if the polynomial already has a term with similar exponent
            terms.remove(oldTerm);
            Term updatedTerm = oldTerm.withCoefficient(oldTerm.getCoefficient() + newTerm.getCoefficient());
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
    public Polynomial add(Polynomial other) {
        ArrayList<Term> allTerms = new ArrayList<>();

        for (Term term : this.terms)
            allTerms.add(term);

        for (Term term : other.terms)
            allTerms.add(term);

        return new Polynomial(allTerms); // The constructor will organize this list of terms
    }


    /**
     * This Polynomial will be decreased by another Polynomial.
     * Collect all terms and make a new Polynomial with it.
     * @param other the other Polynomial to be subtracted to this object
     */
    public Polynomial subtract(Polynomial other) {
        ArrayList<Term> allTerms = new ArrayList<>();

        for (Term term : this.terms)
            allTerms.add(term);

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

        // Distribute each term
        for (Term thisTerm : this.terms)
            for (Term otherTerm : other.terms) {
                Term product = new Term(otherTerm); // The Term object is mutable, so avoid modifying it for the next multiplication (if applicable)
                product.multiply(thisTerm);
                results.add(product);
            }

        return new Polynomial(results); // The constructor will organize this list of terms
    }


    /**
     * This Polynomial will be divided by another Polynomial
     * @param other the other Polynomial to be divided to this object
     */
    public Polynomial divideBy(Polynomial other) {
        if (this.degree < other.degree)
            throw new IllegalArgumentException("Quotient is not Polynomial, instead it would be a rational expression. ");
        if (this.isEmpty() || other.isEmpty())
            throw new IllegalArgumentException("One of the Polynomials is empty. ");

        if (this.terms.size() < other.terms.size())
            return null;

        ArrayList<Term> quotients = new ArrayList<>();
        return divisionRecursion(this, other, quotients);
    }


    private Polynomial divisionRecursion(Polynomial dividend, Polynomial divisor, ArrayList<Term> quotients) {
        if (cannotPerformDivision(dividend, divisor))
            return new Polynomial(quotients, dividend.terms);

        Term dividendLeadTerm = dividend.terms.getFirst();
        Term divisorLeadTerm = divisor.terms.getFirst();
        Term quotient = dividendLeadTerm.divideBy(divisorLeadTerm);
        quotients.add(quotient);

        ArrayList<Term> queue = new ArrayList<>(); // the terms which to be subtracted in the current dividend

        for (Term term : divisor.terms)
            queue.add(quotient.multiply(term));

        // Update the dividend and perform division again
        Polynomial toBeSubtracted = new Polynomial(queue);
        Polynomial newDividend = dividend.subtract(toBeSubtracted);
        return divisionRecursion(newDividend, divisor, quotients);
    }

    private boolean cannotPerformDivision(Polynomial dividend, Polynomial divisor) {
        // Cannot perform basic division
        return dividend.degree < divisor.degree || dividend.terms.size() < divisor.terms.size();
    }


    @Override
    public String toString() {
        if (terms.isEmpty())
            return "";

        StringBuilder polynomial = new StringBuilder();
        polynomial.append(terms.get(0));

        for (int i = 1; i < terms.size(); i++) {
            double coefficient = terms.get(i).getCoefficient();
            char sign = coefficient < 0 ? '-' : '+';
            coefficient = sign == '+' ? coefficient : -coefficient;
            polynomial.append(" ").append(sign).append(" ").append(coefficient).append(terms.get(i).getVariable()).append("^").append(terms.get(i).getExponent());
        }
        return polynomial.toString();
    }




}
