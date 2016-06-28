package com.example.sqlisttext;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created at 陈 on 2016/6/28.
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "mydata.db"; //数据库名称
    private static final int version = 2; //数据库版本

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table user" +
                "(_id integer primary key autoincrement , username varchar(20) not null , password varchar(60) not null );";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void clear() {
        String sql = "DELETE FROM user";//清空数据
//        String sql = "DROP TABLE user";//删除表
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
        sql = "update sqlite_sequence SET seq = 0 where name ='user'";//自增长ID为0
        database.execSQL(sql);
        database.close();
    }
}
