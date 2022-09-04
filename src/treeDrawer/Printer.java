package treeDrawer;

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

public class Printer implements Visitor {

    
    int i = 0;
    int lvl = 0;
    StringBuilder bd = new StringBuilder();

    public String print(Program program) {
        
        bd.append("\t\n - IMPRESSÃO: ÁRVORE SINTÁTICA ABSTRATA");
        program.visit(this);
        String arvorest = bd.toString();

        
        return (arvorest);
    }

    
    private void out(String nodeInfo, int indentLevel) {
        for (int x = 0; x <= indentLevel / 2; x++) {
            
            bd.append("|  ");
            
            bd.append("  ");
        }
        
        bd.append(nodeInfo).append("\r\n");
    }

    
    @Override
    public void visitAtribuicao(Attribution becomes) {
        i++;
        out("Atribuicao", i);
        if (becomes.variable != null) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            becomes.variable.visit(this);
            i--;
        }
        if (becomes.expression != null) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            becomes.expression.visit(this);
            i--;
        }
        i--;

    }

    @Override
    public void visitBoolLit(BooleanLiteral boolLit) {
        out("Boolean Literal", i);
        out(boolLit.name.value, i + 2);
    }

    @Override
    public void visitComandoComposto(CommandComposite compositeCommands) {
        i++;
        out("Comando Composto", i);
        if (compositeCommands.listOfCommands != null) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            compositeCommands.listOfCommands.visit(this);
            i--;
        }
        i--;
    }

    @Override
    public void visitCondicional(Conditional conditional) {
        i++;
        out("Condicional", i);
        if (conditional.expression != null) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            conditional.expression.visit(this);
            i--;
        }

        if (conditional.command instanceof Attribution) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Attribution) conditional.command).visit(this);
            i--;
        } else if (conditional.command instanceof CommandComposite) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((CommandComposite) conditional.command).visit(this);
            i--;
        } else if (conditional.command instanceof Iterative) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Iterative) conditional.command).visit(this);
            i--;
        } else if (conditional.command instanceof Conditional) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Conditional) conditional.command).visit(this);
            i--;
        }

        if (conditional.commandElse instanceof Attribution) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Attribution) conditional.commandElse).visit(this);
            i--;
        } else if (conditional.commandElse instanceof CommandComposite) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((CommandComposite) conditional.commandElse).visit(this);
            i--;
        } else if (conditional.commandElse instanceof Iterative) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Iterative) conditional.commandElse).visit(this);
            i--;
        } else if (conditional.commandElse instanceof Conditional) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Conditional) conditional.commandElse).visit(this);
            i--;
        }
        i--;
    }

    @Override
    public void visitCorpo(Body body) {
        out("Corpo", i);
        if (body != null) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            if (body.declarations != null) {
                body.declarations.visit(this);
            }
            body.compositeCommand.visit(this);
            i--;
        }
    }

    @Override
    public void visitDeclaracaoDeVariavel(VariableDeclaration variableDeclaration) {
        i++;
        out("Declaracao de Variavel", i);
        if (variableDeclaration != null) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            variableDeclaration.listOfIds.visit(this);
            i--;
            if (variableDeclaration.type instanceof TypeAggregate) {
                i++;
                if (i > lvl) {
                    lvl = i;
                }
                ((TypeAggregate) variableDeclaration.type).visit(this);
                i--;
            } else {
                if (variableDeclaration.type instanceof TypeSimple) {
                    i++;
                    if (i > lvl) {
                        lvl = i;
                    }
                    ((TypeSimple) variableDeclaration.type).visit(this);
                    i--;
                }
            }
        }
        i--;

        
    }

    @Override
    public void visitDeclaracoes(Declarations declarations) {
        out("Declaracoes", i);
        Declarations aux = declarations;
        while (aux != null) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            aux.declarationOfVariable.visit(this);
            i--;
            aux = aux.next;
        }
    }

    @Override
    public void visitExpressao(Expression expression) {
        i++;
        out("Expressao", i);
        if (expression.simpleExpression != null) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            expression.simpleExpression.visit(this);
            i--;
        }
        if (expression.operator != null) {
            out(expression.operator.value, i + 2);
        }
        if (expression.simpleExpressionR != null) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            expression.simpleExpressionR.visit(this);
            i--;
        }
        i--;

    }

    @Override
    public void visitExpressaoSimples(ExpressionSimple simpleExpression) {
        i++;
        out("Expressao Simples", i);
        ExpressionSimple aux = simpleExpression;
        int c = 0;
        boolean op_switch = false;
        while (aux != null) {
            c++;
            if (op_switch) {
                if (aux.operator != null) {
                    out(aux.operator.value, i + 2);
                    op_switch = false;
                }
            }

            if (aux.term != null) {
                i++;
                if (i > lvl) {
                    lvl = i;
                }
                aux.term.visit(this);
                i--;
                op_switch = true;
            }

            aux = aux.next;

        }
        i--;
    }

    @Override
    public void visitIterativo(Iterative iterative) {
        i++;
        out("Iterativo", i);
        i++;
        if (i > lvl) {
            lvl = i;
        }
        iterative.expression.visit(this);
        i--;
        if (iterative.command instanceof Attribution) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Attribution) iterative.command).visit(this);
            i--;
        } else if (iterative.command instanceof CommandComposite) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((CommandComposite) iterative.command).visit(this);
            i--;
        } else if (iterative.command instanceof Iterative) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Iterative) iterative.command).visit(this);
            i--;
        } else if (iterative.command instanceof Conditional) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Conditional) iterative.command).visit(this);
            i--;
        }
        i--;

    }

    @Override
    public void visitListaDeComandos(CommandsList listOfCommands) {
        i++;
        out("Lista de Comandos", i);
        CommandsList aux = listOfCommands;

        while (aux != null) {
            if (aux.command != null) {
                if (aux.command instanceof Attribution) {
                    i++;
                    if (i > lvl) {
                        lvl = i;
                    }
                    ((Attribution) aux.command).visit(this);
                    i--;
                } else if (aux.command instanceof CommandComposite) {
                    i++;
                    if (i > lvl) {
                        lvl = i;
                    }
                    ((CommandComposite) aux.command).visit(this);
                    i--;
                } else if (aux.command instanceof Iterative) {
                    i++;
                    if (i > lvl) {
                        lvl = i;
                    }
                    ((Iterative) aux.command).visit(this);
                    i--;
                } else if (aux.command instanceof Conditional) {
                    i++;
                    if (i > lvl) {
                        lvl = i;
                    }
                    ((Conditional) aux.command).visit(this);
                    i--;
                }
            }
            aux = aux.next;
        }
        i--;
    }

    @Override
    public void visitListaDeIds(IdList listOfIds) {
        i++;
        out("Lista de IDs", i);
        if (listOfIds != null) {
            IdList aux = listOfIds;
            while (aux != null) {
                out(aux.id.value, i + 2);
                aux = aux.next;
            }
        }
        i--;
    }

    @Override
    public void visitLiteral(Literal literal) {
        i++;
        out("Literal", i);
        if (literal.name != null) {
            out(literal.name.value, i + 2);
        }
        i--;
    }

    @Override
    public void visitPrograma(Program program) {
        
        bd.append("\n\rPrograma\n");
        if (program != null) {
            if (program.body != null) {
                i++;
                if (i > lvl) {
                    lvl = i;
                }
                program.body.visit(this);
                i--;
            }
        }
    }

    @Override
    public void visitSeletor(Selector selector) {
        i++;
        out("Seletor", i);
        Selector aux = selector;
        while (aux != null) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            aux.expression.visit(this);
            i--;
            aux = aux.next;
        }
        i--;
    }

    @Override
    public void visitTermo(Term term) {
        i++;
        out("Termo", i);
        Term aux = term;
        int c = 0;
        boolean op_switch = false;
        while (aux != null) {
            c++;
            if (op_switch) {
                if (aux.operator != null) {
                    out(aux.operator.value, i + 2);
                    op_switch = false;
                }
            }
            if (aux.factor instanceof Variable) {
                i++;
                if (i > lvl) {
                    lvl = i;
                }
                ((Variable) aux.factor).visit(this);
                i--;
                op_switch = true;
            } else if (aux.factor instanceof Literal) {
                i++;
                if (i > lvl) {
                    lvl = i;
                }
                ((Literal) aux.factor).visit(this);
                i--;
                op_switch = true;
            } else if (aux.factor instanceof Expression) {
                i++;
                if (i > lvl) {
                    lvl = i;
                }
                ((Expression) aux.factor).visit(this);
                i--;
                op_switch = true;
            }

            aux = aux.next;
        }
        i--;

    }

    @Override
    public void visitTipoAgregado(TypeAggregate type) {
        i++;
        out("Tipo Agregado", i);
        out(type.literal1.name.value, i + 2);
        out(type.literal2.name.value, i + 2);
        if (type.typo instanceof TypeAggregate) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((TypeAggregate) type.typo).visit(this);
            i--;
        } else {
            if (type.typo instanceof TypeSimple) {
                i++;
                if (i > lvl) {
                    lvl = i;
                }
                ((TypeSimple) type.typo).visit(this);
                i--;
            }
        }
        i--;
    }

    @Override
    public void visitTipoSimples(TypeSimple type) {
        i++;
        out("Tipo Simples", i);
        if (type.typo != null) {
            out(type.typo.value, i + 2);
        }
        i--;

    }

    @Override
    public void visitVariavel(Variable variable) {
        out("Variavel", i);
        out(variable.id.value, i + 2);
        if (variable.selector != null) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            variable.selector.visit(this);
            i--;
        }

    }

}
