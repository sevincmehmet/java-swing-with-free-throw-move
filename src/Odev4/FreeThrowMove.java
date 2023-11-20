package Odev4;


import javax.swing.*;

class FreeThrowMove extends JFrame {

    public FreeThrowMove() {
        initUI();
    }

    private void initUI() {
        setTitle("Ramazan Topu");
        add(new Surface());

        setSize(1920, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
            FreeThrowMove dim = new FreeThrowMove();
            dim.setVisible(true);
    }
}
