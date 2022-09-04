package abstractSyntaxTree;

import lexicon.Token;

public class Term {

    public Factor factor;
    public Term next;
    public Token operator;
    public String type;

    public void visit(Visitor v) {
        v.visitTermo(this);
    }
}
