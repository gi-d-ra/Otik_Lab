public class Archiver {

    public static String defCompression(String text) {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < text.length(); ) {
            int hex = (int) (Math.random() * 16 + 1);
            if (i + hex >= text.length())
                hex = text.length() - i;
            if (hex < 10)
                data.append(hex);
            else data.append((char) ('A' + (hex - 10)));
            if (i + hex < text.length())
                data.append(text, i, i + hex);
            else
                data.append(text.substring(i));
            i += hex;
        }
        return data.toString();
    }

    public static String defDecompression(String text) {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < text.length() - 1; ) {
            int hex = ((text.charAt(i) - 'A') < 0) ?
                    (text.charAt(i) - 48) :
                    (text.charAt(i) - 'A' + 10);
            if ((hex < 0) && (i + 2 == text.length())) {
                data.append(text, i, 2);
                break;
            }
            if (i + 1 + hex < text.length())
                data.append(text, i, i + hex + 1);
            else
                data.append(text.substring(i));
            i += hex + 1;
        }
        return data.toString();
    }

    public static StringBuilder defCompression(StringBuilder text) {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < text.length(); ) {
            int hex = (int) (Math.random() * 16 + 1);
            if (i + hex >= text.length())
                hex = text.length() - i;
            if (hex < 10)
                data.append(hex);
            else data.append((char) ('A' + (hex - 10)));
            if (i + hex < text.length())
                data.append(text, i, i + hex);
            else
                data.append(text.substring(i));
            i += hex;
        }
        return data;
    }

    public static StringBuilder defDecompression(StringBuilder text) {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < text.length() - 1; ) {
            int hex = ((text.charAt(i) - 'A') < 0) ?
                    (text.charAt(i) - 48) :
                    (text.charAt(i) - 'A' + 10);
            if ((hex < 0) && (i + 2 == text.length())) {
                data.append(text, i, 2);
                break;
            }
            if (i + 1 + hex < text.length())
                data.append(text, i, i + hex + 1);
            else
                data.append(text.substring(i));
            i += hex + 1;
        }
        return data;
    }

    public static StringBuilder huffmanCompression(StringBuilder text) {
        return null;
    }

    public static StringBuilder huffmanDecompression(StringBuilder text) {
        return null;
    }
}
