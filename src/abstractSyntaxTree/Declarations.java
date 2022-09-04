package abstractSyntaxTree;

public class Declarations {

    public VariableDeclaration declarationOfVariable;
    public Declarations next;

    public void visit(Visitor v) {
        v.visitDeclaracoes(this);
    }
}
