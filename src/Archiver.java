public class Archiver {

    public static String compression(String text) {
        String[] parts = splitText(text);
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < parts[1].length(); ) {
            int hex = (int) (Math.random() * 16 + 1);
            if (i + hex >= parts[1].length())
                hex = parts[1].length() - i;
            if (hex < 10)
                data.append(hex);
            else data.append((char) ('A' + (hex - 10)));
            if (i + hex < parts[1].length())
                data.append(parts[1], i, i + hex);
            else
                data.append(parts[1].substring(i));
            i += hex;
        }
        return parts[0] + data.toString();
    }

    public static String decompression(String text) {
        String[] parts = splitText(text);
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < parts[1].length() - 2; ) {
            int hex = ((parts[1].charAt(i) - 'A') < 0) ?
                    (parts[1].charAt(i) - 48) :
                    (parts[1].charAt(i) - 'A' + 10);
            if ((hex < 0) && (i + 2 == parts[1].length())) {
                data.append(parts[1], i, 2);
                break;
            }
            if (i + 1 + hex < parts[1].length())
                data.append(parts[1], i + 1, i + hex + 1);
            else
                data.append(parts[1].substring(i + 1));
            i += hex + 1;
        }
        return parts[0] + data.toString();
    }

    public static String[] splitText(String text){
        String[] parts = new String[2];
        parts[0] = text.substring(0,31);
        parts[1] = text.substring(31);
        return parts;
    }
}
