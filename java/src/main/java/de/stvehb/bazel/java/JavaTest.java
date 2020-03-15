package de.stvehb.bazel.java;

import de.stvehb.bazel.core.api.Project;
import de.stvehb.bazel.core.api.files.BazelPackage;
import de.stvehb.bazel.core.api.value.ArrayValue;
import de.stvehb.bazel.core.api.value.Glob;
import de.stvehb.bazel.core.api.value.StringValue;
import lombok.Getter;

/**
 * See <a href=https://docs.bazel.build/versions/master/be/java.html#java_test>Bazel Documentation - java_test</a>
 */
public class JavaTest extends JavaLibrary {

	@Getter private final String ruleName = "java_test";

	public JavaTest(String name, BazelPackage bazelPackage) {
		super(name, bazelPackage);
		this.getProperties().put("visibility", new ArrayValue<>(new StringValue("//visibility:private")));
	}

	public void test_class(StringValue testClass) {
		this.getProperties().put("test_class", testClass);
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
	public JavaTest srcs(StringValue... srcs) {
		((ArrayValue<StringValue>) this.getProperties().computeIfAbsent("srcs", s -> new ArrayValue<>()))
			.addValues(srcs);
		return this;
	}

	public void resources(Glob srcs) {
		this.getProperties().put("resources", srcs);
	}

	@SuppressWarnings("unchecked")
	public JavaTest resources(StringValue... srcs) {
		((ArrayValue<StringValue>) this.getProperties().computeIfAbsent("resources", s -> new ArrayValue<>()))
			.addValues(srcs);
		return this;
	}

}
