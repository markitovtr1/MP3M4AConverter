package br.com.crazycrowd.mp3m4aconverter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.Guice;
import com.google.inject.Injector;

import br.com.crazycrowd.mp3m4aconverter.config.EnvironmentModule;
import br.com.crazycrowd.mp3m4aconverter.steps.PathProcessor;

/**
 *
 * @author marcos.romero
 */
public class StartPoint {

	public static void main(final String[] args) throws Exception {
		if (args.length != 1) {
			throw new IllegalArgumentException("Wrong number of arguments! Expected only a single one specifying a directory.");
		}

		Path directory = Paths.get(args[0]);
		if (!Files.isDirectory(directory)) {
			throw new IllegalArgumentException("Path " + directory + " is not a directory.");
		}

		Injector injector = Guice.createInjector(new EnvironmentModule());
		PathProcessor processor = injector.getInstance(PathProcessor.class);
		processor.process(directory);
	}

}
