package errors;

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
        this.totalErrors++;
        switch (type) {
            case 11:
                Logger.getLogger(this.name).log(Level.SEVERE, "[{0}] ERRO DE CONTEXTO 2.1.1", this.totalErrors);
                break;
            case 12:
                Logger.getLogger(this.name).log(Level.SEVERE, "[{0}] ERRO DE CONTEXTO 2.1.2", this.totalErrors);
                break;
            case 21:
                Logger.getLogger(this.name).log(Level.SEVERE, "[{0}] ERRO DE CONTEXTO 2.2.1 - Atribuicao de valores incompat\u00edveis.", this.totalErrors);
                break;
            case 22:
                Logger.getLogger(this.name).log(Level.SEVERE, "[{0}] ERRO DE CONTEXTO 2.2.2 - Esperava-se expressao booleana.", this.totalErrors);
                break;
            case 23:
                Logger.getLogger(this.name).log(Level.SEVERE, "[{0}] ERRO DE CONTEXTO 2.2.3 - Comparacao entre valores incompativeis.", this.totalErrors);
                break;
            case 24:
                Logger.getLogger(this.name).log(Level.SEVERE, "[{0}] ERRO DE CONTEXTO 2.2.4 - Operandos inv\u00e1lidos.", this.totalErrors);
                break;
            case 25:
                Logger.getLogger(this.name).log(Level.SEVERE, "[{0}] ERRO DE CONTEXTO 2.2.5 - Seletor inv\u00e1lido.", this.totalErrors);
                break;
            default:
        }

        Logger.getLogger(this.name).log(Level.SEVERE, msg);
    }

    public int getNumberErrors() {
        return this.totalErrors;
    }

    public void status() {
        if (this.totalErrors > 0) {
            Logger.getLogger(this.name).log(Level.INFO, "Compila\u00e7\u00e3o interrompida durante a AN\u00c1LISE DE CONTEXTO. ({0} erros encontrados)", this.totalErrors);
            System.exit(0);
        } else {
            Logger.getLogger(this.name).log(
                    Level.INFO,
                    "Análise de contexto foi concluída com sucesso!"
            );
        }
    }
}
