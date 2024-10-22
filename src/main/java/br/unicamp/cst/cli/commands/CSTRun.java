package br.unicamp.cst.cli.commands;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "run", description = "Executes current project")
public class CSTRun implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        return 0;
    }
}
