package abstractSyntaxTree;

public class Iterative extends Command {

    public Expression expression;
    public Command command;

    public void visit(Visitor v) {
        v.visitIterativo(this);
    }
}
