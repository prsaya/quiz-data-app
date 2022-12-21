package com.javaquizplayer;

import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;

import java.util.Vector;


/*
 * Starter class for this application.
 * Has functionality to:
 * - Set the Look and Feel for the GUI.
 * - Call the JSON file read method and get list model data.
 * - Invoke the main GUI window.
 */
public class AppStarter {


    public static void main(String [] args)
            throws Exception {
        System.out.println("*** Quiz Data App ***");
        setupLookAndFeel();
        final Vector<QuizData> listModel = FileUtils.getModelDataFromJson();
        new QuizDataApp(listModel);
    }

    /*
     * Set the Look and Feel (LaF) for the GUI.
     * Tries to set the Nimbus LaF, and if not available
     * for the environment uses the default Metal LaF.
     */
    private static void setupLookAndFeel()
            throws Exception {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        }
        catch (Exception e) {
            e.printStackTrace();
            // For some reason Nimbus LaF could not be set, so use this:
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        }
        // JOptionPane's default setting for this app
        UIManager.put("OptionPane.buttonFont", AppUtils.FONT_16P);
    }
}
