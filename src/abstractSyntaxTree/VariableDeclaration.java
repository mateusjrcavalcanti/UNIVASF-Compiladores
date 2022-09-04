package abstractSyntaxTree;

public class VariableDeclaration {

    public IdList listOfIds;
    public Type type;

    public void visit(Visitor v) {
        v.visitDeclaracaoDeVariavel(this);
    }
}
