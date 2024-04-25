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
import java.util.Collections;
import java.util.Scanner;

public class Compression {
    private static int rangeLimit = 5;

    /**
     * Constructor for the compression functions
     * @param img - A Java BufferedImage already loaded from disk
     * @param rangelimit Maximum colour difference allowed between next pixel (default 5)
     * @return A compressed image object in memory
     */
    public static ImgCompressed runCompressionAlg(BufferedImage img, int rangelimit) {
        System.out.println("Running Compression Algorithm...");

        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        rangeLimit = rangelimit;

        /* Compress each colour stream (Red, Green, Blue) */
        System.out.println("--------------------------------------");
        ByteArrayOutputStream redArr =  compressToByteArr(img, 'R', imgWidth, imgHeight);
        ByteArrayOutputStream greenArr = compressToByteArr(img, 'G', imgWidth, imgHeight);
        ByteArrayOutputStream blueArr = compressToByteArr(img, 'B', imgWidth, imgHeight);
        System.out.println("--------------------------------------");

        //Create compress image in memory with the compress colour streams
        ImgCompressed resImg = new ImgCompressed(imgWidth, imgHeight, redArr.toByteArray(), greenArr.toByteArray(), blueArr.toByteArray(), rangelimit);
        System.out.println("Compression Algorithm Completed!");

        return resImg;
    }

    private static ByteArrayOutputStream compressToByteArr(BufferedImage img, char colourCode, int imgWidth, int imgHeight) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        for (int y = 0; y < imgHeight; y++) { //Iterate over each Y column in the image.
            for (int x = 0; x < imgWidth; x++) {
                int runCounter = 1; //Run Length Counter
                int pixelColourCounter = getColourLevel(img, colourCode, x, y); //Keep count of pixel values (for mean value)
                byte curPixelValue = getColourLevel(img, colourCode, x, y); //Used to compare the next pixel colour value with this.
                System.out.println("pixelColour" + pixelColourCounter);
                System.out.println("curPixelValue" + curPixelValue);
                for (int ix = x + 1; ix < imgWidth + 1; ix++) { //For each next pixel from x -> imgwidth or to runlength.
                    if (ix == imgWidth) {
                        x = imgWidth; //Break row if its reached the end of image width.
                        break;
                    } else {
                        byte nextPixelValue = getColourLevel(img, colourCode, ix, y);

                        /* Compare next pixel colour value, if its within range limit then include it.
                         * BUGFIX: Limit runCounter to maximum 200 (prevent exceeding image boundaries)
                         */
                        if ((Math.abs(curPixelValue - nextPixelValue) <= rangeLimit) && runCounter < 200) {
                            runCounter++; //Increment the run length counter
                            pixelColourCounter += nextPixelValue; //Add this pixel to colour counter
                        }
                        else { //Else Next Pixel is not within range limit, so start next break and start new..
                            x = ix - 1;
                            break;
                        }
                    }
                }

                //Write to 'ByteStream Array': [meanColourValue, runLength, meanColourValue, runLength...]
                output.write((int) Math.floor(pixelColourCounter / runCounter)); //Calculate and Write Mean Value then RunLength
                output.write((byte) runCounter);
            }
        }

        System.out.println("Completed Compression on colour: " + colourCode + " | Size: " + output.size());
        return output;
    }

    /**
     * Gets the value of a colour level (0 - 255)
     * @param img A Java BufferedImage loaded already from disk
     * @param getWhichRGB Specify character: R = Red, B = Blue, G = Green
     * @param x X Corrd
     * @param y Y Corrd
     * @return Byte range 0-255 for colour level at x,y
     */
    private static byte getColourLevel(BufferedImage img, char getWhichRGB, int x, int y) {
        Color getColour = new Color(img.getRGB(x, y));

        switch (getWhichRGB) {
            case 'R':
                return (byte) getColour.getRed();
            case 'G':
                return (byte) getColour.getGreen();
            case 'B':
                return (byte) getColour.getBlue();
            default:
                return (byte) 0;
        }
    }

    public static void main(String[] args) throws IOException {
        //Scanner scanner = new Scanner(System.in);
        //String uChoice = "";
        //System.out.println("Compressao e descompressao de imagens\n Escolha uma opcao");
        //System.out.println("\n\t 1. Comprimir a imagem \n\t 2. Carregar imagem comprimida e descomprimir ");

        BufferedImage bImage = ImageIO.read(new File("C:\\Github\\Mult2_Proj\\Images\\Lenna.png"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos);  // Converte a imagem em um array de bytes
        byte[] data = bos.toByteArray();
        int count=1;
        StringBuilder result= new StringBuilder();
        result=compImagem(data,count,result);
        String outputFileName = "C:\\Github\\Mult2_Proj\\Images\\compressed_image.dat";
        writeCompressedDataToFile(result.toString(), outputFileName);
        byte[] decompressedImageData = decompressImage(result.toString());

        // Crie um ByteArrayInputStream com os dados do array de bytes
        ByteArrayInputStream bis = new ByteArrayInputStream(decompressedImageData);
        System.out.println("bisss" + bis);
        try{
        // Carregue a imagem a partir do ByteArrayInputStream
        BufferedImage image = ImageIO.read(bis);
        if(image!=null){
            File outputFile = new File("imagem.png");
            ImageIO.write(image, "png", outputFile);
            System.out.println("criada");
        }
        else{
            System.out.println("nao deu");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

        System.out.println("\n\n\n");
        System.out.println(result);


         //uChoice = scanner.nextLine();
        //System.out.println("Escolheu a opcao: " + uChoice);

        //switch (uChoice) {
          //  case "1":
            //    compressImage();
              //  break;
            //case "2":
              //  loadImageFromDisk("C:\\Users\\User\\Desktop\\UFP\\Git-Hub\\Mult2_Proj\\Images\\CompressedOutput.ckcomp");
                //break;
            //default:
              //  System.out.println("Opcao invalida");
               // break;
       // }
    }

    private static StringBuilder compImagem(byte[] data, int count, StringBuilder result) {
        for (int i = 0; i < data.length; i++) {
            // Verifica se o byte está na faixa de 1 a 255
            if ((data[i] & 0xFF) >= 1 && (data[i] & 0xFF) <= 255) {
                if(i+1< data.length && data[i] == data[i+1]){
                    count++;
                }
                else if(count>=4){
                    result.append("255").append(count).append(data[i] & 0xFF);
                    count=1;
                }
                else{
                    result.append(String.join("", Collections.nCopies(count, String.valueOf(data[i] & 0xFF))));
                    count=1;
                }
                System.out.println("Byte: " + (data[i] & 0xFF));
            }
        }
        return result;
    }
    private static void writeCompressedDataToFile(String compressedData, String fileName) {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName))) {
            bos.write(compressedData.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static byte[] decompressImage(String compressedData) {
        byte[] decompressedImageData = new byte[compressedData.length()]; // Tamanho pode ser ajustado conforme necessário
        int index = 0;
        for (int i = 0; i < compressedData.length(); i++) {
            char currentChar = compressedData.charAt(i);
            if (currentChar == '2' && i + 2 < compressedData.length()) {
                char nextChar = compressedData.charAt(i + 1);
                char nextNextChar = compressedData.charAt(i + 2);
                int count = Integer.parseInt(String.valueOf(nextChar));
                int value = Integer.parseInt(String.valueOf(nextNextChar));
                for (int j = 0; j < count; j++) {
                    decompressedImageData[index++] = (byte) value;
                }
                i += 2; // Pula os próximos dois caracteres
            } else {
                decompressedImageData[index++] = (byte) currentChar;
            }
        }
        // Agora você tem os dados descomprimidos em decompressedImageData
        return decompressedImageData;
    }

    /**
     *  Runs the compression, information and decompression algorithms.
     */
    private static void compressImage() {
        ImgCompressed compressedImage;
        File imgPath;
        boolean isPPM = false; //Different way of reading a PPM file. Other files can just use the Java Library.
        System.out.println("Please select an image file to compress...");
        FileDialog fd = new FileDialog(new JFrame(), "Choose a file", FileDialog.LOAD); //Windows dialog to select file.
        fd.setDirectory("C:\\Users\\User\\Desktop\\UFP\\Git-Hub\\Mult2_Proj\\Images");
        fd.setVisible(true);
        if (fd.getFile() == null) {
            System.out.println("Nenhum ficheiro escolhido, encerrando o programa...");
            return;
        } else
            imgPath = new File(fd.getDirectory() + fd.getFile());

        if (fd.getFile().contains("ppm")) //crude and highly unreliable way to check if its PPM (improve in future)
            isPPM = true;

        System.out.println("The File Selected: " + imgPath + " | PPM: " + isPPM);

        BufferedImage img = null;
        try {
            if (isPPM)
            {
//                ImageDecoder ppmImgDecoder = ImageCodec.createImageDecoder("PNM", new File(String.valueOf(imgPath)), null);
//                img = new RenderedImageAdapter(ppmImgDecoder.decodeAsRenderedImage()).getAsBufferedImage();
            }
            else {
                img = ImageIO.read(imgPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /* Ask user to choose a compression rate. Where 0 is lossless, beyond is high compression*/
        System.out.print("Please enter a compression rate (0 = Lossless TO 10(+) = High Compression Rate, Lower Quality: ");
        Scanner scanner = new Scanner(System.in);
        int compressRate = scanner.nextInt();
        compressedImage = Compression.runCompressionAlg(img, compressRate); //Run Compression Algorithm
        compareImageSize(imgPath, compressedImage); //Show the compression ration
        //compareImageSize(img, compressedImage); //Show the compression ratio
        compressedImage.outputToFile(fd.getDirectory() + "\\"); //Output the compressed image to a file (ckcomp file)


        System.out.println("------------------------------------------------------------------------");
        System.out.println("Press any key to run decompression algorithm FROM MEMORY...");

        Scanner pauser = new Scanner(System.in);
        pauser.nextLine();
        pauser.close();

        //Decompression.runDecompressAlg(fd.getDirectory() + "\\output\\", compressedImage); //Run Decompression Algorithm
    }

    /**
     * Outputs information regarding size of original and compress image size.
     * @param originalImagePath The original image path
     * @param compressedImage The compressed image in memory
     */
    private static void compareImageSize(File originalImagePath, ImgCompressed compressedImage)
    {
        long originalImgSize = originalImagePath.length();
        long compressedImgSize = compressedImage.getTotalByteSize();
        System.out.println("**********************************************");

        System.out.println("Compression Information:");
        System.out.println("\tCompression Level (0 - Lossless. Higher Number has greater compress rate but less Quality: " + compressedImage.getCompressLevel());
        System.out.println("\tOriginal Image Size (Bytes): " + originalImgSize);
        System.out.println("\tCompressed Image Size (Bytes): " + compressedImgSize);

        double percentageSaved = (double) Math.round(((double) (originalImgSize - compressedImgSize) / originalImgSize) * 100 * 100) / 100;
        System.out.println("\tPercent Saved (2dp): " + percentageSaved + "%");

        System.out.println("**********************************************");
    }

    private static ImgCompressed loadImageFromDisk(String compressedImgPath) {
        /* IMPORANT TO NOTE: Potential off-by-ones. Make sure when read to increment lengths by 1 to ensure correct byte is read!!!*/
        BufferedReader firstLineReader;
        ImgCompressed compressedImg = null;

        byte[] redArray;
        byte[] greenArray;
        byte[] blueArray;

        try {
            firstLineReader = new BufferedReader(new FileReader(compressedImgPath));
            String firstLineText = firstLineReader.readLine();
            String[] picOptionsArr = firstLineText.split(","); //Gives first line of file in array: [width, height, compressLevel, redLen, greenLen, blueLen]

            int imgWidth = Integer.parseInt(picOptionsArr[0]);
            int imgHeight = Integer.parseInt(picOptionsArr[1]);
            int compressLevel = Integer.parseInt(picOptionsArr[2]);
            redArray = new byte[Integer.parseInt(picOptionsArr[3])];
            greenArray = new byte[Integer.parseInt(picOptionsArr[4])]; //Set length of colour-level byte arrays from the pic options.
            blueArray = new byte[Integer.parseInt(picOptionsArr[5])];

            FileInputStream fis = new FileInputStream(compressedImgPath); //Create Byte Stream Array Reader
            long skip = fis.skip(firstLineText.getBytes().length + 1); //Ignore the first-line (skip the byte length), since we have the picOptions already
            /* Read each colour level byte array */
            fis.read(redArray);
            fis.read(greenArray);
            fis.read(blueArray);
            compressedImg = new ImgCompressed(imgWidth, imgHeight, redArray, greenArray, blueArray, compressLevel);
            Decompression.runDecompressAlg("C:\\Users\\User\\Desktop\\UFP\\Git-Hub\\Mult2_Proj\\Images", compressedImg);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return compressedImg;
    }

}