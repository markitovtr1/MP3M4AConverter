package br.com.crazycrowd.mp3m4aconverter.steps;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface DirectoryListStrategy {

	Stream<Path> list(Path directory) throws IOException;
	
}
