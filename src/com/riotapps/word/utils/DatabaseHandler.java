package com.riotapps.word.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "wordDB";
 
    // Words table name
    private static final String TABLE_WORDS = "words";
 
    // Words Table Columns names

    private static final String KEY_WORD = "word";

 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WORDS_TABLE = "CREATE TABLE " + TABLE_WORDS + "("
                + KEY_WORD + " TEXT PRIMARY KEY)";
        db.execSQL(CREATE_WORDS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
 
        // Create tables again
        onCreate(db);
    }
    
 //   private void loadWords(SQLiteDatabase db){
  //  	try {
 //   		db.beginTransaction();
  //  	    for (Value value : values) {
 //   	        ih.prepareForInsert();
 //   	        
 //   	        ih.bind(colIdx, value.getSomeValue());
 //   	        // ...
 //   	        ih.execute();
 //   	    }
 //   	    db.setTransactionSuccessful();
 //   	} finally {
  //  		db.endTransaction();           
   // 	}
  //  }
    
}
