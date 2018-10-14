package br.com.crazycrowd.mp3m4aconverter.utils;

import java.io.IOException;
import java.nio.file.Path;

import javax.inject.Singleton;

import org.jaudiotagger.tag.datatype.Artwork;

/**
 * Class to avoid using Artwork directly for creating from an Artwork from a
 * File . This helps us avoid using PowerMock.
 * 
 * @author marcos.romero
 *
 */
@Singleton
public class ArtworkHelper {

	public Artwork createArtworkFromFile(Path path) throws IOException {
		return Artwork.createArtworkFromFile(path.toFile());
	}

}
