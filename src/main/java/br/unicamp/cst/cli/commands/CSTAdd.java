package br.unicamp.cst.cli.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

@Command(name = "add", description = "Adds a new codelet to the project structure")
public class CSTAdd implements Callable<Integer> {
    private static final String CODELET = "1";
    private static final String MEMORY_OBJECT = "2";
    private static final String MEMORY_CONTAINER = "3";
    private static final List<String> VALID_OPTIONS = Arrays.asList(CODELET, MEMORY_OBJECT, MEMORY_CONTAINER);

    @Override
    public Integer call() throws Exception {
        String options = Ansi.AUTO.string("Select element to add\n"
                + "   (1) Codelet\n"
                + "   (2) Memory Object\n"
                + "   (3) Memory Container\n"
                + " @|bold Select an option (default 1) [1..3]:|@ ");
        System.out.println(options);
        Scanner input = new Scanner(System.in);
        String inputOption = input.nextLine();
        String selected = "1";
        if (!inputOption.isBlank() && VALID_OPTIONS.contains(inputOption))
            selected = inputOption;
        switch (selected){
            case CODELET:
                String askName = Ansi.AUTO.string("Codelet Name: ");
                System.out.println(askName);
                String codeletName = input.nextLine();
                while (codeletName.isBlank())
                    System.out.println(Ansi.AUTO.string("@|red Codelet name cannot be empty|@"));
                    codeletName = input.nextLine();
                System.out.println("Codelet inputs (comma separated): ");
                String codeletInputs = input.nextLine();
                System.out.println("Codelet outputs (comma separated): ");
                String codeletOutputs = input.nextLine();
                System.out.println("Codelet broadcast outputs (comma separated): ");
                String codeletBroadcasts = input.nextLine();

                break;
            case MEMORY_OBJECT:
                break;
            case MEMORY_CONTAINER:
                break;

        }
        return 0;
    }
}
