package compilador.TreeDrawer;

import compilador.abstractSyntaxTree.Atribuicao;
import compilador.abstractSyntaxTree.BoolLit;
import compilador.abstractSyntaxTree.ComandoComposto;
import compilador.abstractSyntaxTree.Condicional;
import compilador.abstractSyntaxTree.Corpo;
import compilador.abstractSyntaxTree.DeclaracaoDeVariavel;
import compilador.abstractSyntaxTree.Declaracoes;
import compilador.abstractSyntaxTree.Expressao;
import compilador.abstractSyntaxTree.ExpressaoSimples;
import compilador.abstractSyntaxTree.Iterativo;
import compilador.abstractSyntaxTree.ListaDeComandos;
import compilador.abstractSyntaxTree.ListaDeIds;
import compilador.abstractSyntaxTree.Literal;
import compilador.abstractSyntaxTree.Programa;
import compilador.abstractSyntaxTree.Seletor;
import compilador.abstractSyntaxTree.Termo;
import compilador.abstractSyntaxTree.TipoAgregado;
import compilador.abstractSyntaxTree.TipoSimples;
import compilador.abstractSyntaxTree.Variavel;
import compilador.abstractSyntaxTree.Visitor;

public class Printer implements Visitor {
    
    int i = 0;
    int lvl = 0;
    StringBuilder text = new StringBuilder();

    public String print(Programa program) {
        program.visit(this);
        String arvorest = text.toString();
        
        return (arvorest);
    }
    
    private void out(String nodeInfo, int indentLevel) {
        for (int x = 0; x <= indentLevel / 2; x++) {
            text.append("|  ");
            text.append("  ");
        }
        text.append(nodeInfo).append("\r\n");
    }
    
    @Override
    public void visitAtribuicao(Atribuicao becomes) {
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
    public void visitBoolLit(BoolLit boolLit) {
        out("Boolean Literal", i);
        out(boolLit.name.value, i + 2);
    }

    @Override
    public void visitComandoComposto(ComandoComposto compositeCommands) {
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
    public void visitCondicional(Condicional conditional) {
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

        if (conditional.command instanceof Atribuicao) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Atribuicao) conditional.command).visit(this);
            i--;
        } else if (conditional.command instanceof ComandoComposto) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((ComandoComposto) conditional.command).visit(this);
            i--;
        } else if (conditional.command instanceof Iterativo) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Iterativo) conditional.command).visit(this);
            i--;
        } else if (conditional.command instanceof Condicional) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Condicional) conditional.command).visit(this);
            i--;
        }

        if (conditional.commandElse instanceof Atribuicao) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Atribuicao) conditional.commandElse).visit(this);
            i--;
        } else if (conditional.commandElse instanceof ComandoComposto) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((ComandoComposto) conditional.commandElse).visit(this);
            i--;
        } else if (conditional.commandElse instanceof Iterativo) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Iterativo) conditional.commandElse).visit(this);
            i--;
        } else if (conditional.commandElse instanceof Condicional) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Condicional) conditional.commandElse).visit(this);
            i--;
        }
        i--;
    }

    @Override
    public void visitCorpo(Corpo body) {
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
    public void visitDeclaracaoDeVariavel(DeclaracaoDeVariavel variableDeclaration) {
        i++;
        out("Declaracao de Variavel", i);
        if (variableDeclaration != null) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            variableDeclaration.listOfIds.visit(this);
            i--;
            if (variableDeclaration.type instanceof TipoAgregado) {
                i++;
                if (i > lvl) {
                    lvl = i;
                }
                ((TipoAgregado) variableDeclaration.type).visit(this);
                i--;
            } else {
                if (variableDeclaration.type instanceof TipoSimples) {
                    i++;
                    if (i > lvl) {
                        lvl = i;
                    }
                    ((TipoSimples) variableDeclaration.type).visit(this);
                    i--;
                }
            }
        }
        i--;

        //variableDeclaration.type.visit(this);
    }

    @Override
    public void visitDeclaracoes(Declaracoes declarations) {
        out("Declaracoes", i);
        Declaracoes aux = declarations;
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
    public void visitExpressao(Expressao expression) {
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
    public void visitExpressaoSimples(ExpressaoSimples simpleExpression) {
        i++;
        out("Expressao Simples", i);
        ExpressaoSimples aux = simpleExpression;
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
    public void visitIterativo(Iterativo iterative) {
        i++;
        out("Iterativo", i);
        i++;
        if (i > lvl) {
            lvl = i;
        }
        iterative.expression.visit(this);
        i--;
        if (iterative.command instanceof Atribuicao) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Atribuicao) iterative.command).visit(this);
            i--;
        } else if (iterative.command instanceof ComandoComposto) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((ComandoComposto) iterative.command).visit(this);
            i--;
        } else if (iterative.command instanceof Iterativo) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Iterativo) iterative.command).visit(this);
            i--;
        } else if (iterative.command instanceof Condicional) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((Condicional) iterative.command).visit(this);
            i--;
        }
        i--;

    }

    @Override
    public void visitListaDeComandos(ListaDeComandos listOfCommands) {
        i++;
        out("Lista de Comandos", i);
        ListaDeComandos aux = listOfCommands;

        while (aux != null) {
            if (aux.command != null) {
                if (aux.command instanceof Atribuicao) {
                    i++;
                    if (i > lvl) {
                        lvl = i;
                    }
                    ((Atribuicao) aux.command).visit(this);
                    i--;
                } else if (aux.command instanceof ComandoComposto) {
                    i++;
                    if (i > lvl) {
                        lvl = i;
                    }
                    ((ComandoComposto) aux.command).visit(this);
                    i--;
                } else if (aux.command instanceof Iterativo) {
                    i++;
                    if (i > lvl) {
                        lvl = i;
                    }
                    ((Iterativo) aux.command).visit(this);
                    i--;
                } else if (aux.command instanceof Condicional) {
                    i++;
                    if (i > lvl) {
                        lvl = i;
                    }
                    ((Condicional) aux.command).visit(this);
                    i--;
                }
            }
            aux = aux.next;
        }
        i--;
    }

    @Override
    public void visitListaDeIds(ListaDeIds listOfIds) {
        i++;
        out("Lista de IDs", i);
        if (listOfIds != null) {
            ListaDeIds aux = listOfIds;
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
    public void visitPrograma(Programa program) {
        //System.out.println("Programa"); 
        text.append("\n\rPrograma\n");
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
    public void visitSeletor(Seletor selector) {
        i++;
        out("Seletor", i);
        Seletor aux = selector;
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
    public void visitTermo(Termo term) {
        i++;
        out("Termo", i);
        Termo aux = term;
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
            if (aux.factor instanceof Variavel) {
                i++;
                if (i > lvl) {
                    lvl = i;
                }
                ((Variavel) aux.factor).visit(this);
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
            } else if (aux.factor instanceof Expressao) {
                i++;
                if (i > lvl) {
                    lvl = i;
                }
                ((Expressao) aux.factor).visit(this);
                i--;
                op_switch = true;
            }

            aux = aux.next;
        }
        i--;

    }

    @Override
    public void visitTipoAgregado(TipoAgregado type) {
        i++;
        out("Tipo Agregado", i);
        out(type.literal1.name.value, i + 2);
        out(type.literal2.name.value, i + 2);
        if (type.typo instanceof TipoAgregado) {
            i++;
            if (i > lvl) {
                lvl = i;
            }
            ((TipoAgregado) type.typo).visit(this);
            i--;
        } else {
            if (type.typo instanceof TipoSimples) {
                i++;
                if (i > lvl) {
                    lvl = i;
                }
                ((TipoSimples) type.typo).visit(this);
                i--;
            }
        }
        i--;
    }

    @Override
    public void visitTipoSimples(TipoSimples type) {
        i++;
        out("Tipo Simples", i);
        if (type.typo != null) {
            out(type.typo.value, i + 2);
        }
        i--;

    }

    @Override
    public void visitVariavel(Variavel variable) {
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
