package org.ginga.toolbox.command;

import java.text.DecimalFormat;
import java.util.Comparator;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.ginga.toolbox.util.SimbadTargetResolver;
import org.ginga.toolbox.util.SimbadTargetResolver.TargetCoordinates;
import org.ginga.toolbox.util.SimbadTargetResolver.TargetNotResolvedException;

public class TargetNameResolverCmd {

    public static final Logger log = Logger.getLogger(TargetNameResolverCmd.class);

    public void resolve(String target) {
        try {
            TargetCoordinates coords = new SimbadTargetResolver().resolve(target);
            System.out.println(target);
            System.out.println(" Coordinates (epoch=B1950 equinox=1950):");
            DecimalFormat formatter = new DecimalFormat("#.####");
            System.out.println("  Righ Ascension [deg] = " + formatter.format(coords.getRaDeg()));
            System.out.println("  Declination [deg] =" + formatter.format(coords.getDecDeg()));
        } catch (TargetNotResolvedException e) {
            log.error("Target " + target + "could not be resolved." + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            CommandLine commandLine = new BasicParser().parse(getOptions(), args);
            // read command line argument values
            String target = commandLine.getOptionValue("t");
            if (commandLine.hasOption("h")) {
                printHelp();
                System.exit(0);
            }
            // write target list
            TargetNameResolverCmd cmd = new TargetNameResolverCmd();
            cmd.resolve(target);
        } catch (ParseException e) {
            log.error(e.getMessage());
            printHelp();
        }
    }

    private static void printHelp() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setOptionComparator(new Comparator<Option>() {

            private static final String OPTS_ORDER = "th"; // short option names

            @Override
            public int compare(Option o1, Option o2) {
                String argCharOption1 = o1.getLongOpt().substring(0, 1);
                String argCharOption2 = o2.getLongOpt().substring(0, 1);
                return OPTS_ORDER.indexOf(argCharOption1) - OPTS_ORDER.indexOf(argCharOption2);
            }
        });
        helpFormatter.printHelp(TargetNameResolverCmd.class.getCanonicalName(), getOptions());
    }

    @SuppressWarnings("static-access")
    private static Options getOptions() {
        Options options = new Options();

        Option targetOption = OptionBuilder.withArgName("target").withLongOpt("target")
                .isRequired().withDescription("Target name").hasArg().create("t");

        Option helpOption = new Option("h", "help", false, "show command help");

        options.addOption(targetOption);
        options.addOption(helpOption);

        return options;
    }
}
