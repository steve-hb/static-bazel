package de.stvehb.bazel.core.api.rule;

import de.stvehb.bazel.core.api.files.BazelPackage;
import de.stvehb.bazel.core.api.value.Value;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.text.MessageFormat;
import java.util.Map;

/**
 * Represents not really a rule - it's more like a variable declaration and definition. The variables can be referenced
 * later by other rules.
 */
@RequiredArgsConstructor
public class VariableRule implements Rule {

	@Getter @Setter private BazelPackage bazelPackage;
	@Getter private final String key;
	@Getter private final Value value;

	@Override
	public Map<String, Value> getProperties() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRuleName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String compile() {
		return this.key + " = " + MessageFormat.format("{0}\n", this.value.compile());
	}

}
