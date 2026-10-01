package minilang;

/**
 * Excepción para errores léxicos y sintácticos del lenguaje MiniLang.
 * <p>
 * El mensaje incluye automáticamente el número de línea donde ocurrió el error.
 *
 * @author Mathew Ramírez
 * @author kenneth Arce
 */
public class MiniLangException extends Exception {

    /** Línea del archivo .mini donde se detectó el error. */
    private final int line;

    /**
     * Crea una excepción con un mensaje y la línea del error.
     *
     * @param messaje descripción del error
     * @param line   número de línea donde ocurrió
     */
    public MiniLangException(String messaje, int line) {
        super("Línea " + line + ": " + messaje);
        this.line = line;
    }

    /**
     * Devuelve la línea donde ocurrió el error.
     *
     * @return número de línea
     */
    public int getLine() {
        return line;
    }
}