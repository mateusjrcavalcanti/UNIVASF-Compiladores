package abstractSyntaxTree;

import lexicon.Token;

public class Variable extends Factor {

    public Token id;
    public Selector selector;
    public VariableDeclaration declaration;

    public void visit(Visitor v) {
        v.visitVariavel(this);
    }
}
