package minilang.instr;

public abstract class Instruccion {
    protected final int line;

    protected Instruccion(int line) {
        this.line = line;
    }

    public abstract String toIR();
}