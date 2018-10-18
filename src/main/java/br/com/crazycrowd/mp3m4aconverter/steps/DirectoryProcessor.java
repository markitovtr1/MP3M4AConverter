package br.com.crazycrowd.mp3m4aconverter.steps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeoutException;

import br.com.crazycrowd.mp3m4aconverter.utils.FileExtension;
import br.com.crazycrowd.mp3m4aconverter.utils.FileHelper;
import br.com.crazycrowd.mp3m4aconverter.utils.PathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author marcos.romero
 */
@Slf4j
@RequiredArgsConstructor
public class DirectoryProcessor implements PathProcessor {

	private final FileHelper fileHelper;
	private final Mp3PathProcessor mp3PathProcessor;
	private final DirectoryListStrategy directoryListStrategy;

	@Override
	public void process(Path directory) throws IOException, InterruptedException {
		if (!Files.isDirectory(directory)) {
			throw new IllegalArgumentException("Path is not a directory: " + directory);
		}
		log.debug("Starting to process files on dir {}", directory);
		directoryListStrategy.list(directory) //
				.filter(p -> p.getFileName().toString().endsWith(FileExtension.MP3.getExtension()))
				.forEach(this::processMp3AudioPath);
	}

	private void processMp3AudioPath(Path mp3AudioPath) {
		try {
			mp3PathProcessor.process(mp3AudioPath);
		} catch (IOException | InterruptedException | TimeoutException e) {
			log.error("Error while processing file {}", mp3AudioPath, e);
			try {
				fileHelper.deleteIfExists(PathUtils.changeFileExtension(mp3AudioPath, FileExtension.M4A));
			} catch (IOException ioex) {
				log.warn("Could not clean up M4A file for MP3 {}. Maybe it was not generated.", mp3AudioPath, e);
			}
		}
	}

}
