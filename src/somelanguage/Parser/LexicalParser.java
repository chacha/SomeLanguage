package somelanguage.Parser;

import java.util.ArrayList;

/**
 *
 * @author tylercarter
 */
public class LexicalParser implements Parser{

    ArrayList<Token> tokens = new ArrayList<Token>();
    private ArrayList<Keyword> keywords = new ArrayList<Keyword>();

    public LexicalParser() {

        // Basic Keywords
        keywords.add(new Keyword("global", TokenType.GLOBAL_DECLARE));
        keywords.add(new Keyword("var", TokenType.LOCAL_DECLARE));
        keywords.add(new Keyword("=", TokenType.ASSIGNMENT));
        keywords.add(new Keyword(";", TokenType.END_STATEMENT));

        keywords.add(new Keyword("+", TokenType.ADD));
        keywords.add(new Keyword("-", TokenType.SUBTRACT));
        keywords.add(new Keyword("/", TokenType.DIVIDE));
        keywords.add(new Keyword("*", TokenType.MULTIPLY));

        keywords.add(new Keyword("\"", TokenType.QUOTE));
    }

    public ArrayList<Token> parse(String text) {
        String[] strings = text.split(" ");

        for(String string:strings){
            addToken(string);
        }

        return this.tokens;
    }

    private void addToken(String string) {
        ArrayList<Token> endBuffer = new ArrayList<Token>();

        // Check if a semicolon is at the end
        if(string.endsWith(";")){
            string = string.substring(0, string.length() - 1);
            endBuffer.add(new Token(TokenType.END_STATEMENT));
        }

        if(string.startsWith("\"")){
            string = string.substring(1);
            this.tokens.add(new Token(TokenType.QUOTE));
        }

        if(string.endsWith("\"")){
            string = string.substring(0, string.length() - 1);
            endBuffer.add(new Token(TokenType.QUOTE));
        }

        // Check that a string still exists
        if(string.equals("")){

            // Empty String means we're done here
            this.tokens.addAll(endBuffer);
            return;
        }

        // Check if it is a keyword
        if(isKeyword(string)){

            // Add Keyword Token
            this.tokens.add(convertKeyword(string));
            
        }
        else if(isInteger(string)){

            // Add Integer Token
            this.tokens.add(new Token(TokenType.INTEGER, string));

        }
        else{

            // If not a keyword or integer, is a string
            this.tokens.add(new Token(TokenType.STRING, string));
            
        }
        
        // Add end buffer
        this.tokens.addAll(endBuffer);
    }

    private Token convertKeyword(String string) {

        for(Keyword keyword:(Iterable<Keyword>)this.keywords){
            if(keyword.getKeyword().equals(string))
                return new Token(keyword.getTokenType(), string);
        }
        
        throw new IllegalArgumentException("Input is not a valid keyword.");

    }

    private boolean isKeyword(String string) {
        try{
            convertKeyword(string);
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    public boolean isInteger( String input )
    {
       try
       {
          Integer.parseInt( input );
          return true;
       }
       catch( Exception ex )
       {
          return false;
       }
    }

    

}
