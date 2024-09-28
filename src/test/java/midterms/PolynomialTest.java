package midterms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PolynomialTest {


    private ArrayList<Term> terms = new ArrayList<>();

    @BeforeEach
    void setTerms(){
        Term term = new Term(-5.0, 'x', 3);
        Term term1 = new Term(-3.0, 'x', 1);
        Term term2 = new Term(1.0, 'x', 0);

        terms.add(term1);
        terms.add(term);
        terms.add(term2);
    }

    @Test
    void testToString() {
        Polynomial polynomial = new Polynomial(terms);

        String polyString = polynomial.toString();

        assertEquals("-5x^3 - 3x + 1", polyString);
    }


    @Test
    void multiplyTest() {
        Polynomial polynomial = new Polynomial(terms);
        Polynomial polynomial1 = new Polynomial(terms);
        polynomial1.addTerm(new Term(5, 'x', 8));

        Polynomial product= polynomial.multiplyBy(polynomial1);

        assertEquals("-25x^11 - 15x^9 + 5x^8 + 25x^6 + 30x^4 - 10x^3 + 9x^2 - 6x + 1", product.toString());
    }


    @Test
    void divisionTest() {
        Polynomial polynomial = new Polynomial(terms);
        Polynomial polynomial1 = new Polynomial(terms);

        Polynomial quotient = polynomial.divideBy(polynomial1);

        assertEquals("1", quotient.toString());
    }


}