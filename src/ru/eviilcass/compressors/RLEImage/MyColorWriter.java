package ru.eviilcass.compressors.RLEImage;

import java.awt.*;
import java.awt.image.BufferedImage;

class MyColorWriter {

    BufferedImage bimg;
    private int width, height;


    MyColorWriter(int w, int h, byte[] r, byte[] g, byte[] b) {
        width = w;
        height = h;
        Color[][] colors = new Color[w][h];

        int[][] red = writeTableColors(convetrByteToInt(r));
        int[][] green = writeTableColors(convetrByteToInt(g));
        int[][] blue = writeTableColors(convetrByteToInt(b));

        bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                colors[x][y] = new Color(red[x][y],green[x][y],blue[x][y]);
                bimg.setRGB(x, y, colors[x][y].getRGB());
            }

        }
    }

    private int[][] writeTableColors(int[] tab) {
        int[][] TableColors = new int[width][height];
        int x = 0;
        int y = 0;

        for (int i =0 ; i < tab.length; i += 2) {
            int color = tab[i];
            int count = tab[i + 1];

            for (int j = 0; j < count; j++) {
                if (x >= width) {
                    x = 0;
                    y++;
                 if(y == height) return TableColors;
                }
                TableColors[x][y] = color;
                x++;
            }
        }

        return TableColors;
    }

    private int[] convetrByteToInt(byte[] bytes){
        int [] ints = new int[bytes.length];

        for(int i = 0; i < bytes.length; i++){
            ints[i] = bytes[i] & 0xFF;
        }
        return ints;
    }
}