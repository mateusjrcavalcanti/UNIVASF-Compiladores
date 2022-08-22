package compilador.abstractSyntaxTree;

import compilador.lexico.Token;

public class Expressao extends Fator{
    public ExpressaoSimples simpleExpression;
    public ExpressaoSimples simpleExpressionR;
    public Token operator;
    
    public void visit(Visitor v){
        v.visitExpressao(this);
    }
}
