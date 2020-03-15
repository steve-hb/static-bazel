package de.stvehb.bazel.java.maven;

import de.stvehb.bazel.core.api.files.BazelWorkspace;
import de.stvehb.bazel.core.api.rule.HttpArchiveRule;
import de.stvehb.bazel.core.api.rule.ImportRule;
import de.stvehb.bazel.core.api.rule.VariableRule;
import de.stvehb.bazel.core.api.value.ArrayValue;
import de.stvehb.bazel.core.api.value.RawStringValue;
import de.stvehb.bazel.core.api.value.StringValue;
import de.stvehb.bazel.java.MavenInstall;

public class MavenDefaultSetup {

	/**
	 * Adds variables, imports and rules to the given <i>workspace</i> which are essential for a Bazel-Maven setup.
	 *
	 * @param installArtifacts The object which will (in the future) contain all artifacts which have to be installed by Maven
	 */
	public static void setup(BazelWorkspace workspace, ArrayValue<StringValue> installArtifacts) {
		//TODO: Remove hardcoded values
		workspace.addImportRule(new ImportRule("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive"));
		workspace.addVariable(new VariableRule("RULES_JVM_EXTERNAL_TAG", new StringValue("3.0")));
		workspace.addVariable(new VariableRule("RULES_JVM_EXTERNAL_SHA", new StringValue("62133c125bf4109dfd9d2af64830208356ce4ef8b165a6ef15bbff7460b35c3a")));

		workspace.addRepositoryRule(
			new HttpArchiveRule(
				"rules_jvm_external",
				new RawStringValue("RULES_JVM_EXTERNAL_SHA"),
				new RawStringValue("\"https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip\" % RULES_JVM_EXTERNAL_TAG"),
				new RawStringValue("\"rules_jvm_external-%s\" % RULES_JVM_EXTERNAL_TAG")
			)
		);

		workspace.addImportRule(new ImportRule("@rules_jvm_external//:defs.bzl", "maven_install"));

		workspace.addRepositoryRule(new MavenInstall(
			installArtifacts,
			new ArrayValue<>(new StringValue("https://repo1.maven.org/maven2"))
		));
	}

}
