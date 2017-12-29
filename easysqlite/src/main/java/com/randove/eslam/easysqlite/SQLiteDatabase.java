package com.randove.eslam.easysqlite;


import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.randove.eslam.easysqlite.engin.SQLiteHelper;
import com.randove.eslam.easysqlite.utils.Converter;
import com.randove.eslam.easysqlite.utils.StatementGenerator;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Eslam on 12/8/2017.
 * @author Eslam
 * @version 1
 * @since 0.6
 */


public class SQLiteDatabase {
    private SQLiteHelper helper;
    private android.database.sqlite.SQLiteDatabase database;
    private StatementGenerator statementGenerator = new StatementGenerator();

    protected SQLiteDatabase(DatabaseSelector selector){
        helper = new SQLiteHelper(selector.getContext(),selector.getDbName(),selector.getVersion());
        database = helper.getWritableDatabase();
    }

    public boolean updateRecord(Object model){
        database.execSQL(statementGenerator.getEditStatment(model,true,""));
        return true;
    }
    public boolean updateRecord(Object model,String condition){
        database.execSQL(statementGenerator.getEditStatment(model,true,condition));
        return true;
    }
    public boolean updateAllTableRecords(Object model){
        database.execSQL(statementGenerator.getEditStatment(model,false,""));
        return true;
    }

    public long add(Object model){
        database.execSQL(statementGenerator.getCreateTableStatement(model));
        ContentValues values = statementGenerator.getContentValues(model);
        return database.insert(statementGenerator.getTableName(model),null,values);
    }

    public <E extends Object> List<E> getData(Class<E> model, String condition){
        List<E> list = new ArrayList<>();
        try {
            Object o = model.newInstance();
            Cursor c = database.rawQuery(statementGenerator.getSelectStatment(o, condition), null);
            list = new Converter().getListFromCursor(model, c);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteTable(Object model){
        database.delete(statementGenerator.getTableName(model), null,null);
    }
    public void deleteRecord(Object model,String condition){
        if(condition != null){
            if(!condition.equals("")){
                database.delete(statementGenerator.getTableName(model),condition, null);
                Log.i(EasySQLite.TAG,"Delete done and condition is: "+condition);
                return;
            }
        }
        String idCondition = statementGenerator.getIDCondition(model);
        if(idCondition!=null){
            if(!idCondition.equals("")){
                database.delete(statementGenerator.getTableName(model),idCondition, null);
                Log.i(EasySQLite.TAG,"Delete done and condition is: "+idCondition);
                return;
            }
        }
        throw new RuntimeException("EasySQLite: this table has no primary key, so if we execute delete we will delete all table data\n " +
                "if you need to delete all data use deleteAllRecordData(), or to delete record you must provide condition");
    }

}
