package com.javaquizplayer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import javax.swing.border.EmptyBorder;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Insets;
import java.awt.Color;
import java.awt.FlowLayout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Arrays;
import java.util.Vector;
import java.util.stream.Collectors;


/*
 * Main GUI class for this application.
 * Has functions to:
 * - Display data in a JList.
 * - Perform CRUD operations on the data.
 * - Perform a search on the data.
 */
public class QuizDataApp {


    private Vector<QuizData> listModel;
    private Object [] originalModel;
    private boolean searchFlag;

    private final JFrame frame;
    private final JList<QuizData> jlist;
    private JTextArea text;
    private JLabel status;
    private final JButton addBtn;
    private final JTextField srchTxtFld;


    /*
     * Constructs the GUI for the app.
     * Populates the list with the input model data.
     */
    public QuizDataApp(final Vector<QuizData> data) {

        // JFrame

        frame = new JFrame(AppUtils.TITLE);
        final Container pane = frame.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        // JList with data

        this.listModel = data;

        jlist = new JList<QuizData>(listModel);
        jlist.addMouseListener(new ListMouseListener());
        jlist.setFont(AppUtils.FONT_16P);
        jlist.setFixedCellHeight(28);
        jlist.setBorder(new EmptyBorder(4, 4, 4, 4));
        jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jlist.setToolTipText("Double-click an item to update");
        final JScrollPane scroller1 = new JScrollPane(jlist);
        scroller1.setPreferredSize(new Dimension(390, 350));
        jlist.addListSelectionListener(e -> {
            if (jlist.getSelectedIndex() >= 0) {
                final QuizData q = listModel.get(jlist.getSelectedIndex());
                text.setText(q.getDisplayString());
                status.setText("Item id " + q.getId());
                text.setCaretPosition(0);
            }
        });

        // Text Area

        text = new JTextArea(10, 50);
        text.setFont(AppUtils.FONT_16P);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);
        text.setMargin(new Insets(2, 2, 2, 2));
        text.setToolTipText("The selected question");
        final JScrollPane scroller2 = new JScrollPane(text);
        final Box hb1 = Box.createHorizontalBox();
        hb1.add(scroller1);
        hb1.add(scroller2);
        pane.add(hb1);

        pane.add(Box.createVerticalStrut(10));

        // Buttons

        addBtn = AppUtils.getButton("New");
        addBtn.setToolTipText("Add a new item");
        addBtn.addActionListener(e -> {
            new QuizDataDialog(frame, QuizDataApp.this, new QuizData());
            status.setText("Item edit done");
        });
        final JButton delete = AppUtils.getButton("Delete");
        delete.setToolTipText("Delete a selected item");
        delete.addActionListener(e -> delete());
        final JButton json = AppUtils.getButton("Write Json");
        json.setToolTipText("Write to JSON file");
        json.addActionListener(e -> writeToJson());

        // Search widgets

        srchTxtFld = new JTextField();
        srchTxtFld.setToolTipText("Search text wth at least 2 characters");
        srchTxtFld.setFont(AppUtils.FONT_16P);
        srchTxtFld.setColumns(20);
        srchTxtFld.setMargin(new Insets(2, 2, 2, 2));
        srchTxtFld.setText("");
        final JButton search = AppUtils.getButton("Search");
        search.setToolTipText("Search questions and options");
        search.addActionListener(e -> searchQuestions());
        final JButton cancel = AppUtils.getButton("Cancel");
        cancel.setToolTipText("Cancel search");
        cancel.addActionListener(e -> cancelSearch());

        final JPanel hb2 = new JPanel();
        hb2.add(addBtn);
        hb2.add(delete);
        hb2.add(json);
        hb2.add(Box.createHorizontalStrut(20));
        hb2.add(AppUtils.getLabel("Search: "));
        hb2.add(srchTxtFld); hb2.add(search); hb2.add(cancel);
        pane.add(hb2);

        pane.add(Box.createVerticalStrut(10));

        // Status

        status = AppUtils.getLabel("");
        status.setForeground(Color.BLUE);
        final JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelPanel.add(status);
        pane.add(labelPanel);

        // Finish frame

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(100, 50);
        SwingUtilities.updateComponentTreeUI(frame);
        frame.pack();
        frame.setVisible(true);

        status.setText("Loaded " + listModel.size() + " items.");
        if (listModel.isEmpty()) {
            addBtn.requestFocus();
        }
        else {
            jlist.setSelectedIndex(0);
        }
    }

    // Get methods return respective data.
    // These are invoked from the QuizDataDialog class.

    public JList<QuizData> getListComponent() {
        return jlist;
    }

    public Vector<QuizData> getModelData() {
        return listModel;
    }

    public JLabel getStatus() {
        return status;
    }

    public boolean isSearchEnabled() {
        return searchFlag;
    }

    /*
     * Listener class for mouse double-click action on a list item.
     * Opens QuizDataDialog window for the selected QuizData item for
     * editing and updating.
     */
    private class ListMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            // Left mouse button double clicked
            if ((e.getButton() == MouseEvent.BUTTON1) &&
                    (e.getClickCount() == 2)) {
                final Rectangle r =
                        jlist.getCellBounds(0, jlist.getLastVisibleIndex());
                // Double click on the empty space
                if (! r.contains(e.getPoint())) {
                    jlist.requestFocusInWindow();
                    return;
                }

                new QuizDataDialog(frame, QuizDataApp.this, jlist.getSelectedValue());
                text.setText(listModel.get(jlist.getSelectedIndex()).getDisplayString());
                status.setText("Item edit done");
            }
        }
    }

    /*
     * Update original data (also) in case of update during search.
     */
    public void updateOriginalModel(final QuizData q) {
        originalModel = Arrays.stream(originalModel)
                .map(e -> (QuizData) e)
                .map(e -> (e.equals(q)) ? q : e)
                .toArray();
    }

    /*
     * Delete button action listener.
     * Deletes a selected item from the list.
     * This also apples for the searched items.
     * In case of a delete after a search, the item is also
     * deleted from the original model.
     */
    private void delete() {

        final int selectedIx = jlist.getSelectedIndex();

        if (listModel.isEmpty() || selectedIx == -1) {
            status.setText("Select an item to delete!");
            return;
        }

        final int selection =
                JOptionPane.showConfirmDialog(frame,
                        AppUtils.getLabel("Delete quiz item?"),
                        AppUtils.TITLE,
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
        if (selection != JOptionPane.YES_OPTION) {
            return;
        }

        final QuizData q = listModel.remove(selectedIx);
        jlist.updateUI();
        final int select = (selectedIx > 0) ? selectedIx - 1 : selectedIx;
        jlist.setSelectedIndex(select);
        status.setText("Item deleted");

        if (searchFlag) {
            originalModel = Arrays.stream(originalModel)
                    .map(e -> (QuizData) e)
                    .filter(e -> !e.equals(q))
                    .toArray();
        }
    }

    /*
     * Routine for writing data to file as JSON.
     */
    private void writeToJson() {

        try {
            FileUtils.writeModelDataToJson(listModel);
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }

        status.setText("Data written to file.");
    }

    /*
     * Search button action listener method.
     * Searched items are listed and can be edited or deleted.
     * New items cannout be added to the searched list.
     */
    private void searchQuestions() {

        if (searchFlag) {
            AppUtils.showWarningDialog(frame, "Search can be applied on original list data only!");
            srchTxtFld.requestFocusInWindow();
            return;
        }

        final String str = srchTxtFld.getText();

        if (str.length() < 2) {
            AppUtils.showWarningDialog(frame, "Search text must have at least 2 characters!");
            srchTxtFld.requestFocusInWindow();
            return;
        }

        final Vector<QuizData> srchDataModel =
                listModel.stream()
                        .filter(e -> e.getQuestion().contains(str) || e.getOptions().stream().anyMatch(s -> s.contains(str)))
                        .collect(Collectors.toCollection(Vector<QuizData>::new));

        if (srchDataModel.isEmpty()) {
            status.setText("No matches found for search string " + str);
            srchTxtFld.selectAll();
            srchTxtFld.requestFocusInWindow();
            return;
        }

        addBtn.setEnabled(false); // Add item functionality is disabled

        originalModel = new Object [listModel.size()];
        listModel.copyInto(originalModel);
        searchFlag = true;
        listModel = srchDataModel;
        jlist.setListData(srchDataModel);
        jlist.updateUI();
        jlist.setSelectedIndex(0);
        status.setText("Found " + listModel.size() + " items for search string " + str);
    }

    /*
     * Cancel search button action listener method.
     * Resets the list data with original data.
     */
    private void cancelSearch() {

        if (!searchFlag) {
            srchTxtFld.setText("");
            return;
        }

        listModel = Arrays.stream(originalModel)
                .map(e -> (QuizData) e)
                .collect(Collectors.toCollection(Vector<QuizData>::new));
        jlist.setListData(listModel);
        jlist.updateUI();
        jlist.setSelectedIndex(0);
        status.setText("Search cancelled. Showing all items. " + listModel.size());
        srchTxtFld.setText("");
        searchFlag = false;
        originalModel = null;
        addBtn.setEnabled(true); // Add functionality enabled
    }
}
