package minilang;
import minilang.instr.*;
import java.util.List;
import java.util.ArrayList;


public class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

public List<Instruccion> parseProgram() throws MiniLangException {
    List<Instruccion> program = new ArrayList<>();

    program.add(parseData());
    program.add(parseOperacion());

    while (actual().type() == TokenType.FILTER
        || actual().type() == TokenType.MAP
        || actual().type() == TokenType.REDUCE) {
        program.add(parseOperacion());
    }

    Token printToken = consume(TokenType.PRINT);
    program.add(new PrintInstr(printToken.line()));

    consume(TokenType.EOF);
    return program;
}

    private DataInstr parseData() throws MiniLangException {
        Token dataToken = consume(TokenType.DATA);
        List<Integer> values = new ArrayList<>();
        while (actual().type() == TokenType.NUMBER) {
            Token numberToken = consume(TokenType.NUMBER);
            values.add(Integer.parseInt(numberToken.lexeme()));
        }
        if (values.isEmpty()) {
            throw new MiniLangException("DATA requiere al menos un número ", dataToken.line());
        }
        return new DataInstr(dataToken.line(), values);
    }

private Instruccion parseOperacion() throws MiniLangException {
    Token t = actual();

    switch (t.type()) {
        case FILTER -> {
            Token kw   = consume(TokenType.FILTER);
            Token comp = consume(TokenType.COMPARATOR);
            Token num  = consume(TokenType.NUMBER);
            return new FilterInstr(kw.line(), comp.lexeme(), Integer.parseInt(num.lexeme()));
        }
        case MAP -> {
            Token kw   = consume(TokenType.MAP);
            Token op   = consume(TokenType.ARITHMETHIC);
            Token num  = consume(TokenType.NUMBER);
            return new MapInstr(kw.line(), op.lexeme(), Integer.parseInt(num.lexeme()));
        }
        case REDUCE -> {
            Token kw   = consume(TokenType.REDUCE);
            Token agg  = consume(TokenType.AGGREGATOR);
            return new ReduceInstr(kw.line(), agg.lexeme());
        }
        default -> throw new MiniLangException(
            "se esperaba FILTER, MAP o REDUCE pero vino '" + t.lexeme() + "'", t.line());
    }
}

    private Token actual() {
        return tokens.get(pos);
    }

private Token consume(TokenType expectedType) throws MiniLangException {
    Token token = actual();
    if (token.type() != expectedType) {
        String found = (token.type() == TokenType.EOF)
                ? "fin del archivo"
                : "'" + token.lexeme() + "'";
        throw new MiniLangException(
                "se esperaba " + expectedType + " pero vino " + found,
                token.line());
    }
    pos++;
    return token;
}

}
