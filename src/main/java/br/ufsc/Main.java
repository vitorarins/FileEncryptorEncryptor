package br.ufsc;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.Console;
import java.io.IOException;
import java.security.Security;

public class Main {

    public static void main(String[] args) {

        try {
            int addProvider = Security.addProvider(new BouncyCastleProvider());

            Console console = System.console();
            String enteredPassword = new String(console.readPassword("Digite sua senha: "));
            String workDir = args[1];
            if (args[0].equals("-e")){
                FileOperations.encryptFiles(workDir,enteredPassword);
                System.out.println("Arquivos cifrados com sucesso!");
            }
            if (args[0].equals("-d")){
                FileOperations.decryptFiles(workDir,enteredPassword);
                System.out.println("Arquivos decifrados com sucesso!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
