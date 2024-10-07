module br.unicamp.cst.cli {
    requires info.picocli;
    requires org.yaml.snakeyaml;
    requires java.logging;

    exports br.unicamp.cst.cli;
    opens br.unicamp.cst.cli to info.picocli;
    opens br.unicamp.cst.cli.commands to info.picocli;
    opens br.unicamp.cst.cli.data to org.yaml.snakeyaml;
}