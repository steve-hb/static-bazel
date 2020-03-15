package de.stvehb.bazel.core.api.files;

import de.stvehb.bazel.core.api.rule.ImportRule;
import de.stvehb.bazel.core.api.rule.Rule;
import de.stvehb.bazel.core.api.rule.VariableRule;
import lombok.Getter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a workspace defining external dependencies (using repository rules).
 *
 * <ul>
 *     <li>See <a href=https://docs.bazel.build/versions/2.2.0/build-ref.html#workspace>Bazel Documentation - Workspace</a></li>
 * </ul>
 */
@Getter
public class BazelWorkspace extends ProjectFile {

	private final List<Rule> rules = new ArrayList<>();

	public BazelWorkspace(Path directory) {
		super(directory);
	}

	public BazelWorkspace addImportRule(ImportRule rule) {
		this.rules.add(rule);
		return this;
	}

	public BazelWorkspace addRepositoryRule(Rule rule) {
		this.rules.add(rule);
		return this;
	}

	public BazelWorkspace addVariable(VariableRule rule) {
		this.rules.add(rule);
		return this;
	}

	@Override
	public String writeString() {
		return String.join("\n", this.rules.stream().map(Rule::compile).toArray(String[]::new));
	}

}
