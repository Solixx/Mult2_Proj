import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;


import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Compression {


    private static ArrayList<Byte> compImagem(byte[] data) {
        Integer count=1;
        StringBuilder result= new StringBuilder();
        ArrayList<Byte> compressedData = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            // Verifica se o byte está na faixa de 1 a 255
            if ((data[i] & 0xFF) >= 1 && (data[i] & 0xFF) <= 255) {
                if(i+1< data.length && data[i] == data[i+1]){
                    count++;
                }
                else if(count>=4){
                    Integer flag = 255;
                    result.append("255#").append(count).append('\n').append(data[i] & 0xFF).append('\n');
                    compressedData.add(flag.byteValue());
                    compressedData.add(count.byteValue());
                    compressedData.add(data[i]);
                    count=1;
                }
                else{
                    result.append(String.join("", Collections.nCopies(count, String.valueOf(data[i] & 0xFF)))).append('\n');
                    for (int j = 0; j < count; j++) {
                        compressedData.add(data[i]);
                    }
                    count=1;
                }
            }
        }
        System.out.println("niogga\n"+result);
        writeCompressedDataToFile(result.toString(), "C:\\Github\\Mult2_Proj\\Images\\compressed_image.txt");
        writeCompressedDataToFile(compressedData, "C:\\Github\\Mult2_Proj\\Images\\compressed_image.bin");
        System.out.println("Imagem comprimida com sucesso!!!");
        return compressedData;
    }
    private static void writeCompressedDataToFile(ArrayList<Byte> compressedData, String fileName) {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(fileName)))) {
            for (Byte b : compressedData) {
                bos.write(b);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeCompressedDataToFile(String compressedData, String fileName) {
        try (PrintWriter bos = new PrintWriter(new FileWriter(new File(fileName)))) {
                bos.write(compressedData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Byte> decompressImage(ArrayList<Byte> compressedData) {
        ArrayList<Integer> compressedDataIntegerList = byteToInt(compressedData);

        ArrayList<Integer> decompressedImageDataList = new ArrayList<>();

        for ( Integer index : compressedDataIntegerList) {
            if(index == 255){
                Integer count = compressedDataIntegerList.get(index+1);
                Integer value = compressedDataIntegerList.get(index+2);
                for (int i = 0; i < count; i++) {
                    decompressedImageDataList.add(value);
                }
            }else {
                decompressedImageDataList.add(index);
            }
        }

        ArrayList<Byte> decompressedImageData = new ArrayList<Byte>();
        for (int i = 0; i < decompressedImageDataList.size(); i++) {
            decompressedImageData.add(decompressedImageDataList.get(i).byteValue());
        }

        return decompressedImageData;
    }

    private static ArrayList<Integer> byteToInt(ArrayList<Byte> compressedData) {
        ArrayList<Integer> decompressedImageDataList = new ArrayList<>();
        for (Byte b: compressedData ) {
            decompressedImageDataList.add(b & 0xFF);
        }
        return decompressedImageDataList;
    }
    public static void main(String[] args) throws IOException {
        //BufferedImage bImage = ImageIO.read(new File("C:\\Github\\Mult2_Proj\\Images\\Lenna.sgi"));
        //ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //ImageIO.write(bImage, "sgi", bos);  // Converte a imagem em um array de bytes
        ArrayList<Byte> teste =  new ArrayList<Byte>();
        byte[] data = Files.readAllBytes(Path.of("C:\\Github\\Mult2_Proj\\Images\\tesdt_img.sgi"));
        for (int i=0 ;i <data.length;i++){
            teste.add(data[i]);
        }
        ArrayList<Byte> result = compImagem(data);
        ArrayList<Byte> decompressedImageData = decompressImage(teste);
        writeCompressedDataToFile(decompressedImageData,"images/sopinha.sgi");
        // Reconstruir a imagem a partir dos dados descomprimidos
        //ByteArrayInputStream bis = new ByteArrayInputStream(decompressedImageData);
        //System.out.println(bis.available());
        //BufferedImage reconstructedImage = ImageIO.read(bis);

        // Salvar a imagem reconstruída em um arquivo
      //  File outputImageFile = new File("C:\\Github\\Mult2_Proj\\Images\\LennaDecomp.sgi");
        //ImageIO.write(reconstructedImage, "sgi", outputImageFile);


        System.out.println("Imagem reconstruída e salva com sucesso.");
    }
}