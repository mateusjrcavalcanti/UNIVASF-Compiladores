package compilador.contextAnalysis;

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
import compilador.exceptions.contextAnalysisException;
import compilador.lexico.Token;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Checker implements Visitor {

    IdentificationTable table;

    Checker() {
        table = new IdentificationTable();
    }

    public void check(Programa program) throws IOException {
        System.out.println("\n>>> 2. ANALISE DE CONTEXTO <<<");
        program.visit(this);

        System.out.println("  > Análise de Contexto concluída;");
    }

    //Verificação de Ocorrências:
    @Override
    public void visitAtribuicao(Atribuicao becomes) {
        becomes.variable.visit(this);
        becomes.expression.visit(this);

        //Verificacao de tipos:
        if (becomes.variable.type != null) {
            if (becomes.variable.type.equals(becomes.expression.type)) {
                becomes.type = becomes.variable.type;
            } else if (becomes.variable.type.equals("real") && becomes.expression.type.equals("integer")) {
                becomes.type = becomes.variable.type;
            } else {
                throw new contextAnalysisException("ERRO DE CONTEXTO: Atribuicao de valores incompatíveis.', Linha: " + becomes.variable.id.line + ", Coluna: " + becomes.variable.id.col);
            }
        }

    }

    @Override
    public void visitBoolLit(BoolLit boolLit) {
        boolLit.type = "boolean";
    }

    @Override
    public void visitComandoComposto(ComandoComposto compositeCommands) {
        if (compositeCommands.listOfCommands != null) {
            compositeCommands.listOfCommands.visit(this);
        }
    }

    @Override
    public void visitCondicional(Condicional conditional) {
        if (conditional.expression != null) {
            conditional.expression.visit(this);
            if (!conditional.expression.type.equals("boolean")) {
                throw new contextAnalysisException("ERRO DE CONTEXTO: Esperava-se expressao booleana.', Linha: " + conditional.expression.operator.line + ", Coluna: " + conditional.expression.operator.col);
            }

        }
    }

    @Override
    public void visitCorpo(Corpo body) {
        if (body.declarations != null) {
            body.declarations.visit(this);
        }

        body.compositeCommand.visit(this);

    }

    @Override
    public void visitDeclaracaoDeVariavel(DeclaracaoDeVariavel variableDeclaration) {
        ListaDeIds aux = variableDeclaration.listOfIds;

        while (aux != null) {
            try {
                table.enter(aux.id, variableDeclaration);
            } catch (IOException ex) {
                Logger.getLogger(Checker.class.getName()).log(Level.SEVERE, null, ex);
            }
            aux = aux.next;

        }

        if (variableDeclaration.type instanceof TipoAgregado) {
            ((TipoAgregado) variableDeclaration.type).visit(this);

        } else {
            if (variableDeclaration.type instanceof TipoSimples) {
                ((TipoSimples) variableDeclaration.type).visit(this);
            }
        }
    }

    @Override
    public void visitDeclaracoes(Declaracoes declarations) {
        Declaracoes aux = declarations;
        while (aux != null) {
            aux.declarationOfVariable.visit(this);
            aux = aux.next;
        }
    }

    @Override
    public void visitExpressao(Expressao expression) {
        if (expression.simpleExpression != null) {
            expression.simpleExpression.visit(this); 
            expression.type = expression.simpleExpression.type;
        }
        if (expression.simpleExpressionR != null) {
            expression.simpleExpressionR.visit(this);
            if ("real".equals(expression.simpleExpressionR.type) || expression.simpleExpressionR.type.equals("integer")) {
                expression.type = "boolean";
            } else {
                throw new contextAnalysisException("ERRO DE CONTEXTO: Esperava-se expressao booleana.', Linha: " + expression.operator.line + ", Coluna: " + expression.operator.col);
            }

        }

    }

    @Override
    public void visitExpressaoSimples(ExpressaoSimples simpleExpression) {
        ExpressaoSimples aux = simpleExpression; 
        String place = null;
        while (aux != null) {
            if (aux.term != null) {
                aux.term.visit(this);
                if (place == null) {
                    place = aux.term.type; 
                }
                if (aux.operator != null) {
                    switch (aux.operator.kind) {
                        case Token.SUM:
                        case Token.SUB:
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
                                            throw new contextAnalysisException("ERRO DE CONTEXTO: Operandos inválidos.', Linha: " + aux.operator.line + ", Coluna: " + aux.operator.col);
                                        }

                                    }
                                    break;
                                case "real":
                                    if (aux.term.type.equals("boolean")) {
                                        throw new contextAnalysisException("ERRO DE CONTEXTO: Operandos inválidos.', Linha: " + aux.operator.line + ", Coluna: " + aux.operator.col);
                                    }
                                    place = "real";
                                    break;
                                default: {
                                    throw new contextAnalysisException("ERRO DE CONTEXTO: Operandos inválidos.', Linha: " + aux.operator.line + ", Coluna: " + aux.operator.col);
                                }

                            }
                            break;
                        case Token.OR:
                            if (!place.equals("boolean") || !aux.term.type.equals("boolean")) {
                                throw new contextAnalysisException("ERRO DE CONTEXTO: Operandos inválidos.', Linha: " + aux.operator.line + ", Coluna: " + aux.operator.col);
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
    public void visitIterativo(Iterativo iterative) {
        if (iterative.expression != null) {
            iterative.expression.visit(this);
        }

        if (!iterative.expression.type.equals("boolean")) {
            throw new contextAnalysisException("ERRO DE CONTEXTO: Compilação encerrada durante a ANÁLISE DE CONTEXTO.', Linha: " + iterative.expression.operator.line + ", Coluna: " + iterative.expression.operator.col);
        }

        if (iterative.command instanceof Atribuicao) {
            ((Atribuicao) iterative.command).visit(this);
        } else if (iterative.command instanceof ComandoComposto) {
            ((ComandoComposto) iterative.command).visit(this);
        } else if (iterative.command instanceof Iterativo) {
            ((Iterativo) iterative.command).visit(this);
        } else if (iterative.command instanceof Condicional) {
            ((Condicional) iterative.command).visit(this);
        }
    }

    @Override
    public void visitListaDeComandos(ListaDeComandos listOfCommands) {
        ListaDeComandos aux = listOfCommands;
        while (aux != null) {
            if (aux.command instanceof Atribuicao) {
                ((Atribuicao) aux.command).visit(this);
            } else if (aux.command instanceof ComandoComposto) {
                ((ComandoComposto) aux.command).visit(this);
            } else if (aux.command instanceof Iterativo) {
                ((Iterativo) aux.command).visit(this);
            } else if (aux.command instanceof Condicional) {
                ((Condicional) aux.command).visit(this);
            }
            aux = aux.next;
        }
    }

    @Override
    public void visitListaDeIds(ListaDeIds listOfIds) {

    }

    @Override
    public void visitLiteral(Literal literal) {
        
        switch (literal.name.kind) {
            case Token.INT_LIT:
                literal.type = "integer";
                break;
            case Token.FLOAT_LIT:
                literal.type = "real";
                break;
            case Token.BOOLEAN:
                literal.type = "boolean";
                break;
        }
    }

    @Override
    public void visitPrograma(Programa program) {
        program.body.visit(this);
    }

    @Override
    public void visitSeletor(Seletor selector) {
        Seletor aux = selector;
        while (aux != null) {
            aux.expression.visit(this);
            if (!aux.expression.type.equals("integer")) {
                throw new contextAnalysisException("ERRO DE CONTEXTO: Seletor inválido.', Linha: " + aux.expression.operator.line + ", Coluna: " + aux.expression.operator.col);
            }
            aux = aux.next;
        }

    }

    @Override
    public void visitTermo(Termo term) {
        Termo aux = term;
        String place = null;
        
        while (aux != null) {
            if (aux.factor != null) {                 
                if (aux.factor instanceof Variavel) {
                    ((Variavel) aux.factor).visit(this);
                    term.type = aux.factor.type;
                } else if (aux.factor instanceof Literal) {
                    ((Literal) aux.factor).visit(this);
                    term.type = aux.factor.type;
                } else if (aux.factor instanceof Expressao) {
                    ((Expressao) aux.factor).visit(this);
                    term.type = aux.factor.type;
                }                 
                if (place == null) {
                    place = term.type;
                }

                if (aux.operator != null) {                    
                    switch (aux.operator.kind) {
                        case Token.DIV:
                            if (place.equals("boolean") || term.type.equals("boolean")) {
                                throw new contextAnalysisException("ERRO DE CONTEXTO: Operandos inválidos.', Linha: " + aux.operator.line + ", Coluna: " + aux.operator.col);
                            }
                            place = "real";
                            break;

                        case Token.MULT:
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
                                            throw new contextAnalysisException("ERRO DE CONTEXTO: Operandos inválidos.', Linha: " + aux.operator.line + ", Coluna: " + aux.operator.col);
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
                                            throw new contextAnalysisException("ERRO DE CONTEXTO: Operandos inválidos.', Linha: " + aux.operator.line + ", Coluna: " + aux.operator.col);
                                        }
                                    }

                                    break;

                                default: {
                                    throw new contextAnalysisException("ERRO DE CONTEXTO: Operandos inválidos.', Linha: " + aux.operator.line + ", Coluna: " + aux.operator.col);
                                }
                            }

                            break;

                        case Token.AND:                            
                            if (!place.equals("boolean") || !term.type.equals("boolean")) {
                                throw new contextAnalysisException("ERRO DE CONTEXTO: Operandos inválidos.', Linha: " + aux.operator.line + ", Coluna: " + aux.operator.col);
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
    public void visitTipoAgregado(TipoAgregado type) {

        if (type.typo instanceof TipoAgregado) {
            ((TipoAgregado) type.typo).visit(this);
        } else {
            if (type.typo instanceof TipoSimples) {
                ((TipoSimples) type.typo).visit(this);
            }
        }
        type.type = type.typo.type;
    }

    @Override
    public void visitTipoSimples(TipoSimples type) {
        type.type = type.typo.value;
    }

    @Override
    public void visitVariavel(Variavel variable) {
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
