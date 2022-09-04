package abstractSyntaxTree;

import lexicon.Token;

public class Literal extends Factor {

    public Token name;

    public void visit(Visitor v) {
        v.visitLiteral(this);
    }
}
