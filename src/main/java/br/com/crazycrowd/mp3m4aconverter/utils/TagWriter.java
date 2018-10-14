/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.crazycrowd.mp3m4aconverter.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Writes tag information to audio files path. Right now, it is using
 * jaudiotagger API to do this.
 *
 * @author marcos.romero
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TagWriter {

	private final List<FieldKey> supportedTags;
	private final AudioFileIO audioFileIO;
	private final ArtworkHelper artworkHelper;

	/**
	 * Adds artwork to an audio
	 *
	 * @param audioPath   Path to the audio file
	 * @param artworkPath Path to the artwork
	 * @throws IOException In case there is any error reading or writing to the
	 *                     file.
	 */
	public void addArtwork(Path audioPath, Path artworkPath) throws IOException {
		try {
			File file = audioPath.toFile();
			AudioFile audioFile = audioFileIO.readFile(file);
			Tag tag = audioFile.getTag();
			tag.setField(artworkHelper.createArtworkFromFile(artworkPath));
			audioFileIO.writeFile(audioFile);
		} catch (TagException | ReadOnlyFileException | CannotReadException | CannotWriteException
				| InvalidAudioFrameException e) {
			throw new IOException(e);
		}
	}

	/**
	 * Copies tags between files.
	 *
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	public void copyTags(Path source, Path dest) throws IOException {
		try {
			AudioFile sourceAudioFile = audioFileIO.readFile(source.toFile());
			AudioFile destAudioFile = audioFileIO.readFile(dest.toFile());
			Tag sourceTag = sourceAudioFile.getTag();
			Tag destTag = destAudioFile.getTag();
			supportedTags.forEach(fieldKey -> copyFieldBetweenTags(sourceTag, destTag, fieldKey));
			audioFileIO.writeFile(destAudioFile);
		} catch (TagException | ReadOnlyFileException | CannotReadException | CannotWriteException
				| InvalidAudioFrameException e) {
			throw new IOException(e);
		}
	}

	/**
	 * Copy a field value between tags.
	 *
	 * @param sourceTag
	 * @param destTag
	 * @param fieldKey
	 */
	private void copyFieldBetweenTags(Tag sourceTag, Tag destTag, FieldKey fieldKey) {
		try {
			destTag.setField(fieldKey, sourceTag.getFirst(fieldKey));
		} catch (KeyNotFoundException | FieldDataInvalidException e) {
			log.error("Error while copying tag field {} between source {} and dest {} tags", fieldKey, sourceTag,
					destTag, e);
		}
	}

}
