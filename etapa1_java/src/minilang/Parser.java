package minilang;

import minilang.instr.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Analizador sintáctico de MiniLang (descenso recursivo).
 * <p>
 * Verifica que la secuencia de tokens cumpla la gramática y construye
 * la lista de objetos {@link Instruccion}. Cada regla de la gramática
 * corresponde a un método de esta clase.
 *
 * @author Mathew Ramírez
 * @author kenneth Arce
 */
public class Parser {

    /** Tokens producidos por el {@link Lexer}. */
    private final List<Token> tokens;

    /** Posición del token que se está analizando. */
    private int pos = 0;

    /**
     * Crea un analizador sintáctico para la lista de tokens indicada.
     *
     * @param tokens tokens del programa, terminados en {@link TokenType#EOF}
     */
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Analiza un programa completo según la regla:
     * <pre>
     * &lt;programa&gt; ::= &lt;data&gt; &lt;operacion&gt; { &lt;operacion&gt; } "PRINT"
     * </pre>
     *
     * @return la lista de instrucciones en el orden del programa
     * @throws MiniLangException si el programa no cumple la gramática
     */
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

    /**
     * Analiza la instrucción DATA según la regla:
     * <pre>
     * &lt;data&gt; ::= "DATA" &lt;numero&gt; { &lt;numero&gt; }
     * </pre>
     *
     * @return la instrucción DATA con sus valores
     * @throws MiniLangException si falta DATA o no tiene al menos un número
     */
    private DataInstr parseData() throws MiniLangException {
        Token dataToken = consume(TokenType.DATA);
        List<Integer> values = new ArrayList<>();
        while (actual().type() == TokenType.NUMBER) {
            Token numberToken = consume(TokenType.NUMBER);
            values.add(Integer.parseInt(numberToken.lexeme()));
        }
        if (values.isEmpty()) {
            throw new MiniLangException("DATA requiere al menos un número", dataToken.line());
        }
        return new DataInstr(dataToken.line(), values);
    }

    /**
     * Analiza una operación según la regla:
     * <pre>
     * &lt;operacion&gt; ::= &lt;filter&gt; | &lt;map&gt; | &lt;reduce&gt;
     * </pre>
     *
     * @return la instrucción FILTER, MAP o REDUCE correspondiente
     * @throws MiniLangException si el token actual no inicia una operación
     *         válida o sus parámetros son incorrectos
     */
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
                Token kw  = consume(TokenType.MAP);
                Token op  = consume(TokenType.ARITHMETHIC);
                Token num = consume(TokenType.NUMBER);
                return new MapInstr(kw.line(), op.lexeme(), Integer.parseInt(num.lexeme()));
            }
            case REDUCE -> {
                Token kw  = consume(TokenType.REDUCE);
                Token agg = consume(TokenType.AGGREGATOR);
                return new ReduceInstr(kw.line(), agg.lexeme());
            }
            default -> throw new MiniLangException(
                "se esperaba FILTER, MAP o REDUCE pero vino '" + t.lexeme() + "'", t.line());
        }
    }

    /**
     * Devuelve el token actual sin avanzar.
     *
     * @return el token en la posición actual
     */
    private Token actual() {
        return tokens.get(pos);
    }

    /**
     * Consume el token actual si es del tipo esperado y avanza la posición.
     *
     * @param expectedType tipo de token que exige la gramática en este punto
     * @return el token consumido
     * @throws MiniLangException si el token actual no es del tipo esperado
     *         (error sintáctico)
     */
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