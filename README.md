# EasySQLite
An Android Library make manipulating with sqlite more easier and more funny

### Work smart not hard, thus i create this library.
EasySQLite will help you to manipulate sqlite database without efforts

## Usage
To create database
'''java
        SQLiteDatabase db = EasySQLite.withContext(this).dbName("db").dbVersion(1).withModel(new ToDoItem()).getInstance();
'''

To create a table
First you must create a table model, some class like this
''' java
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
'''

You can use table annotation ' @Table(name="bla") ' to specify table name or you can not use it and the table name will be *Class name

For columns you can use column annotation '@Column()' which has some properties
+ 'name' String - define column name if not used or empty the column will named as variable name
+ 'isPrimaryKey' boolean - default 'false' used to define if column is primary key or not
+ 'hasDefaultValue' boolean - default 'false' ,if true the value which assigned to this variable will be a default value
+ 'isAutoIncrement' bollean - default 'false' , used to define if column is Auto-increment or not