package compilador.abstractSyntaxTree;

public interface Visitor {
    
    public void visitAtribuicao(Atribuicao becomes);
    public void visitBoolLit(BoolLit boolLit);
    public void visitComandoComposto(ComandoComposto compositeCommands);
    public void visitCondicional(Condicional conditional);
    public void visitCorpo(Corpo body);
    public void visitDeclaracaoDeVariavel(DeclaracaoDeVariavel variableDeclaration);
    public void visitDeclaracoes(Declaracoes declarations);
    public void visitExpressao(Expressao expression);
    public void visitExpressaoSimples(ExpressaoSimples simpleExpression);
    public void visitIterativo(Iterativo iterative);
    public void visitListaDeComandos(ListaDeComandos listOfCommands);
    public void visitListaDeIds(ListaDeIds listOfIds);
    public void visitLiteral(Literal literal);
    public void visitPrograma(Programa program);
    public void visitSeletor(Seletor selector);
    public void visitTermo(Termo term);
    public void visitTipoAgregado(TipoAgregado type);
    public void visitTipoSimples(TipoSimples type);
    public void visitVariavel(Variavel variable);
    }

