/*
 * Class name : DBManager
 * Version : 1
 * Date : 2015-07-10
 * Copyright notice (License) : dioteck
 *
 * DB파일에 접근하여 데이터 삽입, 갱신, 삭제, 쿼리
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

    //private이므로 getInstance함수를 이욯하여 객체를 받아온다.
    //싱글톤으로 구현
    public static DBManager getInstance(Context context) {

        //DB가 없을 경우 생성한 후 return
        if(mDbManager == null) {
            mDbManager = new DBManager(context, DB_ADDRESS, null, DB_VERSION);
        }
        //이미 존재하는 경우 존재한 것을 return
        return mDbManager;
    }

    //생성자
    private DBManager(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, dbName, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        //테이블 생성
        try {//try catch문을 사용하지 않으면 앱이 실행되지 않는다.
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
        //기존의 테이블을 삭제하고 새로운 테이블을 생성한다.
        if( oldVersion < newVersion ){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
            onCreate(db);
        }
    }

    //db파일을 반환
    public SQLiteDatabase returnDatabase() {
        return getReadableDatabase();
    }

    //db파일 날짜 변경
    public void changeDate() {

        ContentValues addTimeValue = new ContentValues();
        long time = System.currentTimeMillis();

        addTimeValue.put("dbName", DB_ADDRESS);
        addTimeValue.put("Date", time);

        getWritableDatabase().insert(TABLE_DATE, null, addTimeValue);
       // getWritableDatabase().update(TABLE_DATE, addTimeValue, "dbName="+DB_ADDRESS ,null);
    }

    //데이터 삽입
    public long insert(ContentValues addRowValue) {

        changeDate();
        return getWritableDatabase().insert(TABLE_ADDRESS, null, addRowValue);
    }

    //데이터 갱신
    public int update(ContentValues addRowValue,String whereClause, String[] whereArgs ) {

        changeDate();
        return getWritableDatabase().update(TABLE_ADDRESS, addRowValue, whereClause, whereArgs);
    }

    //데이터 삭제
    public int delete(String whereClause, String[] whereArgs) {

        changeDate();
        return getWritableDatabase().delete(TABLE_ADDRESS, whereClause, whereArgs);
    }

    //데이터 쿼리
    public Cursor query( String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return getReadableDatabase().query(TABLE_ADDRESS, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

}
