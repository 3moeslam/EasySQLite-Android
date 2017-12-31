# EasySQLite
An Android Library make manipulating with sqlite more easier and more funny

### Work smart not hard, that why i create this library.
EasySQLite will help you to manipulate sqlite database without efforts


## Installation

Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```


Add the dependency
```
dependencies {
    compile 'com.github.3moeslam:EasySQLite-Android:v0.6'
}
```

## Usage
To create database
```java
SQLiteDatabase db = EasySQLite.withContext(this).dbName("db").dbVersion(1).withModel(new ToDoItem()).getInstance();
```

To create a table
First you must create a table model, some class like this
``` java
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
```

You can use table annotation ``` @Table(name="bla") ``` to specify table name if it empty or not used table name will be *Class name*

For columns you can use column annotation `@Column()` which has some properties
+ ```name``` String - define column name if not used or empty the column will named as variable name
+ ```isPrimaryKey``` boolean - default `false` used to define if column is primary key or not
+ ```hasDefaultValue``` boolean - default `false` ,if true the value which assigned to this variable will be a default value
+ ```isAutoIncrement``` boolean - default `false` , used to define if column is Auto-increment or not

**To Create table and insert item**

```db.add(new ToDoItem("This is a dummy todo item"));```

**To Get all Table Records**

```List<ToDoItem> list = db.getData(ToDoItem.class,null);```

Or Add Select Condition

```List<ToDoItem> conditionList = db.getData(ToDoItem.class,"item like '%test%'");```


**To update record**

```
ToDoItem item = list.get(0);
item.setItem("Test2");
db.updateRecord(item);
```


Or you can update item under condition

```
ToDoItem updateWithCondition = new ToDoItem("Test 3");
db.updateRecord(updateWithCondition,"id=1");
```

Or Update all table records

```
ToDoItem updateAllTableRecords = new ToDoItem("All Record Update");
db.updateAllTableRecords(updateAllTableRecords);
```


**To Delete Record**

```
db.deleteRecord(list.get(0),null);
```

To delete all table

```
db.deleteTable(new ToDoItem(""));
```


