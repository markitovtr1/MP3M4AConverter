package br.com.crazycrowd.mp3m4aconverter.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class DirectoryNonRecursiveListStrategyTest {

	private static final Path TEST_DIR = Paths.get("src", "test", "resources", "audio");
	private static final List<Path> EXPECTED_FILES_TEST_DIR = ImmutableList.of( //
			TEST_DIR.resolve("dir1"), TEST_DIR.resolve("dir2") //
	);

	private DirectoryNonRecursiveListStrategy listStrategy;

	@Before
	public void setUp() {
		listStrategy = new DirectoryNonRecursiveListStrategy();
	}

	@Test
	public void test_whenDirectoryWithMultipleDirs_listContentOfAllDirsRecursively() throws Exception {

		List<Path> pathsOnTestDir = listStrategy.list(TEST_DIR).collect(Collectors.toList());

		assertTrue(pathsOnTestDir.containsAll(EXPECTED_FILES_TEST_DIR));
		assertEquals(EXPECTED_FILES_TEST_DIR.size(), pathsOnTestDir.size());
	}

}
