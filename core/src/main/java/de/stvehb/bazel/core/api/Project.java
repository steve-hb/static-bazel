package de.stvehb.bazel.core.api;

import de.stvehb.bazel.core.api.files.GitIgnore;
import de.stvehb.bazel.core.api.files.BazelPackage;
import de.stvehb.bazel.core.api.files.BazelWorkspace;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a project containing a workspace and one/multiple packages.
 */
@Getter
public class Project {

	private static final Logger LOGGER = LoggerFactory.getLogger(Project.class.getSimpleName());

	// required
	private final BazelWorkspace workspace;
	private final List<BazelPackage> packages = new ArrayList<>();

	// optional
	private GitIgnore gitIgnore; //TODO

	public Project(Path directory) {
		this.workspace = new BazelWorkspace(directory);
	}

	/**
	 * Writes all the currently available data to the corresponding files.
	 */
	public void build() {
		try {
			Files.write(
				this.workspace.getDirectory().resolve("WORKSPACE"), //TODO: Writer-Class -> this is not an implementation package!
				this.workspace.write()
			);
		} catch (IOException ex) {
			LOGGER.error("Couldn't write workspace", ex);
			return;
		}

		this.packages.forEach(bazelPackage -> {
			try {
				Files.write(
					bazelPackage.getDirectory().resolve("BUILD"),
					bazelPackage.write()
				);
			} catch (IOException ex) {
				LOGGER.error("Couldn't write package", ex);
			}
		});

	}

}
