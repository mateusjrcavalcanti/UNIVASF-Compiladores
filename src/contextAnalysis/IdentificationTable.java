package contextAnalysis;

import abstractSyntaxTree.VariableDeclaration;
import errors.ContextErrors;
import lexicon.Token;
import java.io.IOException;
import java.util.HashMap;

public class IdentificationTable {

    HashMap table;
    ContextErrors Errors;

    IdentificationTable(ContextErrors Errors) {
        table = new HashMap();
        this.Errors = Errors;
    }

    public void enter(Token id, VariableDeclaration declaration) throws IOException {
        if (table.put(id.value, declaration) != null) {
            Errors.error(
                    11,
                    "Identificador " + id.value + " ja declarado." +
                    "na linha: " + id.line
            );
        }
    }

    public VariableDeclaration retrieve(Token id) throws IOException {
        if (table.containsKey(id.value) == false) {           
            Errors.error(
                    12,
                    "Identificador " + id.value + " nao declarado." +
                    "na linha: " + id.line
            );
        } else {
            return (VariableDeclaration) table.get(id.value);
        }
        return null;
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
