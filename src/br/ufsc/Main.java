package br.ufsc;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String key;

        Scanner input = new Scanner(System.in);
        System.out.println("Digite a chave que você deseja usar para cifrar o arquivo: ");
        key = input.nextLine();

        while (key.length() != 16) {
            System.out.println("A chave deve ter exatamente 16 caractéres, por favor digite novamente: ");
            key = input.nextLine();
        }

        String fileName;
        System.out.println("Digite o arquivo que deve ser cifrado: ");
        fileName = input.nextLine();

        EncryptFile.encryptFile(key,fileName);
    }
}
