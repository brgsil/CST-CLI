package br.unicamp.cst.cli.data;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.*;
import java.util.*;

import static br.unicamp.cst.cli.data.AgentConfig.getVarName;
import static br.unicamp.cst.cli.data.MemoryConfig.MEMORY_CONTAINER_TYPE;
import static br.unicamp.cst.cli.data.MemoryConfig.MEMORY_OBJECT_TYPE;
import static br.unicamp.cst.cli.util.Constants.*;

public class ConfigParser {

    public static AgentConfig parseProjectToConfig() {
        // Get root dir. Traverse up if necessary, until find folder src/
        File currDir = new File(System.getProperty("user.dir"));

        File srcFolder = new File(currDir.getAbsolutePath() + "/src");

        if (!srcFolder.exists()) return new AgentConfig();

        //Traverse folders and get packageName
        //Find AgentMind file
        srcFolder = new File(srcFolder, "main");
        if (!srcFolder.exists())
            return new AgentConfig();

        File agentMindFile = new File(srcFolder, "AgentMind.java");
        while (!agentMindFile.exists() && srcFolder.listFiles() != null) {
            srcFolder = srcFolder.listFiles()[0];
            agentMindFile = new File(srcFolder, "AgentMind.java");
        }

        if (!agentMindFile.exists())
            return new AgentConfig();

        ConstructorDeclaration agentConstructor = null;
        try {
            CompilationUnit cu = StaticJavaParser.parse(agentMindFile);
            VoidVisitor<List<ConstructorDeclaration>> findConstructorVisitor = new ConstructorCollector();
            List<ConstructorDeclaration> agentConstructors = new ArrayList<>();
            findConstructorVisitor.visit(cu, agentConstructors);
            if (agentConstructors.size() == 1){
                agentConstructor = agentConstructors.get(0);
            } else {
                System.out.println("Multiple Constructors detected for Agent Mind");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //List<String> memoryObjects = new ArrayList<>();
        //agentConstructor.accept(new VariableCollector(MEMORY_OBJECT_TYPE), memoryObjects);
        //List<String> memoryContainers = new ArrayList<>();
        //agentConstructor.accept(new VariableCollector(MEMORY_CONTAINER_TYPE), memoryContainers);
        AgentConfig testConfig = new AgentConfig();
        agentConstructor.accept(new AgentConfigCollector(), testConfig);
        System.out.println(testConfig);
        return testConfig;

        /*
        //Parse file to AgentConfig
        InputStream fileStream = null;
        String agentMindCode = "";
        try {
            fileStream = new FileInputStream(agentMindFile);
            agentMindCode = readFromInputStream(fileStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileStream != null)
                try {
                    fileStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }

        //Parse file
        AgentConfig agentConfig = new AgentConfig();
        List<MemoryConfig> memories = new ArrayList<>();
        List<CodeletConfig> codelets = new ArrayList<>();
        for (String line : agentMindCode.split("\n")) {
            line = line.strip();

            if (line.startsWith("package")) {
                agentConfig.setPackageName(getParam(line, "package"));
            } else if (line.startsWith("Memory")) {
                MemoryConfig memory = new MemoryConfig();
                memory.setName(getParam(line, "Memory"));
                memories.add(memory);
            } else if (line.contains("createMemoryObject")) {
                String memoryName = line.split(" ")[0];
                MemoryConfig memory = memories.stream().filter(e -> e.getName().equals(memoryName)).findFirst().orElse(null);
                if (memory == null) {
                    memory = new MemoryConfig();
                    memory.setName(memoryName);
                    memories.add(memory);
                }
                memory.setType(MEMORY_OBJECT_TYPE);
            } else if (line.contains("createMemoryContainer")) {
                String memoryName = line.split(" ")[0];
                MemoryConfig memory = memories.stream().filter(e -> e.getName().equals(memoryName)).findFirst().orElse(null);
                if (memory == null) {
                    memory = new MemoryConfig();
                    memory.setName(memoryName);
                    memories.add(memory);
                }
                memory.setType(MemoryConfig.MEMORY_CONTAINER_TYPE);
            } else if (line.startsWith("registerMemory")) {
                String memoryName = stringBetween(line, "(", ",");
                MemoryConfig memory = memories.stream().filter(e -> e.getName().equals(memoryName)).findFirst().orElse(null);
                if (memory == null) {
                    memory = new MemoryConfig();
                    memory.setName(memoryName);
                    memories.add(memory);
                }
                memory.setGroup(stringBetween(line, "\"", "\""));
            } else if (line.startsWith("Codelet")) {
                String codeletName = stringBetween(line.substring(line.lastIndexOf("new ")), " ", "(");
                CodeletConfig codelet = new CodeletConfig();
                codelet.setName(codeletName);
                codelets.add(codelet);
            } else if (line.contains(".addInput")) {
                CodeletConfig codelet = getCodeletConfig(line, codelets);
                String memoryName = stringBetween(line, "(", ")");
                codelet.addIn(memoryName);
            } else if (line.contains(".addOutput")) {
                CodeletConfig codelet = getCodeletConfig(line, codelets);
                String memoryName = stringBetween(line, "(", ")");
                codelet.addOut(memoryName);
            } else if (line.contains(".addBroadcast")) {
                CodeletConfig codelet = getCodeletConfig(line, codelets);
                String memoryName = stringBetween(line, "(", ")");
                codelet.addBroadcast(memoryName);
            } else if (line.startsWith("registerCodelet")) {
                String codeletName = stringBetween(line, "(", ",");
                CodeletConfig codelet = codelets.stream().filter(e -> getVarName(e.getName()).equals(codeletName)).findFirst().orElse(null);
                if (codelet == null) {
                    codelet = new CodeletConfig();
                    codelet.setName(codeletName);
                    codelets.add(codelet);
                }
                codelet.setGroup(stringBetween(line, "\"", "\""));
            }
        }
        agentConfig.setMemories(memories);
        agentConfig.setCodelets(codelets);

        return agentConfig;

         */
    }

    private static CodeletConfig getCodeletConfig(String line, List<CodeletConfig> codelets) {
        String codeletName = line.split("\\.")[0];
        CodeletConfig codelet = codelets.stream().filter(e -> getVarName(e.getName()).equals(codeletName)).findFirst().orElse(null);
        if (codelet == null) {
            codelet = new CodeletConfig();
            codelet.setName(codeletName);
            codelets.add(codelet);
        }
        return codelet;
    }

    private static String stringBetween(String line, String s, String s1) {
        return line.substring(line.indexOf(s) + 1, line.lastIndexOf(s1)).strip();
    }

    private static String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    private static String getParam(String original, String prefix) {
        return original.replace(prefix, "").replace(";", "").strip();
    }

    static class ConstructorCollector extends VoidVisitorAdapter<List<ConstructorDeclaration>>{
        @Override
        public void visit(ConstructorDeclaration cd, List<ConstructorDeclaration> constructor){
            super.visit(cd, constructor);
            constructor.add(cd);
        }
    }

    static class VariableCollector extends VoidVisitorAdapter<List<String>> {
        String type;
        VariableCollector(String type){
            super();
            this.type = type;
        }

        @Override
        public void visit(VariableDeclarator vd, List<String> collector){
            super.visit(vd, collector);
            if (vd.getTypeAsString().equals(this.type))
                collector.add(vd.getNameAsString());
        }
    }

    static class AgentConfigCollector extends VoidVisitorAdapter<AgentConfig>{
        Map<String, String> codeletVariables = new HashMap<>();

        @Override
        public void visit(VariableDeclarator vd, AgentConfig agentConfig){
            if (vd.getTypeAsString().equals(MEMORY_OBJECT_TYPE)) {
                MemoryConfig memoryConfig = agentConfig.findMemoryOrCreate(vd.getNameAsString());
                memoryConfig.setType(MEMORY_OBJECT_TYPE);
            } else if (vd.getTypeAsString().equals(MEMORY_CONTAINER_TYPE)) {
                MemoryConfig memoryConfig = agentConfig.findMemoryOrCreate(vd.getNameAsString());
                memoryConfig.setType(MEMORY_CONTAINER_TYPE);
            } else if (vd.getTypeAsString().equals(MEMORY_BASE_TYPE)) {
                agentConfig.findMemoryOrCreate(vd.getNameAsString());
            } else if (vd.getTypeAsString().equals("Codelet")) {
                if (vd.getInitializer().isPresent()) {
                    String codeletName = vd.getInitializer().get().asObjectCreationExpr().getType().asString();
                    CodeletConfig codeletConfig = agentConfig.findCodeletOrCreate(codeletName);
                }
            }
            super.visit(vd, agentConfig);
        }

        @Override
        public void visit(MethodCallExpr mc, AgentConfig agentConfig){
            if (mc.getNameAsString().equals(CREATE_MEMORY_OBJECT_FUNCTION)){
                NodeList<Expression> args = mc.getArguments();
                if (!args.isEmpty()){
                    AssignExpr parentExp = (AssignExpr) mc.getParentNode().get();
                    String memoryName = parentExp.getTarget().toString();
                    Optional<MemoryConfig> memoryConfig = agentConfig.findMemory(memoryName);
                    memoryConfig.ifPresent(config -> config.setType(MEMORY_OBJECT_TYPE));
                }
            } else if (mc.getNameAsString().equals(CREATE_MEMORY_CONTAINER_FUNCTION)){
                NodeList<Expression> args = mc.getArguments();
                if (!args.isEmpty()){
                    AssignExpr parentExp = (AssignExpr) mc.getParentNode().get();
                    String memoryName = parentExp.getTarget().toString();
                    Optional<MemoryConfig> memoryConfig = agentConfig.findMemory(memoryName);
                    memoryConfig.ifPresent(config -> config.setType(MEMORY_CONTAINER_TYPE));
                }
            } else if (mc.getNameAsString().equals(REGISTER_MEMORY_FUNCTION)){
                NodeList<Expression> args = mc.getArguments();
                if (!args.isEmpty()) {
                    String memoryName = args.get(0).toString();
                    String memoryGroup = args.get(1).toString().replaceAll("\"","");
                    Optional<MemoryConfig> memoryConfig = agentConfig.findMemory(memoryName);
                    memoryConfig.ifPresent(config -> config.setGroup(memoryGroup));
                }
            } else if (mc.getNameAsString().equals("registerCodelet")) {
                NodeList<Expression> args = mc.getArguments();
                if (!args.isEmpty()){
                    String codeletName = args.get(0).toString().replaceAll("\"","");
                    String codeletGroup = args.get(1).toString().replaceAll("\"","");
                    Optional<CodeletConfig> codeletConfig = agentConfig.findCodelet(codeletName);
                    codeletConfig.ifPresent(config -> config.setGroup(codeletGroup));
                }
            } else if (mc.getNameAsString().equals("addInput")) {
                addMemoryToCodelet(mc, agentConfig, 1);
            } else if (mc.getNameAsString().equals("addOutput")) {
                addMemoryToCodelet(mc, agentConfig, 2);
            } else if (mc.getNameAsString().equals("addBroadcast")) {
                addMemoryToCodelet(mc, agentConfig, 3);
            }
            super.visit(mc, agentConfig);
        }

        private static void addMemoryToCodelet(MethodCallExpr mc, AgentConfig agentConfig, int type) {
            if (mc.getScope().isPresent()){
                String codeletName = mc.getScope().get().toString();
                String memoryName = mc.getArguments().get(0).toString();
                Optional<CodeletConfig> codeletConfig = agentConfig.findCodelet(codeletName);
                Optional<MemoryConfig> memoryConfig = agentConfig.findMemory(memoryName);
                if (codeletConfig.isPresent() && memoryConfig.isPresent()){
                    if (type == 1)
                        codeletConfig.get().addIn(memoryConfig.get().getName());
                    if (type == 2)
                        codeletConfig.get().addOut(memoryConfig.get().getName());
                    if (type == 3)
                        codeletConfig.get().addBroadcast(memoryConfig.get().getName());
                }
            }
        }
    }
}