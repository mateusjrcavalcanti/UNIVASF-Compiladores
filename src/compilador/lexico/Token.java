package compilador.lexico;

public class Token {

    public byte kind;
    public String value;
    public int line;
    public int col;

    public Token(byte kind, String value, int line, int col) {
        this.kind = kind;
        this.value = value;
        this.line = line;
        this.col = col;

        if (kind == ID) {
            for (byte k = BEGIN; k <= BOOLEAN; k++) {
                if (value.equals(SPELLINGS[k])) {
                    this.kind = k;
                    this.value = SPELLINGS[k];
                    break;
                }
            }
        }

    }
    
    @Override
    public String toString() {
        return "Token [type: " + SPELLINGS[kind] + " , text: " + value + " , line: " + line + " , column: " + col + "]";
    }

    public final static byte ID = 0,
            INT_LIT = 1,
            FLOAT_LIT = 2,
            SUM = 3,
            SUB = 4,
            MULT = 5,
            DIV = 6,
            GREATER = 7,
            GREATER_EQUAL = 8,
            LESS_EQUAL = 9,
            DIFF = 10,
            LESS = 11,
            LBRACKET = 12,
            RBRACKET = 13,
            SEMICOLON = 14,
            BECOMES = 15,
            COLON = 16,
            LPAREN = 17,
            RPAREN = 18,
            TILDE = 19,
            DOT = 20,
            COMMA = 21,
            BEGIN = 22,
            END = 23,
            IF = 24,
            THEN = 25,
            ELSE = 26,
            VAR = 27,
            WHILE = 28,
            DO = 29,
            OR = 30,
            AND = 31,
            EQUAL = 32,
            PROGRAM = 33,
            ARRAY = 34,
            OF = 35,
            INTEGER = 36,
            REAL = 37,
            TRUE = 38,
            FALSE = 39,
            BOOLEAN = 40,
            EOF = 41,
            ERROR = 42;

    public final static String[] SPELLINGS = {
        "<id>", 
        "<int-lit>", 
        "<float-lit>", 
        "+", 
        "-", 
        "*", 
        "/", 
        ">",
        ">=",
        "<=", 
        "<>", 
        "<",
        "[", 
        "]", 
        ";", 
        ":=", 
        ":", 
        "(", 
        ")", 
        "~", 
        ".", 
        ",",
        "begin",
        "end",
        "if",
        "then",
        "else",
        "var",
        "while",
        "do",
        "or",
        "and",
        "=",
        "program",
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
