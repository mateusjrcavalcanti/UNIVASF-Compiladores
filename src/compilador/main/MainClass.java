package compilador.main;

import compilador.TreeDrawer.Printer;
import compilador.abstractSyntaxTree.Programa;
import compilador.contextAnalysis.Checker;
import compilador.exceptions.LexicalException;
import compilador.exceptions.SyntaxException;
import compilador.exceptions.contextAnalysisException;
import compilador.lexico.Scanner;
import compilador.lexico.Token;
import compilador.sintatico.Parser;
import java.io.File;
import java.io.FileWriter;

public class MainClass {

    public static void main(String[] args) throws Exception {
        int opcao;
        java.util.Scanner ler = new java.util.Scanner(System.in);
        String FileDir;
        System.out.print("Caminho do código fonte: ");
        FileDir = ler.nextLine();

        if ("".equals(FileDir)) {
            FileDir = "program.pas";
        }

        do {
            System.out.println("Selecione até qual fase deseja executar o compilador: ");
            System.out.println("1 - Análise Léxica");
            System.out.println("2 - Análise Sintática");
            System.out.println("3 - Impressão da Árvore");
            System.out.println("4 - Análise de Contexto");
            System.out.println("5 - Geração de Código");
            System.out.println("6 - Sair");
            System.out.print("Opção: ");
            try {
                opcao = Integer.parseInt(ler.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }
        } while (opcao < 1 || opcao > 6);

        switch (opcao) {
            case 1:
                System.out.println("----------------------------------------------------------------");
                System.out.println("----------------------- ANÁLISE LÉXICA -------------------------");
                System.out.println("----------------------------------------------------------------");
                try {
                    Scanner sc = new Scanner(FileDir);
                    Token token = null;

                    while (token == null || Token.EOF != token.kind) {
                        token = sc.scan();
                        if (token != null) {
                            System.out.println(token.toString());
                        }
                    }
                } catch (LexicalException ex) {
                    System.out.println("Lexical Error " + ex.getMessage());
                } catch (Exception ex) {
                    System.out.println("Generic Error!!");
                    System.out.println(ex.getClass().getName());
                }
                break;
            case 2:
                System.out.println("----------------------------------------------------------------");
                System.out.println("--------------------- ANÁLISE SINTÁTICA ------------------------");
                System.out.println("----------------------------------------------------------------");
                try {
                    Programa program = null;
                    Parser parser = new Parser();
                    program = parser.parse(FileDir);
                } catch (SyntaxException ex) {
                    System.out.println("Syntax Error " + ex.getMessage());
                } catch (Exception ex) {
                    System.out.println("Generic Error!!");
                    System.out.println(ex.getClass().getName());
                }
                break;
            case 3:
                System.out.println("----------------------------------------------------------------");
                System.out.println("---------------------- ÁRVORE SINTÁTICA ------------------------");
                System.out.println("----------------------------------------------------------------");
                try {
                    Parser parser = new Parser();
                    Printer printer = new Printer();
                    Programa program = parser.parse(FileDir);
                    printer.print(program);

                    FileWriter TreeFile = new FileWriter(new File("Outputs/Tree.txt"));
                    String Tree = printer.print(program);
                    TreeFile.write(Tree);
                    TreeFile.close();

                } catch (LexicalException ex) {
                    System.out.println("Lexical Error " + ex.getMessage());
                } catch (SyntaxException ex) {
                    System.out.println("Syntax Error " + ex.getMessage());
                } catch (contextAnalysisException ex) {
                    System.out.println("Context Error " + ex.getMessage());
                } catch (Exception ex) {
                    System.out.println("Generic Error!!");
                    System.out.println(ex.getClass().getName());
                }
                break;
            case 4:
                System.out.println("----------------------------------------------------------------");
                System.out.println("----------------------- ANÁLISE DE CONTEXTO --------------------");
                System.out.println("----------------------------------------------------------------");

                Parser parser = new Parser();
                Checker checker = new Checker();
                Programa program = parser.parse(FileDir);

                checker.check(program);  // Erro aqui              

                //FileWriter TableFile = new FileWriter(new File("Outputs/Table.txt"));
                //String Tree = checker.ImpimeIdentificationTable();
                //TableFile.write(Tree);
                //TableFile.close();

                break;
            default:
                System.exit(0);
                break;
        }
    }
}
