package com.demo.optimal;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 程祥 on 16/1/4.
 * Function：记录图的节点
 */
public class Node {
    //节点ID
    private String nodeId;
    //节点名称
    private String nodeName;
    //与当前节点相关的节点
    private Map<Node,Integer> relatedNode =  new HashMap<Node, Integer>();

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Map<Node, Integer> getRelatedNode() {
        return relatedNode;
    }

    public void setRelatedNode(Map<Node, Integer> relatedNode) {
        this.relatedNode = relatedNode;
    }
}
