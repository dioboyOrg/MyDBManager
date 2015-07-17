/*
 * Class name : FileManager
 * Version : 1
 * Date : 2015-07-10
 * Copyright notice (License) : dioteck
 *
 * DB������ SDī�忡 ����
 * SDī�带 ������ ����
 *
 */
package com.example.diotek.mydbmanager5;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileManager {

    static final String DB_ADDRESS = "Address.db";
    static final String TABLE_DATE = "Date";
    static final int FILE_VERSION = 1;

    private static final String SD_DB_ADDRESS = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Address.db";
    private static final String DBNAME = "dbName";
    private static final String DATE = "Date";
    private SQLiteDatabase mDatabase = null;
    private File mFile = null;

    public FileManager (Context context){
        mDatabase = DBManager.getInstance(context).returnDatabase();
        mFile= context.getDatabasePath(DB_ADDRESS);
    }

    //db������ SDī�忡 ����
    public void saveFile() {

        FileInputStream fis = null;
        FileOutputStream fos = null;
        try{
            fis = new FileInputStream(mFile);
            fos = new FileOutputStream(SD_DB_ADDRESS);
            byte[] memoData = new byte[fis.available()];

            while(fis.read(memoData) != -1) {
                fos.write(memoData);
            }

            fis.close();
            fos.close();

        }catch (Exception e){
            e.printStackTrace();

        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //db������ SDī�忡�� ����
    public void deleteFile() {
        File fos = new File(SD_DB_ADDRESS);
        fos.delete();
    }

    //SDī�忡 �ִ� db������ ������ ����
    public void copyFile() {
        if(checkNew()) {
            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                fis = new FileInputStream(SD_DB_ADDRESS);
                fos = new FileOutputStream(mFile);
                byte[] memoData = new byte[fis.available()];

                while (fis.read(memoData) != -1) {
                    fos.write(memoData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(fis != null){
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(fos != null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //SD���Ͽ� db���� ���� ���� Ȯ��
    public boolean checkFile() {
        File file = new File (SD_DB_ADDRESS);
        return file.exists();
    }

    //db������ �ֽ����� Ȯ��
    public boolean checkNew() {

        SQLiteDatabase mSdDatabase = SQLiteDatabase.openDatabase(SD_DB_ADDRESS, null, SQLiteDatabase.OPEN_READONLY);
        String[] columns = new String[] {DBNAME, DATE};
        Cursor DBc = null;
        Cursor SDc = null;

        DBc = mDatabase.query(TABLE_DATE, columns, null, null, null, null, null);
        SDc = mSdDatabase.query(TABLE_DATE, columns, null, null, null, null, null);

        DBc.moveToFirst();
        SDc.moveToFirst();

        if(DBc.getString(DBc.getColumnIndex(DATE)).compareTo(SDc.getString(SDc.getColumnIndex(DATE))) < 0 ) {
            DBc.close();
            SDc.close();
            return true;
        }
        else{
            DBc.close();
            SDc.close();
            return false;
        }

    }
}
