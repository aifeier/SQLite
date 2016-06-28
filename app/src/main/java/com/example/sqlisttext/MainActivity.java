package com.example.sqlisttext;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;

import java.util.ArrayDeque;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private AppCompatTextView result;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private ArrayDeque<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (AppCompatTextView) findViewById(R.id.result);
        result.setMovementMethod(ScrollingMovementMethod.getInstance());
        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();
        clear();
        db.close();
        users = new ArrayDeque<>();
    }

    private int position = 0;

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.create:
                    break;
                case R.id.insert:
                    insert(users.size() + 1);
                    break;
                case R.id.delete:
                    delete(users.size());
                    break;
                case R.id.update:
                    update(users.size());
                    break;
                case R.id.clear:
                    clear();
                    break;

            }
            query();
            StringBuilder stringBuilder = new StringBuilder();
            for (User u : users) {
                stringBuilder.append(u.getId());
                stringBuilder.append(" " + u.getUserName());
                stringBuilder.append(" " + u.getPassword());
                stringBuilder.append("\n");

            }
            result.setText(stringBuilder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insert(int position) {
        db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();//实例化一个ContentValues用来装载待插入的数据cv.put("username","Jack Johnson");//添加用户名
        cv.put("password", "password" + position); //添加密码
        cv.put("username", "username" + position); //添加密码
        long r = db.insert("user", null, cv);//执行插入操作
        db.close();
        Log.e("ABC", r + "");
//        String sql = "insert into user(username,password) values ('Jack Johnson" + position + "','iLovePopMuisc')";//插入操作的SQL语句
//        db.execSQL(sql);//执行SQL语句
    }

    private void delete(int position) {
        db = databaseHelper.getWritableDatabase();
//        String whereClause = "username=?";//删除的条件
//        String[] whereArgs = {"username" + position};//删除的条件参数

        String whereClause = "_id=?";//删除的条件
        String[] whereArgs = {position + ""};//删除的条件参数
        long r = db.delete("user", whereClause, whereArgs);//执行删除
        db.close();
        Log.e("ABC", r + "");

//        String sql = "delete from user where username='Jack Johnson" + position + "'";//删除操作的SQL语句
//        db.execSQL(sql);//执行删除操作
    }

    private void update(int position) {
        db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();//实例化ContentValues
        cv.put("password", "iHatePopMusics" + position);//添加要更改的字段及内容
        String whereClause = "username=?";//修改条件
        String[] whereArgs = {"username" + position};//修改条件的参数
        db.update("user", cv, whereClause, whereArgs);//执行修改
        db.close();
    }

    private void clear() {
        databaseHelper.clear();
    }


    private void query() {
        users.clear();
        db = databaseHelper.getReadableDatabase();
        Cursor c = db.query("user", null, null, null, null, null, null);//查询并获得游标
        if (c.moveToFirst()) {//判断游标是否为空
            do {//移动到指定记录
                User u = new User();
                u.setId(c.getLong(c.getColumnIndex("_id")));
                u.setUserName(c.getString(c.getColumnIndex("username")));
                u.setPassword(c.getString(c.getColumnIndex("password")));
                users.add(u);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
    }
}
