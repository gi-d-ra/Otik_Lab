package ru.eviilcass.compressors.RLEImage;

import java.awt.*;
import java.util.ArrayList;

class MyColorReader {

    private ImageToCompression image;
    private int[] red, green, blue;
    byte[] toFile;
    ArrayList<Byte> cRed, cGreen, cBlue;

    MyColorReader(ImageToCompression img){
        image = img;

        red = new int[image.h * image.w];
        green = new int[image.h * image.w];
        blue = new int[image.h * image.w];

        takeColor();
        cRed = compressTab(red);
        cGreen = compressTab(green);
        cBlue = compressTab(blue);

        ArrayList<Byte> allArray = new ArrayList<>(cRed);
        allArray.addAll(cGreen);
        allArray.addAll(cBlue);


        int x = 0;
        toFile = new byte[allArray.size()];
        for (Byte z : allArray) {
            toFile[x++] = z;
        }

    }

    private void takeColor(){
        int x = 0;

        for (int i = 0;i < image.h; i++)
            for(int j = 0; j < image.w; j++){
                Color color = new Color(image.buffImg.getRGB(j,i));
                red[x] = color.getRed();
                green[x] =color.getGreen();
                blue[x++] =color.getBlue();
            }
    }

    private ArrayList<Byte> compressTab(int[] tab){

        int prev = tab[0];
        int count = 0;
        ArrayList<Byte> al = new ArrayList<>();

        for(int i = 1; i < tab.length; i++)
        {
            count++;
            if(tab[i] != prev || count == 255)
            {
                al.add((byte)prev);
                al.add((byte)count);
                prev = tab[i];
                count=0;
            }
            if(i == (tab.length -1)){
                al.add((byte)prev);
                al.add((byte)(++count));
            }
        }
        return al;
    }


}
