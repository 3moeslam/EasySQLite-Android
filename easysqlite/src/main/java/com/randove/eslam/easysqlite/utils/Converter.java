package com.randove.eslam.easysqlite.utils;

import android.database.Cursor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eslam on 12/9/2017.
 */

public class Converter {

    public <E extends Object> List<E> getListFromCursor(Class<E> cls,Cursor cursor){
        List<E> list = new ArrayList<>();
        while ((cursor.moveToNext())) {
            Object item = null;
            try {
                item = cls.newInstance();
                for (Field field : cls.getDeclaredFields()) {
                    if (!field.getName().equals("$change") && !field.getName().equals("serialVersionUID")) {

                        field.setAccessible(true);
                        String fieldName = field.getName();
                        Column columnAnnotation = field.getAnnotation(Column.class);
                        if (columnAnnotation != null) {
                            if (!columnAnnotation.name().equals(""))
                                fieldName = columnAnnotation.name();
                        }
                        if (field.getType().equals(String.class) || field.getType().equals(char.class)) {
                            field.set(item, cursor.getString(cursor.getColumnIndex(fieldName)));
                        } else if (field.getType().equals(int.class)) {
                            field.set(item, cursor.getInt(cursor.getColumnIndex(fieldName)));
                        } else if (field.getType().equals(float.class)) {
                            field.set(item, cursor.getFloat(cursor.getColumnIndex(fieldName)));
                        } else if (field.getType().equals(short.class)) {
                            field.set(item, cursor.getShort(cursor.getColumnIndex(fieldName)));
                        } else if (field.getType().equals(long.class)) {
                            field.set(item, cursor.getLong(cursor.getColumnIndex(fieldName)));
                        } else if (field.getType().equals(byte[].class)) {
                            field.set(item, cursor.getBlob(cursor.getColumnIndex(fieldName)));
                        } else if (field.getType().equals(byte.class)) {
                            field.set(item, (byte) cursor.getShort(cursor.getColumnIndex(fieldName)));
                        } else if (field.getType().equals(double.class)) {
                            field.set(item, cursor.getDouble(cursor.getColumnIndex(fieldName)));
                        }

                    }
                }
                if (item != null) list.add((E) item);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("EasySQLite: there are an error, please submit it at github repository by cpoy and past the error stack trace");
            } catch (InstantiationException e) {
                e.printStackTrace();
                throw new RuntimeException("EasySQLite: there are an error, please submit it at github repository by cpoy and past the error stack trace");
            }
        }
        return list;
    }
}
