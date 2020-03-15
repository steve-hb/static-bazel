package de.stvehb.bazel.core.api.rule;

import de.stvehb.bazel.core.api.files.BazelPackage;
import de.stvehb.bazel.core.api.value.StringValue;
import de.stvehb.bazel.core.api.value.Value;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractRule implements Rule {

	@Getter private final Map<String, Value> properties = new LinkedHashMap<>(); // to keep order use linked hash map
	@Getter @Setter private BazelPackage bazelPackage;
	@Getter private final String targetName;

	public AbstractRule(String targetName) {
		if (targetName != null) {
			targetName = targetName.replaceAll("-", "_").replaceAll("\\.", "_"); //TODO: Method
			this.properties.put("name", new StringValue(targetName));
		}

		this.targetName = targetName;
	}

}
