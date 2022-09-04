package abstractSyntaxTree;

public class TypeAggregate extends Type {
    public Literal literal1, literal2;
    public Type typo;
    
    public void visit(Visitor v){
        v.visitTipoAgregado(this);
    }
}