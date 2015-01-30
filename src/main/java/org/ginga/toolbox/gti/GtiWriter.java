package org.ginga.toolbox.gti;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nom.tam.fits.Fits;
import nom.tam.fits.FitsFactory;
import nom.tam.util.BufferedFile;

import org.apache.log4j.Logger;
import org.ginga.toolbox.lacdump.LacdumpSfEntity;
import org.ginga.toolbox.lacdump.dao.LacdumpDao;
import org.ginga.toolbox.lacdump.dao.impl.LacdumpDaoImpl;

public class GtiWriter {

    private final static Logger log = Logger.getLogger(GtiWriter.class);

    public class GtiRow {

        private Date startDate;
        private Date endDate;

        /**
         * @return the startDate
         */
        public Date getStartDate() {
            return this.startDate;
        }

        /**
         * @param startDate the startDate to set
         */
        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        /**
         * @return the endDate
         */
        public Date getEndDate() {
            return this.endDate;
        }

        /**
         * @param endDate the endDate to set
         */
        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }
    }

    public static void main(String[] args) {
        String target = "GS2000+25";
        File f = new File("/tmp/GTI_" + target + "-5.fits");
        try {
            // query entities matching the criteria
            LacdumpDao dao = new LacdumpDaoImpl();
            List<LacdumpSfEntity> sfList = dao.findSfList("MPC3", target, "1988-05-02 01:34:31",
                    "1988-05-02 03:09:27", 5.0, 10.0);
            log.info("Query executed successfully. " + sfList.size() + " result(s) found");
            // save matching results into a GTI file
            GtiWriter gtiFitsWriter = new GtiWriter();
            gtiFitsWriter.writeToFits(sfList, f);
            log.info("GTI file " + f.getPath() + " written successfully");
        } catch (Exception e) {
            log.error("Error generating GTI file " + f.getPath(), e);
        }
    }

    public void writeToFits(List<LacdumpSfEntity> sfList, File f) throws IOException {
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
                        row.setEndDate(lastDate);
                        rows.add(row);
                    }
                    row = new GtiRow();
                    row.setStartDate(sf.getDate());
                } else if (sf.getSequenceNumber() > lastSeqNo + 1) {
                    // end previous
                    row.setEndDate(lastDate);
                    rows.add(row);
                    // begin new
                    row = new GtiRow();
                    row.setStartDate(sf.getDate());
                }
                lastSeqNo = sf.getSequenceNumber();
                lastDate = sf.getDate();
                lastSuperFrame = sf.getSuperFrame();
            }
            if (lastSeqNo > 0) {
                // end previous
                row.setEndDate(lastDate);
                rows.add(row);
            }
            // build FITS data
            log.info(rows.size() + " GTI row(s) found");
            double[][] data = new double[rows.size()][2];
            long zeroTime = rows.get(0).getStartDate().getTime();
            for (int i = 0; i < rows.size(); i++) {
                data[i][0] = (rows.get(i).getStartDate().getTime() - zeroTime) / 1000;
                data[i][1] = (rows.get(i).getEndDate().getTime() - zeroTime) / 1000;
            }
            // build FITS
            Fits fits = new Fits();
            fits.addHDU(FitsFactory.HDUFactory(data));
            BufferedFile bf = new BufferedFile(f, "rw");
            fits.write(bf);
            bf.flush();
            bf.close();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}