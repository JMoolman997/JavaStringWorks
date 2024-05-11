package src;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.Map;
import java.util.Iterator;
import java.util.function.BiFunction;

public interface ArithmeticEvaluator {
    
    class OperatorMap {

        Map<String, BiFunction<Double, Double, Double>> operators = Map.of(
            "+", (a, b) -> a + b,  // Addition
            "-", (a, b) -> a - b,  // Subtraction
            "*", (a, b) -> a * b,  // Multiplication
            "/", (a, b) -> {
                if (b == 0) {
                    throw new ArithmeticException("Division by zero.");
                }
                return a / b;        // Division
            }
        );

    }

    <T> T evaluate(String expression);
}
class DoubleArithmeticEvaluator implements ArithmeticEvaluator {
    
    Stack<String> ops = new Stack<>();
    Stack<Double> vals = new Stack<>();
    OperatorMap operatorMap = new OperatorMap();

    @Override
    public Double evaluate(String expression) {
        Stream<String> tokensStream = tokenizeExpression(expression);
        Iterator<String> tokens = tokensStream.iterator();

        while (tokens.hasNext()) {
            String token = tokens.next();
            switch (token) {
                case "(":
                    break; // Do nothing for "("
                case "+":
                case "-":
                case "*":
                case "/":
                    ops.push(token);
                    break;
                case ")":
                    performOperation();
                    break;
                default:
                    vals.push(Double.parseDouble(token)); // Push number onto the value stack
                    break;
            }
        }

        return vals.pop(); // Return the final result from the value stack
    }

    private void performOperation() {
        if (ops.isEmpty() || vals.size() < 2) {
            return; // Not enough operands or operators
        }

        String op = ops.pop();
        double v2 = vals.pop();
        double v1 = vals.pop();
        BiFunction<Double, Double, Double> operation = operatorMap.operators.get(op);
        double result = operation.apply(v1, v2);

        vals.push(result); // Push the result back to the stack
    }

    private Stream<String> tokenizeExpression(String expression) {
        String regex = "\\d+\\.?\\d*|[-+/*()]"; // Match numbers and operators
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);

        List<String> tokens = new ArrayList<>();
        while (matcher.find()) {
            tokens.add(matcher.group());
        }

        return tokens.stream();
    }
    
    public static void main(String[] args) {
        // Check that an arithmetic expression is provided as an argument
        if (args.length == 0) {
            System.err.println("Usage: java DoubleArithmeticEvaluatorApp \"<arithmetic expression>\"");
            System.exit(1);
        }

        // Combine the arguments into a single expression string
        String expression = String.join(" ", args);

        // Instantiate the evaluator and calculate the result
        DoubleArithmeticEvaluator evaluator = new DoubleArithmeticEvaluator();
        try {
            Double result = evaluator.evaluate(expression);
            System.out.printf("Result of '%s' is: %f%n", expression, result);
        } catch (ArithmeticException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }    
}    
