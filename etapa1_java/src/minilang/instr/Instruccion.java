package minilang.instr;

/**
 * Instrucción abstracta del lenguaje MiniLang.
 * <p>
 * Es la raíz de la jerarquía de instrucciones. Cada subclase sobrescribe
 * {@link #toIR()} para producir su propia línea de representación
 * intermedia; así el resto del programa puede tratarlas de forma
 * polimórfica sin conocer su tipo concreto.
 *
 * @author Mathew Ramírez
 * @author kenneth Arce
 */
public abstract class Instruccion {

    /** Línea del archivo .mini donde aparece la instrucción. */
    protected final int line;

    /**
     * Inicializa la instrucción con su número de línea.
     *
     * @param line número de línea en el archivo .mini
     */
    protected Instruccion(int line) {
        this.line = line;
    }

    /**
     * Convierte la instrucción a su línea de representación intermedia.
     *
     * @return la línea IR, por ejemplo {@code FILTER|>|5}
     */
    public abstract String toIR();
}