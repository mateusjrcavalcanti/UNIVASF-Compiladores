package compilador.abstractSyntaxTree;

public class DeclaracaoDeVariavel {
    public ListaDeIds listOfIds;
    public Tipo type;
    
    public void visit(Visitor v){
        v.visitDeclaracaoDeVariavel(this);
    }
}
