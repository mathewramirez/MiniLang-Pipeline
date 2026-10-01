package minilang.instr;

/**
 * Instrucción {@code PRINT}: marca el final del programa.
 *
 * @author Mathew Ramírez
 * @author kenneth Arce
 */
public class PrintInstr extends Instruccion {

    /**
     * Crea una instrucción PRINT.
     *
     * @param line número de línea en el archivo .mini
     */
    public PrintInstr(int line) {
        super(line);
    }

    /**
     * {@inheritDoc}
     *
     * @return la línea {@code PRINT}
     */
    @Override
    public String toIR() {
        return "PRINT";
    }
}