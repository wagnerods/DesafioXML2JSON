package br.edu.cefsa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class DesafioXML2JSON {

	static Scanner scan;
	static PrintWriter pWriter;
	static FileWriter fWriter;
	static Regras regras = new Regras();

	public static void main(String[] args) throws IOException {

		Verificar();
	}

	public static void Verificar() throws IOException {
		regras.contar = 0;
		regras.contar2 = 0;
		regras.variavel = "";
		try {
			fWriter = new FileWriter("./src/Exemplo de JSON.txt");
			pWriter = new PrintWriter(fWriter);
			scan = new Scanner(new File("./src/Exemplo de XML.xml"), "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (scan.hasNextLine()) { 
			String line = scan.nextLine();
			estaValido(line);
			String conteudo = estaValido(line);

			pWriter.write("\n" + conteudo);
			System.out.println(conteudo);
			pWriter.flush();
		}
	}

	private static String estaValido(String expr) {
		String t = expr.trim();
		Deque<Character> pilha = new ArrayDeque<Character>();
		String status = null;
		int count = 0;
		String variavel = "";
		String conteudo = "";
		boolean l = false;

		for (int i = 0; i < t.length(); i++) {
			char x = t.charAt(i);

			if (regras.contar == 2 && x != '<' && x != '>') {
				regras.variavel += x;
			}
			if (regras.contar == 0 && x == '<') {
				conteudo += "{" + "\n";
				count++;
				regras.contar2 = regras.contar2 + 1;
			}

			if (x == '<' && regras.status != "fechado") {
				conteudo += '"';
				regras.status = "aberto";
				status = "aberto";
				regras.contar2 = regras.contar2 + 1;
			} else if (x == '>' && status == "aberto" && count == 1) {
				conteudo += '"' + ": ";
				regras.status = "fechado";
			} else if (x == '>' && status == "aberto" && count != 1) {
				conteudo += '"' + ": " + '"';
				regras.status = "fechado";
			} else if (x == '<' && regras.status == "fechado") {
				l = true;
				conteudo += '"' + ",";
				break;
			} else if (l == false) {
				conteudo += x;
			}
		}
		regras.status = "";

		String tag1 = '<' + regras.variavel + ">";
		String tag2 = "</" + regras.variavel + ">";

		if ((t == null ? tag1 == null : t.equals(tag1)) && regras.contar != 2) {
			regras.contar++;

			return "{";
		}
		if (regras.contar2 == 2) {
			return conteudo + " [";
		} else if ((t == null ? tag2 == null : t.equals(tag2)) && regras.contar != 2) {
			regras.contar++;

			return "},";
		} else {
			regras.contar = regras.contar + 1;
			return conteudo;
		}
	}
}
