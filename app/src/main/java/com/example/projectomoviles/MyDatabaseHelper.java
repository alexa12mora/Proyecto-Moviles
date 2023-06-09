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
    private static final String DATABASE_NAME = "moviles.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)";
        db.execSQL(createTableQuery);
        db.execSQL("CREATE TABLE IF NOT EXISTS calendar (id INTEGER PRIMARY KEY AUTOINCREMENT, hour TEXT, nomMedi TEXT, nomComer TEXT, present TEXT, viaAdmin TEXT, mili TEXT, fInicio TEXT, fSuspension TEXT, frecToma TEXT, idUsuario INTEGER, idGroupMedi INTEGER, FOREIGN KEY (idUsuario)  REFERENCES users(id))");
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
    public int getIdUser(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select id from users where username = ?", new String[] {username});

        int id_usuario = -1;
        if(cursor.moveToFirst())
            id_usuario = cursor.getInt(0);
        return id_usuario;
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
        contentValues.put("idUsuario", event.getUserId());
        contentValues.put("idGroupMedi", event.getIdGroupMedi());
        long result = db.insert("calendar", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void populateCalendarListArray(int _userId){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try(Cursor result= sqLiteDatabase.rawQuery("SELECT * FROM calendar WHERE idUsuario = ?" , new String[] {String.valueOf(_userId)})){
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
                    int idGroupMedi = result.getInt(11);
                    Event event = new Event(id,nomComer, LocalDate.parse(fInicio), LocalTime.parse(hour),nomMedi,present,viaAdmin,mili,LocalDate.parse(fSuspension),frecToma,_userId,idGroupMedi);
                    Event.Companion.getEventsList().add(event);
                }
            }
        }

    }

    //metodo para obtener el ultimo id que se ingresó en la tabla de calendario
    public int obtenerUltimoId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String consulta = "SELECT MAX(id) FROM calendar";
        Cursor cursor = db.rawQuery(consulta, null);

        int ultimoId = -1;

        if (cursor.moveToFirst()) {
            ultimoId = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return ultimoId;
    }

    public int obtenerUltimoIdmMedi() {
        SQLiteDatabase db = this.getReadableDatabase();
        String consulta = "SELECT MAX(idGroupMedi) FROM calendar";
        Cursor cursor = db.rawQuery(consulta, null);

        int ultimoId = -1;

        if (cursor.moveToFirst()) {
            ultimoId = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return ultimoId;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean updateCalendarInDB(Event event){
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
        long result = db.update("calendar",contentValues,"id =?",new String[]{String.valueOf(event.getId())});
        if(result==-1) return false;
        else
            return true;
    }

    public boolean updateCalendarAllInDB(Event event){
        SQLiteDatabase db = getWritableDatabase();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor= sqLiteDatabase.rawQuery("SELECT * FROM calendar WHERE idUsuario = ? AND idGroupMedi = ?" , new String[] {String.valueOf(event.getUserId()),String.valueOf(event.getIdGroupMedi())});
        long result = 0;
        if (cursor.getCount() > 0) {
            do{
                ContentValues contentValues = new ContentValues();
                contentValues.put("nomMedi", event.getNMedi());
                contentValues.put("nomComer", event.getName());
                contentValues.put("present", event.getPresent());
                contentValues.put("viaAdmin", event.getViaAdmin());
                contentValues.put("mili", event.getMili());
                contentValues.put("frecToma", event.getFrecToma());
                contentValues.put("hour", event.getTime().toString());
                result = db.update("calendar",contentValues,"idGroupMedi =?",new String[]{String.valueOf(event.getIdGroupMedi())});
            }while (cursor.moveToNext());
        }
        if(result==-1) return false;
        else
            return true;
    }

    public boolean deleteEventInDB(int id){
        SQLiteDatabase db = getWritableDatabase();
        long result = db.delete("calendar","id =?",new String[]{String.valueOf(id)});
        if(result==-1) return false;
        else
            return true;
    }
}