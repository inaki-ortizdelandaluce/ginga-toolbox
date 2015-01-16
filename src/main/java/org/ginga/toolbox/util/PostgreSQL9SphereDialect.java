package org.ginga.toolbox.util;

import org.hibernate.dialect.PostgreSQL9Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;

public class PostgreSQL9SphereDialect extends PostgreSQL9Dialect {

    public PostgreSQL9SphereDialect() {
        super();
        // register Q3C functions
        // http://code.google.com/p/q3c/
        registerFunction("g3c_dist", new StandardSQLFunction("Sphedist"));
    }
}
