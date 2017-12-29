package com.randove.eslam.sqliteexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.randove.eslam.easysqlite.EasySQLite;
import com.randove.eslam.easysqlite.SQLiteDatabase;
import com.randove.eslam.easysqlite.utils.Column;
import com.randove.eslam.easysqlite.utils.Table;

import java.util.List;

public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db = EasySQLite.withContext(this).dbName("db").dbVersion(1).withModel(new ToDoItem()).getInstance();
        //To Create table and insert item
        db.add(new ToDoItem("This is a dummy todo item"));

        //To Get all Table Records
        List<ToDoItem> list = db.getData(ToDoItem.class,null);
        //Or Add Select Condition
        List<ToDoItem> conditionList = db.getData(ToDoItem.class,"item like '%test%");

        //To update record
        ToDoItem item = list.get(0);
        item.setItem("Test2");
        db.updateRecord(item);
        //Or you can update item under condition
        ToDoItem updateWithCondition = new ToDoItem("Test 3");
        db.updateRecord(updateWithCondition,"id=1");
        //Or Update all table records
        ToDoItem updateAllTableRecords = new ToDoItem("All Record Update");
        db.updateAllTableRecords(updateAllTableRecords);

        //To Delete Record
        db.deleteRecord(list.get(0),null);
        //To delete all table
        db.deleteTable(new ToDoItem(""));


    }
    @Table(name = "TODO")
    public class ToDoItem {
        @Column(isPrimeryKey = true, isAutoIncrement = true)
        int id;
        private String item;

        public ToDoItem() {
        }

        public ToDoItem(String item) {
            this.id = id;
            this.item = item;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }
    }
}
