package minilang.instr;

public class ReduceInstr extends Instruccion {
    private final String aggregator;

    public ReduceInstr(int line, String aggregator) {
        super(line);
        this.aggregator = aggregator;
    }

    @Override
    public String toIR() {
        return "REDUCE|" + aggregator;
    }
}