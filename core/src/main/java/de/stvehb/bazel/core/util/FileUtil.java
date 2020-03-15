package de.stvehb.bazel.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class FileUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class.getSimpleName());

	public static void copyDirectory(Path src, Path dest) {
		try {
			Files.walk(src).forEach(child -> {
				try {
					Path copy = dest.resolve(src.relativize(child));

					if(Files.isDirectory(child)) {
						if(!Files.exists(copy)) Files.createDirectory(copy);
					} else {
						Files.copy(child, copy);
					}

				} catch(Exception ex) {
					LOGGER.error("Error while copying directory", ex);
				}
			});
		} catch(Exception ex) {
			LOGGER.error("Error while copying directory", ex);
		}
	}

	public static void deleteDirectory(Path root) {
		try (Stream<Path> walk = Files.walk(root)) {
			walk.sorted(Comparator.reverseOrder())
				.forEach(path -> {
					try {
						Files.deleteIfExists(path);
					} catch (IOException ex) {
						LOGGER.error("Error while deleting directory", ex);
					}
				});
		} catch (IOException ex) {
			LOGGER.error("Error while deleting directory", ex);
		}
	}

}
