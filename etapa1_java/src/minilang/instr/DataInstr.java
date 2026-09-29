package minilang.instr;

import java.util.List;

public class DataInstr extends Instruccion {
    private final List<Integer> values;

    public DataInstr(int line, List<Integer> values) {
        super(line);
        this.values = values;
    }

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