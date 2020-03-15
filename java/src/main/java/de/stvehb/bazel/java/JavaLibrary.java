package de.stvehb.bazel.java;

import de.stvehb.bazel.core.api.Project;
import de.stvehb.bazel.core.api.files.BazelPackage;
import de.stvehb.bazel.core.api.rule.AbstractRule;
import de.stvehb.bazel.core.api.value.ArrayValue;
import de.stvehb.bazel.core.api.value.Glob;
import de.stvehb.bazel.core.api.value.StringValue;
import de.stvehb.bazel.core.util.AnalysisUtil;
import lombok.Getter;

/**
 * See <a href="https://docs.bazel.build/versions/2.2.0/be/java.html#java_library">Bazel Documentation - java_library</a>
 */
public class JavaLibrary extends AbstractRule {

	@Getter private final String ruleName = "java_library";

	public JavaLibrary(String name, BazelPackage bazelPackage) {
		super(name);
		this.setBazelPackage(bazelPackage);
		this.getProperties().put("visibility", new ArrayValue<>(new StringValue("//visibility:public")));
		//TODO: Should we restrict access to packages known to us or even private?
		// Could be helpful for "test" libraries: Never should another package rely on other packages test classes!
	}

	/**
	 * Resolves the dependencies: Are they internal targets or external maven artifacts?
	 */
	@SuppressWarnings("unchecked")
	public void resolveDependencies(Project project) {
		ArrayValue<StringValue> placeholderDeps = (ArrayValue<StringValue>) this.getProperties().get("deps");
		ArrayValue<StringValue> resolvedDeps = new ArrayValue<>();

		if (placeholderDeps != null) {
			placeholderDeps.getValues().forEach(dependency -> {
				// generate a clean target
				String cleanTarget = AnalysisUtil.cleanTarget(dependency.getValue());

				String resolvedTarget;
				if (AnalysisUtil.isInternalTarget(project, cleanTarget))
					resolvedTarget = AnalysisUtil.toInternalTarget(project, cleanTarget);
				else resolvedTarget = AnalysisUtil.toMavenTarget(cleanTarget);

				resolvedDeps.getValues().add(new StringValue(resolvedTarget));
			});
		}

		// replace placeholders with resolved dependencies
		this.getProperties().put("deps", resolvedDeps);
	}

	@SuppressWarnings("unchecked")
	public void deps(StringValue... deps) {
		((ArrayValue<StringValue>) this.getProperties().computeIfAbsent("deps", s -> new ArrayValue<>()))
			.addValues(deps);
	}

	public void srcs(Glob srcs) {
		this.getProperties().put("srcs", srcs);
	}

	@SuppressWarnings("unchecked")
	public JavaLibrary srcs(StringValue... srcs) {
		((ArrayValue<StringValue>) this.getProperties().computeIfAbsent("srcs", s -> new ArrayValue<>()))
			.addValues(srcs);
		return this;
	}

}
