package br.unicamp.cscli;

import br.unicamp.cscli.commands.CSTInit;
import picocli.CommandLine.Command;
import picocli.CommandLine;

@Command(name = "cscli",
        synopsisSubcommandLabel = "COMMAND",
        mixinStandardHelpOptions = true,
        version = "cscli 0.1",
        subcommands = {
            CSTInit.class,
        })
public class Main implements Runnable {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        throw new CommandLine.ParameterException(spec.commandLine(), "Missing required subcommand");
    }
}