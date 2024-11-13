package commands;

import br.unicamp.cst.cli.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CSTInitTest {

    @TempDir
    Path tempDir;

    String dirName;
    int exitCode;
    final PrintStream originalOut = System.out;
    final PrintStream originalErr = System.err;
    final InputStream originalIn = System.in;
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final ByteArrayOutputStream err = new ByteArrayOutputStream();
    final InputStream in = new ByteArrayInputStream(new byte[0]);

    @BeforeEach
    public void setDir() {
        System.setProperty("user.dir", tempDir.toString());
        String[] dirs = tempDir.toString().split("/");
        dirName = dirs[dirs.length - 1];
    }

    @BeforeEach
    public void setUpStreams(){
        out.reset();
        err.reset();
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    @AfterEach
    public void restoreStreams(){
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(originalIn);
    }

    private void setInput(String data){
        System.setIn(new ByteArrayInputStream(data.getBytes())); // Inject mock Scanner
    }

    private void initBasicTestProject(){
        setInput("TestProject\ntestPackage\n");
        exitCode = new CommandLine(new Main()).execute("init");
    }

    @Test
    public void testInitAsksForRequiredParams(){
        initBasicTestProject();
        assertEquals("Enter project name (default: " + dirName + ") : Enter package name (default: testproject): ", out.toString());
        File[] files = tempDir.toFile().listFiles();
        assertNotNull(files);
        assertTrue(files.length > 0);
        assertEquals(0, exitCode);
    }

    @Test
    public void testDirectoryStructure(){
        initBasicTestProject();
        List<String> expectedPaths = Arrays.asList(
                "/src/main/java/testPackage/Main.java",
                "/src/main/java/testPackage/AgentMind.java",
                "/src/main/java/testPackage/codelets",
                "/src/main/resources",
                "/src/test/java",
                "/build.gradle",
                "/gradle/wrapper",
                "/gradlew",
                "/settings.gradle"
        );

        for (String expectedPath : expectedPaths) {
            File file = new File(tempDir + expectedPath);
            assertTrue(file.exists(), "File or directory does not exist: " + expectedPath);
        }
    }
}
