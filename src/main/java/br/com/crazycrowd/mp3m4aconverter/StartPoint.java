package br.com.crazycrowd.mp3m4aconverter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.Guice;
import com.google.inject.Injector;

import br.com.crazycrowd.mp3m4aconverter.config.EnvironmentModule;
import br.com.crazycrowd.mp3m4aconverter.steps.PathProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author marcos.romero
 */
@Slf4j
public class StartPoint {

	public static void main(final String[] args) throws Exception {
		if (args.length != 1) {
			log.error("Wrong number of arguments! Expected only a single one specifying a directory.");
			System.exit(-1);
		}

		Path directory = Paths.get(args[0]);
		if (!Files.isDirectory(directory)) {
			log.error("Path {} is not a directory.", directory);
			System.exit(-1);
		}

		Injector injector = Guice.createInjector(new EnvironmentModule());
		PathProcessor processor = injector.getInstance(PathProcessor.class);
		processor.process(directory);
	}

}
