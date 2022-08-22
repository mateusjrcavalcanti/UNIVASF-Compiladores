package compilador.abstractSyntaxTree;

import compilador.lexico.Token;

public class TipoSimples extends Tipo{
    public Token typo;
    
    public void visit(Visitor v){
        v.visitTipoSimples(this);
    }
}
