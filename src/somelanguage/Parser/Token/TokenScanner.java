package somelanguage.Parser.Token;

import java.util.ArrayList;

/**
 * Provides an iterable interface for dealing with arrays of tokens
 * @author tylercarter
 */
public class TokenScanner {
    
    private ArrayList<Token> tokens;
    private int counter = -1;
    
    public TokenScanner(ArrayList<Token> tokens){
        this.tokens = tokens;
    }

    public Token get(int index){
        return this.tokens.get(index);
    }

    public Token getCurrent(){
        if(counter == -1)
            return next();
        
        return get(counter);
    }

    public Token next(){
        return next(true);
    }

    public Token next(boolean advanceCounter){
        if(!hasNext())
            return new Token(TokenType.UNDEFINED);

        if(advanceCounter == true){
            counter++;
            return getCurrent();
        }else{
            return get(counter + 1);
        }

    }

    public Token next(int advance){
        if((counter + advance) >= this.tokens.size())
            return new Token(TokenType.UNDEFINED);

        return get(counter + advance);
    }

    public ArrayList<Token> getTokens(){
        return this.tokens;
    }

    public TokenScanner getTokens(int num){

        ArrayList<Token> subset = new ArrayList<Token>();

        for(int i = counter + 1; i <= counter + num; i++){
            if(i >= this.tokens.size())
                break;

            subset.add(get(i));
        }

        counter = counter + num + 1;
        return new TokenScanner(subset);
    }

    public TokenScanner getTokenToEndStatement(){

        int count = counter + 1;
        int start = counter + 1;

        int scopeLevel = 0;
        while(count < this.tokens.size()){
            if(this.tokens.get(count).getTokenType() == TokenType.OPENBRACES){
                scopeLevel += 1;
            }

            if(this.tokens.get(count).getTokenType() == TokenType.CLOSEBRACES){
                scopeLevel -= 1;
            }

            if(this.tokens.get(count).getTokenType() == TokenType.END_STATEMENT && scopeLevel == 0){
                break;
            }
            count++;
        }
        return getTokens(count - start);
    }

    public boolean hasNext() {
        if((counter + 1) < this.tokens.size()){
            return true;
        }else{
            return false;
        }
    }

    public void reset(){
        counter = -1;
    }

}
