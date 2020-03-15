package de.stvehb.bazel.core.api.files;

import java.nio.file.Path;
import java.text.MessageFormat;

/**
 * Represents the ".bazelversion" file which officially is only supported by <i>Bazelisk</i> but nevertheless internally
 * checked by Bazel before every startup. (Btw: this is currently undocumented behaviour)
 *
 * <ul>
 *     <li>See <a href=https://github.com/bazelbuild/bazelisk#how-does-bazelisk-know-which-bazel-version-to-run-and-where-to-get-it-from>Bazelisk on GitHub</a></li>
 * </ul>
 */
public class BazelVersion extends ProjectFile {

	private final String version;

	public BazelVersion(Path directory, String version) {
		super(directory);
		this.version = version;
	}

	@Override
	public String writeString() {
		return MessageFormat.format("{0}\n", this.version); // New line needed because of critical Bazel bug in 2.1.0 which led to instant shutdowns before any output
	}

}
