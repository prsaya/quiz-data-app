package com.javaquizplayer;

import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import java.awt.Insets;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Dialog.ModalityType;

import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/*
 * Constructs the GUI for editing a quiz data item.
 * This is for both the new item and updating an existing item.
 */
public class QuizDataDialog {


    private final JDialog dialog;
    private final JTextArea text1;
    private final JTextArea text2;
    private final JTextArea text3;
    private final Box hb1;
    private final Box hb3;
    private JTextArea lastFocused;
    private JLabel qLabel;

    private final QuizDataApp quizApp;
    private int currId;

    private static final String [] checkBoxText = { "a", "b", "c", "d", "e", "f" };


    /*
     * Constructs the GUI for editing a quiz data item.
     */
    public QuizDataDialog(final JFrame parent, final QuizDataApp app, final QuizData obj) {

        this.quizApp = app;
        this.currId = obj.getId();

        quizApp.getStatus().setText("Item being edited");

        // Dialog window

        dialog = new JDialog(parent, AppUtils.DIALOG_TITLE);
        dialog.setModalityType(ModalityType.DOCUMENT_MODAL);

        final Container pane = dialog.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        // Question text

        final JPanel panel1 = getLabelPanel("");
        for (final Component c : panel1.getComponents()) {
            if (c instanceof JLabel) {
                qLabel = (JLabel) c;
                qLabel.setText(((currId == 0) ? "New Question" : "Question " + currId) + ":");
            }
        }
        pane.add(panel1);

        final TextFocusListener focusListener = new TextFocusListener();

        text1 = getTextArea(obj.getQuestion(), focusListener);
        final JScrollPane scroll1 = new JScrollPane(text1);
        pane.add(scroll1);

        pane.add(Box.createVerticalStrut(10));

        // Radio buttons

        final ButtonGroup group = new ButtonGroup();
        final JRadioButton sBtn = new JRadioButton("Single Selection",
                (obj.getType().isEmpty() || obj.getType().equals("S"))
        );
        sBtn.setFont(AppUtils.FONT_16B);
        sBtn.setName("S");
        final JRadioButton mBtn =
                new JRadioButton("Multiple Selection", obj.getType().equals("M"));
        mBtn.setFont(AppUtils.FONT_16B);
        mBtn.setName("M");
        group.add(sBtn);
        group.add(mBtn);
        hb1 = Box.createHorizontalBox();
        hb1.add(sBtn);
        hb1.add(Box.createHorizontalStrut(20));
        hb1.add(mBtn);
        pane.add(hb1);

        pane.add(Box.createVerticalStrut(10));

        // Options text

        final JPanel panel2 = getLabelPanel("Options:");
        pane.add(panel2);

        final String s1 = String.join("\n", obj.getOptions());
        text2 = getTextArea(s1, focusListener);
        final JScrollPane scroll2 = new JScrollPane(text2);
        pane.add(scroll2);

        pane.add(Box.createVerticalStrut(10));

        // Answers check boxes

        final JLabel label3 = AppUtils.getLabel("Answers:");
        label3.setFont(AppUtils.FONT_16B);

        hb3 = Box.createHorizontalBox();
        hb3.add(label3);
        hb3.add(Box.createHorizontalStrut(20));
        for (String s : checkBoxText) {
            final JCheckBox cb = new JCheckBox(s, obj.getAnswers().contains(s));
            cb.putClientProperty("JComponent.sizeVariant", "large");
            cb.setFont(AppUtils.FONT_16B);
            hb3.add(cb);
            hb3.add(Box.createHorizontalStrut(10));
        }
        pane.add(hb3);

        pane.add(Box.createVerticalStrut(10));

        // Notes text

        final JPanel panel3 = getLabelPanel("Notes:");
        pane.add(panel3);

        final String s2 = String.join("\n", obj.getNotes());
        text3 = getTextArea(s2, focusListener);
        text3.setRows(3);
        final JScrollPane scroll3 = new JScrollPane(text3);
        pane.add(scroll3);

        pane.add(Box.createVerticalStrut(10));

        // Buttons

        final JButton save = AppUtils.getButton("Save");
        save.setToolTipText("Save the new or updated data");
        save.addActionListener(e -> saveData());
        final JButton clear = AppUtils.getButton("Clear");
        clear.setToolTipText("Initializes the input fields");
        clear.addActionListener(e -> clear());
        final JButton tag = AppUtils.getButton("Code Tags");
        tag.setToolTipText("Inserts <code> tags for any selected text");
        tag.addActionListener(e -> insertCodeTags());
        final Box hb4 = Box.createHorizontalBox();
        hb4.add(save);
        hb4.add(Box.createHorizontalStrut(10));
        hb4.add(clear);
        hb4.add(Box.createHorizontalStrut(10));
        hb4.add(tag);
        pane.add(hb4);

        pane.add(Box.createVerticalStrut(5));

        // Finish dialog window

        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setLocation(350, 30);
        dialog.pack();
        dialog.setVisible(true);

        text1.requestFocusInWindow();
    }

    /*
     * Returns a JPanel with a JLabel for a given text.
     */
    private JPanel getLabelPanel(final String s) {
        final JLabel label = AppUtils.getLabel(s);
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(label);
        return panel;
    }

    /*
     * Returns a JTextArea with a focus listener for a given text.
     */
    private JTextArea getTextArea(final String s, final TextFocusListener focusListener) {
        final JTextArea ta = new JTextArea(s, 6, 60);
        ta.addFocusListener(focusListener);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFont(AppUtils.FONT_16P);
        ta.setMargin(new Insets(2, 2, 2, 2));
        ta.setTabSize(2);
        return ta;
    }

    /*
     * Focus listener inner class for identifying and storing the last
     * text area component reference. This is used to determine the component
     * in which the code tags need to be inserted.
     * Also, see insertCodeTags().
     */
    private class TextFocusListener extends FocusAdapter {
        @Override
        public void focusGained(FocusEvent e) {
            final Component c = e.getComponent();
            if (c instanceof JTextArea) {
                lastFocused = (JTextArea) c;
            }
        }
    }

    /*
     * Save button action listsner method.
     * Validates the input data.
     * Saves the new or updated data to the list data model.
     */
    private void saveData() {

        // Validate input data

        final String question = text1.getText();

        if (question.trim().isEmpty()) {
            AppUtils.showWarningDialog(dialog, "Question text cannot be empty!");
            text1.requestFocusInWindow();
            return;
        }

        final String type =
                Arrays.stream(hb1.getComponents())
                        .filter(e -> (e instanceof JRadioButton) &&
                                ((JRadioButton) e).isSelected())
                        .findFirst()
                        .get()
                        .getName();

        final String s2 = text2.getText();
        final List<String> options = Stream.of(s2.split("\n"))
                .map(String::trim)
                .filter(e -> !e.isEmpty())
                .collect(Collectors.toList());
        if (options.size() < 2) {
            AppUtils.showWarningDialog(dialog, "Requires at least two options!");
            text2.requestFocusInWindow();
            return;
        }

        final List<String> answers =
                Arrays.stream(hb3.getComponents())
                        .filter(e -> (e instanceof JCheckBox) &&
                                ((JCheckBox) e).isSelected())
                        .map(e -> ((JCheckBox) e).getText())
                        .collect(Collectors.toList());

        if (answers.size() < 1) {
            AppUtils.showWarningDialog(dialog, "Requires at least one answer checked!");
            return;
        }

        final String s3 = text3.getText();
        final List<String> notes = Stream.of(s3.split("\n"))
                .map(String::trim)
                .filter(e -> !e.isEmpty())
                .collect(Collectors.toList());
        if (notes.size() < 1) {
            AppUtils.showWarningDialog(dialog, "Notes cannot be empty!");
            text3.requestFocusInWindow();
            return;
        }

        // Insert or update valid data to the list data model

        final QuizData q =
                new QuizData(question, type, options, answers, notes);
        final Vector<QuizData> model = quizApp.getModelData();
        final JList<QuizData> jlist = quizApp.getListComponent();

        if (currId > 0) {
            // update the existing item
            final int selected = jlist.getSelectedIndex();
            q.setId(currId);
            model.set(selected, q);

            // update original data
            if (quizApp.isSearchEnabled()) {
                quizApp.updateOriginalModel(q);
            }
        }
        else { // add new item
            q.setId(getNextId());
            model.add(q);
            jlist.setSelectedIndex(model.size() - 1);
            jlist.updateUI();
            jlist.ensureIndexIsVisible(model.size() - 1);

            // update dialog
            currId = q.getId();
            qLabel.setText("Question " + currId + ":");
        }

        quizApp.getStatus().setText("Item saved.");
        JOptionPane.showMessageDialog(dialog,
                AppUtils.getLabel("Quiz data saved."),
                AppUtils.TITLE,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /*
     * Creates and returns the id value for a new item.
     * The id value is 1 for the first item being added -or-
     * the highest id value incremented by 1.
     */
    private int getNextId() {
        final int maxId = quizApp.getModelData()
                .stream()
                .mapToInt(QuizData::getId)
                .max()
                .orElse(0);
        return (maxId + 1);
    }

    /*
     * Clear button action listener method.
     * Resets default values for all input components.
     */
    private void clear() {

        text1.setText("");
        text2.setText("");
        text3.setText("");

        Arrays.stream(hb1.getComponents())
                .filter(e -> e instanceof JRadioButton)
                .map(e -> (JRadioButton) e)
                .filter(e -> e.getName().equals("S"))
                .forEach(e -> e.setSelected(true));

        Arrays.stream(hb3.getComponents())
                .filter(e -> e instanceof JCheckBox)
                .forEach(e -> ((JCheckBox) e).setSelected(false));
    }

    /*
     * Button action listener method to insert <code>...</code> tags
     * for a selected text in any of the three text area fields.
     * Also, see TextFocusListener inner class.
     */
    private void insertCodeTags() {
        if (lastFocused == null ||
                lastFocused.getSelectedText() == null ||
                lastFocused.getSelectedText().isEmpty()) {
            return;
        }
        lastFocused.replaceSelection("<code>" +
                lastFocused.getSelectedText() +
                "</code>");
    }
}
