package de.stvehb.bazel.core.api.files;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@RequiredArgsConstructor
public abstract class ProjectFile {

	@Getter private final Path directory;

	public abstract String writeString();

	public byte[] write() {
		return this.writeString().getBytes(StandardCharsets.UTF_8);
	}

}
