package com.javaquizplayer;

import java.awt.Font;
import java.awt.Window;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JButton;

/*
 * Utility class with constant definitions and common static methods.
 */
public class AppUtils {


    public final static String JSON_FILE = "data/json_data.txt";
    public final static String TITLE = "Quiz Data App";
    public final static String DIALOG_TITLE = "Quiz Data";
    public final static Font FONT_16B = new Font("SansSerif", Font.BOLD, 16);
    public final static Font FONT_16P = new Font("SansSerif", Font.PLAIN, 16);


    /*
     * Displays a GUI warning message dialog with the provided message.
     */
    public static void showWarningDialog(final Window parent, final String msg) {
        JOptionPane.showMessageDialog(parent,
                getLabel(msg),
                AppUtils.TITLE,
                JOptionPane.WARNING_MESSAGE);
    }

    /*
     * Returns a JLabel for a given text.
     */
    public static JLabel getLabel(final String s) {
        final JLabel label = new JLabel(s);
        label.setFont(AppUtils.FONT_16B);
        return label;
    }

    /*
     * Returns a JButton for a given text.
     */
    public static JButton getButton(final String s) {
        final JButton btn = new JButton(s);
        btn.setFont(AppUtils.FONT_16B);
        return btn;
    }
}
