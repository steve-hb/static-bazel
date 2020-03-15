package de.stvehb.bazel.core.api.files;

import java.nio.file.Path;

/**
 * Represents the ".gitIgnore" file which can be used to tell git to ignore specific files. (as the name indicates...)
 */
public class GitIgnore extends ProjectFile {

	public GitIgnore(Path directory) {
		super(directory);
	}

	@Override
	public String writeString() {
		return null;
	}

}
