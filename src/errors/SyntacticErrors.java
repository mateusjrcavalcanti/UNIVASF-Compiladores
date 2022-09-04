package errors;

import exceptions.SyntacticException;
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
        totalErrors++;
        switch (type) {
            case 1:
                Logger.getLogger(this.name).log(
                        Level.SEVERE, null,
                        "[" + totalErrors + "] ERRO SINTÁTICO 1.1 - Token inesperado."
                );
                Logger.getLogger(this.name).log(Level.SEVERE, null, msg);
                break;
            case 2:
                Logger.getLogger(this.name).log(
                        Level.SEVERE, null,
                        "[" + totalErrors + "] ERRO SINTÁTICO 1.2 - Booleano invalido. (Atribuição Lógica deve conter 'true' ou 'false')"
                );
                Logger.getLogger(this.name).log(Level.SEVERE, null, msg);
                break;
            case 3:
                Logger.getLogger(this.name).log(
                        Level.SEVERE, null,
                        "[" + totalErrors + "] ERRO SINTÁTICO 1.3 - Comando invalido. (Comando deve ser do tipo ATRIBUIÇAO (IDENTIFIER), CONDICIONAL (IF), ITERATIVO (WHILE) ou COMANDO COMPOSTO (BEGIN)"
                );
                Logger.getLogger(this.name).log(Level.SEVERE, null, msg);
                break;
            case 4:
                Logger.getLogger(this.name).log(
                        Level.SEVERE, null,
                        "[" + totalErrors + "] ERRO SINTÁTICO 1.4 - Fator invalido. (Fator espera receber ATRIBUIÇAO (IDENTIFIER), LITERAL ou EXPRESSAO ENTRE PARENTESES)"
                );
                Logger.getLogger(this.name).log(Level.SEVERE, null, msg);
                break;
            case 5:
                Logger.getLogger(this.name).log(
                        Level.SEVERE, null,
                        "[" + totalErrors + "] ERRO SINTÁTICO 1.5 - Literal invalido. (Literal deve ser do tipo BOOLEANO ('true' ou 'false'), INTEIRO ou FLOAT)"
                );
                Logger.getLogger(this.name).log(Level.SEVERE, null, msg);
                break;
            case 6:
                Logger.getLogger(this.name).log(
                        Level.SEVERE, null,
                        "[" + totalErrors + "] ERRO SINTÁTICO 1.6 - Tipo invalido. (Tipo deve ser TIPO AGREGADO (array) ou TIPO SIMPLES (inteiro, real ou booleano))"
                );
                Logger.getLogger(this.name).log(Level.SEVERE, null, msg);
                break;
        }
    }

    public int getNumberErrors() {
        return totalErrors;
    }

    public void status() {
        if (totalErrors > 0) {
            throw new SyntacticException("Compilação interrompida durante a ANÁLISE SINTATICA. ("
                    + totalErrors
                    + " erros encontrados)"
            );
        } else {
            Logger.getLogger(this.name).log(
                    Level.INFO,
                    "Análise Sintática concluída com sucesso!"
            );
        }
    }
}
