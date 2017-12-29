package com.randove.eslam.easysqlite.utils;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.util.Log;

import com.randove.eslam.easysqlite.EasySQLite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eslam on 12/8/2017.
 * @author Eslam
 * @version 1
 * @since 1
 */

public class StatementGenerator {
    /***
     * Generate String ArrayList contain table columns (class fields)
     * @param model model class
     * @return names
     */
    public List<String> getColumns(Object model){
        if(model == null)
            throw new IllegalArgumentException("EasySQLite: Model must not be null.");
        List<String> columns = new ArrayList<>();
        Class c = model.getClass();
        for(Field field : c.getDeclaredFields()){
            if(!field.getName().equals("$change")&&!field.getName().equals("serialVersionUID")){
                field.setAccessible(true);
                Column columnAnnotation = field.getAnnotation(Column.class);
                if(columnAnnotation != null)
                    columns.add(columnAnnotation.name());
                else
                    columns.add(field.getName());
            }
        }
        return columns;
    }

    /**
     * Generate String ArrayList contain table columns (class fields) and it's data
     * @param model model class
     * @return names
     */
    public ContentValues getContentValues(@NonNull Object model){
        Class c = model.getClass();
        if(c.getDeclaredFields().length == 2)
            throw new IllegalArgumentException("EasySQLite: Model must not be empty.");
        ContentValues values = new ContentValues();
        try {
            for (Field field : c.getDeclaredFields()) {
                if (!field.getName().equals("$change") && !field.getName().equals("serialVersionUID")) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    if(columnAnnotation != null) {
                        fieldName = columnAnnotation.name();
                        if(columnAnnotation.isAutoIncrement())
                            continue;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(field.getName());
                    //TODO Work with more data types
                    if(field.getType().equals(String.class)){
                        values.put(fieldName,String.valueOf(field.get(model)));
                    }if(field.getType().equals(char.class)){
                        values.put(fieldName,String.valueOf(field.getChar(model)));
                    }else if(field.getType().equals(byte.class)){
                        values.put(fieldName,field.getByte(model));
                    }else if(field.getType().equals(short.class)){
                        values.put(fieldName,field.getShort(model));
                    }else if(field.getType().equals(int.class)){
                        values.put(fieldName,field.getInt(model));
                    }else if(field.getType().equals(long.class)){
                        values.put(fieldName,field.getLong(model));
                    }else if(field.getType().equals(float.class)){
                        values.put(fieldName,field.getFloat(model));
                    }else if(field.getType().equals(double.class)){
                        values.put(fieldName,field.getDouble(model));
                    }else if(field.getType().equals(byte[].class)){
                        values.put(fieldName,(byte[]) field.get(model));
                    }
                }
            }
        }catch (Exception ex){
        }
        return values;
    }


    public String getSelectStatment(Object model,String condition){
        Class c = model.getClass();
        if(c.getDeclaredFields().length == 2)
            throw new IllegalArgumentException("EasySQLite: Model must not be empty.");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(getTableName(model));
        if(condition != null){
            if(!condition.isEmpty()){
                sb.append(" WHERE ");
                sb.append(condition);
            }
        }
        Log.i(EasySQLite.TAG,"Select Statment is: "+sb.toString());
        return sb.toString();
    }

    public String getEditStatment(Object model ,boolean isRecordEdit ,String condition){
        Class c = model.getClass();
        if(c.getDeclaredFields().length == 2)
            throw new IllegalArgumentException("EasySQLite: Model must not be empty.");
            StringBuilder sb = new StringBuilder();
            try {
                sb.append("UPDATE ");
                sb.append(getTableName(model));
                sb.append(" SET ");
                boolean tableHasPrimaryKey = false;
                Field primaryField = null;
                for (Field field : c.getDeclaredFields()) {
                    if (!field.getName().equals("$change") && !field.getName().equals("serialVersionUID")) {
                        field.setAccessible(true);
                        String columnName = field.getName();
                        Column columnAnnotation = field.getAnnotation(Column.class);
                        if (columnAnnotation != null) {
                            if (!tableHasPrimaryKey) {
                                tableHasPrimaryKey = columnAnnotation.isPrimeryKey();
                                primaryField = field;
                            }
                            if (!columnAnnotation.name().equals("")) {
                                columnName = columnAnnotation.name();
                            }
                        }
                        sb.append(columnName);
                        sb.append("=");
                        if (field.getType().equals(String.class)) {
                            sb.append("'");
                            sb.append(field.get(model));
                            sb.append("'");
                        }else{
                            sb.append(field.get(model));
                        }
                        sb.append(",");
                    }
                }
                sb.replace(sb.lastIndexOf(","),sb.lastIndexOf(",")+1,"");
                if(!isRecordEdit) {
                    if (!condition.equals("")) {
                        sb.append(" WHERE ");
                        if (tableHasPrimaryKey) {
                            Column col = primaryField.getAnnotation(Column.class);
                            if (!col.name().equals("")) {
                                sb.append(col.name());
                            } else {
                                sb.append(primaryField.getName());
                            }
                            sb.append("=");
                            if (primaryField.getType().equals(String.class)) {
                                sb.append("'");
                                sb.append(primaryField.get(model));
                                sb.append("'");
                            } else {
                                sb.append(primaryField.get(model));
                            }
                        } else {
                            throw new RuntimeException("EasySQLite: this table has no primary key, so if we execute update we will update all table data\n " +
                                    "if you need to update all data use updateAllTableRecords(), or to update record you must provide condition");
                        }
                    } else {
                        sb.append(condition);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        return sb.toString();
    }

    public String getDeleteTableStatment(Object model){
        Class c = model.getClass();
        if(c.getDeclaredFields().length == 2)
            throw new IllegalArgumentException("EasySQLite: Model must not be empty.");
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE ");
        sb.append("FROM ");
        sb.append(getTableName(model));
        Log.i(EasySQLite.TAG,sb.toString());
        return sb.toString();
    }
    public String getDeleteStatement(Object model, String condition){
        Class c = model.getClass();
        if(c.getDeclaredFields().length == 2)
            throw new IllegalArgumentException("EasySQLite: Model must not be empty.");
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("DELETE ");
            sb.append("FROM ");
            sb.append(getTableName(model));
            if (condition != null) {
                if (!condition.isEmpty()) {
                    sb.append(" WHERE ");
                    sb.append(condition);
                    Log.i(EasySQLite.TAG,sb.toString());
                    return sb.toString();
                }
            }
            boolean tableHasPrimaryKey = false;
            Field primaryField = null;
            String primaryName = "s";
            for (Field field : c.getDeclaredFields()) {
                if (!field.getName().equals("$change") && !field.getName().equals("serialVersionUID")) {
                    field.setAccessible(true);
                    primaryName = field.getName();
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    if (columnAnnotation != null) {
                        if (!tableHasPrimaryKey) {
                            tableHasPrimaryKey = columnAnnotation.isPrimeryKey();
                            primaryField = field;
                        }
                        if (!columnAnnotation.name().equals("")) {
                            primaryName = columnAnnotation.name();
                        }
                        if (tableHasPrimaryKey) break;
                    }
                }
            }
            if(tableHasPrimaryKey) {
                sb.append(" WHERE ");
                sb.append(primaryName);
                sb.append("=");
                if (primaryField.getType().equals(String.class)) {
                    sb.append("'");
                    sb.append(primaryField.get(model));
                    sb.append("'");
                } else {
                    sb.append(primaryField.get(model));
                }
            }else{
                throw new RuntimeException("EasySQLite: this table has no primary key, so if we execute delete we will delete all table data\n " +
                        "if you need to delete all data use deleteAllRecordData(), or to delete record you must provide condition");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        Log.i(EasySQLite.TAG,sb.toString());
        return sb.toString();
    }

    public String getIDCondition(Object model){
        Class c = model.getClass();
        if(c.getDeclaredFields().length == 2)
            throw new IllegalArgumentException("EasySQLite: Model must not be empty.");
        StringBuilder sb = new StringBuilder();
        for (Field field : c.getDeclaredFields()) {
            if (!field.getName().equals("$change") && !field.getName().equals("serialVersionUID")) {
                try {
                    field.setAccessible(true);
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    if (columnAnnotation != null) {
                        if (columnAnnotation.isPrimeryKey()) {
                            if (!columnAnnotation.name().equals("")) {
                                sb.append(columnAnnotation.name());
                                sb.append("=");
                            }else{
                                sb.append(field.getName());
                                sb.append("=");
                            }
                            if (field.getType().equals(String.class)) {
                                sb.append("'");
                                sb.append(field.get(model));
                                sb.append("'");
                            }else {
                                sb.append(field.get(model));
                            }
                            return sb.toString();
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }
    /**
     * @param model database table model
     * @return Table name
     */
    public String getTableName(Object model){
        Table tableAnnotation = model.getClass().getAnnotation(Table.class);
        if(tableAnnotation != null) {
            if(tableAnnotation.name().contains(" "))
                throw new IllegalArgumentException("EasySQLite: Table name must not contain white spaces, error in name of table ("+tableAnnotation.name()+")");
            return tableAnnotation.name();
        }
        return model.getClass().getSimpleName();
    }

    /**
     * @param model
     * @return Create table if not exist statement
     */
    public String getCreateTableStatement(Object model){
        Class c = model.getClass();
        if(c.getDeclaredFields().length == 2)
            throw new IllegalArgumentException("EasySQLite: Model must not be empty.");

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(getTableName(model));
        sb.append("(");

        for(Field field : c.getDeclaredFields()){
            if (!field.getName().equals("$change") && !field.getName().equals("serialVersionUID")) {

                field.setAccessible(true);
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    if (!column.name().equals("")) {
                        if(column.name().contains(" "))
                            throw new IllegalArgumentException("EasySQLite: Column name must not contain white spaces, error in name of column ("+column.name()+")");
                        sb.append(column.name());
                        Log.i(EasySQLite.TAG,"column :"+field.getName()+" is named: "+column.name());
                    }else{
                        sb.append(field.getName());
                    }
                }else
                    sb.append(field.getName());
                sb.append(" ");
                if (field.getType().equals(byte.class) || field.getType().equals(short.class)
                        || field.getType().equals(int.class) || field.getType().equals(long.class)) {
                    sb.append("INTEGER ");
                } else if (field.getType().equals(String.class) || field.getType().equals(char.class)) {
                    sb.append("TEXT ");
                } else if (field.getType().equals(byte[].class)) {
                    sb.append("BLOB ");
                }
                if (column != null) {
                    if (column.isPrimeryKey()) {
                        sb.append("PRIMARY KEY ");
                    }
                    if (column.hasDefaultValue()) {
                        sb.append("DEFAULT ");
                        if (field.getType().equals(String.class) || field.getType().equals(char.class)) {
                            sb.append("'");
                            try {
                                sb.append(String.valueOf(field.get(model)));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            sb.append("' ");
                        } else {
                            try {
                                sb.append(String.valueOf(field.get(model)));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    if (column.isAutoIncrement()) {
                        sb.append("AUTOINCREMENT ");
                    }
                }
                sb.append(",");
            }
        }
        sb.replace(sb.lastIndexOf(","),sb.lastIndexOf(",")+1,"");
        sb.append(")");
        return sb.toString();
    }
}
