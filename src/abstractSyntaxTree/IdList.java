package abstractSyntaxTree;

import lexicon.Token;

public class IdList {

    public Token id;
    public IdList next;

    public void visit(Visitor v) {
        v.visitListaDeIds(this);
    }
}
