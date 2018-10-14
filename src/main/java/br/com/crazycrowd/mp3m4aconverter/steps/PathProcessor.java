package br.com.crazycrowd.mp3m4aconverter.steps;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author marcos.romero
 */
public interface PathProcessor {

	void process(Path path) throws IOException, InterruptedException, TimeoutException;

}
