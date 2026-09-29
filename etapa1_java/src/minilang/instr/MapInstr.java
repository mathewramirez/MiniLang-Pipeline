package minilang.instr;

public class MapInstr extends Instruccion {
    private final String operator;
    private final int value;

    public MapInstr(int line, String operator, int value) {
        super(line);
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String toIR() {
        return "MAP|" + operator + "|" + value;
    }
}