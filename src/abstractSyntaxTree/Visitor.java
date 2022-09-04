package abstractSyntaxTree;

public interface Visitor {

    public void visitAtribuicao(Attribution becomes);

    public void visitBoolLit(BooleanLiteral boolLit);

    public void visitComandoComposto(CommandComposite compositeCommands);

    public void visitCondicional(Conditional conditional);

    public void visitCorpo(Body body);

    public void visitDeclaracaoDeVariavel(VariableDeclaration variableDeclaration);

    public void visitDeclaracoes(Declarations declarations);

    public void visitExpressao(Expression expression);

    public void visitExpressaoSimples(ExpressionSimple simpleExpression);

    public void visitIterativo(Iterative iterative);

    public void visitListaDeComandos(CommandsList listOfCommands);

    public void visitListaDeIds(IdList listOfIds);

    public void visitLiteral(Literal literal);

    public void visitPrograma(Program program);

    public void visitSeletor(Selector selector);

    public void visitTermo(Term term);

    public void visitTipoAgregado(TypeAggregate type);

    public void visitTipoSimples(TypeSimple type);

    public void visitVariavel(Variable variable);
}
