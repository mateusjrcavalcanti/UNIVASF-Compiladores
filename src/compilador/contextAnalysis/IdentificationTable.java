package compilador.contextAnalysis;

import compilador.abstractSyntaxTree.DeclaracaoDeVariavel;
import compilador.exceptions.contextAnalysisException;
import compilador.lexico.Token;
import java.io.IOException;
import java.util.HashMap;

public class IdentificationTable {

    public int erroV = 0;
    HashMap table;

    IdentificationTable() {
        table = new HashMap();
    }

    public void enter(Token id, DeclaracaoDeVariavel declaration) throws IOException {
        if (table.put(id.value, declaration) != null) {
            throw new contextAnalysisException("ERRO DE CONTEXTO: Compilação terminada durante a ANÁLISE DE CONTEXTO.', Linha: " + id.line);
        }
    }

    public DeclaracaoDeVariavel retrieve(Token id) throws IOException {
        if (table.containsKey(id.value) == false) {
            throw new contextAnalysisException("ERRO DE CONTEXTO: Compilação terminada durante a ANÁLISE DE CONTEXTO.', Linha: " + id.line);
        } else {
            return (DeclaracaoDeVariavel) table.get(id.value);
        }
    }

    public String print() {

        StringBuilder bd = new StringBuilder();

        bd.append("\t\n - IMPRESSÃO: TABELA DE IDENTIFICADORES\n");
        bd.append("\n--------------------------------------------------------------------------------------------------");
        bd.append("\n " + "VAR" + "\t|\t" + "\tADDR");
        bd.append("\n--------------------------------------------------------------------------------------------------");
        bd.append("\n--------------------------------------------------------------------------------------------------");

        table.keySet().forEach((name) -> {
            String key = name.toString();
            String value = table.get(name).toString();
            bd.append("\n ").append(key).append(" \t|\t").append(value);
            bd.append("\n--------------------------------------------------------------------------------------------------");
        });

        String tableprint = bd.toString();
        return (tableprint);
    }

}
