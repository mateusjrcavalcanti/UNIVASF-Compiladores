package compilador.abstractSyntaxTree;

import compilador.lexico.Token;

public class Variavel extends Fator{
    public Token id;
    public Seletor selector;
    public DeclaracaoDeVariavel declaration;
    
    
    public void visit(Visitor v){
        v.visitVariavel(this);
    }
}