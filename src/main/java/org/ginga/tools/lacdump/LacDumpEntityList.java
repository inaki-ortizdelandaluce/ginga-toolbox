/**
 *
 */
package org.ginga.tools.lacdump;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LacDumpEntityList {

    private List<LacDumpEntity> entities = new ArrayList<LacDumpEntity>();

    public LacDumpEntityList() {

    }

    public void addEntity(LacDumpEntity row) {
        this.entities.add(row);
    }

    public int getEntityCount() {
        return this.entities.size();
    }

    public Iterator<LacDumpEntity> iterator() {
        return this.entities.iterator();
    }
}
