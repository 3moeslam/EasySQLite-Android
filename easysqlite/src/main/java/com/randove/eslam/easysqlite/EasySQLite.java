package com.randove.eslam.easysqlite;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by Eslam on 12/8/2017.
 * @author Eslam
 * @version 1
 * @since 1
 */

public class EasySQLite {

    public static final String TAG = "EasySQLITE";
    public static DatabaseSelector withContext(@NonNull Context context){
        if(context == null)
            throw new IllegalArgumentException("EasySQLite: Context must not be null.");
        return new DatabaseSelector(context);
    }


}
