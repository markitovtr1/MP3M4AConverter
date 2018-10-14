package br.com.crazycrowd.mp3m4aconverter.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Singleton;

/**
 * Class to avoid using Files directly for some actions. This helps us avoid
 * using PowerMock.
 * 
 * @author marcos.romero
 *
 */
@Singleton
public class FileHelper {

	public boolean deleteIfExists(Path path) throws IOException {
		return Files.deleteIfExists(path);
	}

	public boolean exists(Path path) {
		return Files.exists(path);
	}

}
