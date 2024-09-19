package br.unicamp.cscli.commands;

import br.unicamp.cscli.data.AgentConfig;
import br.unicamp.cscli.data.CodeletConfig;

import br.unicamp.cscli.util.TemplatesBundle;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Command(name = "init", description = "Initialize a new CST project")
public class CSTInit implements Callable<Integer> {
    public static String TAB = "    ";
    public static String PARSER_ERROR = "Error parsing config file";

    @Option(names = {"--project-name"}, description = "Name of the project")
    String projectName;

    @Option(names = {"--package"}, description = "Package name for the project")
    String packageName;

    @Option(names= {"-f", "--file"}, description = "Config file for project creation")
    File config;

    @Override
    public Integer call() throws Exception {
        process(Files.lines(config.toPath()).collect(Collectors.joining("\n")));
        getRequiredParams();
        createDirs();
        if (config != null)
            process(Files.lines(config.toPath()).collect(Collectors.joining("\n")));
        return 0;
    }

    private void getRequiredParams() {
        if (projectName == null){
            String osName = System.getProperty("os.name").toLowerCase();
            String[] splitPath = new String[0];
            if (osName.contains("win"))
                splitPath = System.getProperty("user.dir").split("\\");
            if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix"))
                splitPath = System.getProperty("user.dir").split("/");

            projectName = splitPath[splitPath.length - 1];
            System.out.print("Enter project name (default: " + projectName + ") : ");
            Scanner input = new Scanner(System.in);
            String inputName = input.nextLine();
            if (!inputName.isBlank())
                projectName = inputName;
            System.out.println(projectName);
        }

        if (packageName == null) {
            packageName = projectName.toLowerCase();
            System.out.print("Enter package name (default: " + packageName + "): ");
            Scanner input = new Scanner(System.in);
            String inputName = input.nextLine();
            if (!inputName.isBlank())
                packageName = inputName;
            System.out.println(packageName);
        }
    }

    private void createDirs() {
        // Main java package dir
        File path = new File("./src/main/java/"+ packageName.replace(".", "/")+"/codelets");
        path.mkdirs();
        // Resources dir
        path = new File("./src/main/resources");
        path.mkdirs();
        // Test package dir
        path = new File("./src/test/java");
        path.mkdirs();
    }


    private void process(String configInfo) throws IOException {
        Yaml yamlParser = new Yaml(new Constructor(AgentConfig.class, new LoaderOptions()));
        AgentConfig agentConfig = yamlParser.load(configInfo);

        for (CodeletConfig codelet : agentConfig.getCodelets()){
            File path = new File("./src/main/java/"+ packageName.replace(".", "/")+"/codelets/" + codelet.getGroup().toLowerCase());
            path.mkdirs();
            String codeletCode = codelet.generateCode();
            FileWriter writer = new FileWriter(path + "/" + codelet.getName() + ".java");
            writer.write(codeletCode);
            writer.close();
        }

        File path = new File("./src/main/java/" + packageName.replace(".", "/"));
        path.mkdirs();
        String agentMindCode = agentConfig.generateCode();
        FileWriter writer = new FileWriter(path + "/AgentMind.java");
        writer.write(agentMindCode);
        writer.close();
    }

}
