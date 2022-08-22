package compilador.abstractSyntaxTree;

public class Corpo {
    public Declaracoes declarations;
    public ComandoComposto compositeCommand;
    
    public void visit(Visitor v){
        v.visitCorpo(this);
    }
    
}