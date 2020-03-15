package de.stvehb.bazel.core.api.value;

/**
 * Represents a valid Starlark value.
 */
public interface Value {

	/**
	 * Compiles the {@link Value} into a Starlark compatible string.
 	 */
	String compile();

}
