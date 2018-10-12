/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.crazycrowd.mp3m4aconverter.steps;

import br.com.crazycrowd.mp3m4aconverter.utils.FileExtension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author marcos.romero
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractDirectoryProcessor implements PathProcessor {

	private final Mp3PathProcessor mp3PathProcessor;

	@Override
	public void process(Path directory) throws IOException, InterruptedException {
		if (!Files.isDirectory(directory)) {
			throw new IllegalArgumentException("Path is not a directory: " + directory);
		}
		log.debug("Starting to process files on dir {}", directory);
		getDirectoryStream(directory) //
				.filter(p -> p.getFileName().toString().endsWith(FileExtension.MP3.getExtension()))
				.forEach(this::processMp3AudioPath);
	}

	protected abstract Stream<Path> getDirectoryStream(Path directory) throws IOException;

	private void processMp3AudioPath(Path mp3AudioPath) {
		try {
			mp3PathProcessor.process(mp3AudioPath);
		} catch (IOException | InterruptedException e) {
			log.error("Error while processing file {}", mp3AudioPath, e);
		}
	}

}