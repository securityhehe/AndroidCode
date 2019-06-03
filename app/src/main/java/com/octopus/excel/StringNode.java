package com.octopus.excel;

/**
 * node 节点。
 */
public class StringNode {
    @Override
    public String toString() {
        return "StringNode{" +
                "xmlFiileName='" + xmlFiileName + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String execlName;
    public String xmlFiileName;
    public String name;
    public String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
