package ru.eviilcass.compressors.RLEImage;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class ImageToCompression {

   BufferedImage buffImg;
   String title;

   int h, w;

   ImageToCompression(File file) throws IOException {

       title = file.getName();
       buffImg = ImageIO.read(file);
       h = buffImg.getHeight();
       w = buffImg.getWidth();

   }
}
