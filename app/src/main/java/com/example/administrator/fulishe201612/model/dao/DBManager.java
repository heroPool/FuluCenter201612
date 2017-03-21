package com.example.administrator.fulishe201612.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.fulishe201612.model.bean.User;

/**
 * Created by Administrator on 2017/3/21.
 */

public class DBManager {
    private static DBManager dbManager = new DBManager();
    private DBOpenHelper dbOpenHelper;

    void onInit(Context context) {
        dbOpenHelper = new DBOpenHelper(context);

    }

    public static synchronized DBManager getInstance() {
        return dbManager;
    }

    public synchronized void closeDB() {
        if (dbOpenHelper != null) {
            dbOpenHelper.closeDB();

        }
    }

    public synchronized boolean saveUser(User user) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NAME,user.getMuserName());
        values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
        values.put(UserDao.USER_COLUMN_AVATAR_ID,user.getMavatarId());
        values.put(UserDao.USER_COLUMN_AVATAR_TYPE,user.getMavatarType());
        values.put(UserDao.USER_COLUMN_AVATAR_PATH,user.getMavatarPath());
        values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX,user.getMavatarSuffix());
        values.put(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME,user.getMavatarLastUpdateTime());

        if (db.isOpen()) {
            return db.replace(UserDao.USER_TABLE_NAME, null, values) != -1;
        }
        return false;
    }

    public synchronized User getUser(String username) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        String sql = "select * from " + UserDao.USER_TABLE_NAME + " where " +
                UserDao.USER_COLUMN_NAME + " =?";
        User user = null;
        Cursor cursor = db.rawQuery(sql, new String[]{username});
        if(cursor.moveToNext()){
            user = new User();
            user.setMuserName(username);
            user.setMuserNick(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NICK)));
            user.setMavatarId(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_ID)));
            user.setMavatarType(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_TYPE)));
            user.setMavatarPath(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_PATH)));
            user.setMavatarSuffix(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_SUFFIX)));
            user.setMavatarLastUpdateTime(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME)));
            return user;

        }
        return user;
    }

    public synchronized boolean updateUser(User user) {
        int result = -1;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String sql = UserDao.USER_COLUMN_NAME + "=?";
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserDao.USER_COLUMN_NICK, user.getMuserNick());

        if (db.isOpen()) {
            result = db.update(UserDao.USER_TABLE_NAME, contentValues, sql, new String[]{user.getMuserName()});
        }
        return result > 0;
    }
}
