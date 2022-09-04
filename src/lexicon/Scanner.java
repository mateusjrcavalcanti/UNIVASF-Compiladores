package lexicon;

import exceptions.SyntacticException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Scanner {

    final private BufferedReader file;
    private char currentChar;
    private int line;
    private int col, aux;
    private byte currentKind;
    private StringBuffer currentValue;

    public Scanner(String fileName) throws Exception {
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        this.file = file;
        currentChar = (char) file.read();
        col = 0;
        line = 1;
    }

    public Token scan() throws Exception {
        currentValue = new StringBuffer("");
        while (currentChar == '#' || currentChar == ' ' || currentChar == '\n' || currentChar == 13 || currentChar == '\t' || currentChar == 10) {
            scanSeparator();
        }
        currentValue.delete(0, currentValue.length());
        currentKind = scanToken();

        if (currentKind == Token.ERROR) {
            throw new SyntacticException(currentKind + " valor: " + currentValue.toString() + "linha: " + line + ", coluna: " + aux);
        } else {
            return new Token(currentKind, currentValue.toString(), line, aux);
        }
    }

    private void take(char expectedChar) throws Exception {
        if (currentChar == expectedChar) {
            currentValue.append(currentChar);
            currentChar = (char) file.read();
        }
    }

    private void takeIt() throws Exception {
        currentValue.append(currentChar);
        currentChar = (char) file.read();
        col++;
    }

    private byte scanToken() throws Exception {

        if (isLetter(currentChar)) {
            takeIt();
            aux = col;
            while (isLetter(currentChar) || isDigit(currentChar)) {
                takeIt();
            }
            return Token.IDENTIFIER;
        }

        if (isDigit(currentChar)) {
            takeIt();
            aux = col;
            while (isDigit(currentChar)) {
                takeIt();
            }
            if (currentChar == '.') {
                takeIt();
                if (isDigit(currentChar)) {
                    takeIt();
                    while (isDigit(currentChar)) {
                        takeIt();
                    }
                    return Token.LITERAL_FLOAT;
                } else {
                    return Token.LITERAL_FLOAT;
                }
            }
            return Token.LITERAL_INTEGER;
        }

        if (currentChar == '+') {
            takeIt();
            aux = col;
            return Token.SUM;
        }

        if (currentChar == '-') {
            takeIt();
            aux = col;
            return Token.SUBTRACTION;
        }

        if (currentChar == '*') {
            takeIt();
            aux = col;
            return Token.MULTIPLICATION;
        }

        if (currentChar == '/') {
            takeIt();
            aux = col;
            return Token.DIVISION;
        }

        if (currentChar == '>') {
            takeIt();
            aux = col;
            if (currentChar == '=') {
                takeIt();
                return Token.GREATER_EQUAL;
            } else {
                return Token.GREATER;
            }
        }

        if (currentChar == '<') {
            takeIt();
            aux = col;
            if (currentChar == '=') {
                takeIt();
                return Token.LESS_EQUAL;
            } else if (currentChar == '>') {
                takeIt();
                return Token.DIFFERENT;
            } else {
                return Token.LESS;

            }
        }

        if (currentChar == '[') {
            takeIt();
            aux = col;
            return Token.LEFT_BRACKET;
        }

        if (currentChar == ']') {
            takeIt();
            aux = col;
            return Token.RIGHT_BRACKET;
        }

        if (currentChar == ';') {
            takeIt();
            aux = col;
            return Token.SEMICOLON;
        }

        if (currentChar == ':') {
            takeIt();
            aux = col;
            if (currentChar == '=') {
                takeIt();
                return Token.BECOMES;
            } else {
                return Token.COLON;
            }
        }

        if (currentChar == '(') {
            takeIt();
            aux = col;
            return Token.LEFT_PARENTHESES;
        }

        if (currentChar == ')') {
            takeIt();
            aux = col;
            return Token.RIGHT_PARENTHESES;
        }

        if (currentChar == '~') {
            takeIt();
            aux = col;
            return Token.TILDE;
        }

        if (currentChar == '.') {
            takeIt();
            aux = col;
            if (isDigit(currentChar)) {
                takeIt();
                while (isDigit(currentChar)) {
                    takeIt();
                }
                return Token.LITERAL_FLOAT;
            } else {
                return Token.DOT;
            }
        }

        if (currentChar == ',') {
            takeIt();
            aux = col;
            return Token.COMMA;
        }

        if (currentChar == 65535) {
            takeIt();
            aux = col;
            return Token.EOF;
        }

        if (currentChar == '=') {
            takeIt();
            aux = col;
            return Token.EQUAL;
        }

        takeIt();
        return Token.ERROR;
    }

    private void scanSeparator() throws Exception {
        switch (currentChar) {

            case '#': {
                takeIt();
                aux = col;
                while (isGraphic(currentChar)) {
                    takeIt();

                }
                take('\n');
                col = -1;
            }
            break;
            case '\n':
                line++;
                col = -1;
            case '\r':
            case '\t':
            case ' ':
                takeIt();
                break;
        }
    }

    public ArrayList<Token> ler() throws Exception {
        ArrayList<Token> lista = new ArrayList<>();
        Token tk;
        do {
            tk = scan();
            lista.add(tk);
        } while (tk.type != Token.EOF);
        return lista;
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isGraphic(char c) {
        return (c >= 32 && c <= 125) || (c == 'ç');
    }
}
