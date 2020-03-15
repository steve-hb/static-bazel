package de.stvehb.bazel.core.api.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;

/**
 * Represents a string which will be compiled with quotation marks around it to be valid for Bazels Skylark language.
 */
@RequiredArgsConstructor
public class StringValue implements Value {

	@Getter private final String value;

	@Override
	public String compile() {
		return MessageFormat.format("\"{0}\"", this.value);
	}

}
