/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.crazycrowd.mp3m4aconverter.steps;

import br.com.crazycrowd.mp3m4aconverter.utils.FileExtension;
import br.com.crazycrowd.mp3m4aconverter.utils.PathUtils;
import br.com.crazycrowd.mp3m4aconverter.utils.ShellCommands;
import br.com.crazycrowd.mp3m4aconverter.utils.TagWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Process an mp3 path to generate a m4a converted file with copied tags.
 *
 * @author marcos.romero
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class Mp3PathProcessor implements PathProcessor {

	private static final String ARTWORK_PATH = "folder.jpg";

	private final ShellCommands shellCommands;
	private final TagWriter tagWriter;

	@Override
	public void process(Path mp3AudioPath) throws IOException, InterruptedException {
		log.debug("Processing MP3 file {}", mp3AudioPath);
		if (!Files.exists(PathUtils.changeFileExtension(mp3AudioPath, FileExtension.M4A))) {
			try {
				Path wavPath = shellCommands.ffmpeg(mp3AudioPath);
				Path m4aPath = shellCommands.neroAacEnc(wavPath);
				tagWriter.copyTags(mp3AudioPath, m4aPath);

				Path artworkPath = mp3AudioPath.getParent().resolve(ARTWORK_PATH);
				if (Files.exists(artworkPath)) {
					tagWriter.addArtwork(m4aPath, artworkPath);
				} else {
					log.warn("No artwork found on path {}. Skipping addArtwork for file {}", artworkPath, mp3AudioPath);
				}
				log.debug("File {} processed. Output on {}", mp3AudioPath, m4aPath);
			} finally {
				Files.deleteIfExists(PathUtils.changeFileExtension(mp3AudioPath, FileExtension.WAVE));
			}
		} else {
			log.debug("File {} not processed. M4A file already exists.", mp3AudioPath);
		}
	}

}
