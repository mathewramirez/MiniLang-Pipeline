package minilang;

    public class MiniLangException extends Exception {
    private final int line;

    public MiniLangException(String message, int line) {
        super("Línea " + line + ": " + message);
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}
