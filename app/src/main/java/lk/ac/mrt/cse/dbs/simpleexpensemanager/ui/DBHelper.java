package lk.ac.mrt.cse.dbs.simpleexpensemanager.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.Date;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "200250T.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table account(acc_no TEXT primary key, bank TEXT, acc_holder TEXT, init_bal NUMERIC)");
        sqLiteDatabase.execSQL("create Table log(date TEXT, acc_no TEXT, type TEXT, amount NUMERIC)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists account");
        sqLiteDatabase.execSQL("drop table if exists logs");
    }

    public boolean insertAccountdata(String acc_no, String bank, String acc_holder, double init_bal){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("acc_no", acc_no);
        contentValues.put("bank", bank);
        contentValues.put("acc_holder", acc_holder);
        contentValues.put("init_bal", init_bal);
        long result = sqLiteDatabase.insert("account", null, contentValues);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public boolean updateAccountdata(String acc_no, String bank, String acc_holder, double init_bal){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("init_bal", init_bal);
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from account where acc_no=?", new String[] {acc_no});
        if (cursor.getCount()>0) {
            long result = sqLiteDatabase.update("account", contentValues, "acc_no=?", new String[]{acc_no});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else{
            return false;
        }
    }

    public boolean deleteAccountdata(String acc_no){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("acc_no", acc_no);
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from account where acc_no=?", new String[] {acc_no});
        if (cursor.getCount()>0) {
            long result = sqLiteDatabase.delete("account", "acc_no=?", new String[]{acc_no});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else{
            return false;
        }
    }

    public Cursor getAccountdata(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from account", null);
        return cursor;
    }

    public Cursor getAccountdata(String acc_no){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from account where acc_no=" + acc_no, null);
        return cursor;
    }

    public Cursor getAccountNumbers(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select acc_no from account", null);
        return cursor;
    }

    public boolean insertLogdata(Date date, String accountNo, String expenseType, double amount){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", String.valueOf(date));
        contentValues.put("acc_no", accountNo);
        contentValues.put("type", String.valueOf(expenseType));
        contentValues.put("amount", amount);
        long result = sqLiteDatabase.insert("log", null, contentValues);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getLogdata(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from log", null);
        return cursor;
    }
}
