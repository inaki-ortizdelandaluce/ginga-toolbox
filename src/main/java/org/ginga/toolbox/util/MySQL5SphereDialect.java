package org.ginga.toolbox.util;

import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.function.StandardSQLFunction;

public class MySQL5SphereDialect extends MySQL5InnoDBDialect {
    public MySQL5SphereDialect() {
           super();
           // register Dynamic Index Facility functions
           // See DIF at http://ross.iasfbo.inaf.it/dif/
           registerFunction("Sphedist", new StandardSQLFunction("Sphedist"));
    }
}
