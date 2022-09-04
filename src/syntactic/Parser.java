package syntactic;

import lexicon.Token;
import lexicon.Scanner;
import abstractSyntaxTree.Attribution;
import abstractSyntaxTree.BooleanLiteral;
import abstractSyntaxTree.CommandComposite;
import abstractSyntaxTree.Body;
import abstractSyntaxTree.Command;
import abstractSyntaxTree.Conditional;
import abstractSyntaxTree.VariableDeclaration;
import abstractSyntaxTree.Declarations;
import abstractSyntaxTree.Variable;
import abstractSyntaxTree.Expression;
import abstractSyntaxTree.ExpressionSimple;
import abstractSyntaxTree.Factor;
import abstractSyntaxTree.Iterative;
import abstractSyntaxTree.CommandsList;
import abstractSyntaxTree.IdList;
import abstractSyntaxTree.Literal;
import abstractSyntaxTree.Program;
import abstractSyntaxTree.Selector;
import abstractSyntaxTree.Term;
import abstractSyntaxTree.Type;
import abstractSyntaxTree.TypeAggregate;
import abstractSyntaxTree.TypeSimple;
import errors.SyntacticErrors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Parser {

    private Token currentToken;
    private Token lastToken;
    private Scanner scanner;

    private SyntacticErrors Errors;

    public Parser() {
        this.Errors = new SyntacticErrors(this.getClass());
    }

    public Program parse(String fileName) throws Exception {
        Program program;

        scanner = new Scanner(fileName);
        currentToken = this.scanner.scan();

        Logger.getLogger(this.getClass().getName()).log(
                Level.INFO,
                "1. A análise sintática foi iniciada"
        );

        program = parsePrograma();

        this.Errors.status();
        return program;
    }

    private void accept(byte expectedKind) throws Exception {
        if (currentToken.type == expectedKind) {
            lastToken = currentToken;
            currentToken = scanner.scan();
        } else {
            Errors.error(
                    1,
                    "Esperava encontrar:" + Token.Spellings[expectedKind]
                    + "na linha: " + lastToken.line
                    + ", coluna: " + lastToken.col
            );
        }

    }

    private void acceptIt() throws Exception {
        currentToken = scanner.scan();
    }

    private Attribution parseAtribuicao() throws Exception {

        Attribution becomes = new Attribution();
        becomes.variable = parseVariavel();
        accept(Token.BECOMES);
        becomes.expression = parseExpressao();
        return becomes;
    }

    private BooleanLiteral parseBoolLit() throws Exception {

        BooleanLiteral logic = new BooleanLiteral();
        switch (currentToken.type) {
            case Token.TRUE:
            case Token.FALSE:
                logic.name = currentToken;
                acceptIt();
                break;
            default:
                Errors.error(
                        2,
                        "Na linha: " + currentToken.line
                        + ", coluna: " + currentToken.col
                );
        }
        return logic;
    }

    private Command parseComando() throws Exception {

        Command command;
        switch (currentToken.type) {
            case Token.IDENTIFIER:
                command = parseAtribuicao();
                break;

            case Token.IF:
                command = parseCondicional();
                break;

            case Token.WHILE:
                command = parseIterativo();
                break;

            case Token.BEGIN:
                command = parseComandoComposto();
                break;

            default:
                command = null;
                Errors.error(
                        3,
                        "Na linha: " + currentToken.line
                        + ", coluna: " + currentToken.col
                );

        }
        return command;
    }

    private CommandComposite parseComandoComposto() throws Exception {

        CommandComposite compositeCommand = new CommandComposite();
        accept(Token.BEGIN);
        compositeCommand.listOfCommands = parseListaDeComandos();
        accept(Token.END);

        return compositeCommand;
    }

    private Conditional parseCondicional() throws Exception {

        Conditional conditional = new Conditional();
        accept(Token.IF);
        conditional.expression = parseExpressao();
        accept(Token.THEN);
        conditional.command = parseComando();
        if (currentToken.type == Token.ELSE) {
            acceptIt();
            conditional.commandElse = parseComando();

        } else {
            conditional.commandElse = null;
        }
        return conditional;
    }

    private Body parseCorpo() throws Exception {

        Body body = new Body();
        body.declarations = parseDeclaracoes();
        body.compositeCommand = parseComandoComposto();
        return body;
    }

    private VariableDeclaration parseDeclaracaoDeVariavel() throws Exception {

        VariableDeclaration variableDeclaration = new VariableDeclaration();
        accept(Token.VAR);
        variableDeclaration.listOfIds = parseListaDeIds();
        accept(Token.COLON);
        variableDeclaration.type = parseTipo();

        return variableDeclaration;
    }

    private Declarations parseDeclaracoes() throws Exception {

        Declarations declarations = null;
        while (currentToken.type == Token.VAR) {
            Declarations aux = new Declarations();
            aux.declarationOfVariable = parseDeclaracaoDeVariavel();
            aux.next = null;
            accept(Token.SEMICOLON);

            if (declarations == null) {
                declarations = aux;
            } else {
                Declarations aux2 = declarations;
                while (aux2.next != null) {
                    aux2 = aux2.next;
                }
                aux2.next = aux;
            }
        }
        return declarations;
    }

    private Expression parseExpressao() throws Exception {

        Expression expression = new Expression();
        expression.simpleExpression = parseExpressaoSimples();
        if (currentToken.type == Token.GREATER || currentToken.type == Token.LESS || currentToken.type == Token.LESS_EQUAL
                || currentToken.type == Token.GREATER_EQUAL || currentToken.type == Token.DIFFERENT || currentToken.type == Token.EQUAL) {
            expression.operator = currentToken;
            acceptIt();
            expression.simpleExpressionR = parseExpressaoSimples();
        } else {
            expression.simpleExpressionR = null;
            expression.operator = null;
        }
        return expression;
    }

    private ExpressionSimple parseExpressaoSimples() throws Exception {

        ExpressionSimple simpleExpression = new ExpressionSimple();
        simpleExpression.term = parseTermo();
        simpleExpression.operator = null;
        simpleExpression.next = null;

        while (currentToken.type == Token.SUM || currentToken.type == Token.SUBTRACTION || currentToken.type == Token.OR) {
            ExpressionSimple aux = new ExpressionSimple();
            aux.operator = currentToken;
            acceptIt();

            aux.term = parseTermo();
            aux.next = null;

            if (simpleExpression.next == null) {
                simpleExpression.next = aux;
            } else {
                ExpressionSimple aux2 = simpleExpression;
                while (aux2.next != null) {
                    aux2 = aux2.next;
                }
                aux2.next = aux;
            }
        }

        return simpleExpression;

    }

    private Factor parseFator() throws Exception {

        Factor factor;
        switch (currentToken.type) {
            case Token.IDENTIFIER:
                factor = parseVariavel();
                break;
            case Token.TRUE:
            case Token.FALSE:
            case Token.LITERAL_INTEGER:
            case Token.LITERAL_FLOAT:
                factor = parseLiteral();
                break;
            case Token.LEFT_PARENTHESES:
                acceptIt();
                factor = parseExpressao();
                accept(Token.RIGHT_PARENTHESES);
                break;
            default:
                factor = null;
                Errors.error(
                        4,
                        "Na linha: " + currentToken.line
                        + ", coluna: " + currentToken.col
                );
        }
        return factor;
    }

    private Iterative parseIterativo() throws Exception {

        Iterative iterative = new Iterative();
        accept(Token.WHILE);
        iterative.expression = parseExpressao();
        accept(Token.DO);
        iterative.command = parseComando();

        return iterative;
    }

    private CommandsList parseListaDeComandos() throws Exception {

        CommandsList listOfCommands = null;

        while (currentToken.type == Token.IDENTIFIER || currentToken.type == Token.IF || currentToken.type == Token.WHILE || currentToken.type == Token.BEGIN) {
            CommandsList aux = new CommandsList();
            aux.command = parseComando();
            aux.next = null;
            accept(Token.SEMICOLON);

            if (listOfCommands == null) {
                listOfCommands = aux;
            } else {
                CommandsList aux2 = listOfCommands;
                while (aux2.next != null) {
                    aux2 = aux2.next;
                }
                aux2.next = aux;
            }
        }
        return listOfCommands;
    }

    private IdList parseListaDeIds() throws Exception {

        IdList listOfIds = new IdList();
        listOfIds.id = currentToken;
        listOfIds.next = null;
        accept(Token.IDENTIFIER);

        while (currentToken.type == Token.COMMA) {
            acceptIt();
            IdList aux = new IdList();
            aux.id = currentToken;
            aux.next = null;
            accept(Token.IDENTIFIER);

            IdList aux2;
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

        Literal literal = new Literal();
        switch (currentToken.type) {
            case Token.TRUE:
            case Token.FALSE:
                literal = parseBoolLit();
                break;
            case Token.LITERAL_INTEGER:
                literal.name = currentToken;
                acceptIt();
                break;
            case Token.LITERAL_FLOAT:
                literal.name = currentToken;
                acceptIt();
                break;
            default:
                literal = null;
                Errors.error(
                        5,
                        "Na linha: " + currentToken.line
                        + ", coluna: " + currentToken.col
                );
        }
        return literal;
    }

    private Program parsePrograma() throws Exception {

        Program program = new Program();
        accept(Token.PROGRAM);
        accept(Token.IDENTIFIER);
        accept(Token.SEMICOLON);
        program.body = parseCorpo();
        accept(Token.DOT);
        accept(Token.EOF);
        return program;
    }

    private Selector parseSeletor() throws Exception {

        Selector selector = null;
        while (currentToken.type == Token.LEFT_BRACKET) {
            acceptIt();
            Selector aux = new Selector();
            aux.expression = parseExpressao();
            aux.next = null;
            accept(Token.RIGHT_BRACKET);

            if (selector == null) {
                selector = aux;
            } else {
                Selector aux2 = selector;
                while (aux2.next != null) {
                    aux2 = aux2.next;
                }
                aux2.next = aux;
            }
        }
        return selector;
    }

    private Term parseTermo() throws Exception {

        Term term = new Term();
        term.factor = parseFator();
        term.operator = null;
        term.next = null;

        while (currentToken.type == Token.MULTIPLICATION || currentToken.type == Token.DIVISION || currentToken.type == Token.AND) {
            Term aux = new Term();
            aux.operator = currentToken;
            acceptIt();

            aux.factor = parseFator();
            aux.next = null;

            if (term.next == null) {
                term.next = aux;
            } else {
                Term aux2 = term;
                while (aux2.next != null) {
                    aux2 = aux2.next;
                }
                aux2.next = aux;
            }
        }
        return term;
    }

    private Type parseTipo() throws Exception {

        Type typex;
        switch (currentToken.type) {
            case Token.ARRAY: {

                TypeAggregate type = new TypeAggregate();
                acceptIt();
                accept(Token.LEFT_BRACKET);
                type.literal1 = parseLiteral();
                accept(Token.TILDE);
                type.literal2 = parseLiteral();
                accept(Token.RIGHT_BRACKET);
                accept(Token.OF);
                type.typo = parseTipo();
                typex = type;
            }
            break;
            case Token.INTEGER:
            case Token.REAL:
            case Token.BOOLEAN: {

                TypeSimple type = new TypeSimple();
                type.typo = currentToken;
                acceptIt();
                typex = type;
            }
            break;
            default:
                typex = null;
                Errors.error(
                        6,
                        "Na linha: " + currentToken.line
                        + ", coluna: " + currentToken.col
                );
        }
        return typex;
    }

    private Variable parseVariavel() throws Exception {

        Variable variable = new Variable();
        variable.id = currentToken;
        accept(Token.IDENTIFIER);
        variable.selector = parseSeletor();

        return variable;
    }
}
