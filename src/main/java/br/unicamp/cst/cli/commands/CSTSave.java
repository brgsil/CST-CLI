package br.unicamp.cst.cli.commands;

import br.unicamp.cst.cli.data.AgentConfig;
import br.unicamp.cst.cli.data.ConfigParser;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.Callable;

@Command(name = "save", description = "Save current project as YAML template")
public class CSTSave implements Callable<Integer> {

    @Option(names = {"--out, -o"}, description = "Output file to write YAML template")
    File output;

    @Override
    public Integer call() throws Exception{
        AgentConfig agentConfig = ConfigParser.parseProjectToConfig();

        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setPrettyFlow(true);
        dumperOptions.setIndentWithIndicator(true);
        Yaml yaml = new Yaml(dumperOptions);
        String yamlDump = yaml.dump(agentConfig);
        yamlDump = yamlDump.replaceAll("\\[\n    ", "[").replaceAll("\n  ]", "]").replaceAll("\\{\n  ", "").replaceAll("}\n", "").replaceAll("-", "  -");

        if (output == null) {
            System.out.println(yamlDump);
        } else {
            FileWriter writer = new FileWriter(output);
            writer.write(yamlDump);
            writer.close();
        }

        return 0;
    }
}
