package compilador.abstractSyntaxTree;

public class BoolLit extends Literal {
    
    @Override
    public void visit(Visitor v){
        v.visitBoolLit(this);
    }
}
