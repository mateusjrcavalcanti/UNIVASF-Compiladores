package abstractSyntaxTree;

public class BooleanLiteral extends Literal {

    @Override
    public void visit(Visitor v) {
        v.visitBoolLit(this);
    }
}
