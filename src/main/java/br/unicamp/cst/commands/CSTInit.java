package br.unicamp.cst.commands;

import br.unicamp.cst.data.AgentConfig;
import br.unicamp.cst.data.CodeletConfig;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Command(name = "init", description = "Initialize a new CST project")
public class CSTInit implements Callable<Integer> {

    @Option(names= {"-f", "--file"}, description = "Config file for project creation")
    File config;

    @Override
    public Integer call() throws Exception {
        process(Files.lines(config.toPath()).collect(Collectors.joining("\n")));
        return 0;
    }

    private void process(String configInfo) throws IOException {
        File path = new File("./test/src/java/codelets");
        path.mkdirs();
        FileWriter writer = new FileWriter(path + "/SensoryCodelet.java");

        Yaml yamlParser = new Yaml(new Constructor(AgentConfig.class, new LoaderOptions()));
        AgentConfig agentConfig = yamlParser.load(configInfo);

        for (CodeletConfig codelet : agentConfig.getCodelets()){
            String code = codelet.generateCode();
            System.out.println(code);
            writer.write(code);
        }
        writer.close();
    }

}