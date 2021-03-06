package somelanguage.Value;

import somelanguage.Parser.Token.Token;
import somelanguage.Parser.Token.TokenType;

/**
 *
 * @author tylercarter
 */
public class StringValue extends Value{
    private final String value;

    public StringValue(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    @Override
    public ValueType getType() {
        return ValueType.STRING;
    }

    @Override
    public String toString(){
        return this.value;
    }

    @Override
    public Token toToken() {
        return new Token(TokenType.STRING, this);
    }

}
