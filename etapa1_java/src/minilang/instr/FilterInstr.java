package minilang.instr;

public class FilterInstr extends Instruccion {
    private final String comparator;
    private final int value;

    public FilterInstr(int line, String comparator, int value) {
        super(line);
        this.comparator = comparator;
        this.value = value;
    }

    @Override
    public String toIR() {
        return "FILTER|" + comparator + "|" + value;
    }
}