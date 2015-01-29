package org.ginga.toolbox.gti;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nom.tam.fits.BinaryTableHDU;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsDate;
import nom.tam.fits.Header;
import nom.tam.util.BufferedFile;

import org.apache.log4j.Logger;
import org.ginga.toolbox.lacdump.LacdumpSfEntity;
import org.ginga.toolbox.util.TimeUtil;

public class GtiFitsWriter {

    private final static Logger log = Logger.getLogger(GtiFitsWriter.class);

    public class GtiRow {

        private Date startTime;
        private Date endTime;

        /**
         * @return the startTime
         */
        public Date getStartTime() {
            return this.startTime;
        }

        /**
         * @param startTime the startTime to set
         */
        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        /**
         * @return the endTime
         */
        public Date getEndTime() {
            return this.endTime;
        }

        /**
         * @param endTime the endTime to set
         */
        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }
    }

    public void write(List<LacdumpSfEntity> sfList, File f) throws IOException {
        try {
            // read entities into GTI structure
            List<GtiRow> rows = new ArrayList<GtiRow>();
            String lastSuperFrame = null;
            Date lastDate = null;
            int lastSeqNo = -1;
            GtiRow row = null;
            for (LacdumpSfEntity sf : sfList) {
                if (!sf.getSuperFrame().equals(lastSuperFrame)) { // new SF
                    if (lastSuperFrame != null) {
                        row.setEndTime(lastDate);
                        rows.add(row);
                    }
                    row = new GtiRow();
                    row.setStartTime(sf.getDate());
                } else if (sf.getSequenceNumber() > lastSeqNo + 1) {
                    // end previous
                    row.setEndTime(lastDate);
                    rows.add(row);
                    // begin new
                    row = new GtiRow();
                    row.setStartTime(sf.getDate());
                }
                lastSeqNo = sf.getSequenceNumber();
                lastDate = sf.getDate();
                lastSuperFrame = sf.getSuperFrame();
            }
            if (lastSeqNo > 0) {
                // end previous
                row.setEndTime(lastDate);
                rows.add(row);
            }
            writeToFits(rows, f);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public void writeToFits(List<GtiRow> rows, File f) throws IOException {
        try {
            // build FITS data
            log.info(rows.size() + "GTI row(s) found");
            String[] startTimes = new String[rows.size()];
            String[] endTimes = new String[rows.size()];
            for (int i = 0; i < rows.size(); i++) {
                startTimes[i] = FitsDate.getFitsDateString(rows.get(i).getStartTime());
                endTimes[i] = FitsDate.getFitsDateString(rows.get(i).getEndTime());
            }
            // build FITS header
            Header header = new Header();
            header.addValue("CREATION_TIME",
                    TimeUtil.formatInputDate(new Date(System.currentTimeMillis())),
                    "Creation time of GTI FITS File");
            header.addValue("AUTHOR",
                    "This GTI file has been generated automatically by the GingaToolbox", "Author");
            // build FITS hdu
            BinaryTableHDU hdu = new BinaryTableHDU(null, null);
            hdu.addColumn(startTimes);
            hdu.addColumn(endTimes);
            Fits fits = new Fits();
            fits.addHDU(hdu);
            BufferedFile bf = new BufferedFile(f, "rw");
            fits.write(bf);
            bf.close();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
