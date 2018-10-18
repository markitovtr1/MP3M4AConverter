package br.com.crazycrowd.mp3m4aconverter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import br.com.crazycrowd.mp3m4aconverter.utils.FileExtension;
import br.com.crazycrowd.mp3m4aconverter.utils.FileHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationIntegrationTest {

	private static final Path TEST_DIRECTORY = Paths.get("src", "test", "resources", "audio");

	private FileHelper fileHelper = new FileHelper();

	@After
	public void tearDown() throws IOException {
		Files.walk(TEST_DIRECTORY) //
				.filter(p -> p.getFileName().toString().endsWith(FileExtension.M4A.getExtension())) //
				.forEach(p -> {
					try {
						fileHelper.deleteIfExists(p);
					} catch (Exception e) {
						log.warn("Error while cleaning up path {}", p);
					}
				});
	}

	@Test
	public void test_applicationSanity() throws Exception {
		String[] args = { TEST_DIRECTORY.toString() };
		StartPoint.main(args);

		List<Path> convertedFilePaths = Files.walk(TEST_DIRECTORY) //
				.filter(p -> p.getFileName().toString().endsWith(FileExtension.M4A.getExtension())) //
				.collect(Collectors.toList());

		List<Path> expectedConvertedFilePaths = ImmutableList.of(
				TEST_DIRECTORY.resolve("dir1").resolve("01-01 Test01.m4a"),
				TEST_DIRECTORY.resolve("dir1").resolve("01-02 Test02.m4a"),
				TEST_DIRECTORY.resolve("dir2").resolve("01-03 Test03.m4a"),
				TEST_DIRECTORY.resolve("dir2").resolve("01-04 Test04.m4a") //
		);

		assertEquals(expectedConvertedFilePaths.size(), convertedFilePaths.size());
		assertTrue(convertedFilePaths.containsAll(expectedConvertedFilePaths));
	}

}
