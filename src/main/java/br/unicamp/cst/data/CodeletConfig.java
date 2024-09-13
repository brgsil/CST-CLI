package br.unicamp.cst.data;

import java.util.List;

public class CodeletConfig {
    private String name;
    private String group;
    private List<String> in;
    private List<String> out;
    private List<String> broadcast;
    String codeTemplate = "package br.unicamp.cst.templates;\n" +
            "\n" +
            "public class {{codeletName}} extends Codelet {\n" +
            "    {{memoriesDeclaration}}\n" +
            "\n" +
            "    @Override\n" +
            "    public void accessMemoryObjects() {" +
            "        {{inputAccess}}" +
            "        {{outputAccess}}\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public void calculateActivation() {\n" +
            "\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public void proc() {\n" +
            "\n" +
            "    }\n" +
            "}";

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

    public String generateCode() {
        String templateInstance = new String(codeTemplate);
        templateInstance = templateInstance.replace("{{codeletName}}", this.getName());
        String declarations = "";
        String inMemoriesInit = "";
        String outMemoriesInit = "";
        for (String input : this.getIn()) {
            declarations += "\n    private Memory " + input + ";";
            inMemoriesInit += "\n        " + input + " = getInput(\"" + input + "\");";
        }
        for (String output : this.getOut()) {
            // If memory was not already declare in the inputs do it now
            if (!this.getIn().contains(output))
                declarations += "\n    private Memory " + output + ";";
            outMemoriesInit += "\n        " + output + " = getOutput(\"" + output + "\");";
        }
        templateInstance = templateInstance.replace("{{memoriesDeclaration}}", declarations);
        templateInstance = templateInstance.replace("{{inputAccess}}", inMemoriesInit);
        templateInstance = templateInstance.replace("{{outputAccess}}", outMemoriesInit);
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
