package minilang;

import minilang.instr.Instruccion;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Punto de entrada de la etapa Java del pipeline MiniLang.
 * <p>
 * Lee un archivo {@code .mini}, lo analiza léxica y sintácticamente, y
 * genera {@code programa.ir} solo si el programa es válido.
 * <p>
 * Códigos de salida: {@code 0} éxito, {@code 1} error del lenguaje,
 * {@code 2} error de entrada/salida.
 *
 * @author Mathew Ramírez
 * @author kenneth Arce
 */

public class Main {

    /**
     * Ejecuta la etapa Java.
     *
     * @param args {@code args[0]}: archivo de entrada (por defecto
     *             {@code casos/programa.mini}); {@code args[1]}: archivo de
     *             salida (por defecto {@code salida/programa.ir})
     */
    public static void main(String[] args) {
        String input  = args.length > 0 ? args[0] : "casos/programa.mini";
        String output = args.length > 1 ? args[1] : "salida/programa.ir";
        try {
            List<String> lines = Files.readAllLines(Path.of(input));
            List<Token> tokens = new Lexer(lines).tokenize();
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