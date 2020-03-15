package de.stvehb.bazel.core.util;

import de.stvehb.bazel.core.api.Project;
import de.stvehb.bazel.core.api.files.BazelPackage;
import de.stvehb.bazel.core.api.rule.AbstractRule;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalysisUtil {

	private static final Pattern CLEAN_TARGET_MATCH = Pattern.compile("[a-zA-Z0-9_]*");
	private static final Pattern CLEAN_TARGET_SPLIT = Pattern.compile("(?:[^a-zA-Z0-9_]*)([a-zA-Z0-9_]*)(?:[^a-zA-Z0-9_]*)");

	public static boolean isInternalTarget(Project project, String target) {
		return getRuleByInternalTarget(project, target).isPresent();
	}

	public static String toInternalTarget(Project project, String cleanTarget) {
		return toInternalTarget(AnalysisUtil.getPackageByInternalTarget(project, cleanTarget).get(), cleanTarget);
	}

	public static String toInternalTarget(BazelPackage bazelPackage, String cleanTarget) {
		if (!isTargetClean(cleanTarget)) throw new IllegalArgumentException("Target is not clean: " + cleanTarget);

		return StringUtil.format(
			"//{}:{}",
			bazelPackage.getWorkspace().getDirectory().relativize(bazelPackage.getDirectory()),
			cleanTarget
		);
	}

	public static Optional<BazelPackage> getPackageByInternalTarget(Project project, String target) {
		Optional<AbstractRule> optional = getRuleByInternalTarget(project, target);
		return optional.map(AbstractRule::getBazelPackage);
	}

	public static Optional<AbstractRule> getRuleByInternalTarget(Project project, String target) {
		if (!isTargetClean(target)) throw new IllegalArgumentException("Target is not clean: " + target);

		return project.getPackages().stream().map(bazelPackage ->
			bazelPackage.getRules().stream()
				.filter(rule -> rule instanceof AbstractRule)
				.map(rule -> (AbstractRule) rule)
				.filter(rule -> rule.getTargetName().equals(target))
				.findFirst().orElse(null)
		).filter(Objects::nonNull).findFirst();
	}

	public static String cleanTarget(String target) {
		Matcher matcher = CLEAN_TARGET_SPLIT.matcher(target);
		if (!matcher.find()) return null;
		return matcher.group(1);
	}

	public static String convertToTarget(String mavenDependency) {
		int end = mavenDependency.lastIndexOf(":");
		if (end == -1) end = mavenDependency.length();
		return mavenDependency.substring(0, end).replaceAll("[:.-]", "_");
	}

	public static String toMavenTarget(String cleanTarget) {
		if (!isTargetClean(cleanTarget)) throw new IllegalArgumentException("Target is not clean: " + cleanTarget);
		return StringUtil.format("@maven//:{}", cleanTarget);
	}

	private static boolean isTargetClean(String target) {
		return CLEAN_TARGET_MATCH.matcher(target).matches();
	}

}
