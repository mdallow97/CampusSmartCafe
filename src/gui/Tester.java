package gui;


import javax.swing.SwingUtilities;

public class Tester {
    public static void main(String[] args) {
        // Run the GUI codes on Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                        new LogIn();
                }
        });
}
}
