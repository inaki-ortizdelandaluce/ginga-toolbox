package org.ginga.toolbox.command;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public abstract class Command {
	
	private CommandLine commandLine;
	private Options options;
	
	public Command(String[] args) throws IllegalArgumentException {
		this.options = getOptions();
		try {
			this.commandLine = new BasicParser().parse(this.options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			printHelp();
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public String getOptionValue(String opt) {
		return this.commandLine.getOptionValue(opt);
	}
	
	public abstract Options getOptions();
	
	public abstract void execute() throws CommandExecutionException;
	
	protected void printHelp() {
		HelpFormatter helpFormatter = new HelpFormatter();
    	helpFormatter.printHelp(SpectraExtractorCmd.class.getCanonicalName(), this.options);
    }   
}
