package compilador.abstractSyntaxTree;

public class Condicional extends Comando{
    public Expressao expression;
    public Comando command;
    public Comando commandElse;
    
    public void visit(Visitor v){
        v.visitCondicional(this);
    }
    
}
