package de.stvehb.bazel.core.api.value;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an array of {@link Value}s.
 *
 * <p>
 *     Example output:
 *     	<pre>
 *      	{@code
 * 	[
 * 	  "src/test/java/Test1.java",
 * 	  "src/test/java/Test2.java",
 * 	  "src/test/java/Test3.java"
 * 	]
 *      	}
 * 		</pre>
 * </p>
 */
public class ArrayValue<T extends Value> implements Value {

	@Getter private final List<T> values = new ArrayList<>();

	public ArrayValue(T... values) {
		this.addValues(values);
	}

	public ArrayValue<T> addValues(T... values) {
		this.values.addAll(Arrays.asList(values));
		return this;
	}

	@Override
	public String compile() {
		return "[\n" + String.join(
			",\n",
			this.values.stream().map(v -> "		" + v.compile())
				.toArray(String[]::new)
		) + "\n	]";
	}

}
