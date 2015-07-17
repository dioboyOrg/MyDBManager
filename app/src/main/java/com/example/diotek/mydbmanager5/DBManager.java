/*
 * Class name : DBManager
 * Version : 1
 * Date : 2015-07-10
 * Copyright notice (License) : dioteck
 *
 * DB���Ͽ� �����Ͽ� ������ ����, ����, ����, ����
 *
 */

package com.example.diotek.mydbmanager5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper{

    Context mContext = null;

    static final String DB_ADDRESS = "Address.db";
    static final String TABLE_ADDRESS = "Address";
    static final String TABLE_DATE = "Date";
    static final int DB_VERSION = 1;

    private static DBManager mDbManager = null;

    //private�̹Ƿ� getInstance�Լ��� �̟G�Ͽ� ��ü�� �޾ƿ´�.
    //�̱������� ����
    public static DBManager getInstance(Context context) {

        //DB�� ���� ��� ������ �� return
        if(mDbManager == null) {
            mDbManager = new DBManager(context, DB_ADDRESS, null, DB_VERSION);
        }
        //�̹� �����ϴ� ��� ������ ���� return
        return mDbManager;
    }

    //������
    private DBManager(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, dbName, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        //���̺� ����
        try {//try catch���� ������� ������ ���� ������� �ʴ´�.
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ADDRESS +
                    "(" + "_index INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "phone_number TEXT, " +
                    "home_number TEXT, " +
                    "company_number TEXT ); ");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_DATE +
                    "(" + "dbName TEXT PRIMARY KEY, " +
                    "Date DATETIME ); ");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //������ ���̺��� �����ϰ� ���ο� ���̺��� �����Ѵ�.
        if( oldVersion < newVersion ){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
            onCreate(db);
        }
    }

    //db������ ��ȯ
    public SQLiteDatabase returnDatabase() {
        return getReadableDatabase();
    }

    //db���� ��¥ ����
    public void changeDate() {

        ContentValues addTimeValue = new ContentValues();
        long time = System.currentTimeMillis();

        addTimeValue.put("dbName", DB_ADDRESS);
        addTimeValue.put("Date", time);

        getWritableDatabase().insert(TABLE_DATE, null, addTimeValue);
       // getWritableDatabase().update(TABLE_DATE, addTimeValue, "dbName="+DB_ADDRESS ,null);
    }

    //������ ����
    public long insert(ContentValues addRowValue) {

        changeDate();
        return getWritableDatabase().insert(TABLE_ADDRESS, null, addRowValue);
    }

    //������ ����
    public int update(ContentValues addRowValue,String whereClause, String[] whereArgs ) {

        changeDate();
        return getWritableDatabase().update(TABLE_ADDRESS, addRowValue, whereClause, whereArgs);
    }

    //������ ����
    public int delete(String whereClause, String[] whereArgs) {

        changeDate();
        return getWritableDatabase().delete(TABLE_ADDRESS, whereClause, whereArgs);
    }

    //������ ����
    public Cursor query( String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return getReadableDatabase().query(TABLE_ADDRESS, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

}
