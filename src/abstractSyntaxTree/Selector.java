package abstractSyntaxTree;

public class Selector {

    public Expression expression;
    public Selector next;

    public void visit(Visitor v) {
        v.visitSeletor(this);
    }
}
