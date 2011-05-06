package somelanguage.Interpreter;

import java.util.ArrayList;
import somelanguage.Variables.ComplexScope;
import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;
import somelanguage.Parser.Token.Tokens;
import somelanguage.Value.NullValue;
import somelanguage.Value.ReturnValue;
import somelanguage.Value.BooleanValue;
import somelanguage.Value.Value;
import somelanguage.Value.ValueType;

/**
 *
 * @author tylercarter
 */
public class Runner {

    public Value run(ArrayList<Token> tokens, ComplexScope scope) throws Exception{

        // Add a end statement to whatever we are working with
        tokens.add(new Token(TokenType.END_STATEMENT));

        // Create a scanner
        Scanner scanner = new Scanner(tokens);

        // Run
        while(scanner.hasNext()){

            // Get Line
            Scanner statement = scanner.getTokenToEndStatement();

            // Parse Line
            Value value = parseLine(statement, scope);

            if(value.getType() == ValueType.RETURN){
                return ((ReturnValue) value).getValue();
            }
            
        }

        // No return value returns null
        return new NullValue();


    }

    private Value parseLine(Scanner statement, ComplexScope fullScope) throws Exception{

        Token token = statement.next(false);

        // First Resolve Scope
        if(token.getTokenType() == TokenType.CLOSEBRACES 
                || token.getTokenType() == TokenType.OPENBRACES){

            if(token.getTokenType() == TokenType.CLOSEBRACES)
                fullScope.local.removeStack("Anonymous Scope");
            else
                fullScope.local.addStack("Anonymous Scope");

            // Scan past the bracket
            statement.next();
            token = statement.next(false);
            statement = statement.getTokenToEndStatement();
        }

        // Local Declaration
        if(token.getTokenType() == TokenType.LOCAL_DECLARE){

            // Add variable to scope
            String name = statement.next(2).getTokenValue().toString();
            declareVariable(name, fullScope, true);

            // Trim statement
            statement.next();
            statement = statement.getTokenToEndStatement();

            return evaluateOperation(statement, fullScope);

        }

        // Global Declaration
        else if(token.getTokenType() == TokenType.GLOBAL_DECLARE){

            // Add variable to scope
            String name = statement.next(2).getTokenValue().toString();
            declareVariable(name, fullScope, false);

            // Trim statement
            statement.next();
            statement = statement.getTokenToEndStatement();

            return  evaluateOperation(statement, fullScope);
        }

        // Return Value
        else if(token.getTokenType() == TokenType.RETURN){

            // Trim statement
            statement.next();
            statement = statement.getTokenToEndStatement();

            return new ReturnValue(evaluateOperation(statement, fullScope));
        }

        else if(token.getTokenType() == TokenType.IF){

            // Trim statement
            statement.next();
            statement = statement.getTokenToEndStatement();
            
            return evaluateConditional(statement, fullScope);

        }

        else {

            return evaluateOperation(statement, fullScope);

        }
        
    }

    private void declareVariable(String name, ComplexScope scope) throws Exception{
        declareVariable(name, scope, false);
    }

    private void declareVariable(String name, ComplexScope scope, boolean local) throws Exception{
        
        if(local == true){
            scope.getLocal().addVariable(name);
        }
        else{
            scope.getGlobal().addVariable(name);
        }
    }

    /*
     * Evaulating an operation is going to require taking from the assignment operator
     * to the end statement as a new scanner.
     *
     * Scanner should return a scanner with the next X tokens in it, and advance
     * the current scanner to the end of it
     */
    private Value evaluateOperation(Scanner scanner, ComplexScope scope) throws Exception {
        return evaluateOperation(scanner.getTokens(), scope);
    }

    private Value evaluateOperation(ArrayList<Token> tokens, ComplexScope scope) throws Exception {

        ExpressionEngine math = new ExpressionEngine();
        return math.evaluate(tokens, scope);
        
    }

    private Value evaluateConditional(Scanner statement, ComplexScope fullScope) throws Exception {

        ArrayList<Token> tokens = statement.getTokens();

        // Get Conditional
        ArrayList<Token> conditional = Tokens.sliceBody(tokens, TokenType.OPENBRACKET, 0);

        // Check if it evaluates to true
        Value value = evaluateOperation(conditional, fullScope);
        
        BooleanValue proceed = new BooleanValue("false");
        try{
            proceed = (BooleanValue) value;
        }catch(ClassCastException ex){
            System.out.println("Could not convert conditional to boolean.");
        }

        // Check whether to proceed with body
        if(proceed.getValue() == true){

            // Get Conditional
            ArrayList<Token> body = Tokens.sliceBody(tokens, TokenType.OPENBRACES, 0);

            // Make new function and run
            Function bodyOp = new Function(this, body, fullScope);
            Value v = bodyOp.run(new ArrayList<Value>(), fullScope);

            return v;

        }

        return new NullValue();
    }

}
