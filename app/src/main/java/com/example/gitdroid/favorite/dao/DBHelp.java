package com.example.gitdroid.favorite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.gitdroid.favorite.model.LocalRepo;
import com.example.gitdroid.favorite.model.RepoGroup;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by 93432 on 2016/8/4.
 */
public class DBHelp extends OrmLiteSqliteOpenHelper {

    private static final String TABLE_NAME = "repo_favorite.db";
    private static final int VERSION = 3;
    private Context context;

    private DBHelp(Context context) {
        super(context, TABLE_NAME, null, VERSION);
        this.context = context;
    }

    private static DBHelp dbHelp;

    public static synchronized  DBHelp getInstance(Context context){
        if (dbHelp == null){
            dbHelp = new DBHelp(context.getApplicationContext());
        }
        return dbHelp;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            //创建类别表（里面还没有数据）
            TableUtils.createTableIfNotExists(connectionSource, RepoGroup.class);
            //创建本地仓库表（里面还没有数据）
            TableUtils.createTableIfNotExists(connectionSource, LocalRepo.class);

            //将本地的默认数据，添加到表里去
            new RepoGroupDao(this).createOrUpdate(RepoGroup.getDefaultGroups(context));
            new LocalRepoDao(this).createOrUpdate(LocalRepo.getDefaultLocalRepos(context));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, RepoGroup.class, true);
            TableUtils.dropTable(connectionSource, LocalRepo.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
