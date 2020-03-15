package de.stvehb.bazel.cli;

import com.beust.jcommander.Parameter;
import lombok.Data;

@Data
public class Options {

	@Parameter(names = {"--help", "-h"}, help = true, description = "Shows this help/usage page")
	private boolean help;

	@Parameter(names = {"--directory", "--dir", "-d"}, description = "Project directory")
	private String directory = "";

	@Parameter(names = {"--relative", "--rel", "-r"}, description = "Whether the --directory option is relative")
	private boolean relative = true;

	@Parameter(names = {"--verbose", "--ver", "-v"}, description = "Verbose mode")
	private boolean verbose = true;

}
