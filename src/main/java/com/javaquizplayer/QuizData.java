package com.javaquizplayer;

import java.util.List;
import java.util.ArrayList;


/*
 * Class represents a quiz data item.
 */
public class QuizData {


    private int id; // unique value for each QuizData object
    private String no;
    private String question = "";
    private String type = "";
    private final List<String> options = new ArrayList<>();
    private final List<String> answers = new ArrayList<>();
    private final List<String> notes = new ArrayList<>();


    public QuizData () {
    }

    public QuizData (final String s1, final String s2, final List<String> l1,
                     final List<String> l2, final List<String> l3) {
        question = s1;
        type = s2;
        options.addAll(l1);
        answers.addAll(l2);
        notes.addAll(l3);
    }

    public void setId(int i) {
        id = i;
    }

    public int getId() {
        return id;
    }

    public String getNo() {
        return no;
    }

    public String getQuestion() {
        return question;
    }

    public String getType() {
        return type;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public List<String> getNotes() {
        return notes;
    }

    /*
     * Override Object class's equals method.
     * Two QuizData objects are equal when their id value is same.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof QuizData) {
            return (((QuizData) obj).getId() == this.getId());
        }
        return false;
    }

    /*
     * Returns a string with a substring of question field prefixed
     * with the id. This is used for the showing QuizData items in the
     * QuizDataApp application's JList
     */
    @Override
    public String toString() {
        return id +
                ": " +
                ((question.length() > 40) ? question.substring(0, 40)+"..." : question);
    }

    /*
     * Returns the question and options as a concatenated string to
     * be displayed in the JTextArea of the QuizDataApp.
     */
    public String getDisplayString() {
        return question +
                "\n\n" +
                String.join("\n", options) +
                "\n";
    }
}
