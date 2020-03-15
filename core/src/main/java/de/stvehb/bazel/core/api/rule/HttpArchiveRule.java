package de.stvehb.bazel.core.api.rule;

import de.stvehb.bazel.core.api.value.Value;
import lombok.Getter;

/**
 * A Bazel rule which downloads the given repository and makes its targets available for the project.
 *
 * <ul>
 *     <li>See <a href=https://docs.bazel.build/versions/2.2.0/repo/http.html#http_archive>Bazel Documentation - http_archive</a></li>
 * </ul>
 */
public class HttpArchiveRule extends AbstractRule {

	@Getter private final String ruleName = "http_archive";

	public HttpArchiveRule(String targetName, Value sha256, Value url) {
		this(targetName, sha256, url, null);
	}

	public HttpArchiveRule(String targetName, Value sha256, Value url, Value strip_prefix) {
		super(targetName);
		this.getProperties().put("sha256", sha256);
		this.getProperties().put("url", url);
		if (strip_prefix != null) this.getProperties().put("strip_prefix", strip_prefix);
	}

}
