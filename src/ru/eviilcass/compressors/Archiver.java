package ru.eviilcass.compressors;

import java.util.regex.Pattern;

public class Archiver {

    public static StringBuilder defCompression(StringBuilder text) {
        int k = 0;
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < text.length(); ) {
            int randNumber = (int) (Math.random() * 16 + 1);
            if (randNumber + i <= text.length()) {
                data.append(text.substring(i, randNumber + i)).append('q').append(k);
            } else {
                data.append(text.substring(i, text.length())).append('q').append(k);
            }
            k++;
            k = checkK(k);
            i += randNumber;
        }
        return data;
    }

    private static int checkK(int k) {
        if (k == 256) {
            k = 0;
        }
        return k;
    }

    public static StringBuilder defDecompression(StringBuilder text) {
        StringBuilder data = new StringBuilder();
        Pattern p = Pattern.compile("q\\d+");
        data.append(text.toString().replaceAll(p.toString(), ""));
        return data;
    }

    public static StringBuilder huffmanCompression(StringBuilder text) {
        return null;
    }

    public static StringBuilder huffmanDecompression(StringBuilder text) {
        return null;
    }
}
