package br.com.crazycrowd.mp3m4aconverter.config;

import java.util.List;

import javax.inject.Named;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import br.com.crazycrowd.mp3m4aconverter.steps.DirectoryProcessor;
import br.com.crazycrowd.mp3m4aconverter.steps.DirectoryRecursiveListStrategy;
import br.com.crazycrowd.mp3m4aconverter.steps.Mp3PathProcessor;
import br.com.crazycrowd.mp3m4aconverter.steps.PathProcessor;
import br.com.crazycrowd.mp3m4aconverter.utils.FileHelper;

/**
 *
 * @author marcos.romero
 */
public class EnvironmentModule extends AbstractModule {

	@VisibleForTesting
	static final String SHELL_COMMANDS_WAIT_SECONDS_PROPERTY = "SHELL_COMMANDS_WAIT_SECONDS";

	@VisibleForTesting
	static final Long DEFAULT_WAIT_SECONDS = 30L;

	@Override
	protected void configure() {

	}

	@Provides
	@Singleton
	@Named("shellCommandsWaitSeconds")
	public Long shellCommandsWaitSeconds() {
		String shellCommandsWaitSeconds = System.getProperty(SHELL_COMMANDS_WAIT_SECONDS_PROPERTY);
		try {
			return Long.parseLong(shellCommandsWaitSeconds);
		} catch (NullPointerException | NumberFormatException ex) {
			return DEFAULT_WAIT_SECONDS;
		}
	}

	@Provides
	@Singleton
	public PathProcessor fileProcessor(FileHelper fileHelper, Mp3PathProcessor mp3PathProcessor) {
		return new DirectoryProcessor(fileHelper, mp3PathProcessor, new DirectoryRecursiveListStrategy());
	}

	@Provides
	@Singleton
	public AudioFileIO audioFileIO() {
		return AudioFileIO.getDefaultAudioFileIO();
	}

	@Provides
	@Singleton
	public List<FieldKey> supportedAudioTags() {
		return ImmutableList.of( //
				FieldKey.ALBUM_ARTIST, FieldKey.ARTIST, //
				FieldKey.TITLE, FieldKey.ALBUM, FieldKey.GENRE, //
				FieldKey.YEAR, FieldKey.TRACK, FieldKey.DISC_NO //
		);
	}

}
