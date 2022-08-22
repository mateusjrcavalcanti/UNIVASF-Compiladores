package compilador.abstractSyntaxTree;

public class ComandoComposto extends Comando{
    public ListaDeComandos listOfCommands;
    
    public void visit(Visitor v){
        v.visitComandoComposto(this);
    }
}
