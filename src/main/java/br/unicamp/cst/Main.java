package br.unicamp.cst;

import br.unicamp.cst.data.AgentConfig;
import br.unicamp.cst.data.CodeletConfig;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@CommandLine.Command(name = "cst-init", mixinStandardHelpOptions = true, version = "cst-init 0.1")
public class Main implements Callable<Integer> {
    @Option(names= {"-f", "--file"}, description = "Config file for project creation")
    File config;

    @Override
    public Integer call() throws Exception {
        process(Files.lines(config.toPath()).collect(Collectors.joining("\n")));
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    private void process(String configInfo) throws IOException {
        Yaml yamlParser = new Yaml(new Constructor(AgentConfig.class, new LoaderOptions()));
        AgentConfig agentConfig = yamlParser.load(configInfo);

        InputStream codeletTemplateStream = this.getClass().getClassLoader().getResourceAsStream("templates/CodeletTemplate.java");
        StringBuilder sb = new StringBuilder();
        for (int ch; (ch = codeletTemplateStream.read()) != -1; ) {
            sb.append((char) ch);
        }
        String codeletTemplate = sb.toString();
        System.out.println(codeletTemplate);

        for (CodeletConfig codelet : agentConfig.getCodelets()){
            String templateInstance = new String(codeletTemplate);
            templateInstance = templateInstance.replace("{{codeletName}}", codelet.getName());
            String declarations = "";
            String ins = "";
            for (String input : codelet.getIn()){
                declarations += "private Memory " + input + ";\n";
                ins += input + " = getInput(\"" + input + "\");\n";
            }
            templateInstance = templateInstance.replace("{{inputDeclaration}}", declarations);
            templateInstance = templateInstance.replace("{{inputAccess}}", ins);
            System.out.println(templateInstance);
        }
    }
}