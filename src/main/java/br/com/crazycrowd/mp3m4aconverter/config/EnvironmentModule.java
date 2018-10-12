/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.crazycrowd.mp3m4aconverter.config;

import java.util.List;

import javax.inject.Named;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import br.com.crazycrowd.mp3m4aconverter.steps.Mp3PathProcessor;
import br.com.crazycrowd.mp3m4aconverter.steps.PathProcessor;
import br.com.crazycrowd.mp3m4aconverter.steps.RecursiveDirectoryProcessor;

/**
 *
 * @author marcos.romero
 */
public class EnvironmentModule extends AbstractModule {

	@Override
	protected void configure() {

	}

	@Provides
	@Singleton
	@Named("shellCommandsWaitSeconds")
	public Long shellCommandsWaitSeconds() {
		String shellCommandsWaitSeconds = System.getenv("SHELL_COMMANDS_WAIT_SECONDS");
		try {
			return Long.parseLong(shellCommandsWaitSeconds);
		} catch (NullPointerException | NumberFormatException ex) {
			return 30L;
		}
	}

	@Provides
	@Singleton
	public PathProcessor fileProcessor(Mp3PathProcessor mp3PathProcessor) {
		return new RecursiveDirectoryProcessor(mp3PathProcessor);
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
