package de.stvehb.bazel.core.api.files;

import de.stvehb.bazel.core.api.rule.ImportRule;
import de.stvehb.bazel.core.api.rule.Rule;
import lombok.Getter;

import java.nio.file.Path;
import java.util.List;

/**
 * Represents a directory containing source files and one "Build" file.
 *
 * <ul>
 *     <li>See <a href=https://docs.bazel.build/versions/2.2.0/build-ref.html#packages>Bazel Documentation - Packages</a></li>
 * </ul>
 */
public class BazelPackage extends ProjectFile {

	@Getter private final BazelWorkspace workspace;
	@Getter private final List<ImportRule> imports;
	@Getter private final List<Rule> rules;

	public BazelPackage(Path directory, BazelWorkspace workspace, List<ImportRule> imports, List<Rule> rules) {
		super(directory);
		this.imports = imports;
		this.rules = rules;
		this.workspace = workspace;

		this.imports.forEach(r -> r.setBazelPackage(this));
		this.rules.forEach(r -> r.setBazelPackage(this));
	}

	@Override
	public String writeString() {
		StringBuilder sb = new StringBuilder();

		this.imports.forEach(rule -> sb.append(rule.compile()));
		this.rules.forEach(rule -> sb.append(rule.compile()));

		return sb.toString();
	}

}
