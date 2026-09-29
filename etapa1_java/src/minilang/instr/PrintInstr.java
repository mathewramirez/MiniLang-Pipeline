package minilang.instr;

public class PrintInstr extends Instruccion {

    public PrintInstr(int line) {
        super(line);
    }

    @Override
    public String toIR() {
        return "PRINT";
    }
}