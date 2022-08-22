package compilador.abstractSyntaxTree;

public class TipoAgregado extends Tipo {
    public Literal literal1, literal2;
    public Tipo typo;
    
    public void visit(Visitor v){
        v.visitTipoAgregado(this);
    }
}