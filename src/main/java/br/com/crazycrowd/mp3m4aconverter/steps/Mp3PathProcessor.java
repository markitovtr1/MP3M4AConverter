package br.com.crazycrowd.mp3m4aconverter.steps;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.crazycrowd.mp3m4aconverter.shell.CommandExecutor;
import br.com.crazycrowd.mp3m4aconverter.shell.Ffmpeg;
import br.com.crazycrowd.mp3m4aconverter.shell.NeroAacEnc;
import br.com.crazycrowd.mp3m4aconverter.utils.FileExtension;
import br.com.crazycrowd.mp3m4aconverter.utils.FileHelper;
import br.com.crazycrowd.mp3m4aconverter.utils.PathUtils;
import br.com.crazycrowd.mp3m4aconverter.utils.TagWriter;
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

	private final FileHelper fileHelper;
	private final CommandExecutor commandExecutor;
	private final TagWriter tagWriter;

	@Override
	public void process(Path mp3AudioPath) throws IOException, InterruptedException, TimeoutException {
		log.debug("Processing MP3 file {}", mp3AudioPath);
		if (!fileHelper.exists(PathUtils.changeFileExtension(mp3AudioPath, FileExtension.M4A))) {
			try {
				Path tmpWavPath = PathUtils.changeFileExtension(mp3AudioPath, FileExtension.WAVE);
				Path m4aPath = PathUtils.changeFileExtension(tmpWavPath, FileExtension.M4A);
				Ffmpeg ffmpeg = new Ffmpeg(mp3AudioPath, tmpWavPath, FileExtension.WAVE.getExtension());
				NeroAacEnc neroAacEnc = new NeroAacEnc(tmpWavPath, m4aPath, true);
				commandExecutor.execute(ffmpeg, neroAacEnc);
				tagWriter.copyTags(mp3AudioPath, m4aPath);

				Path artworkPath = mp3AudioPath.getParent().resolve(ARTWORK_PATH);
				if (fileHelper.exists(artworkPath)) {
					tagWriter.addArtwork(m4aPath, artworkPath);
				} else {
					log.warn("No artwork found on path {}. Skipping addArtwork for file {}", artworkPath, mp3AudioPath);
				}
				log.debug("File {} processed. Output on {}", mp3AudioPath, m4aPath);
			} finally {
				fileHelper.deleteIfExists(PathUtils.changeFileExtension(mp3AudioPath, FileExtension.WAVE));
			}
		} else {
			log.debug("File {} not processed. M4A file already exists.", mp3AudioPath);
		}
	}

}
