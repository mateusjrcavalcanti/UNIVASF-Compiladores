package compilador.sintatico;

import compilador.abstractSyntaxTree.Atribuicao;
import compilador.abstractSyntaxTree.BoolLit;
import compilador.abstractSyntaxTree.ComandoComposto;
import compilador.abstractSyntaxTree.Corpo;
import compilador.abstractSyntaxTree.Comando;
import compilador.abstractSyntaxTree.Condicional;
import compilador.abstractSyntaxTree.DeclaracaoDeVariavel;
import compilador.abstractSyntaxTree.Declaracoes;
import compilador.abstractSyntaxTree.Variavel;
import compilador.abstractSyntaxTree.Expressao;
import compilador.abstractSyntaxTree.ExpressaoSimples;
import compilador.abstractSyntaxTree.Fator;
import compilador.abstractSyntaxTree.Iterativo;
import compilador.abstractSyntaxTree.ListaDeComandos;
import compilador.abstractSyntaxTree.ListaDeIds;
import compilador.abstractSyntaxTree.Literal;
import compilador.abstractSyntaxTree.Programa;
import compilador.abstractSyntaxTree.Seletor;
import compilador.abstractSyntaxTree.Termo;
import compilador.abstractSyntaxTree.Tipo;
import compilador.abstractSyntaxTree.TipoAgregado;
import compilador.abstractSyntaxTree.TipoSimples;
import compilador.exceptions.SyntaxException;
import compilador.lexico.Scanner;
import compilador.lexico.Token;

public class Parser {

    private Token currentToken;
    private Token lastToken;
    private Scanner scanner;

    public Parser() {
    }
    
    public Programa parse(String fileName) throws Exception {
        Programa program;
        scanner = new Scanner(fileName);
        currentToken = this.scanner.scan();        
        program = parsePrograma();
        
        System.out.println("  > Análise Sintática concluída;");
        
        return program;
    }

    private void accept(byte expectedKind) throws Exception {
        if (currentToken.kind == expectedKind) {
            lastToken = currentToken;
            currentToken = scanner.scan();
        } else {
            throw new SyntaxException("TOKEN INESPERADO: Esperava encontrar: '" + Token.SPELLINGS[expectedKind] + "', Linha: " + lastToken.line + ", Coluna: " + lastToken.col);
        }
    }

    private void acceptIt() throws Exception {
        currentToken = scanner.scan();
    }
    
    private Atribuicao parseAtribuicao() throws Exception {
        // <Variavel> := <Expressao>
        Atribuicao becomes = new Atribuicao();
        becomes.variable = parseVariavel();
        accept(Token.BECOMES);
        becomes.expression = parseExpressao();
        return becomes;
    }

    private BoolLit parseBoolLit() throws Exception {
        // Bool-Lit == True | False
        BoolLit logic = new BoolLit();
        switch (currentToken.kind) {
            case Token.TRUE:
            case Token.FALSE:
                logic.name = currentToken;
                acceptIt();
                break;
            default:
                throw new SyntaxException("BOOLEANO INVÁLIDO: Atribuição Lógica deve conter 'true' ou 'false', Linha: " + lastToken.line + ", Coluna: " + lastToken.col);
        }
        return logic;
    }

    private Comando parseComando() throws Exception {
        // <Atribuiçao> | <Condicional> | <Iterativo> | <Comando-Composto>
        Comando command;
        switch (currentToken.kind) {

            // <Atribuição>
            case Token.ID:
                command = parseAtribuicao();
                break;

            // <Condicional>
            case Token.IF:
                command = parseCondicional();
                break;

            // <Iterativo>
            case Token.WHILE:
                command = parseIterativo();
                break;
        
            // <Comando-Composto>
            case Token.BEGIN:
                command = parseComandoComposto();
                break;

            default:
                command = null;
                throw new SyntaxException("COMANDO INVÁLIDO: Comando deve ser do tipo ATRIBUIÇAO (ID), CONDICIONAL (IF), ITERATIVO (WHILE) ou COMANDO COMPOSTO (BEGIN), Linha: " + lastToken.line + ", Coluna: " + lastToken.col);
        }
        return command;
    }

    private ComandoComposto parseComandoComposto() throws Exception {
        // begin <Lista-de-Comandos> end
        ComandoComposto compositeCommand = new ComandoComposto();
        accept(Token.BEGIN);
        compositeCommand.listOfCommands = parseListaDeComandos();
        accept(Token.END);

        return compositeCommand;
    }

    private Condicional parseCondicional() throws Exception {
        // if <Expressão> then <Comando> (else <Comando> | <Vazio>)
        Condicional conditional = new Condicional();
        accept(Token.IF);
        conditional.expression = parseExpressao();
        accept(Token.THEN);
        conditional.command = parseComando();
        if (currentToken.kind == Token.ELSE) {
            acceptIt();
            conditional.commandElse = parseComando();
            // commandElse
        } else {
            conditional.commandElse = null;
        }
        return conditional;
    }

    private Corpo parseCorpo() throws Exception {
        // <Declarações> <Comando-Composto>
        Corpo body = new Corpo();
        body.declarations = parseDeclaracoes();
        body.compositeCommand = parseComandoComposto();
        return body;
    }

    private DeclaracaoDeVariavel parseDeclaracaoDeVariavel() throws Exception {
        // var <Lista-de-IDs>: <Tipo>
        DeclaracaoDeVariavel variableDeclaration = new DeclaracaoDeVariavel();
        accept(Token.VAR);
        variableDeclaration.listOfIds = parseListaDeIds();
        accept(Token.COLON);
        variableDeclaration.type = parseTipo();

        return variableDeclaration;
    }

    private Declaracoes parseDeclaracoes() throws Exception {
        // (<Declaracao-De-Variavel>;)* 
        Declaracoes declarations = null;
        while (currentToken.kind == Token.VAR) {
            Declaracoes aux = new Declaracoes();
            aux.declarationOfVariable = parseDeclaracaoDeVariavel();
            aux.next = null;
            accept(Token.SEMICOLON);

            if (declarations == null) {
                declarations = aux;
            } else {
                Declaracoes aux2 = declarations;
                while (aux2.next != null) {
                    aux2 = aux2.next;
                }
                aux2.next = aux;
            }
        }
        return declarations;
    }

    private Expressao parseExpressao() throws Exception {
        // <Expresso-Simples> | <Expresso-Simples> <Op-Rel> <Expresso-Simples>
        // <Op-Rel> == < | > | <= | >= | <> | =
        Expressao expression = new Expressao();
        expression.simpleExpression = parseExpressaoSimples();
        if (currentToken.kind == Token.GREATER || currentToken.kind == Token.LESS || currentToken.kind == Token.LESS_EQUAL
                || currentToken.kind == Token.GREATER_EQUAL || currentToken.kind == Token.DIFF || currentToken.kind == Token.EQUAL) {
            expression.operator = currentToken;
            acceptIt();
            expression.simpleExpressionR = parseExpressaoSimples();
        } else {
            expression.simpleExpressionR = null;
            expression.operator = null;
        }
        return expression;
    }

    private ExpressaoSimples parseExpressaoSimples() throws Exception {
        // a: == <Termo> (<Op-Ad><Termo>)* 
        // <Op-Ad> == + | - | or

        ExpressaoSimples simpleExpression = new ExpressaoSimples();
        simpleExpression.term = parseTermo();
        simpleExpression.operator = null;
        simpleExpression.next = null;

        while (currentToken.kind == Token.SUM || currentToken.kind == Token.SUB || currentToken.kind == Token.OR) {
            ExpressaoSimples aux = new ExpressaoSimples();
            aux.operator = currentToken;
            acceptIt();

            aux.term = parseTermo();
            aux.next = null;

            if (simpleExpression.next == null) {
                simpleExpression.next = aux;
            } else {
                ExpressaoSimples aux2 = simpleExpression;
                while (aux2.next != null) {
                    aux2 = aux2.next;
                }
                aux2.next = aux;
            }
        }
        return simpleExpression;
    }

    private Fator parseFator() throws Exception {
        // <Variável> | <Literal> | "(" <Expressão> ")"
        Fator factor;
        switch (currentToken.kind) {
            case Token.ID:
                factor = parseVariavel();
                break;
            case Token.TRUE:
            case Token.FALSE:
            case Token.INT_LIT:
            case Token.FLOAT_LIT:
                factor = parseLiteral();
                break;
            case Token.LPAREN:
                acceptIt();
                factor = parseExpressao();
                accept(Token.RPAREN);
                break;
            default:
                factor = null;
                throw new SyntaxException("FATOR INVÁLIDO: Fator espera receber ATRIBUIÇAO (ID), LITERAL ou EXPRESSAO ENTRE PARENTESES, Linha: " + lastToken.line + ", Coluna: " + lastToken.col);
        }
        return factor;
    }

    private Iterativo parseIterativo() throws Exception {
        // Iterativo == while <Expressão> do <Comando>
        Iterativo iterative = new Iterativo();
        accept(Token.WHILE);
        iterative.expression = parseExpressao();
        accept(Token.DO);
        iterative.command = parseComando();

        return iterative;
    }

    private ListaDeComandos parseListaDeComandos() throws Exception {
        // Lista-de-Comandos> == (<Comando>;)*
        ListaDeComandos listOfCommands = null;

        while (currentToken.kind == Token.ID || currentToken.kind == Token.IF || currentToken.kind == Token.WHILE || currentToken.kind == Token.BEGIN) {
            ListaDeComandos aux = new ListaDeComandos();
            aux.command = parseComando();
            aux.next = null;
            accept(Token.SEMICOLON);

            if (listOfCommands == null) {
                listOfCommands = aux;
            } else {
                ListaDeComandos aux2 = listOfCommands;
                while (aux2.next != null) {
                    aux2 = aux2.next;
                }
                aux2.next = aux;
            }
        }
        return listOfCommands;
    }

    private ListaDeIds parseListaDeIds() throws Exception {
        // Lista-De-Ids> == <Id>	(, <Id>)*
        ListaDeIds listOfIds = new ListaDeIds();
        listOfIds.id = currentToken;
        listOfIds.next = null;
        accept(Token.ID);

        while (currentToken.kind == Token.COMMA) {
            acceptIt();
            ListaDeIds aux = new ListaDeIds();
            aux.id = currentToken;
            aux.next = null;
            accept(Token.ID);

            ListaDeIds aux2;
            if (listOfIds.next == null) {
                listOfIds.next = aux;
            } else {
                aux2 = listOfIds;
                while (aux2.next != null) {
                    aux2 = aux2.next;
                }
                aux2.next = aux;
            }
        }

        return listOfIds;
    }

    private Literal parseLiteral() throws Exception {
        // <Bool-Lit> | <Int-Lit> | <Float-Lit>
        Literal literal = new Literal();
        switch (currentToken.kind) {
            case Token.TRUE:
            case Token.FALSE:
                literal = parseBoolLit();
                break;
            case Token.INT_LIT:
                literal.name = currentToken;
                acceptIt();
                break;
            case Token.FLOAT_LIT:
                literal.name = currentToken;
                acceptIt();
                break;
            default:
                literal = null;
                throw new SyntaxException("LITERAL INVÁLIDO: Literal deve ser do tipo BOOLEANO ('true' ou 'false'), INTEIRO ou FLOAT, Linha: " + lastToken.line + ", Coluna: " + lastToken.col);
        }
        return literal;
    }

    private Programa parsePrograma() throws Exception {
        // program <Id> ; <Corpo> . [EOF]
        Programa program = new Programa();
        accept(Token.PROGRAM);
        accept(Token.ID);
        accept(Token.SEMICOLON);
        program.body = parseCorpo();
        accept(Token.DOT);
        accept(Token.EOF);
        return program;
    }

    private Seletor parseSeletor() throws Exception {
        // ("["<Expressão>"]")*
        Seletor selector = null;
        while (currentToken.kind == Token.LBRACKET) {
            acceptIt();
            Seletor aux = new Seletor();
            aux.expression = parseExpressao();
            aux.next = null;
            accept(Token.RBRACKET);

            if (selector == null) {
                selector = aux;
            } else {
                Seletor aux2 = selector;
                while (aux2.next != null) {
                    aux2 = aux2.next;
                }
                aux2.next = aux;
            }
        }
        return selector;
    }

    private Termo parseTermo() throws Exception {
        // Termo == <Fator> (<Op-Mul><Fator>)*
        Termo term = new Termo();
        term.factor = parseFator();
        term.operator = null;
        term.next = null;

        while (currentToken.kind == Token.MULT || currentToken.kind == Token.DIV || currentToken.kind == Token.AND) {
            Termo aux = new Termo();
            aux.operator = currentToken;
            acceptIt();

            aux.factor = parseFator();
            aux.next = null;

            if (term.next == null) {
                term.next = aux;
            } else {
                Termo aux2 = term;
                while (aux2.next != null) {
                    aux2 = aux2.next;
                }
                aux2.next = aux;
            }
        }
        return term;
    }

    private Tipo parseTipo() throws Exception {
        //integer | real | boolean | array[<Literal>~<Literal>] of <Tipo>
        Tipo typex;
        switch (currentToken.kind) {
            case Token.ARRAY: {
                // array [ <Literal>~<Literal> ] of <Tipo>
                TipoAgregado type = new TipoAgregado();
                acceptIt();
                accept(Token.LBRACKET);
                type.literal1 = parseLiteral();
                accept(Token.TILDE); //Acento til separa Literals de Arrays 
                type.literal2 = parseLiteral();
                accept(Token.RBRACKET);
                accept(Token.OF);
                type.typo = parseTipo();
                typex = type;
            }
            break;
            case Token.INTEGER:
            case Token.REAL:
            case Token.BOOLEAN: {
                // integer | real | boolean
                TipoSimples type = new TipoSimples();
                type.typo = currentToken;
                acceptIt();
                typex = type;
            }
            break;
            default:
                typex = null;
                throw new SyntaxException("TIPO INVÁLIDO: Tipo deve ser TIPO AGREGADO (array) ou TIPO SIMPLES (inteiro, real ou booleano), Linha: " + lastToken.line + ", Coluna: " + lastToken.col);
        }
        return typex;
    }

    private Variavel parseVariavel() throws Exception {
        // Varivel == <Id> <Seletor>
        Variavel variable = new Variavel();
        variable.id = currentToken;
        accept(Token.ID);
        variable.selector = parseSeletor();

        return variable;
    }
}
