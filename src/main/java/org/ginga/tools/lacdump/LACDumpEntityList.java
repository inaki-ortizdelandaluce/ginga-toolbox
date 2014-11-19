/**
 *
 */
package org.ginga.tools.lacdump;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LACDumpEntityList {

    private List<LACDumpEntity> entities = new ArrayList<LACDumpEntity>();

    public LACDumpEntityList() {

    }

    public void addEntity(LACDumpEntity row) {
        this.entities.add(row);
    }

    public int getEntityCount() {
        return this.entities.size();
    }

    public Iterator<LACDumpEntity> iterator() {
        return this.entities.iterator();
    }
}
