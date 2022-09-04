package abstractSyntaxTree;

public class Program {

    public Body body;

    public void visit(Visitor v) {
        v.visitPrograma(this);
    }
}
