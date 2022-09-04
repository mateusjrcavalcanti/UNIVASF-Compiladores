package Compiler;

import abstractSyntaxTree.Program;
import contextAnalysis.Checker;
import codeGenerator.Coder;
import exceptions.ContextException;
import exceptions.LexiconException;
import exceptions.SyntacticException;
import syntactic.Parser;
import treeDrawer.Printer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainClass {

    public static void main(String[] args) throws Exception {

        // Instâncias     
        FileWriter fileTree, fileTable, fileCode;
        Program program = null;
        Parser parser = new Parser();
        Printer printer = new Printer();
        Checker checker = new Checker();
        Coder coder = new Coder();

        // Define o caminho do Arquivo
        java.util.Scanner inputData = new java.util.Scanner(System.in);
        System.out.print("Caminho do código fonte: ");
        String programDir = inputData.nextLine();
        if (programDir.equals("")) {
            programDir = "program.pas";
        }

        //PARSE para PROGRAM;
        try {
            program = parser.parse(programDir);
            checker.check(program);
        } catch (LexiconException ex) {
            Logger.getLogger(ex.getClass().getName()).log(Level.SEVERE, null, "Lexicon Error " + ex.getMessage());
        } catch (SyntacticException ex) {
            Logger.getLogger(ex.getClass().getName()).log(Level.SEVERE, null, "Syntactic Error " + ex.getMessage());
        } catch (ContextException ex) {
            Logger.getLogger(ex.getClass().getName()).log(Level.SEVERE, null, "Context Error " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Generic Error!!");
            System.out.println(ex.getClass().getName());
        }

        //Salva a Árvore;
        try {
            fileTree = new FileWriter(new File("out/Tree.txt"));
            fileTree.write(printer.print(program));
            fileTree.close();
        } catch (IOException ex) {
            Logger.getLogger(ex.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        //Salva a Tabela;
        try {
            fileTable = new FileWriter(new File("out/Table.txt"));
            fileTable.write(checker.getTable().print());
            fileTable.close();
        } catch (IOException ex) {
            Logger.getLogger(ex.getClass().getName()).log(Level.SEVERE, null, ex);
        }

        //Salva o Código;
        try {
            fileCode = new FileWriter(new File("out/Code.txt"));
            fileCode.write(coder.encode(program));
            fileCode.close();
        } catch (IOException ex) {
            Logger.getLogger(ex.getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }
}
