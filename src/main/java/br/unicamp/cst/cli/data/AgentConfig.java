package br.unicamp.cst.cli.data;

import br.unicamp.cst.cli.util.TemplatesBundle;

import java.util.*;

import static br.unicamp.cst.cli.commands.CSTInit.TAB;
import static br.unicamp.cst.cli.data.MemoryConfig.CONTAINER_TYPE;
import static br.unicamp.cst.cli.data.MemoryConfig.OBJECT_TYPE;

public class AgentConfig {

    private String projectName;
    private String packageName;

    private List<CodeletConfig> codelets = new ArrayList<>();
    private List<MemoryConfig> memories = new ArrayList<>();

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<CodeletConfig> getCodelets() {
        return codelets;
    }

    public void setCodelets(List<CodeletConfig> codelets) {
        this.codelets = codelets;
    }

    public List<MemoryConfig> getMemories() {
        return memories;
    }

    public void setMemories(List<MemoryConfig> memories) {
        this.memories = memories;
    }

    public String generateCode() {
        String templateInstance = TemplatesBundle.getInstance().getTemplate("AgentMindTemplate");

        // Codelets class import
        StringBuilder codeletsImport = new StringBuilder();
        for (CodeletConfig codelet : this.getCodelets()) {
           codeletsImport.append("\nimport ")
                        .append(packageName)
                        .append(".codelets.")
                        .append(codelet.getGroup().toLowerCase())
                        .append(".")
                        .append(codelet.getName())
                        .append(";");
        }

        Iterator<String> uniqueCodeletsGroups = getCodelets().stream()
                .map(CodeletConfig::getGroup).distinct().iterator();
        StringBuilder codeletGroups = new StringBuilder();
        while (uniqueCodeletsGroups.hasNext()) {
            codeletGroups.append("\n")
                    .append(TAB)
                    .append(TAB)
                    .append("createCodeletGroup(\"")
                    .append(uniqueCodeletsGroups.next())
                    .append("\");");
        }

        Iterator<String> uniqueMemoryGroups = getMemories().stream()
                .map(MemoryConfig::getGroup).distinct().iterator();
        StringBuilder memoryGroups = new StringBuilder();
        while (uniqueMemoryGroups.hasNext()) {
            memoryGroups.append("\n")
                    .append(TAB)
                    .append(TAB)
                    .append("createMemoryGroup(\"")
                    .append(uniqueMemoryGroups.next())
                    .append("\");");
        }

        StringBuilder memoryDeclarations = new StringBuilder();
        StringBuilder memoryInit = new StringBuilder();
        for (MemoryConfig memory : this.getMemories()) {
            // Declare memory variable
            memoryDeclarations.append("\n")
                    .append(TAB)
                    .append(TAB)
                    .append("Memory ")
                    .append(getVarName(memory.getName()))
                    .append(";");
            // Initialize memory object
            memoryInit.append("\n")
                    .append(TAB)
                    .append(TAB)
                    .append(getVarName(memory.getName()))
                    .append(" = ");
            if (memory.getType().equals(OBJECT_TYPE))
                if (memory.getContent() == null) {
                    memoryInit.append("createMemoryObject(\"")
                            .append(memory.getName())
                            .append("\");");
                } else {
                    //TODO Initialize memory object with given content type
                }
            else if (memory.getType().equals(CONTAINER_TYPE)) {
                memoryInit.append("createMemoryContainer(\"")
                        .append(memory.getName())
                        .append("\");");
            }
            if (memory.getGroup() != null) {
                memoryInit.append("\n")
                        .append(TAB)
                        .append(TAB)
                        .append("registerMemory(")
                        .append(getVarName(memory.getName()))
                        .append(", \"")
                        .append(memory.getGroup())
                        .append("\");");
            }
        }

        // Codelets initialization
        StringBuilder codeletInit = new StringBuilder();
        for (CodeletConfig codelet : this.getCodelets()) {
            String codeletVarName = getVarName(codelet.getName());
            codeletInit.append("\n\n")
                    .append(TAB)
                    .append(TAB)
                    .append("Codelet ")
                    .append(codeletVarName)
                    .append(" = new ")
                    .append(codelet.getName())
                    .append("();");
            for (String inMemory : codelet.getIn()) {
                appendMemoryConnection(codeletInit, "Input", inMemory, codelet.getName());
            }
            for (String outMemory : codelet.getOut()) {
                appendMemoryConnection(codeletInit, "Output", outMemory, codelet.getName());
            }
            for (String broadcastMemory : codelet.getBroadcast()) {
                appendMemoryConnection(codeletInit, "Broadcast", broadcastMemory, codelet.getName());
            }
            codeletInit.append("\n")
                    .append(TAB)
                    .append(TAB)
                    .append("insertCodelet(")
                    .append(codeletVarName)
                    .append(");");
            if (codelet.getGroup() != null) {
                codeletInit.append("\n")
                        .append(TAB)
                        .append(TAB)
                        .append("registerCodelet(")
                        .append(codeletVarName)
                        .append(", \"")
                        .append(codelet.getGroup())
                        .append("\");");
            }
        }

        templateInstance = templateInstance.replace("{{rootPackage}}", packageName == null ? "" : packageName);
        templateInstance = templateInstance.replace("{{codeletsImport}}", codeletsImport.toString());
        templateInstance = templateInstance.replace("{{codeletGroups}}", codeletGroups.toString());
        templateInstance = templateInstance.replace("{{memoryGroups}}", memoryGroups.toString());
        templateInstance = templateInstance.replace("{{memoryObjects}}", memoryDeclarations.toString());
        templateInstance = templateInstance.replace("{{memoryInit}}", memoryInit.toString());
        templateInstance = templateInstance.replace("{{codeletInit}}", codeletInit.toString());

        return templateInstance;
    }

    private void appendMemoryConnection(StringBuilder codeletInit, String type, String memoryName, String codeletName) {
        codeletInit.append("\n")
                .append(TAB)
                .append(TAB)
                .append(getVarName(codeletName))
                .append(".add")
                .append(type)
                .append("(")
                .append(getVarName(memoryName))
                .append(");");
    }

    public static String getVarName(String name) {
        char[] split = name.toCharArray();
        split[0] = Character.toLowerCase(split[0]);
        return new String(split);
    }

    @Override
    public String toString() {
        return "AgentConfig{" +
                "projectName='" + projectName + '\'' +
                ", codelets=" + codelets +
                ", memories=" + memories +
                '}';
    }

    public String toYaml(){
        StringBuilder sb = new StringBuilder();
        if (projectName != null)
            sb.append("projectName: ").append(projectName).append("\n");
        if (packageName != null)
            sb.append("packageName: ").append(packageName).append("\n");

        sb.append("\ncodelets:\n");
        for (CodeletConfig codelet : codelets){
            sb.append("  ")
                .append("- name: ")
                .append(codelet.getName())
                .append("\n");
            sb.append("    ")
                .append("group: ")
                .append(codelet.getGroup())
                .append("\n");
            sb.append("    ")
                .append("in: ")
                .append(Arrays.toString(codelet.getIn().toArray()))
                .append("\n");
            sb.append("    ")
                .append("out: ")
                .append(Arrays.toString(codelet.getOut().toArray()))
                .append("\n");
            sb.append("    ")
                .append("broadcast: ")
                .append(Arrays.toString(codelet.getBroadcast().toArray()))
                .append("\n");
        }

        sb.append("\nmemories:\n");
        for (MemoryConfig memory : memories){
            sb.append("  ")
                .append("- name: ")
                .append(memory.getName())
                .append("\n");
            sb.append("    ")
                    .append("type: ")
                    .append(memory.getType())
                    .append("\n");
            sb.append("    ")
                    .append("content: ")
                    .append(memory.getContent())
                    .append("\n");
            sb.append("    ")
                    .append("group: ")
                    .append(memory.getGroup())
                    .append("\n");
        }

        return sb.toString();
    }

    public MemoryConfig findMemoryOrCreate(String nameAsString) {
        Optional<MemoryConfig> memoryConfig = this.memories.stream().filter(m->m.getName().equals(nameAsString)).findFirst();
        if (memoryConfig.isPresent()) {
            return memoryConfig.get();
        } else {
            MemoryConfig newMemory = new MemoryConfig(nameAsString);
            this.memories.add(newMemory);
            return newMemory;
        }
    }

    public Optional<MemoryConfig> findMemory(String memoryName) {
        return this.memories.stream().filter(m->m.getName().equals(memoryName)).findFirst();
    }

    public CodeletConfig findCodeletOrCreate(String codeletName) {
        Optional<CodeletConfig> codeletConfig = this.codelets.stream().filter(m->m.getName().equals(codeletName)).findFirst();
        if (codeletConfig.isPresent()) {
            return codeletConfig.get();
        } else {
            CodeletConfig newCodelet = new CodeletConfig(codeletName);
            this.codelets.add(newCodelet);
            return newCodelet;
        }
    }

    public Optional<CodeletConfig> findCodelet(String codeletName) {
        return this.codelets.stream().filter(c-> c.getName().equalsIgnoreCase(codeletName)).findFirst();
    }
}
