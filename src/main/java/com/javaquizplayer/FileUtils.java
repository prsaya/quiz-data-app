package com.javaquizplayer;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.lang.reflect.Type;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;


/*
 * Utility class with static methods to read from JSON file and
 * write to JSON file.
 * Uses Google Gson APIs for JSON to Java object conversion and
 * vice-versa.
 */
public class FileUtils {


    /*
     * Reads all data from JSON file.
     * Builds QuizData objects and populates the list model.
     * Returns the list model.
     */
    public static Vector<QuizData> getModelDataFromJson()
            throws Exception {
        final Path file = Paths.get(AppUtils.JSON_FILE);
        if (! Files.exists(file)) {
            Files.createFile(file);
        }
        final Gson gson = new Gson();
        final Type type = new TypeToken<Vector<QuizData>>(){}.getType();
        try(final BufferedReader reader = Files.newBufferedReader(file)) {
            final Vector<QuizData> data = gson.fromJson(reader, type);
            if (data == null) {
                return new Vector<>();
            }
            return data;
        }
    }

    /*
     * Converts list data Java objects to JSON.
     * Writes the JSON to file.
     */
    public static void writeModelDataToJson(final Vector<QuizData> listData)
            throws Exception {
        final Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        final Type type = new TypeToken<Vector<QuizData>>(){}.getType();
        try (final BufferedWriter buff =
                     Files.newBufferedWriter(Paths.get(AppUtils.JSON_FILE));
             final JsonWriter writer = gson.newJsonWriter(buff)) {
            gson.toJson(listData, type, writer);
        }
    }
}
