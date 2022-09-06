package lexicon;

public class Token {

    public byte type;
    public String value;
    public int line, col;

    public Token(byte type, String value, int line, int col) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.col = col;

        if (type == IDENTIFIER) {
            for (byte k = BEGIN; k <= BOOLEAN; k++) {
                if (value.equals(Spellings[k])) {
                    this.type = k;
                    this.value = Spellings[k];
                    break;
                }
            }
        }

    }

    @Override
    public String toString() {
        return "[" + Spellings[type].toUpperCase() + "] => " + value + " LINHA: " + line + " COLUNA: " + col;
    }

    public final static byte IDENTIFIER = 0, 
            LITERAL_INTEGER = 1, 
            LITERAL_FLOAT = 2, 
            SUM = 3, 
            SUBTRACTION = 4, 
            MULTIPLICATION = 5,
            DIVISION = 6, 
            GREATER = 7, 
            GREATER_EQUAL = 8,
            LESS = 9, 
            LESS_EQUAL = 10,  
            EQUAL = 11, 
            DIFFERENT = 12, 
            LEFT_PARENTHESES = 13, 
            RIGHT_PARENTHESES = 14,
            LEFT_BRACKET = 15, 
            RIGHT_BRACKET = 16, 
            SEMICOLON = 17,
            BECOMES = 18, 
            COLON = 19,
            TILDE = 20, 
            DOT = 21, 
            COMMA = 22, 
            BEGIN = 23, 
            END = 24, 
            PROGRAM = 25,
            IF = 26, 
            THEN = 27, 
            ELSE = 28, 
            VAR = 29, 
            WHILE = 30, 
            DO = 31,
            OR = 32, 
            AND = 33, 
            ARRAY = 34, 
            OF = 35, 
            INTEGER = 36,
            REAL = 37, 
            TRUE = 38, 
            FALSE = 39, 
            BOOLEAN = 40, 
            EOF = 41, 
            ERROR = 42;

    public final static String[] Spellings = {
        "<IDENTIFIER>", 
        "<int-lit>", 
        "<float-lit>", 
        "+", 
        "-", 
        "*", 
        "/", 
        ">", 
        ">=", 
        "<", 
        "<=",  
        "=", 
        "<>",  
        "(", 
        ")",
        "[", 
        "]",
        ";", 
        ":=", 
        ":", 
        "~", 
        ".", 
        ",", 
        "begin", 
        "end",  
        "program", 
        "if",
        "then", 
        "else", 
        "var", 
        "while", 
        "do", 
        "or", 
        "and",
        "array",
        "of", 
        "integer", 
        "real",
        "true",
        "false", 
        "boolean", 
        "<EOF>", 
        "<ERRO>"
    };

}
