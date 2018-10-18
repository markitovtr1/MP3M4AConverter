package br.com.crazycrowd.mp3m4aconverter.steps;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DirectoryRecursiveListStrategyTest {

	private static final Path TEST_DIR = Paths.get("src", "test", "resources", "audio");
	private static final Path TEST_DIR_1 = TEST_DIR.resolve("dir1");
	private static final Path TEST_DIR_2 = TEST_DIR.resolve("dir2");
	private static final List<Path> EXPECTED_FILES_TEST_DIR = ImmutableList.of( //
			TEST_DIR, //
			TEST_DIR_1, TEST_DIR_1.resolve("01-01 Test01.mp3"), TEST_DIR_1.resolve("01-02 Test02.mp3"), //
			TEST_DIR_2, TEST_DIR_2.resolve("01-03 Test03.mp3"), TEST_DIR_2.resolve("01-04 Test04.mp3") //
	);

	private DirectoryRecursiveListStrategy listStrategy;

	@Before
	public void setUp() {
		listStrategy = new DirectoryRecursiveListStrategy();
	}

	@Test
	public void test_whenDirectoryWithMultipleDirs_listContentOfAllDirsRecursively() throws Exception {

		List<Path> pathsOnTestDir = listStrategy.list(TEST_DIR).collect(Collectors.toList());

		assertTrue(pathsOnTestDir.containsAll(EXPECTED_FILES_TEST_DIR));
		assertEquals(EXPECTED_FILES_TEST_DIR.size(), pathsOnTestDir.size());
	}

}
