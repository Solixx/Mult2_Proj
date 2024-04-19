import java.io.*;
import java.util.Collections;

import java.util.Scanner;
import java.util.Random;

public class RunLengthEncoding {

    String word;
    String wordEncoded;
    String wordDecoded;

    public RunLengthEncoding(String word) {
        this.word = word;
    }

    public void runLengthEncodingText(){
        StringBuilder result = new StringBuilder();
        int count = 1;
        char[] chars = this.word.toCharArray();
        this.wordEncoded = runLengthEncodeCicle(result, count, chars);
    }

    public void runLengthEncodingTextFile(File file){
        try {
            Scanner myReader = new Scanner(file);
            StringBuilder stringToWrite = new StringBuilder();

            while(myReader.hasNextLine()){
                StringBuilder result = new StringBuilder();
                String word = myReader.nextLine();
                int count = 1;
                char[] chars = word.toCharArray();
                String encodedWord = runLengthEncodeCicle(result, count, chars);
                stringToWrite.append(encodedWord);
                stringToWrite.append("\n");
            }

            PrintWriter pw = new PrintWriter(new FileWriter(new File("StringsEncoded")));
            pw.write(String.valueOf(stringToWrite));
            pw.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String runLengthEncodeCicle(StringBuilder result, int count, char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (i + 1 < chars.length && c == chars[i + 1]) {
                count++;
            } else if(count >= 4){
                result.append(count).append(c);
                count = 1;
            }else {
                result.append(String.join("", Collections.nCopies(count, String.valueOf(c))));
                count = 1;
            }
        }
        return result.toString();
    }

    public void runLengthDecodeText(){
        StringBuilder result = new StringBuilder();
        char[] chars = this.wordEncoded.toCharArray();

        int count = 0;
        for (char c : chars) {
            if (Character.isDigit(c)) {
                count = 10 * count + Character.getNumericValue(c);
            } else {
                result.append(String.join("", Collections.nCopies(count, String.valueOf(c))));
                count = 0;
            }
        }

        this.wordDecoded = result.toString();
    }

    public void runLengthDecodeTextFile(File file){
        try {
            Scanner myReader = new Scanner(file);
            StringBuilder stringToWrite = new StringBuilder();

            while(myReader.hasNextLine()){
                StringBuilder result = new StringBuilder();
                String word = myReader.nextLine();
                char[] chars = word.toCharArray();

                int count = 0;
                for (char c : chars) {
                    if (Character.isDigit(c)) {
                        count = 10 * count + Character.getNumericValue(c);
                    } else {
                        result.append(String.join("", Collections.nCopies(count, String.valueOf(c))));
                        count = 0;
                    }
                }

                stringToWrite.append(result);
                stringToWrite.append("\n");
            }

            PrintWriter pw = new PrintWriter(new FileWriter(new File("StringsDecoded")));
            pw.write(String.valueOf(stringToWrite));
            pw.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void passoApasso(File file){
        try {
            Scanner myReader = new Scanner(file);
            StringBuilder stringToWriteFinal = new StringBuilder();
            StringBuilder stringToWrite = new StringBuilder();

            while(myReader.hasNextLine()){
                StringBuilder result = new StringBuilder();
                String word = myReader.nextLine();

                stringToWriteFinal.append("Linha Original: ").append(word);
                stringToWriteFinal.append("\n");

                int count = 1;
                char[] chars = word.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    char c = chars[i];

                    stringToWriteFinal.append("Character Lido: ").append(c);
                    stringToWriteFinal.append("\n");

                    if (i + 1 < chars.length && c == chars[i + 1]) {
                        count++;
                        stringToWriteFinal.append("Charater repetido logo incrementa count: ").append(count);
                        stringToWriteFinal.append("\n");
                    } else {
                        result.append(count).append(c);
                        count = 1;
                        stringToWriteFinal.append("Codificacao: ").append(result);
                        stringToWriteFinal.append("\n");
                    }
                }
                String encodedWord = result.toString();
                stringToWriteFinal.append("Linha Codificada: ").append(encodedWord);
                stringToWriteFinal.append("\n");

                stringToWrite.append(encodedWord);
                stringToWrite.append("\n");
            }

            stringToWriteFinal.append("Paragrafo Codificado: ").append(stringToWrite);
            stringToWriteFinal.append("\n");

            PrintWriter pw = new PrintWriter(new FileWriter(new File("TestSegundoModoPassoAPassoCodificado")));
            pw.write(String.valueOf(stringToWriteFinal));
            pw.close();

            File testeCodificado = new File("E:\\GitHub\\Mult2_Proj\\TestSegundoModoPassoAPassoCodificado");

            // Descodificacao
            StringBuilder result = new StringBuilder();
            stringToWriteFinal = new StringBuilder();


                stringToWriteFinal.append("Paragrafo Original: ").append(stringToWrite);
                stringToWriteFinal.append("\n");

                char[] chars = stringToWrite.toString().toCharArray();

                int count = 0;
                for (char c : chars) {

                    stringToWriteFinal.append("Character Lido: ").append(c);
                    stringToWriteFinal.append("\n");

                    if (Character.isDigit(c)) {
                        count = 10 * count + Character.getNumericValue(c);
                        stringToWriteFinal.append("Calculo do count: ").append(count);
                        stringToWriteFinal.append("\n");
                    } else {
                        result.append(String.join("", Collections.nCopies(count, String.valueOf(c))));
                        count = 0;
                        stringToWriteFinal.append("Descodificao: ").append(result);
                        stringToWriteFinal.append("\n");
                    }
                }

                stringToWrite = new StringBuilder();

                stringToWrite.append(result);
                stringToWrite.append("\n");

                stringToWriteFinal.append("Linha descodificada: ").append(result.toString());
                stringToWriteFinal.append("\n");

            stringToWriteFinal.append("Paragrafo Descodificado: " ).append(stringToWrite.toString());

            PrintWriter pw2 = new PrintWriter(new FileWriter(new File("TestSegundoModoPassoAPassoDescodificado")));
            pw2.write(String.valueOf(stringToWriteFinal));
            pw2.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void gerarPalavraAleatoria(File file, int comprimento, int numeroPalavras, int maxRepeat){
        String caracteresPermitidos = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();

        StringBuilder palavras =  new StringBuilder();

        for(int j = 0; j < numeroPalavras; j++){
            StringBuilder palavraAleatoria = new StringBuilder();
            for (int i = 0; i < comprimento; i++) {
                Random charRand = new Random();
                int charRandValue = charRand.nextInt(maxRepeat);
                int l = 0;

                int index = random.nextInt(caracteresPermitidos.length());

                while (l < charRandValue){
                    palavraAleatoria.append(caracteresPermitidos.charAt(index));
                    l++;
                }
            }
            palavras.append(palavraAleatoria);
            palavras.append("\n");
        }

        try{
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            pw.write(String.valueOf(palavras));
            pw.close();
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        RunLengthEncoding rle = new RunLengthEncoding("wwwwaaadexxxxxxywww");

        rle.runLengthEncodingText();
        rle.runLengthDecodeText();

        System.out.println("Econde: " + rle.wordEncoded);
        System.out.println("Decode: " + rle.wordDecoded);

        File file = new File("C:\\Users\\User\\Desktop\\UFP\\Git-Hub\\Mult2_Proj\\Strings");
        // C:\Users\User\Desktop\UFP\Git-Hub\Mult2_Proj\Strings
        // E:\GitHub\Mult2_Proj\Strings
        rle.gerarPalavraAleatoria(file, 10, 20, 10);
        rle.runLengthEncodingTextFile(file);
        rle.runLengthDecodeTextFile(new File("C:\\Users\\User\\Desktop\\UFP\\Git-Hub\\Mult2_Proj\\StringsEncoded"));
        // C:\Users\User\Desktop\UFP\Git-Hub\Mult2_Proj\StringsEncoded
        // E:\GitHub\Mult2_Proj\StringsEncoded

        File file2 = new File("C:\\Users\\User\\Desktop\\UFP\\Git-Hub\\Mult2_Proj\\TesteSegundoModo");
        // C:\Users\User\Desktop\UFP\Git-Hub\Mult2_Proj\TesteSegundoModo
        // E:\GitHub\Mult2_Proj\TesteSegundoModo
        rle.passoApasso(file2);
    }
}
