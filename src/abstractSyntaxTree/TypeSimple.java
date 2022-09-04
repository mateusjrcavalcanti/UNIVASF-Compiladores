package abstractSyntaxTree;

import lexicon.Token;

public class TypeSimple extends Type {

    public Token typo;

    public void visit(Visitor v) {
        v.visitTipoSimples(this);
    }
}
