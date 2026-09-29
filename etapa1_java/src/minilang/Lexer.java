package minilang;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
 private final List<String> lines;

 public Lexer(List<String> lines) {
 this.lines = lines;
 }

 public List<Token> tokenize () throws MiniLangException {

    List<Token> tokens = new ArrayList<>();

  for(int i = 0; i < lines.size(); i++) {
   String line = lines.get(i).trim();
   if (line.isEmpty()) {
    continue;
   }

    String[] words = line.split("\\s+");

    for (String word : words) {
  TokenType type  = switch (word) {
    case "DATA" -> TokenType.DATA;
    case "FILTER" -> TokenType.FILTER;
    case "MAP" -> TokenType.MAP;
    case "REDUCE" -> TokenType.REDUCE;
    case "PRINT" -> TokenType.PRINT;
    case ">", "<", ">=", "<=", "==" -> TokenType.COMPARATOR;
    case "+", "-", "*" -> TokenType.ARITHMETHIC;
    case "SUM", "MAX", "MIN" -> TokenType.AGGREGATOR;
    default -> {
    if (word.matches("\\d+")) yield TokenType.NUMBER;
    throw new MiniLangException("símbolo desconocido '" + word + "'", i + 1);
    }
   };
tokens.add(new Token(type, word, i + 1));
  } 
}

  tokens.add(new Token(TokenType.EOF, "", Math.max(1, lines.size())));
  return tokens;
 }

}
