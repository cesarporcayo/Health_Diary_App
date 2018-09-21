package edu.awilkins6gatech.happyhealthytummyapp.Controller;

import android.net.Uri;

import edu.awilkins6gatech.happyhealthytummyapp.Model.DiaryEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

import static java.lang.Math.random;

public class DiaryEntryTest {
    private DiaryEntry entry;

    @Test
    public void testJsonSaveAndLoad() {
        createEntryTest();
        readEntryTest();
        deleteEntryTest();
    }

    public void createEntryTest() {
        int calories = (int) random();
        String uri = Double.toString(random());
        long timestamp = (long) random();
        String title = "";
        String description = "";
        boolean happy = true;
        entry = new DiaryEntry(Uri.parse(uri), calories, timestamp, title, description, happy);
        entry.createEntry(entry);
        File testFile = new File("Entries/" + entry.getTimestamp() + ".json");
        Assert.assertTrue("The file was not created",testFile.exists());
    }

    public void readEntryTest() {
        DiaryEntry entryFromFile = entry.readEntry(Long.toString(entry.getTimestamp()));
        Assert.assertEquals("Uri fields are not the same", entry.getFileUri(), entryFromFile.getFileUri());
        Assert.assertEquals("Calorie fields are not the same", entry.getCalories(), entryFromFile.getCalories());
        Assert.assertEquals("Timestamp fields are not the same", entry.getTimestamp(), entryFromFile.getTimestamp());
    }

    public void deleteEntryTest() {
        DiaryEntry deletedEntry = entry.deleteEntry(Long.toString(entry.getTimestamp()));
        File testFile = new File("Entries/" + entry.getTimestamp() + ".json");
        Assert.assertFalse("The file was not deleted", testFile.exists());
    }
}