package minilang.instr;

/**
 * Instrucción {@code REDUCE}: reduce la lista a un solo valor.
 *
 * @author Mathew Ramírez
 * @author kenneth Arce
 */
public class ReduceInstr extends Instruccion {

    /** Función de agregación: {@code SUM MAX MIN}. */
    private final String aggregator;

    /**
     * Crea una instrucción REDUCE.
     *
     * @param line       número de línea en el archivo .mini
     * @param aggregator función de agregación
     */
    public ReduceInstr(int line, String aggregator) {
        super(line);
        this.aggregator = aggregator;
    }

    /**
     * {@inheritDoc}
     *
     * @return la línea con formato {@code REDUCE|agregador}
     */
    @Override
    public String toIR() {
        return "REDUCE|" + aggregator;
    }
}