module br.unicamp.cscli {
    requires info.picocli;
    requires org.yaml.snakeyaml;
    requires java.logging;

    exports br.unicamp.cscli;
    opens br.unicamp.cscli to info.picocli;
    opens br.unicamp.cscli.commands to info.picocli;
    opens br.unicamp.cscli.data to org.yaml.snakeyaml;
}