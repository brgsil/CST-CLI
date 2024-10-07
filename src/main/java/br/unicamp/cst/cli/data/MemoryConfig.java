package br.unicamp.cst.cli.data;

import static br.unicamp.cst.cli.commands.CSTInit.PARSER_ERROR;

public class MemoryConfig {
    public static final String MEMORY_OBJECT_TYPE = "object";
    public static final String MEMORY_CONTAINER_TYPE = "container";
    private String name;
    private String type;
    private String content;
    private String group;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type != null) {
            if (MEMORY_OBJECT_TYPE.equals(type.toLowerCase()) || MEMORY_CONTAINER_TYPE.equals(type.toLowerCase())) {
                this.type = type;
            } else {
                System.out.println(PARSER_ERROR);
                System.out.println("MEMORY[" + this.getName() + "]: Memory type should be 'object' or 'container'");
                System.exit(1);
            }
        } else {
            System.out.println(PARSER_ERROR);
            System.out.println("MEMORY[" + this.getName() + "]: Memory type must be specified!");
            System.exit(1);
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "MemoryConfig{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
