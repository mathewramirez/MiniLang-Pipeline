package minilang.instr;

import java.util.List;

/**
 * Instrucción {@code DATA}: define la lista inicial de números.
 *
 * @author Mathew Ramírez
 * @author kenneth Arce
 */
public class DataInstr extends Instruccion {

    /** Valores enteros declarados después de DATA. */
    private final List<Integer> values;

    /**
     * Crea una instrucción DATA.
     *
     * @param line   número de línea en el archivo .mini
     * @param values lista de enteros no negativos
     */
    public DataInstr(int line, List<Integer> values) {
        super(line);
        this.values = values;
    }

    /**
     * {@inheritDoc}
     *
     * @return la línea con formato {@code DATA|n1,n2,...}
     */
    @Override
    public String toIR() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(values.get(i));
        }
        return "DATA|" + sb;
    }
}