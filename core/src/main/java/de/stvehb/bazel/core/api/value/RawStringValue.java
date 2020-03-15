package de.stvehb.bazel.core.api.value;

/**
 * A raw string value without the quotation marks etc.
 */
public class RawStringValue extends StringValue {

	public RawStringValue(String value) {
		super(value);
	}

	@Override
	public String compile() {
		return this.getValue();
	}

}
