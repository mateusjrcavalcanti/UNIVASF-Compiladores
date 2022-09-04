package abstractSyntaxTree;

public class CommandComposite extends Command {

    public CommandsList listOfCommands;

    public void visit(Visitor v) {
        v.visitComandoComposto(this);
    }
}
