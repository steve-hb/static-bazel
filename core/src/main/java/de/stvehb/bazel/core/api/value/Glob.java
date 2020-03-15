package de.stvehb.bazel.core.api.value;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * "Glob" is a Bazel helper function that finds all files matching certain path patterns.
 *
 * <p>
 *     Example (using backslashes because of JavaDocs syntax...):
 *     <pre>
 *         {@code
 *  glob(
 *    ["src\main\java\**\*.java"]
 *  )
 *         }
 *     </pre>
 * </p>
 *
 * <ul>
 *     <li>See <a href=https://docs.bazel.build/versions/master/be/functions.html#glob>Bazel Documentation - Glob function</a></li>
 * </ul>
 */
public class Glob implements Value {

	private final String[] patterns;

	public Glob(String... patterns) {
		this.patterns = patterns;
	}

	@Override
	public String compile() {
		return MessageFormat.format("glob({0})",
			new ArrayValue<>().addValues(Arrays.stream(this.patterns).map(StringValue::new).toArray(StringValue[]::new)).compile()
		);
	}

}
