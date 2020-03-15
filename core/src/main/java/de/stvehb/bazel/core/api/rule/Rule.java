package de.stvehb.bazel.core.api.rule;

import de.stvehb.bazel.core.api.files.BazelPackage;
import de.stvehb.bazel.core.api.value.Value;

import java.util.Map;

/**
 * Represents a Bazel rule.
 *
 * <ul>
 *     <li>See <a href=https://docs.bazel.build/versions/2.2.0/build-ref.html#rules>Bazel Documentation - Rules (theory)</a></li>
 *     <li>See <a href=https://docs.bazel.build/versions/master/rules.html>Bazel Documentation - Rules (list of available rules)</a></li>
 * </ul>
 */
public interface Rule {

	/**
	 * Returns the rule properties as a {@link Map}.
	 */
	Map<String, Value> getProperties();

	String getRuleName();

	BazelPackage getBazelPackage();

	void setBazelPackage(BazelPackage bazelPackage);

	default String compile() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getRuleName()).append("(\n");
		this.getProperties().forEach((key, value) -> {
			sb.append("	").append(key).append(" = ").append(value.compile()).append(",\n");
		});
		sb.append(")\n");
		return sb.toString();
	}

}
