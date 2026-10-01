package minilang.instr;

/**
 * Instrucción {@code MAP}: aplica una operación aritmética a cada valor.
 *
 * @author Mathew Ramírez
 * @author kenneth Arce
 */
public class MapInstr extends Instruccion {

    /** Operador aritmético: {@code + - *}. */
    private final String operator;

    /** Segundo operando de la operación. */
    private final int value;

    /**
     * Crea una instrucción MAP.
     *
     * @param line     número de línea en el archivo .mini
     * @param operator operador aritmético
     * @param value    segundo operando
     */
    public MapInstr(int line, String operator, int value) {
        super(line);
        this.operator = operator;
        this.value = value;
    }

    /**
     * {@inheritDoc}
     *
     * @return la línea con formato {@code MAP|operador|valor}
     */
    @Override
    public String toIR() {
        return "MAP|" + operator + "|" + value;
    }
}