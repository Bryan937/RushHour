package com.example.rushhour.ViewModel;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Records extends ViewModel {

    /**
     * This method is responsible for creating a CSV file named "records.csv" in the
     * application's internal storage directory. It initializes the file with a header
     * and default records if it's the first run of the application. The function uses
     * SharedPreferences to keep track of whether it's the first run or not.
     *
     * @param context The context of the application.
     */
    public void createRecordCSV(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            try {
                File file = new File(context.getFilesDir(), "records.csv");
                FileWriter writer = new FileWriter(file);

                writer.write("puzzle,record\n");
                writer.write("1,0\n");
                writer.write("2,0\n");
                writer.write("3,0\n");
                writer.close();

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isFirstRun", false);
                editor.apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * This method retrieves the record associated with a specific challenge ID from
     * the "records.csv" file stored in the application's internal storage directory.
     * It reads the CSV file, searches for the given challenge ID, and returns the
     * corresponding record value.
     *
     * @param context      The context of the application.
     * @param challengeId  The ID of the challenge for which the record is requested.
     * @return             The record associated with the given challenge ID. If not found, returns 0.
     */
    public int getRecord(Context context, String challengeId) {
       int record = 0;
       try {
           List<String[]> csvBody = getCSVAsList(context);
           for (String[] row : csvBody) {
               if (row[0].equals(challengeId)) {
                   record = Integer.parseInt(row[1]);
                   break;
               }
           }
       } catch (IOException | CsvException e) {
           e.printStackTrace();
       }
       return record;
   }


    /**
     * This method updates the record associated with a specific challenge ID in the
     * "records.csv" file. It first checks the current record for the given challenge ID.
     * If the current record is not set (0) or is less than the provided moves, the function
     * returns without making any changes. Otherwise, it updates the record with the new moves value.
     *
     * @param context      The context of the application.
     * @param challengeId  The ID of the challenge for which the record is being updated.
     * @param moves         The new moves value to update the record with.
     */
    public void updateRecord(Context context, String challengeId, int moves) {
       int currentRecord = getRecord(context, challengeId);
       if (currentRecord != 0 && currentRecord < moves) {
           return;
       }
        try {
            List<String[]> csvBody = getCSVAsList(context);

            for (String[] row : csvBody) {
                if (row[0].equals(challengeId)) {
                    row[1] = String.valueOf(moves);
                    break;
                }
            }

            File file = new File(context.getFilesDir(), "records.csv");
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            writer.writeAll(csvBody);
            writer.close();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method reads the content of the "records.csv" and returns it as a List of String arrays.
     *
     * @param context The context of the application.
     * @return        A List of String arrays representing the rows and columns of the CSV file.
     */
    private List<String[]> getCSVAsList(Context context) throws IOException, CsvException {
        File file = new File(context.getFilesDir(), "records.csv");
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        CSVReader reader = new CSVReader(isr);
        List<String[]> csvBody = reader.readAll();
        reader.close();
        return csvBody;
    }
}
