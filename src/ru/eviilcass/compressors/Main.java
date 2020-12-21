package ru.eviilcass.compressors;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        GUI gui = new GUI(controller, model);
    }
}
