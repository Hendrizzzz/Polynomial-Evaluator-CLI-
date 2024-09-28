package midterms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class PolynomialEvaluatorTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    void readLiteral() {
        PolynomialEvaluator polynomialEvaluator = new PolynomialEvaluator();
        BufferedReader reader = new BufferedReader(new StringReader("A"));
        char literal = polynomialEvaluator.readLiteral(reader);
        assertEquals('A', literal);
    }


    @Test
    void constructPolynomialDirectlyTest() throws IOException {
        PolynomialEvaluator polynomial = new PolynomialEvaluator();
        BufferedReader reader = new BufferedReader(new StringReader("-5x^2 + 3x + 1"));

        Polynomial polynomial1 = polynomial.constructPolynomialDirectly(reader);
        String polynomialString = polynomial1.toString();

        assertEquals("-5x^2 + 3x + 1", polynomialString);
    }

    @Test
    void convertStringToTermGivenLiteralTest() {
        PolynomialEvaluator polynomialEvaluator = new PolynomialEvaluator();
        String termString  = "5x^2";
        char literal = 'x';

        Term term = polynomialEvaluator.convertStringToTermGivenLiteral(termString, literal);
        String termStr = term.toString();
        assertEquals("5x^2", termStr);
    }


    @Test
    void constructPolynomialFromString() {
        PolynomialEvaluator polynomialEvaluator = new PolynomialEvaluator();
        String polyString = "-5x^2- 3x^1 + 1";

        Polynomial polynomial = polynomialEvaluator.constructPolynomialFromString(polyString);
        String result = polynomial.toString();

        assertEquals("-5x^2 - 3x + 1", result);
    }

}