package edu.awilkins6gatech.happyhealthytummyapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import edu.awilkins6gatech.happyhealthytummyapp.Model.DiaryEntry;

public class EntryDB extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "entries.db";

    public static final String ENTRIES_TABLE_NAME = "entries_table";
    public static final String ENTRYID = "ENTRYID";
    public static final String FILEURI = "FILEURI";
    public static final String CALORIES = "CALORIES";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String TITLE = "TITLE";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String HAPPY = "HAPPY";

    public static final String SELECTED_ENTRIES_TABLE_NAME = "selected_entries";
    public static final String SELECTENTRYLIST = "SELECTENTRYLIST";


    public EntryDB(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ENTRIES_TABLE_NAME + " (IDL INTEGER PRIMARY KEY AUTOINCREMENT, ENTRYID, FILEURI, CALORIES, TIMESTAMP," +
                " TITLE, DESCRIPTION, HAPPY)");
        db.execSQL("CREATE TABLE " + SELECTED_ENTRIES_TABLE_NAME + "(IDL INTEGER PRIMARY KEY AUTOINCREMENT, SELECTENTRYLIST)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ENTRIES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SELECTED_ENTRIES_TABLE_NAME);
        onCreate(db);
    }

    public boolean addEntry(DiaryEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ENTRYID, entry.getEntryID());
        contentValues.put(FILEURI, entry.getFileUri());
        contentValues.put(CALORIES, entry.getCalories());
        contentValues.put(TIMESTAMP, entry.getTimestamp());
        contentValues.put(TITLE, entry.getTitle());
        contentValues.put(DESCRIPTION, entry.getDescription());
        contentValues.put(HAPPY, entry.getHappy());

        long result = db.insert(ENTRIES_TABLE_NAME, null, contentValues);

        return (result != -1);
    }

    public boolean editEntry(DiaryEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, entry.getTitle());
        contentValues.put(DESCRIPTION, entry.getDescription());
        contentValues.put(CALORIES, entry.getCalories());
        contentValues.put(HAPPY, entry.getHappy());
        long result = db.update(ENTRIES_TABLE_NAME, contentValues, "TIMESTAMP" + "=?", new String[]{entry.getTimestamp()});
        return (result != -1);
    }
    public List<DiaryEntry> getEntries() {
        List<DiaryEntry> entries = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor res = db.rawQuery("SELECT * FROM " + ENTRIES_TABLE_NAME, null);
        Cursor res = db.query(ENTRIES_TABLE_NAME, null, null, null, null, null, null);
        if (res.moveToFirst()) {
            do {
                DiaryEntry entry = new DiaryEntry();

                entry.setFileUri(res.getString(res.getColumnIndex("FILEURI")));
                entry.setCalories((res.getInt(res.getColumnIndex("CALORIES"))));
                entry.setTimestamp(res.getString(res.getColumnIndex("TIMESTAMP")));
                entry.setTitle(res.getString(res.getColumnIndex("TITLE")));
                entry.setDescription(res.getString(res.getColumnIndex("DESCRIPTION")));
                entry.setHappy(res.getInt(res.getColumnIndex("HAPPY")));
                entries.add(entry);
            } while (res.moveToNext());
        }
        return entries;
    }

    public DiaryEntry getEntry(String timestamp) {
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor res = db.rawQuery("SELECT * FROM " + ENTRIES_TABLE_NAME + " WHERE TIMESTAMP = \'" + timestamp + "\'", null);
        Cursor res = db.query(ENTRIES_TABLE_NAME, null, "TIMESTAMP = ?", new String[]{timestamp}, null, null, null, null);
        DiaryEntry entry = new DiaryEntry();
        if (res.moveToFirst()) {
            do {
                entry.setFileUri(res.getString(res.getColumnIndex("FILEURI")));
                entry.setCalories((res.getInt(res.getColumnIndex("CALORIES"))));
                entry.setTimestamp(res.getString(res.getColumnIndex("TIMESTAMP")));
                entry.setTitle(res.getString(res.getColumnIndex("TITLE")));
                entry.setDescription(res.getString(res.getColumnIndex("DESCRIPTION")));
                entry.setHappy(res.getInt(res.getColumnIndex("HAPPY")));
            } while (res.moveToNext());
        }
        return entry;
    }

    public List<DiaryEntry> getEntriesByName(String keyword) {
        List<DiaryEntry> entries = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor res = db.rawQuery("SELECT * FROM " + ENTRIES_TABLE_NAME + " WHERE TITLE LIKE \'%" + keyword + "%\'", null);
        Cursor res = db.query(ENTRIES_TABLE_NAME, null, "TITLE LIKE ?", new String[]{"%" + keyword + "%"}, null, null, null, null);
        if (res.moveToFirst()) {
            do {
                DiaryEntry entry = new DiaryEntry();

                entry.setFileUri(res.getString(res.getColumnIndex("FILEURI")));
                entry.setCalories((res.getInt(res.getColumnIndex("CALORIES"))));
                entry.setTimestamp(res.getString(res.getColumnIndex("TIMESTAMP")));
                entry.setTitle(res.getString(res.getColumnIndex("TITLE")));
                entry.setDescription(res.getString(res.getColumnIndex("DESCRIPTION")));
                entry.setHappy(res.getInt(res.getColumnIndex("HAPPY")));
                entries.add(entry);
            } while (res.moveToNext());
        }
        return entries;
    }

    public boolean deleteEntry(String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        long deleteTestInt = db.delete(ENTRIES_TABLE_NAME, "TIMESTAMP = ?", new String[]{timestamp});
        boolean deletedSuccessfully = deleteTestInt != 0;
        return deletedSuccessfully;
    }

    public boolean addSelection(int index) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SELECTENTRYLIST, index);
        db.insert(SELECTED_ENTRIES_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean deleteSelection(String index) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SELECTED_ENTRIES_TABLE_NAME,"SELECTENTRYLIST = ?", new String[]{index});
        return true;
    }

    public List<Integer> getSelectedEntriesIndices() {
        List<Integer> selectedIndices = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor res = db.rawQuery("SELECT * FROM " + SELECTED_ENTRIES_TABLE_NAME, null);
        Cursor res = db.query(SELECTED_ENTRIES_TABLE_NAME, null, null, null, null, null, null, null);
        if (res.moveToFirst()) {
            do {
                selectedIndices.add(Integer.parseInt(res.getString(res.getColumnIndex("SELECTENTRYLIST"))));
            } while (res.moveToNext());
        }
        return selectedIndices;
    }
}


