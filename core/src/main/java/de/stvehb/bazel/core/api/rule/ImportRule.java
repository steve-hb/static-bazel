package de.stvehb.bazel.core.api.rule;

import de.stvehb.bazel.core.api.files.BazelPackage;
import de.stvehb.bazel.core.api.value.Value;
import lombok.Getter;
import lombok.Setter;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A rule which imports the given <i>rules</i> from the given <i>source</i> file to make them available to the file.
 *
 * <ul>
 *     <li>See <a href=https://docs.bazel.build/versions/master/skylark/rules.html#rule-creation>Bazel Documentation - Rule creation</a></li>
 * </ul>
 */
public class ImportRule implements Rule {

	@Getter @Setter private BazelPackage bazelPackage;
	@Getter private final String source;
	@Getter private final List<String> rules;

	public ImportRule(String source, String... rules) {
		this.source = source;
		this.rules = Arrays.asList(rules);
	}

	@Override
	public Map<String, Value> getProperties() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRuleName() {
		return "load";
	}

	@Override
	public String compile() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getRuleName()).append("(");
		sb.append(MessageFormat.format("\"{0}\"", this.source));
		this.rules.forEach(rule -> {
			sb.append(MessageFormat.format(", \"{0}\"", rule));
		});
		sb.append(")\n");
		return sb.toString();
	}

}
