package minilang;

/**
 * Categorías de tokens reconocidas por el lenguaje MiniLang.
 *
 * @author Mathew Ramírez
 * @author kenneth Arce
 */
public enum TokenType {
    /** Palabra clave {@code DATA}. */
    DATA,
    /** Palabra clave {@code FILTER}. */
    FILTER,
    /** Palabra clave {@code MAP}. */
    MAP,
    /** Palabra clave {@code REDUCE}. */
    REDUCE,
    /** Palabra clave {@code PRINT}. */
    PRINT,
    /** Comparadores: {@code > < >= <= ==}. */
    COMPARATOR,
    /** Operadores aritméticos: {@code + - *}. */
    ARITHMETHIC,
    /** Funciones de agregación: {@code SUM MAX MIN}. */
    AGGREGATOR,
    /** Entero no negativo. */
    NUMBER,
    /** Marca de fin de archivo. */
    EOF
}