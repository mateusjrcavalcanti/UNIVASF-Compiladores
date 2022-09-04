package abstractSyntaxTree;

import lexicon.Token;

public class ExpressionSimple {

    public Term term;
    public ExpressionSimple next;
    public Token operator;
    public String type;

    public void visit(Visitor v) {
        v.visitExpressaoSimples(this);
    }
}
