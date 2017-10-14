package com.comtip.tip.ctcreate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TipRayong on 11/10/2560 11:40
 * CTCreate
 */

public class CritiqueDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "CritiqueDB";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "CritiqueTable";
    public static final String COL_KEYWORD = "keyword";
    public static final String COL_CT_0 = "ct0";
    public static final String COL_CT_1 = "ct1";
    public static final String COL_CT_2 = "ct2";
    public static final String COL_CT_3 = "ct3";
    public static final String COL_CT_4 = "ct4";
    public static final String COL_CT_5 = "ct5";
    Context context;

    public CritiqueDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_KEYWORD + " TEXT, "
                + COL_CT_0 + " TEXT, "
                + COL_CT_1 + " TEXT, "
                + COL_CT_2 + " TEXT, "
                + COL_CT_3 + " TEXT, "
                + COL_CT_4 + " TEXT, "
                + COL_CT_5 + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /*--------------------------------------------------------------------------------------
      Method: addCritique(SQLiteDatabase db, CritiqueClass critiqueClass)
      Description:  Add Interesting Data
    -------------------------------------------------------------------------------------*/
    public void addCritique(SQLiteDatabase db, CritiqueClass critiqueClass) {
        ContentValues values = new ContentValues();
        values.put(COL_KEYWORD, critiqueClass.get_keyword());
        values.put(COL_CT_0, critiqueClass.get_CT_0());
        values.put(COL_CT_1, critiqueClass.get_CT_1());
        values.put(COL_CT_2, critiqueClass.get_CT_2());
        values.put(COL_CT_3, critiqueClass.get_CT_3());
        values.put(COL_CT_4, critiqueClass.get_CT_4());
        values.put(COL_CT_5, critiqueClass.get_CT_5());
        db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    /*--------------------------------------------------------------------------------------
      Method: updateCritique(SQLiteDatabase db, CritiqueClass critiqueClass,String uKeyword)
      Description:  Update Existing Data
    -------------------------------------------------------------------------------------*/
    public void updateCritique(SQLiteDatabase db, CritiqueClass critiqueClass,String uKeyword) {
        ContentValues values = new ContentValues();
        values.put(COL_KEYWORD, critiqueClass.get_keyword());
        values.put(COL_CT_0, critiqueClass.get_CT_0());
        values.put(COL_CT_1, critiqueClass.get_CT_1());
        values.put(COL_CT_2, critiqueClass.get_CT_2());
        values.put(COL_CT_3, critiqueClass.get_CT_3());
        values.put(COL_CT_4, critiqueClass.get_CT_4());
        values.put(COL_CT_5, critiqueClass.get_CT_5());
        db = getWritableDatabase();
        String whereClause = COL_KEYWORD + "=?";
        String[] whereArgs = {uKeyword};
        db.update(TABLE_NAME,values,whereClause,whereArgs);
        db.close();
    }

    /*--------------------------------------------------------------------------------------
    Method: deleteCritique(SQLiteDatabase db, String dKeyword)
    Description:  Remove Unnecessary Data
   -------------------------------------------------------------------------------------*/
    public void deleteCritique(SQLiteDatabase db, String dKeyword) {
        db = getWritableDatabase();
        String whereClause = COL_KEYWORD + "=?";
        String[] whereArgs = {dKeyword};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    /*--------------------------------------------------------------------------------------
    Method: String [] getAllArchive(SQLiteDatabase db)
    Description:   Query all "keyword" data from database.
   -------------------------------------------------------------------------------------*/
    public  String [] getAllArchive(SQLiteDatabase db) {
        db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY _id DESC";
        Cursor cursor = db.rawQuery(query, null);
        List <String> mArchive = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex(COL_KEYWORD)) != null) {
                mArchive.add(cursor.getString(cursor.getColumnIndex(COL_KEYWORD)));
            }
            cursor.moveToNext();
        }
        db.close();
        String[] archive = {"null"};
        if(mArchive.size() > 0) {
            archive = new String[mArchive.size()];
            for (int i = 0; i < mArchive.size(); i++) {
                archive[i] = mArchive.get(i);
            }
        }
        return archive;
    }

    /*--------------------------------------------------------------------------------------
    Method: CritiqueClass getArchive (SQLiteDatabase db,String mKeyword)
    Description:   Query "critique" data from database by keyword.
    -------------------------------------------------------------------------------------*/
    public CritiqueClass getArchive (SQLiteDatabase db,String mKeyword){
        CritiqueClass critiqueClass = new CritiqueClass();
        db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_KEYWORD,COL_CT_0,COL_CT_1,COL_CT_2,COL_CT_3,COL_CT_4,COL_CT_5}
                , COL_KEYWORD+" =?" ,new String[]{mKeyword}, null, null, null);
        cursor.moveToFirst();

        critiqueClass.set_keyword(cursor.getString(cursor.getColumnIndex(COL_KEYWORD)));
        critiqueClass.set_CT_0(cursor.getString(cursor.getColumnIndex(COL_CT_0)));
        critiqueClass.set_CT_1(cursor.getString(cursor.getColumnIndex(COL_CT_1)));
        critiqueClass.set_CT_2(cursor.getString(cursor.getColumnIndex(COL_CT_2)));
        critiqueClass.set_CT_3(cursor.getString(cursor.getColumnIndex(COL_CT_3)));
        critiqueClass.set_CT_4(cursor.getString(cursor.getColumnIndex(COL_CT_4)));
        critiqueClass.set_CT_5(cursor.getString(cursor.getColumnIndex(COL_CT_5)));

        db.close();
        return  critiqueClass;
    }

    /*--------------------------------------------------------------------------------------
     Method: inArchive (SQLiteDatabase db,String nKeyword)
     Description:   Check "keyword" exist in database.
     -------------------------------------------------------------------------------------*/
    public boolean  inArchive (SQLiteDatabase db,String nKeyword) {
        db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_KEYWORD}
                , COL_KEYWORD+" =?" ,new String[]{nKeyword}, null, null, null);
        if(cursor.getCount() == 0) {
            db.close();
            return  false;
        } else {
            db.close();
            return true;
        }
    }

}
