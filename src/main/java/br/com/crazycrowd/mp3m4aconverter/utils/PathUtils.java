package br.com.crazycrowd.mp3m4aconverter.utils;

import com.google.common.io.Files;
import java.nio.file.Path;

/**
 *
 * @author marcos.romero
 */
public class PathUtils {
	
	/**
	 * Change a file extension
	 *
	 * @param filePath Path to a file.
	 * @param extension New extension for a file.
	 * @return Path for the a file with a new extension.
	 * @author marcos.romero
	 */
	public static Path changeFileExtension(Path filePath, FileExtension extension) {
		String fileNameWithoutExtension = Files.getNameWithoutExtension(filePath.getFileName().toString());
		return filePath.resolveSibling(fileNameWithoutExtension + "." + extension.getExtension());
	}
	
}
