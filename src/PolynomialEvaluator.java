public class PolynomialEvaluator {
    public static void main(String[] args) {
        Polynomial polynomial1 = new Polynomial();

        polynomial1.addTerm(2,"x",3);
        polynomial1.addTerm(2,"x", 2);
        polynomial1.addTerm(5,"x", 1);
        polynomial1.addTerm(2,"x", 0);


        Polynomial polynomial2 = new Polynomial();

        polynomial2.addTerm(2,"x",3);
        polynomial2.addTerm(2,"x", 2);
        polynomial2.addTerm(5,"x", 1);
        polynomial2.addTerm(2,"x", 0);
        polynomial2.addTerm(2,"x",3);
        polynomial2.addTerm(2,"x", 2);
        polynomial2.addTerm(5,"x", 1);
        polynomial2.addTerm(2,"x", 0);


        System.out.println(polynomial2);
        System.out.println(polynomial1.divideBy(polynomial2));
    }
}
