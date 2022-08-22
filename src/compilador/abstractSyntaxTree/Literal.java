package compilador.abstractSyntaxTree;

import compilador.lexico.Token;

public class Literal extends Fator{
    public Token name;
    
    public void visit(Visitor v){
        v.visitLiteral(this);
    }
}
