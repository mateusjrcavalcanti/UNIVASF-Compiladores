package abstractSyntaxTree;

public class Body {

    public Declarations declarations;
    public CommandComposite compositeCommand;

    public void visit(Visitor v) {
        v.visitCorpo(this);
    }

}
