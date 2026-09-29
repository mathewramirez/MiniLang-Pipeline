package minilang;

import minilang.instr.Instruccion;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
    String input = args.length > 0 ? args[0] : "casos/programa.mini";
        String output  = args.length > 1 ? args[1] : "salida/programa.ir";
        try {
            List<String> line = Files.readAllLines(Path.of(input));
            List<Token> tokens = new Lexer(line).tokenize();
            List<Instruccion> prog = new Parser(tokens).parseProgram();

            List<String> irLines = new ArrayList<>();
            for (Instruccion instr : prog) {
                irLines.add(instr.toIR());
            }
            Files.createDirectories(Path.of(output).getParent());
            Files.write(Path.of(output), irLines);


            System.out.println("OK: IR generado en " + output);
        } catch (MiniLangException e) {
            System.err.println("ERROR " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("ERROR E/S: " + e.getMessage());
            System.exit(2);
        }
    }
}