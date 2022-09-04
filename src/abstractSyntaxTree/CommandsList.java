package abstractSyntaxTree;

public class CommandsList {

    public Command command;
    public CommandsList next;

    public void visit(Visitor v) {
        v.visitListaDeComandos(this);
    }
}
