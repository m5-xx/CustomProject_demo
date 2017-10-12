package com.mayday.sql.music;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mayday.tool.localMusicManager.MusicInfo;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xy-pc on 2017/6/26.
 */

public class MyLoveSql extends OrmLiteSqliteOpenHelper {


    private static final String DATABASE_NAME="love.db";
    private static int Version=1;
    private static MyLoveSql myLoveSql;
    private Map<String ,Dao> maps=new HashMap<>();

    public MyLoveSql(Context context) {
        super(context, DATABASE_NAME, null, Version);
    }

    //单利模式
    public static synchronized MyLoveSql getInstance(Context context){
        if(myLoveSql==null){
            synchronized (MyLoveSql.class){
                if(myLoveSql==null){
                    myLoveSql=new MyLoveSql(context);
                }
            }
        }
        return myLoveSql;
    }

    //获取实体类(MusicInfo)对象
    public synchronized Dao getDaoInstance(Class cls) throws SQLException {
        Dao dao=null;
        String clsaaName=cls.getSimpleName();
        //如果存在该类
        if(maps.containsKey(clsaaName)){
            dao=maps.get(clsaaName);
        }else {
            dao=super.getDao(cls);
            maps.put(clsaaName,dao);
        }
        return dao;
    }

    /**
     * 关闭对对象的访问(ORMLite特有的)
     */
    public void close(){
        super.close();
        for(String str:maps.keySet()){
            Dao dao = maps.get(str);
            dao=null;
        }
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, MusicInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
