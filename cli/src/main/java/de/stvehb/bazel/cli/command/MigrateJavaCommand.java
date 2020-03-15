package de.stvehb.bazel.cli.command;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import de.stvehb.bazel.cli.Options;
import de.stvehb.bazel.core.api.Project;
import de.stvehb.bazel.core.util.FileUtil;
import de.stvehb.bazel.java.maven.MavenProjectAnalyzer;
import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
@Parameters(commandNames = "migrate:java", commandDescription = "Migrates a Java (Maven) based project to Bazel")
public class MigrateJavaCommand {

	@Parameter(names = {"--build", "-b"}, arity = 1, description = "Build the Bazel project and export WORKSPACE & BUILD files")
	private boolean build = true;

	@Parameter(names = {"--backup", "-B"}, arity = 1, description = "Creates a backup of the project directory")
	private boolean backup = false;

	@Parameter(names = {"--copy", "-c"}, arity = 1, description = "Creates a copy of the directory and migrates the copy")
	private boolean copy = false;

	@Parameter(names = {"--useExternalTestRule"}, description = "Use external Bazel rule for java tests")
	private boolean useExternalTestRule = true;

	public static void handle(JCommander commander, Options options, MigrateJavaCommand cmd) {
		Path directory = Paths.get(System.getProperty("user.dir") + options.getDirectory());
		String directoryName = directory.getParent().relativize(directory).toString();

		if (cmd.isBackup()) FileUtil.copyDirectory(directory, directory.getParent().resolve(directoryName + "_backup"));
		if (cmd.isCopy()) {
			Path copy = directory.getParent().resolve(directoryName + "_copy");
			FileUtil.copyDirectory(directory, copy);
			directory = copy;
		}

		Project project = new Project(directory);
		MavenProjectAnalyzer analyzer = new MavenProjectAnalyzer(project);
		analyzer.analyzeProject(directory);

		if (cmd.isBuild()) project.build();
	}

}
