package com.virtual.xuancao.virtualapkprogram.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.virtual.xuancao.virtualapkprogram.db.dao.DaoMaster;

import org.greenrobot.greendao.database.Database;


/**
 * @author xiaolei.li
 */
public class THDevOpenHelper extends DaoMaster.OpenHelper {

    public THDevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.getInstance().migrate(db);
    }
}