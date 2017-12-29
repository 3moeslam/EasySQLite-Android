package com.randove.eslam.easysqlite.utils;

/**
 * Declare Columns data
 * Created by Eslam on 12/8/2017.
 * @author Eslam
 * @version 1
 * @since 1
 */


public @interface Column {
    String name() default "";
    boolean isAutoIncrement() default false;
    boolean isPrimeryKey() default false;
    boolean hasDefaultValue() default false;
}
