package compilador.abstractSyntaxTree;

public class Declaracoes {
    public DeclaracaoDeVariavel declarationOfVariable;
    public Declaracoes next;
    
    public void visit(Visitor v){
        v.visitDeclaracoes(this);
    }
}
