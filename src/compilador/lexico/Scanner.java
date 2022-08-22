package compilador.lexico;

import compilador.exceptions.LexicalException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Scanner {

    final private BufferedReader file;
    private char currentChar;
    private int line, countCol, column;
    private byte currentKind;
    private StringBuffer currentValue;

    public Scanner(String fileName) throws Exception {
        BufferedReader SourceFile = new BufferedReader(new FileReader(fileName));
        this.file = SourceFile;
        currentChar = (char) SourceFile.read();
        countCol = 0;
        line = 1;
    }

    public Token scan() throws Exception {
        currentValue = new StringBuffer("");
        while (currentChar == '#' || currentChar == ' ' || currentChar == '\n' || currentChar == '\r' || currentChar == '\t') {
            scanSeparator();
        }
        currentValue.delete(0, currentValue.length());
        currentKind = scanToken();
        return new Token(currentKind, currentValue.toString(), line, column);
    }

    private byte scanToken() throws Exception {

        // <letra>(<letra> | <digito>)*
        if (isLetter(currentChar)) {
            takeIt();           
            column = countCol;
            while (isLetter(currentChar) || isDigit(currentChar)) {
                takeIt();
            }
            return Token.ID;
        }

        // <digito><digito>*
        if (isDigit(currentChar)) {
            takeIt();           
            column = countCol;
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
                    return Token.FLOAT_LIT;
                } else {
                    return Token.FLOAT_LIT;
                }
            }
            return Token.INT_LIT;
        }

        // Operadores Aritméticos        
        if (currentChar == '+') {
            takeIt();           
            column = countCol;
            return Token.SUM;
        }
        if (currentChar == '-') {
            takeIt();           
            column = countCol;
            return Token.SUB;
        }
        if (currentChar == '*') {
            takeIt();           
            column = countCol;
            return Token.MULT;
        }
        if (currentChar == '/') {
            takeIt();           
            column = countCol;
            return Token.DIV;
        }

        //Operadores Relacionais        
        if (currentChar == '>') {
            takeIt();           
            column = countCol;
            if (currentChar == '=') {
                takeIt();
                return Token.GREATER_EQUAL;
            } else {
                return Token.GREATER;
            }
        }
        if (currentChar == '<') {
            takeIt();           
            column = countCol;
            if (currentChar == '=') {
                takeIt();
                return Token.LESS_EQUAL;
            } else if (currentChar == '>') {
                takeIt();
                return Token.DIFF;
            } else {
                return Token.LESS;
            }
        }
        if (currentChar == '=') {
            takeIt();           
            column = countCol;
            return Token.EQUAL;
        }

        // Graphics
        if (currentChar == '[') {
            takeIt();           
            column = countCol;
            return Token.LBRACKET;
        }
        if (currentChar == ']') {
            takeIt();           
            column = countCol;
            return Token.RBRACKET;
        }
        if (currentChar == ';') {
            takeIt();           
            column = countCol;
            return Token.SEMICOLON;
        }
        if (currentChar == ':') {
            takeIt();           
            column = countCol;
            if (currentChar == '=') {
                takeIt();
                return Token.BECOMES;
            } else {
                return Token.COLON;
            }
        }
        if (currentChar == '(') {
            takeIt();           
            column = countCol;
            return Token.LPAREN;
        }
        if (currentChar == ')') {
            takeIt();           
            column = countCol;
            return Token.RPAREN;
        }
        if (currentChar == '~') {
            takeIt();           
            column = countCol;
            return Token.TILDE;
        }
        if (currentChar == '.') {
            takeIt();           
            column = countCol;
            if (isDigit(currentChar)) {
                takeIt();
                while (isDigit(currentChar)) {
                    takeIt();
                }
                return Token.FLOAT_LIT;
            } else {
                return Token.DOT;
            }
        }
        if (currentChar == ',') {
            takeIt();           
            column = countCol;
            return Token.COMMA;
        }

        //EOF SYMBOL
        if (currentChar == 65535) {
            takeIt();           
            column = countCol;
            return Token.EOF;
        }

        takeIt();
        return Token.ERROR;
    }

    private void scanSeparator() throws Exception {
        switch (currentChar) {
            case '#': {
                takeIt();
                column = countCol;
                while (isGraphic(currentChar)) {
                    takeIt();
                }
                take('\n');
                countCol=-1;
            }
            break;
            case '\n':
                line++;
                countCol=-1;
            case '\r':
            case '\t':
            case ' ':
                takeIt();
                break;
        }
    }
    
    private void take(char expectedChar) throws Exception{
        if(currentChar == expectedChar){
            currentValue.append(currentChar);
            currentChar = (char)file.read();
        } else {
            throw new LexicalException("Unrecognized SYMBOL");
        }
    }

    private void takeIt() throws Exception {
        currentValue.append(currentChar);
        currentChar = (char) file.read();        
        countCol++;    
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
    
    public ArrayList<Token> ler() throws Exception {
        ArrayList<Token> lista = new ArrayList<>();
        Token tk;
        do {
            tk = scan();
            lista.add(tk);
        } while (tk.kind != Token.EOF);
        return lista;
    }
}
