module cscli {
    requires info.picocli;
    requires org.yaml.snakeyaml;
    requires java.logging;

    exports br.unicamp.cst;
    opens br.unicamp.cst to info.picocli;
    opens br.unicamp.cst.commands to info.picocli;
    opens br.unicamp.cst.data to org.yaml.snakeyaml;
}