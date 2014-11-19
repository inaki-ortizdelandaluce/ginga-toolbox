/**
 *
 */
package org.ginga.tools.lacdump;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LACDumpTable {

    private List<LACDumpRow> rows = new ArrayList<LACDumpRow>();

    public LACDumpTable() {

    }

    public void addRow(LACDumpRow row) {
        this.rows.add(row);
    }

    public int getRowCount() {
        return this.rows.size();
    }

    public Iterator<LACDumpRow> iterator() {
        return this.rows.iterator();
    }
}
