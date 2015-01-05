package org.ginga.toolbox.command;

import java.io.File;
import java.util.Comparator;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.ginga.toolbox.observation.ObservationBgDatabaseIngestor;

public class BackgroundDatabaseIngestorCmd {

    public static final Logger log = Logger.getLogger(BackgroundDatabaseIngestorCmd.class);

    public static void main(String[] args) {
        try {
            CommandLine commandLine = new BasicParser().parse(getOptions(), args);
            // read command line argument values
            // read command line argument values
            File root = null;
            if (commandLine.hasOption("d")) {
                String dirPath = commandLine.getOptionValue("d");
                root = new File(dirPath);
            } else {
                printHelp();
                System.exit(0);
            }
            // write target list
            ObservationBgDatabaseIngestor ingestor = new ObservationBgDatabaseIngestor();
            ingestor.ingest(root);
        } catch (ParseException e) {
            log.error(e.getMessage());
            printHelp();
        }
    }

    private static void printHelp() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setOptionComparator(new Comparator<Option>() {

            private static final String OPTS_ORDER = "dh"; // short option names

            @Override
            public int compare(Option o1, Option o2) {
                String argCharOption1 = o1.getLongOpt().substring(0, 1);
                String argCharOption2 = o2.getLongOpt().substring(0, 1);
                return OPTS_ORDER.indexOf(argCharOption1) - OPTS_ORDER.indexOf(argCharOption2);
            }
        });
        helpFormatter.printHelp(BackgroundDatabaseIngestorCmd.class.getCanonicalName(),
                getOptions());
    }

    @SuppressWarnings("static-access")
    private static Options getOptions() {
        Options options = new Options();
        Option fileOption = OptionBuilder.withArgName("file path").withLongOpt("directory")
                .withDescription("Ginga data root directory").hasArg().create("d");
        Option helpOption = new Option("h", "help", false, "show command help");

        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        group.addOption(helpOption);
        group.addOption(fileOption);
        options.addOptionGroup(group);
        return options;
    }
}