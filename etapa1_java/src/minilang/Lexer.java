package minilang;

import java.util.ArrayList;
import java.util.List;

/**
 * Analizador léxico de MiniLang.
 * <p>
 * Recorre las líneas del programa fuente, separa cada palabra y la
 * clasifica en un {@link TokenType}. Si encuentra un símbolo que no
 * pertenece al lenguaje, lanza un error léxico con el número de línea.
 *
 * @author Mathew Ramírez
 * @author kenneth Arce
 */
public class Lexer {

    /** Líneas del archivo .mini. */
    private final List<String> lines;

    /**
     * Crea un analizador léxico para las líneas indicadas.
     *
     * @param lines contenido del archivo .mini, una entrada por línea
     */
    public Lexer(List<String> lines) {
        this.lines = lines;
    }

    /**
     * Convierte el programa fuente en una lista de tokens.
     * <p>
     * Ignora las líneas vacías y agrega un token {@link TokenType#EOF}
     * al final para marcar el fin del archivo.
     *
     * @return la lista de tokens en el orden en que aparecen
     * @throws MiniLangException si alguna palabra no es un símbolo válido
     *         del lenguaje (error léxico)
     */
    public List<Token> tokenize() throws MiniLangException {
        List<Token> tokens = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] words = line.split("\\s+");

            for (String word : words) {
                TokenType type = switch (word) {
                    case "DATA" -> TokenType.DATA;
                    case "FILTER" -> TokenType.FILTER;
                    case "MAP" -> TokenType.MAP;
                    case "REDUCE" -> TokenType.REDUCE;
                    case "PRINT" -> TokenType.PRINT;
                    case ">", "<", ">=", "<=", "==" -> TokenType.COMPARATOR;
                    case "+", "-", "*" -> TokenType.ARITHMETHIC;
                    case "SUM", "MAX", "MIN" -> TokenType.AGGREGATOR;
                    default -> {
                        if (word.matches("\\d+")) yield TokenType.NUMBER;
                        throw new MiniLangException("símbolo desconocido '" + word + "'", i + 1);
                    }
                };
                tokens.add(new Token(type, word, i + 1));
            }
        }

        tokens.add(new Token(TokenType.EOF, "", Math.max(1, lines.size())));
        return tokens;
    }
}