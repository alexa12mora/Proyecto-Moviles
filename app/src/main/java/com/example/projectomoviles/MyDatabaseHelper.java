package com.example.projectomoviles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.projectomoviles.calendario.Event;

import java.time.LocalDate;
import java.time.LocalTime;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT)";
        db.execSQL(createTableQuery);
        db.execSQL("CREATE TABLE IF NOT EXISTS calendar (id INTEGER PRIMARY KEY AUTOINCREMENT, hour TEXT, nomMedi TEXT, nomComer TEXT, present TEXT, viaAdmin TEXT, mili TEXT, fInicio TEXT, fSuspension TEXT, frecToma TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aquí puedes ejecutar las sentencias SQL para actualizar tu esquema de base de datos si es necesario
        String dropTableQuery = "DROP TABLE IF EXISTS users";
        String dropTableCalendarQuery = "DROP TABLE IF EXISTS calendar";
        db.execSQL(dropTableQuery);
        db.execSQL(dropTableCalendarQuery);
        onCreate(db);
    }

    public boolean insertData(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password",password);
        long result = db.insert("users", null,contentValues);
        if(result==-1) return false;
        else
            return true;
    }
    public boolean updateData(String username, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPassword);
        int rowsAffected = db.update("users", contentValues, "username=?", new String[]{username});
        return rowsAffected > 0;
    }




    public boolean chechUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from users where username = ?", new String[] {username});
        if(cursor.getCount() > 0 ) return true;
        else
            return false;
    }

    public boolean chechUserPassword(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount() > 0 ) return true;
        else
            return false;
    }

    public boolean addMedToDatabase(Event event) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hour", event.getTime().toString());
        contentValues.put("nomMedi", event.getNMedi());
        contentValues.put("nomComer", event.getName());
        contentValues.put("present", event.getPresent());
        contentValues.put("viaAdmin", event.getViaAdmin());
        contentValues.put("mili", event.getMili());
        contentValues.put("fInicio", event.getDate().toString());
        contentValues.put("fSuspension", event.getDateFinish().toString());
        contentValues.put("frecToma", event.getName());
        long result = db.insert("calendar", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void populateCalendarListArray(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try(Cursor result= sqLiteDatabase.rawQuery("SELECT * FROM calendar",null)){
            if(result.getCount() > 0 ){
                while (result.moveToNext()){
                    int id = result.getInt(0);
                    String hour =result.getString(1);
                    String nomMedi = result.getString(2);
                    String nomComer = result.getString(3);
                    String present = result.getString(4);
                    String viaAdmin = result.getString(5);
                    String mili = result.getString(6);
                    String fInicio = result.getString(7);
                    String fSuspension = result.getString(8);
                    String frecToma = result.getString(9);
                    Event event = new Event(id,nomComer, LocalDate.parse(fInicio), LocalTime.parse(hour),nomMedi,present,viaAdmin,mili,LocalDate.parse(fSuspension),frecToma);
                    Event.Companion.getEventsList().add(event);
                }
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateCalendarInDB(Event event){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hour", event.getTime().toString());
        contentValues.put("nomMedi", event.getNMedi());
        contentValues.put("nomComer", event.getName());
        contentValues.put("present", event.getPresent());
        contentValues.put("viaAdmin", event.getViaAdmin());
        contentValues.put("mili", event.getMili());
        contentValues.put("fInicio", event.getDate().toString());
        contentValues.put("fSuspension", event.getDateFinish().toString());
        contentValues.put("frecToma", event.getFrecToma());
        db.update("calendar",contentValues,"id =?",new String[]{String.valueOf(event.getId())});
    }

    public void deleteEventInDB(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("calendar","id =?",new String[]{String.valueOf(id)});

    }
}
