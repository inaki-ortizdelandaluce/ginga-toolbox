package org.ginga.tools.lacdump;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class LACDumpParser {

    private static final Logger log = Logger.getLogger(LACDumpParser.class);

    private static final String SUPERFRAME_PREFIX = "*";
    private static final String LHV_ON_DATA_PREFIX = "LHV ON DATA";
    private static final String SEQ_NO = "SEQ_NO";
    private static final String DATE = "DATE";

    private File lacdumpFile;

    public LACDumpParser(File f) {
        this.lacdumpFile = f;
    }

    public LACDumpTable parse() throws IOException {
        LACDumpTable table = new LACDumpTable();
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new FileReader(this.lacdumpFile));
            String line = null;
            String lastSuperFrame = null;
            LACDumpRow row = null;

            int seqno;
            int seqnoBeginIdx = 0;
            int seqnoLength = 5;
            int dateBeginIdx = 5;
            int dateLength = 20;
            Date date;
            SimpleDateFormat dateFmt = new SimpleDateFormat("yyMMdd hh:mm:s");

            while ((line = reader.readLine()) != null) {

                log.debug("Line " + reader.getLineNumber() + ": " + line);

                if (line.startsWith(SUPERFRAME_PREFIX)) {
                    if (lastSuperFrame != null) {
                        log.debug("END Super Frame " + lastSuperFrame);
                    }
                    lastSuperFrame = line.substring(1, line.length() - 1).trim();
                    log.debug("BEGIN Super Frame " + lastSuperFrame);

                } else if (line.contains(LHV_ON_DATA_PREFIX)) {
                    continue;

                } else {
                    row = new LACDumpRow();
                    row.setSuperFrame(lastSuperFrame);
                    // seqno
                    seqno = Integer.valueOf(line.substring(seqnoBeginIdx, seqnoLength - 1).trim())
                            .intValue();
                    log.debug(SEQ_NO + " " + seqno);
                    row.setSequenceNumber(seqno);

                    // datetime
                    date = dateFmt.parse(line.substring(dateBeginIdx, dateLength).trim());
                    log.debug(DATE + " "
                            + new SimpleDateFormat("yyy-MM-dd'T'hh:mm:ss").format(date));
                    row.setDate(date);

                    // add tow to table
                    table.addRow(row);

                    break; // To be removed
                }
            }
        } catch (IOException | ParseException e) {
            throw new IOException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.debug("Could not close file reader");
                }
            }
        }

        return table;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage org.ginga.tools.lacdump.LacDumpParser <LAC dump file path>");
            System.exit(1);
        } else {
            File f = new File(args[0]);
            if (!f.exists()) {
                log.error("File " + f.getPath() + " not found");
                System.exit(1);
            }
            try {
                LACDumpParser parser = new LACDumpParser(f);
                LACDumpTable table = parser.parse();
                log.info("LACDUMP contains " + table.getRowCount() + " row(s)");
            } catch (IOException e) {
                log.error("Error parsing LACDUMP " + f.getPath() + ". Message=" + e.getMessage());
            }
        }
    }
}
