package compilador.abstractSyntaxTree;

public class Atribuicao extends Comando{
    public Variavel variable;
    public Expressao expression;
    public String type;
    
    public void visit(Visitor v){
        v.visitAtribuicao(this);
    }
}

