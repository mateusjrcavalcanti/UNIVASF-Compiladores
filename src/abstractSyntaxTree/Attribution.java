package abstractSyntaxTree;

public class Attribution extends Command {

    public Variable variable;
    public Expression expression;
    public String type;

    public void visit(Visitor v) {
        v.visitAtribuicao(this);
    }
}
