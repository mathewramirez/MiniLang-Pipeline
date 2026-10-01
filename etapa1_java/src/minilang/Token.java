package minilang;

/**
 * Unidad léxica producida por el {@link Lexer}.
 * <p>
 * Guarda el tipo del token, el texto original y la línea del archivo
 * fuente donde apareció, para poder reportar errores con número de línea.
 *
 * @param type   categoría del token
 * @param lexeme texto exacto leído del archivo .mini
 * @param line   número de línea (empezando en 1) donde aparece el token
 * @author Mathew Ramírez
 * @author kenneth Arce
 */
public record Token(TokenType type, String lexeme, int line) {}