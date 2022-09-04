package abstractSyntaxTree;

import lexicon.Token;

public class Expression extends Factor {

    public ExpressionSimple simpleExpression;
    public ExpressionSimple simpleExpressionR;
    public Token operator;

    public void visit(Visitor v) {
        v.visitExpressao(this);
    }
}
