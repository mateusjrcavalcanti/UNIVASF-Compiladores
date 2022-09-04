package abstractSyntaxTree;

public class Conditional extends Command {

    public Expression expression;
    public Command command;
    public Command commandElse;

    public void visit(Visitor v) {
        v.visitCondicional(this);
    }

}
