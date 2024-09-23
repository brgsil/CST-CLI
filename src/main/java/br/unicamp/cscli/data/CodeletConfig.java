package br.unicamp.cscli.data;

import br.unicamp.cscli.util.TemplatesBundle;

import java.util.List;

import static br.unicamp.cscli.commands.CSTInit.TAB;

public class CodeletConfig {
    private String name;
    private String group;
    private List<String> in;
    private List<String> out;
    private List<String> broadcast;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<String> getIn() {
        return in;
    }

    public void setIn(List<String> in) {
        this.in = in;
    }

    public List<String> getOut() {
        return out;
    }

    public void setOut(List<String> out) {
        this.out = out;
    }

    public List<String> getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(List<String> broadcast) {
        this.broadcast = broadcast;
    }

    public String generateCode(String rootPackage) {
        String templateInstance = TemplatesBundle.getInstance().getTemplate("CodeletTemplate");

        templateInstance = templateInstance.replace("{{rootPackage}}", rootPackage);
        templateInstance = templateInstance.replace("{{type}}", this.getGroup().toLowerCase());
        templateInstance = templateInstance.replace("{{codeletName}}", this.getName());

        StringBuilder declarations = new StringBuilder();
        StringBuilder inMemoriesInit = new StringBuilder();
        StringBuilder outMemoriesInit = new StringBuilder();

        for (String input : this.getIn()) {
            declarations.append("\n")
                    .append(TAB)
                    .append("private Memory ")
                    .append(input)
                    .append(";");
            inMemoriesInit.append("\n")
                    .append(TAB)
                    .append(TAB)
                    .append(input)
                    .append(" = getInput(\"")
                    .append(input)
                    .append("\");");
        }
        for (String output : this.getOut()) {
            // If memory was not already declare in the inputs do it now
            if (!this.getIn().contains(output))
                declarations.append("\n")
                        .append(TAB)
                        .append("private Memory ")
                        .append(output)
                        .append(";");
            outMemoriesInit.append("\n")
                    .append(TAB)
                    .append(TAB)
                    .append(output)
                    .append(" = getOutput(\"")
                    .append(output)
                    .append("\");");
        }
        templateInstance = templateInstance.replace("{{memoriesDeclaration}}", declarations.toString());
        templateInstance = templateInstance.replace("{{inputAccess}}", inMemoriesInit.toString());
        templateInstance = templateInstance.replace("{{outputAccess}}", outMemoriesInit.toString());

        return templateInstance;
    }

    @Override
    public String toString() {
        return "CodeletConfig{" +
                "name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", in=" + in +
                ", out=" + out +
                ", broadcast=" + broadcast +
                '}';
    }
}
