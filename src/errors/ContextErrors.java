package errors;

import exceptions.ContextException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContextErrors {

    private int totalErrors;
    private String name;

    public ContextErrors(Class classe) {
        this.totalErrors = 0;
        this.name = classe.getName();
    }

    public void error(int type, String msg) {
        switch (type) {
            case 11:
                Logger.getLogger(this.name).log(
                        Level.SEVERE, null,
                        "[" + totalErrors + "] ERRO DE CONTEXTO 2.1.1"
                );
                break;
            case 12:
                Logger.getLogger(this.name).log(
                        Level.SEVERE, null,
                        "[" + totalErrors + "] ERRO DE CONTEXTO 2.1.2"
                );
                break;
            case 21:
                Logger.getLogger(this.name).log(
                        Level.SEVERE, null,
                        "[" + totalErrors + "] ERRO DE CONTEXTO 2.2.1 - Atribuicao de valores incompatíveis."
                );
                break;
            case 22:
                Logger.getLogger(this.name).log(
                        Level.SEVERE, null,
                        "[" + totalErrors + "] ERRO DE CONTEXTO 2.2.2 - Esperava-se expressao booleana."
                );
                break;
            case 23:
                Logger.getLogger(this.name).log(
                        Level.SEVERE, null,
                        "[" + totalErrors + "] ERRO DE CONTEXTO 2.2.3 - Comparacao entre valores incompativeis."
                );
                break;
            case 24:
                Logger.getLogger(this.name).log(
                        Level.SEVERE, null,
                        "[" + totalErrors + "] ERRO DE CONTEXTO 2.2.4 - Operandos inválidos."
                );
                break;
            case 25:
                Logger.getLogger(this.name).log(
                        Level.SEVERE, null,
                        "[" + totalErrors + "] ERRO DE CONTEXTO 2.2.5 - Seletor inválido."
                );
                break;
            default:
        }

        Logger.getLogger(this.name).log(Level.SEVERE, null, msg);
    }

    public int getNumberErrors() {
        return totalErrors;
    }

    public void status() {
        if (totalErrors > 0) {
            throw new ContextException("Compilação interrompida durante a ANÁLISE DE CONTEXTO. ("
                    + totalErrors
                    + " erros encontrados)"
            );
        } else {
            Logger.getLogger(this.name).log(
                    Level.INFO,
                    "Análise de contexto foi concluída com sucesso!"
            );
        }
    }
}
