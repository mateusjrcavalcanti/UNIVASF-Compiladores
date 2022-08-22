package compilador.abstractSyntaxTree;

import compilador.lexico.Token;

public class ExpressaoSimples {
    public Termo term;
    public ExpressaoSimples next;
    public Token operator;
    public String type;
    
    public void visit(Visitor v){
        v.visitExpressaoSimples(this);
    }
}
