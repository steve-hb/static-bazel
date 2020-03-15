package de.stvehb.bazel.java;

import de.stvehb.bazel.core.api.rule.AbstractRule;
import de.stvehb.bazel.core.api.value.ArrayValue;
import de.stvehb.bazel.core.api.value.StringValue;
import lombok.Getter;

/**
 * "maven_install" is a repository rule which allows transitive maven dependency fetching.
 *
 * <ul>
 *     <li>See <a href="https://docs.bazel.build/versions/master/migrate-maven.html#guava-project-example-external-dependencies">Bazel Maven Migration Guide - External dependencies</a></li>
 *     <li>See <a href="https://github.com/bazelbuild/rules_jvm_external#usage">Rules JVM External on GitHub</a></li>
 * </ul>
 */
public class MavenInstall extends AbstractRule {

	@Getter private final String ruleName = "maven_install";

	public MavenInstall(ArrayValue<StringValue> artifacts, ArrayValue<StringValue> repositories) {
		super(null);
		this.getProperties().put("artifacts", artifacts);
		this.getProperties().put("repositories", repositories);
	}

}
