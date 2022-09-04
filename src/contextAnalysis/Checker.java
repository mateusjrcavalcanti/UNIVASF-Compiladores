package contextAnalysis;

import abstractSyntaxTree.Attribution;
import abstractSyntaxTree.BooleanLiteral;
import abstractSyntaxTree.CommandComposite;
import abstractSyntaxTree.Conditional;
import abstractSyntaxTree.Body;
import abstractSyntaxTree.VariableDeclaration;
import abstractSyntaxTree.Declarations;
import abstractSyntaxTree.Expression;
import abstractSyntaxTree.ExpressionSimple;
import abstractSyntaxTree.Iterative;
import abstractSyntaxTree.CommandsList;
import abstractSyntaxTree.IdList;
import abstractSyntaxTree.Literal;
import abstractSyntaxTree.Program;
import abstractSyntaxTree.Selector;
import abstractSyntaxTree.Term;
import abstractSyntaxTree.TypeAggregate;
import abstractSyntaxTree.TypeSimple;
import abstractSyntaxTree.Variable;
import abstractSyntaxTree.Visitor;
import errors.ContextErrors;
import lexicon.Token;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Checker implements Visitor {

    private ContextErrors Errors;
    IdentificationTable table;

    public Checker() {
        this.Errors = new ContextErrors(this.getClass());
        table = new IdentificationTable(this.Errors);
    }

    public IdentificationTable getTable() {
        return this.table;
    }

    public void check(Program program) throws IOException {

        Logger.getLogger(this.getClass().getName()).log(
                Level.INFO,
                "2. A análise de contexto foi iniciada"
        );

        program.visit(this);

        this.Errors.status();

    }

    @Override
    public void visitAtribuicao(Attribution becomes) {
        becomes.variable.visit(this);
        becomes.expression.visit(this);

        if (becomes.variable.type != null) {
            if (becomes.variable.type.equals(becomes.expression.type)) {
                becomes.type = becomes.variable.type;
            } else if (becomes.variable.type.equals("real") && becomes.expression.type.equals("integer")) {
                becomes.type = becomes.variable.type;
            } else {
                Errors.error(
                        21,
                        "Esperava encontrar:" + becomes.variable.id.type
                        + "na linha: " + becomes.variable.id.line
                        + ", coluna: " + becomes.variable.id.col
                );
            }
        }

    }

    @Override
    public void visitBoolLit(BooleanLiteral boolLit) {
        boolLit.type = "boolean";
    }

    @Override
    public void visitComandoComposto(CommandComposite compositeCommands) {
        if (compositeCommands.listOfCommands != null) {
            compositeCommands.listOfCommands.visit(this);
        }
    }

    @Override
    public void visitCondicional(Conditional conditional) {
        if (conditional.expression != null) {
            conditional.expression.visit(this);
            if (!conditional.expression.type.equals("boolean")) {
                Errors.error(
                        22,
                        "Tipo retornado:" + conditional.expression.type
                        + "na linha: " + conditional.expression.operator.line
                );
            }

        }
    }

    @Override
    public void visitCorpo(Body body) {
        if (body.declarations != null) {
            body.declarations.visit(this);
        }

        body.compositeCommand.visit(this);

    }

    @Override
    public void visitDeclaracaoDeVariavel(VariableDeclaration variableDeclaration) {
        IdList aux = variableDeclaration.listOfIds;

        while (aux != null) {
            try {
                table.enter(aux.id, variableDeclaration);
            } catch (IOException ex) {
                Logger.getLogger(Checker.class.getName()).log(Level.SEVERE, null, ex);
            }
            aux = aux.next;

        }

        if (variableDeclaration.type instanceof TypeAggregate) {
            ((TypeAggregate) variableDeclaration.type).visit(this);

        } else {
            if (variableDeclaration.type instanceof TypeSimple) {
                ((TypeSimple) variableDeclaration.type).visit(this);
            }
        }
    }

    @Override
    public void visitDeclaracoes(Declarations declarations) {
        Declarations aux = declarations;
        while (aux != null) {
            aux.declarationOfVariable.visit(this);
            aux = aux.next;
        }
    }

    @Override
    public void visitExpressao(Expression expression) {
        if (expression.simpleExpression != null) {
            expression.simpleExpression.visit(this);
            expression.type = expression.simpleExpression.type;
        }
        if (expression.simpleExpressionR != null) {
            expression.simpleExpressionR.visit(this);

            if ("real".equals(expression.simpleExpressionR.type) || expression.simpleExpressionR.type.equals("integer")) {
                expression.type = "boolean";
            } else {
                Errors.error(
                        23,
                        "Na linha: " + expression.operator.line
                );
            }

        }

    }

    @Override
    public void visitExpressaoSimples(ExpressionSimple simpleExpression) {
        ExpressionSimple aux = simpleExpression;
        String place = null;
        while (aux != null) {
            if (aux.term != null) {
                aux.term.visit(this);
                if (place == null) {
                    place = aux.term.type;
                }
                if (aux.operator != null) {

                    switch (aux.operator.type) {
                        case Token.SUM:
                        case Token.SUBTRACTION:
                            switch (place) {

                                case "integer":
                                    switch (aux.term.type) {
                                        case "integer":
                                            place = "integer";
                                            break;
                                        case "real":
                                            place = "real";
                                            break;
                                        default: {
                                            Errors.error(
                                                    24,
                                                    "Na linha: " + aux.operator.line
                                            );
                                        }
                                    }
                                    break;
                                case "real":
                                    if (aux.term.type.equals("boolean")) {
                                        Errors.error(
                                                24,
                                                "Na linha: " + aux.operator.line
                                        );
                                    }
                                    place = "real";
                                    break;
                                default: {
                                    Errors.error(
                                            24,
                                            "Na linha: " + aux.operator.line
                                    );
                                }

                            }
                            break;
                        case Token.OR:
                            if (!place.equals("boolean") || !aux.term.type.equals("boolean")) {
                                Errors.error(
                                        24,
                                        "Na linha: " + aux.operator.line
                                );
                            }
                            place = "boolean";
                            break;
                    }
                }
            }
            aux = aux.next;
        }

        simpleExpression.type = place;

    }

    @Override
    public void visitIterativo(Iterative iterative) {
        if (iterative.expression != null) {
            iterative.expression.visit(this);
        }

        if (!iterative.expression.type.equals("boolean")) {
            Errors.error(
                    22,
                    "Tipo retornado: " + iterative.expression.type
                    + "na linha: " + iterative.expression.operator.line
            );
        }

        if (iterative.command instanceof Attribution) {
            ((Attribution) iterative.command).visit(this);
        } else if (iterative.command instanceof CommandComposite) {
            ((CommandComposite) iterative.command).visit(this);
        } else if (iterative.command instanceof Iterative) {
            ((Iterative) iterative.command).visit(this);
        } else if (iterative.command instanceof Conditional) {
            ((Conditional) iterative.command).visit(this);
        }
    }

    @Override
    public void visitListaDeComandos(CommandsList listOfCommands) {
        CommandsList aux = listOfCommands;
        while (aux != null) {
            if (aux.command instanceof Attribution) {
                ((Attribution) aux.command).visit(this);
            } else if (aux.command instanceof CommandComposite) {
                ((CommandComposite) aux.command).visit(this);
            } else if (aux.command instanceof Iterative) {
                ((Iterative) aux.command).visit(this);
            } else if (aux.command instanceof Conditional) {
                ((Conditional) aux.command).visit(this);
            }
            aux = aux.next;
        }
    }

    @Override
    public void visitListaDeIds(IdList listOfIds) {

    }

    @Override
    public void visitLiteral(Literal literal) {

        switch (literal.name.type) {
            case Token.LITERAL_INTEGER:
                literal.type = "integer";
                break;
            case Token.LITERAL_FLOAT:
                literal.type = "real";
                break;
            case Token.BOOLEAN:
                literal.type = "boolean";
                break;
        }
    }

    @Override
    public void visitPrograma(Program program) {
        program.body.visit(this);
    }

    @Override
    public void visitSeletor(Selector selector) {
        Selector aux = selector;
        while (aux != null) {
            aux.expression.visit(this);

            if (!aux.expression.type.equals("integer")) {
                Errors.error(
                        25,
                        "Na linha: " + aux.expression.operator.line
                );
            }

            aux = aux.next;
        }

    }

    @Override
    public void visitTermo(Term term) {
        Term aux = term;
        String place = null;

        while (aux != null) {
            if (aux.factor != null) {

                if (aux.factor instanceof Variable) {
                    ((Variable) aux.factor).visit(this);
                    term.type = aux.factor.type;
                } else if (aux.factor instanceof Literal) {
                    ((Literal) aux.factor).visit(this);
                    term.type = aux.factor.type;
                } else if (aux.factor instanceof Expression) {
                    ((Expression) aux.factor).visit(this);
                    term.type = aux.factor.type;
                }

                if (place == null) {
                    place = term.type;
                }

                if (aux.operator != null) {

                    switch (aux.operator.type) {
                        case Token.DIVISION:
                            if (place.equals("boolean") || term.type.equals("boolean")) {
                                Errors.error(
                                        24,
                                        "Na linha: " + aux.operator.line
                                );
                            }
                            place = "real";
                            break;

                        case Token.MULTIPLICATION:
                            switch (place) {
                                case "integer":

                                    switch (term.type) {
                                        case "integer":
                                            place = "integer";
                                            break;
                                        case "real":
                                            place = "real";
                                            break;
                                        default: {
                                            Errors.error(
                                                    24,
                                                    "Na linha: " + aux.operator.line
                                            );
                                        }
                                    }
                                    break;

                                case "real":
                                    switch (term.type) {

                                        case "integer":
                                        case "real":
                                            place = "real";
                                            break;

                                        default: {
                                            Errors.error(
                                                    24,
                                                    "Na linha: " + aux.operator.line
                                            );
                                        }
                                        break;
                                    }

                                    break;

                                default: {
                                    Errors.error(
                                            24,
                                            "Na linha: " + aux.operator.line
                                    );
                                }
                                break;
                            }

                            break;

                        case Token.AND:

                            if (!place.equals("boolean") || !term.type.equals("boolean")) {
                                Errors.error(
                                        24,
                                        "Na linha: " + aux.operator.line
                                );
                            }
                            place = "boolean";
                            break;
                    }
                }
            }

            aux = aux.next;

        }

    }

    @Override
    public void visitTipoAgregado(TypeAggregate type) {

        if (type.typo instanceof TypeAggregate) {
            ((TypeAggregate) type.typo).visit(this);
        } else {

            if (type.typo instanceof TypeSimple) {
                ((TypeSimple) type.typo).visit(this);
            }
        }
        type.type = type.typo.type;
    }

    @Override
    public void visitTipoSimples(TypeSimple type) {
        type.type = type.typo.value;
    }

    @Override
    public void visitVariavel(Variable variable) {
        try {

            variable.declaration = table.retrieve(variable.id);
        } catch (IOException ex) {
            Logger.getLogger(Checker.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (variable.declaration != null) {
            variable.type = variable.declaration.type.type;
        }

        if (variable.selector != null) {
            variable.selector.visit(this);
        }
    }

}
