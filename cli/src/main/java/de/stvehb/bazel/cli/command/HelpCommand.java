package de.stvehb.bazel.cli.command;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameters;
import de.stvehb.bazel.cli.Options;

@Parameters(commandDescription = "Shows the help/usage page")
public class HelpCommand {

	public static void handle(JCommander commander, Options options, HelpCommand cmd) {
		commander.usage();
	}

}
