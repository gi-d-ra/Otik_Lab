public class Main {
    public static void main(String[] args) {
        String s = "Папа купил auto";
        s = Archiver.compression(s);
        System.out.println(s);
        s = Archiver.decompression(s);
        System.out.println(s);
    }
}
