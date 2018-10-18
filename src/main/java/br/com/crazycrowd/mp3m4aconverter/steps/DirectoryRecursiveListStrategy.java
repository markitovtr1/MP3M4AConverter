package br.com.crazycrowd.mp3m4aconverter.steps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class DirectoryRecursiveListStrategy implements DirectoryListStrategy {
	
	@Override
	public Stream<Path> list(Path directory) throws IOException {
		return Files.walk(directory);
	}

}
