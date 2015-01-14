package org.ginga.toolbox.command;

import org.apache.log4j.Logger;
import org.ginga.toolbox.target.TargetDatabaseIngestor;

public class TargetDatabaseIngestorCmd {

    public static final Logger log = Logger.getLogger(TargetDatabaseIngestorCmd.class);

    public static void main(String[] args) {
        try {
            // write target list
            TargetDatabaseIngestor ingestor = new TargetDatabaseIngestor();
            ingestor.ingest();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}