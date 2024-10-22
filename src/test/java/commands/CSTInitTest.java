package commands;

import br.unicamp.cst.cli.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSTInitTest {

    @TempDir
    Path tempDir;

    String dirName;

    @BeforeEach
    public void setDir(){
        System.setProperty("user.dir", tempDir.toString());
        String[] dirs = tempDir.toString().split("/");
        dirName = dirs[dirs.length - 1];
    }

    @Test
    public void testRequiredParams(){
        Main main = new Main();
        CommandLine cmd = new CommandLine(main);

        StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        cmd.execute("init");
        assertEquals("Enter project name (default: " + dirName + ") : ", sw.toString());
    }

    @Test
    public void testDirectoryStructure(){

    }
}
