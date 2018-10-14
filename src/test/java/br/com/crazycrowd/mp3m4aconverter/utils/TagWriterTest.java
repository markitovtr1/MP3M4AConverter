package br.com.crazycrowd.mp3m4aconverter.utils;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;

@RunWith(MockitoJUnitRunner.class)
public class TagWriterTest {

	private static final List<FieldKey> TEST_TAGS = ImmutableList.of(FieldKey.TITLE, FieldKey.ARTIST);
	private static final Path TEST_ARTWORK_PATH = Paths.get("artwork.file");
	private static final Path TEST_SOURCE_PATH = Paths.get("source.file");
	private static final Path TEST_DEST_PATH = Paths.get("dest.file");

	@Mock
	private AudioFileIO audioFileIOMock;

	@Mock
	private Artwork artworkMock;

	@Mock
	private ArtworkHelper artworkHelper;

	private TagWriter tagWriter;

	@Before
	public void setUp() throws Exception {
		tagWriter = new TagWriter(TEST_TAGS, audioFileIOMock, artworkHelper);
	}

	@Test
	public void test_whenFilesWithoutProblems_AllTagsAreCopied() throws Exception {
		AudioFile destAudioFile = mockAudioFile(TEST_DEST_PATH);
		Tag destAudioTag = destAudioFile.getTag();
		Tag sourceAudioTag = mockAudioFile(TEST_SOURCE_PATH).getTag();
		doNothing().when(audioFileIOMock).writeFile(destAudioFile);

		for (FieldKey fieldKey : TEST_TAGS) {
			when(sourceAudioTag.getFirst(fieldKey)).thenReturn(fieldKey.toString());
			doNothing().when(destAudioTag).setField(fieldKey, fieldKey.toString());
		}

		tagWriter.copyTags(TEST_SOURCE_PATH, TEST_DEST_PATH);

		verify(audioFileIOMock, times(1)).writeFile(destAudioFile);
		for (FieldKey fieldKey : TEST_TAGS) {
			verify(sourceAudioTag, times(1)).getFirst(fieldKey);
			verify(destAudioTag, times(1)).setField(fieldKey, fieldKey.toString());
		}
	}

	@Test
	public void test_whenFilesWithoutProblems_ArtworkIsWritten() throws Exception {
		AudioFile audioFile = mockAudioFile(TEST_SOURCE_PATH);
		Tag tag = audioFile.getTag();
		when(artworkHelper.createArtworkFromFile(TEST_ARTWORK_PATH)).thenReturn(artworkMock);
		doNothing().when(tag).setField(artworkMock);
		doNothing().when(audioFileIOMock).writeFile(audioFile);

		tagWriter.addArtwork(TEST_SOURCE_PATH, TEST_ARTWORK_PATH);

		verify(tag, times(1)).setField(artworkMock);
		verify(audioFileIOMock, times(1)).writeFile(audioFile);
	}

	private AudioFile mockAudioFile(Path path) throws Exception {
		AudioFile audioFile = mock(AudioFile.class);
		Tag audioTag = mock(Tag.class);
		when(audioFileIOMock.readFile(path.toFile())).thenReturn(audioFile);
		when(audioFile.getTag()).thenReturn(audioTag);
		return audioFile;
	}

}
