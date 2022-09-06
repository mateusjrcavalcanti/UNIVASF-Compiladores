package codeGenerator;

import lexicon.Token;
import abstractSyntaxTree.Attribution;
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
import abstractSyntaxTree.BooleanLiteral;
import abstractSyntaxTree.Program;
import abstractSyntaxTree.Selector;
import abstractSyntaxTree.Term;
import abstractSyntaxTree.TypeAggregate;
import abstractSyntaxTree.TypeSimple;
import abstractSyntaxTree.Variable;
import abstractSyntaxTree.Visitor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Coder implements Visitor {

    int mem = 0, varqtd = 0, ElseCont = 1, ElseAux, WhileCont = 1, WhileAux = 0;

    StringBuilder bd = new StringBuilder();

    public String encode(Program program) {
        Logger.getLogger(this.getClass().getName()).log(
                Level.INFO,
                "3. A geração de código foi iniciada"
        );
        program.visit(this);

        if (ElseAux != 0) {
            bd.append("\n\nELSE").append(ElseAux).append(":\n");
        } else {
            bd.append("\n\nEND").append(":\n");
        }

        bd.append("\n0x").append(mem).append(":\t").append("POP \t").append(varqtd);
        mem++;
        bd.append("\n0x").append(mem).append(":\t").append("HALT");

        Logger.getLogger(this.getClass().getName()).log(
                Level.INFO,
                "Código gerado com sucesso!"
        );

        String codgen = bd.toString();

        return (codgen);

    }

    @Override
    public void visitAtribuicao(Attribution becomes) {

        if (becomes.expression != null) {
            becomes.expression.visit(this);
        }

        if (becomes.variable != null) {
            bd.append("\n0x").append(mem).append(":\t").append("STORE \t").append(becomes.variable.id.value);
            mem++;
        }

    }

    @Override
    public void visitBoolLit(BooleanLiteral boolLit) {
        boolLit.type = "boolean";
        bd.append("\n0x").append(mem).append(":\t").append("LOADL \t").append(boolLit.name.value);
        mem++;
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
            bd.append("\n0x").append(mem).append(":\t").append("JUMPIF(0)\t").append("ELSE").append(ElseCont);
            mem++;
            ElseAux = ElseCont;
            ElseCont++;
        }

        if (conditional.command instanceof Attribution) {
            ((Attribution) conditional.command).visit(this);
        } else if (conditional.command instanceof CommandComposite) {
            ((CommandComposite) conditional.command).visit(this);
        } else if (conditional.command instanceof Iterative) {
            ((Iterative) conditional.command).visit(this);
        } else if (conditional.command instanceof Conditional) {
            ((Conditional) conditional.command).visit(this);
        }

        if (conditional.commandElse instanceof Attribution) {
            bd.append("\n0x").append(mem).append(":\t").append("JUMP\t").append("ELSE").append(ElseCont);
            mem++;
            bd.append("\n\nELSE").append(ElseCont - 1).append(":\n");
            ElseAux++;
            ((Attribution) conditional.commandElse).visit(this);
        } else if (conditional.commandElse instanceof CommandComposite) {
            bd.append("\n0x").append(mem).append(":\t").append("JUMP\t").append("ELSE").append(ElseCont);
            mem++;
            bd.append("\n\nELSE").append(ElseCont - 1).append(":\n");
            ElseAux++;
            ((CommandComposite) conditional.commandElse).visit(this);
        } else if (conditional.commandElse instanceof Iterative) {
            bd.append("\n0x").append(mem).append(":\t").append("JUMP\t").append("ELSE").append(ElseCont);
            mem++;
            bd.append("\n\nELSE").append(ElseCont - 1).append(":\n");
            ElseAux++;
            ((Iterative) conditional.commandElse).visit(this);
        } else if (conditional.commandElse instanceof Conditional) {
            bd.append("\n0x").append(mem).append(":\t").append("JUMP\t").append("ELSE").append(ElseCont);
            mem++;
            bd.append("\n\nELSE").append(ElseCont - 1).append(":\n");
            ElseAux++;
            ((Conditional) conditional.commandElse).visit(this);
        }

    }

    @Override
    public void visitCorpo(Body body) {

        if (body.declarations != null) {
            body.declarations.visit(this);
        }

        bd.append("\n0x").append(mem).append(":\t").append("PUSH \t").append(varqtd);
        mem++;
        body.compositeCommand.visit(this);
    }

    @Override
    public void visitDeclaracaoDeVariavel(VariableDeclaration variableDeclaration) {

        IdList aux = variableDeclaration.listOfIds;
        while (aux != null) {
            aux = aux.next;
            varqtd++;
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
        }

        if (expression.simpleExpressionR != null) {
            expression.simpleExpressionR.visit(this);
        }

        if (expression.operator != null) {
            switch (expression.operator.value) {
                case "<":
                    bd.append("\n0x").append(mem).append(":\t").append("CALL\tlt");
                    mem++;
                    break;
                case ">":
                    bd.append("\n0x").append(mem).append(":\t").append("CALL\tgt");
                    mem++;
                    break;
                case ">=":
                    bd.append("\n0x").append(mem).append(":\t").append("CALL\tgtoe");
                    mem++;
                    break;
                case "<=":
                    bd.append("\n0x").append(mem).append(":\t").append("CALL\tltoe");
                    mem++;
                    break;
                case "=":
                    bd.append("\n0x").append(mem).append(":\t").append("CALL\teq");
                    mem++;
                    break;
                case "<>":
                    bd.append("\n0x").append(mem).append(":\t").append("CALL\tneq");
                    mem++;
                    break;

            }
        }

    }

    @Override
    public void visitExpressaoSimples(ExpressionSimple simpleExpression) {
        ExpressionSimple aux = simpleExpression;
        ExpressionSimple aux2 = aux;
        String place = null;

        while (aux != null) {
            if (aux.term != null) {
                aux.term.visit(this);
            }
            aux = aux.next;
        }

        while (aux2 != null) {
            if (aux2.operator != null) {
                switch (aux2.operator.type) {
                    case Token.SUM:
                        bd.append("\n0x").append(mem).append(":\t").append("CALL\tadd");
                        mem++;
                        break;
                    case Token.SUBTRACTION:
                        bd.append("\n0x").append(mem).append(":\t").append("CALL\tsub");
                        mem++;
                        break;
                    case Token.OR:
                        bd.append("\n0x").append(mem).append(":\t").append("CALL\tor");
                        mem++;
                        break;
                }
            }
            aux2 = aux2.next;
        }

    }

    @Override
    public void visitIterativo(Iterative iterative) {

        bd.append("\n0x").append(mem).append(":\t").append("JUMP\t").append("EVL").append(WhileCont);
        mem++;
        WhileAux = WhileCont;
        WhileCont++;
        bd.append("\n\nWHILE").append(WhileAux).append(":\n");

        if (iterative.command instanceof Attribution) {
            ((Attribution) iterative.command).visit(this);
        } else if (iterative.command instanceof CommandComposite) {

            ((CommandComposite) iterative.command).visit(this);
        } else if (iterative.command instanceof Iterative) {
            ((Iterative) iterative.command).visit(this);
        } else if (iterative.command instanceof Conditional) {
            ((Conditional) iterative.command).visit(this);
        }

        bd.append("\n\nEVL").append(WhileAux).append(":\n");

        if (iterative.expression != null) {
            iterative.expression.visit(this);
            mem++;
        }

        bd.append("\n0x").append(mem).append(":\t").append("JUMPIF(1)\t").append("WHILE").append(WhileAux);
        mem++;

        WhileAux--;

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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void visitLiteral(Literal literal) {
        bd.append("\n0x").append(mem).append(":\t").append("LOADL \t").append(literal.name.value);
        mem++;
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
            aux = aux.next;
        }
    }

    @Override
    public void visitTermo(Term term) {
        Term aux = term;

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

                if (aux.operator != null) {
                    switch (aux.operator.type) {
                        case Token.DIVISION:
                            bd.append("\n0x").append(mem).append(":\t").append("CALL\tdiv");
                            mem++;
                            break;

                        case Token.MULTIPLICATION:
                            bd.append("\n0x").append(mem).append(":\t").append("CALL\tmult");
                            mem++;
                            break;

                        case Token.AND:
                            bd.append("\n0x").append(mem).append(":\t").append("CALL\tand");
                            mem++;
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

        bd.append("\n0x").append(mem).append(":\t").append("LOAD \t").append(variable.id.value);
        mem++;

        if (variable.selector != null) {
            variable.selector.visit(this);
        }
    }

}
