package compilador.abstractSyntaxTree;

public class Programa {
    public Corpo body;
    
    public void visit(Visitor v){
        v.visitPrograma(this);
    }
}
