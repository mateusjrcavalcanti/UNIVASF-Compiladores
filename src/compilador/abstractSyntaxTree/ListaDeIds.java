package compilador.abstractSyntaxTree;

import compilador.lexico.Token;

public class ListaDeIds {
    public Token id;
    public ListaDeIds next;
    
    public void visit(Visitor v){
        v.visitListaDeIds(this);
    }
}
