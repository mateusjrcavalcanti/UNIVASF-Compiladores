package compilador.abstractSyntaxTree;

import compilador.lexico.Token;

public class Termo {
    public Fator factor;
    public Termo next;
    public Token operator;
    public String type;
    
    public void visit(Visitor v){
        v.visitTermo(this);
    }
}
