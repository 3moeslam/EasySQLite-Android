package com.randove.eslam.easysqlite;

import android.content.Context;

/**
 * Created by Eslam on 12/8/2017.
 * @author Eslam
 * @version 1
 * @since 1
 */

public class DatabaseSelector {

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    private Context context;
    private String dbName;
    private int version;
    private Object model;

    public DatabaseSelector(Context context){
        this.context = context;
    }
    public DatabaseSelector dbName(String dbName){
        if(dbName == null || dbName.equals(""))
            throw new IllegalArgumentException("EasySQLite: Database Name must not be null, or empty");
        this.dbName = dbName;
        return this;
    }

    public DatabaseSelector dbVersion(int version){
        this.version = version;
        return this;
    }

    public DatabaseSelector withModel(Object model){
        if(model == null)
            throw new IllegalArgumentException("EasySQLite: Model must not be null.");
        this.model = model;
        return this;
    }

    public SQLiteDatabase getInstance(){
        return new SQLiteDatabase(this);
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
