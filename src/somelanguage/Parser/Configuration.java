package somelanguage.Parser;

import java.util.ArrayList;
import somelanguage.Parser.Token.TokenType;

/**
 *
 * @author tylercarter
 */
public class Configuration {

    private ArrayList<Keyword> keywords = new ArrayList<Keyword>();
    private ArrayList<Symbol> symbols = new ArrayList<Symbol>();

    public void addKeyword(Keyword keyword){
        this.keywords.add(keyword);
    }

    public void addKeyword(String search, TokenType token){
        Keyword keyword = new Keyword(search, token, false);
        this.keywords.add(keyword);
    }
    
    public void addKeyword(String search, TokenType token, boolean breaking){
        Keyword keyword = new Keyword(search, token, breaking);
        this.keywords.add(keyword);
    }

    public ArrayList<Keyword> getKeywords(){
        return keywords;
    }

    public void addSymbol(Symbol symbol){
        this.symbols.add(symbol);
    }

    public void addSymbol(String search, TokenType token){
        Symbol symbol = new Symbol(search, token);
        this.symbols.add(symbol);
    }

    public ArrayList<Symbol> getSymbols(){
        return symbols;
    }

}
