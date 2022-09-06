package errors;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SyntacticErrors {

    private int totalErrors;
    private String name;

    public SyntacticErrors(Class classe) {
        this.totalErrors = 0;
        this.name = classe.getName();
    }

    public void error(int type, String msg) {
        this.totalErrors++;
        switch (type) {
            case 1:
                Logger.getLogger(this.name).log(Level.SEVERE, "[{0}] ERRO SINT\u00c1TICO 1.1 - Token inesperado.", this.totalErrors);                
                break;
            case 2:
                Logger.getLogger(this.name).log(Level.SEVERE, "[{0}] ERRO SINT\u00c1TICO 1.2 - Booleano invalido. (Atribui\u00e7\u00e3o L\u00f3gica deve conter ''true'' ou ''false'')", this.totalErrors);                
                break;
            case 3:
                Logger.getLogger(this.name).log(Level.SEVERE, "[{0}] ERRO SINT\u00c1TICO 1.3 - Comando invalido. (Comando deve ser do tipo ATRIBUI\u00c7AO (IDENTIFIER), CONDICIONAL (IF), ITERATIVO (WHILE) ou COMANDO COMPOSTO (BEGIN)", this.totalErrors);                
                break;
            case 4:
                Logger.getLogger(this.name).log(Level.SEVERE, "[{0}] ERRO SINT\u00c1TICO 1.4 - Fator invalido. (Fator espera receber ATRIBUI\u00c7AO (IDENTIFIER), LITERAL ou EXPRESSAO ENTRE PARENTESES)", this.totalErrors);                
                break;
            case 5:
                Logger.getLogger(this.name).log(Level.SEVERE, "[{0}] ERRO SINT\u00c1TICO 1.5 - Literal invalido. (Literal deve ser do tipo BOOLEANO (''true'' ou ''false''), INTEIRO ou FLOAT)", this.totalErrors);                
                break;
            case 6:
                Logger.getLogger(this.name).log(Level.SEVERE, "[{0}] ERRO SINT\u00c1TICO 1.6 - Tipo invalido. (Tipo deve ser TIPO AGREGADO (array) ou TIPO SIMPLES (inteiro, real ou booleano))", this.totalErrors);                
                break;
        }
        Logger.getLogger(this.name).log(Level.SEVERE, msg);
    }

    public int getNumberErrors() {
        return totalErrors;
    }

    public void status() {
        if (this.totalErrors > 0) {          
            Logger.getLogger(this.name).log(Level.INFO, "Compila\u00e7\u00e3o interrompida durante a AN\u00c1LISE SINTATICA. ({0} erros encontrados)", this.totalErrors);
            System.exit(0);
        } else {
            Logger.getLogger(this.name).log(
                    Level.INFO,
                    "Análise Sintática concluída com sucesso!"
            );
        }
    }
}
