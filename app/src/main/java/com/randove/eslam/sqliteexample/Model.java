package com.randove.eslam.sqliteexample;

import com.randove.eslam.easysqlite.utils.Column;
import com.randove.eslam.easysqlite.utils.Table;

/**
 * Created by Eslam on 12/8/2017.
 */

@Table(name = "NewModel2")
public class Model {
    @Column(isPrimeryKey = true, isAutoIncrement = true)
    int id;

    public String getAllah() {
        return allah;
    }

    public void setAllah(String allah) {
        this.allah = allah;
    }

    @Column(name = "Allah_Akbar")
    private String allah = "allah";

    public int getId() {
        return id;
    }

    public String getAkbar() {

        return Akbar;
    }

    public void setAkbar(String akbar) {
        Akbar = akbar;
    }

    private String Akbar= "Akbar";
    private String yarab="yarab";
}
