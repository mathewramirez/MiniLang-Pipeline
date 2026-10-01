package minilang.instr;

/**
 * Instrucción {@code FILTER}: conserva los valores que cumplen una comparación.
 *
 * @author Mathew Ramírez
 * @author kenneth Arce
 */
public class FilterInstr extends Instruccion {

    /** Comparador: {@code > < >= <= ==}. */
    private final String comparator;

    /** Valor contra el que se compara. */
    private final int value;

    /**
     * Crea una instrucción FILTER.
     *
     * @param line       número de línea en el archivo .mini
     * @param comparator comparador a aplicar
     * @param value      valor de comparación
     */
    public FilterInstr(int line, String comparator, int value) {
        super(line);
        this.comparator = comparator;
        this.value = value;
    }

    /**
     * {@inheritDoc}
     *
     * @return la línea con formato {@code FILTER|comparador|valor}
     */
    @Override
    public String toIR() {
        return "FILTER|" + comparator + "|" + value;
    }
}