package com.gow.clilog.log.data.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.gow.clilog.log.data.AbstractLogData;
import com.gow.clilog.util.JSONUtil;
import com.gow.clilog.conf.SystemConfig;

import java.util.Iterator;
import java.util.Map;

/**
 * @author zhouhe
 * @date 2021/9/23
 */
public class JsonLogData extends AbstractLogData<String> {
    public JsonLogData() {
        super();
    }

    public JsonLogData(long createTime, String source) {
        super(createTime, source);
    }

    public JsonLogData(long createTime) {
        super(createTime);
    }

    /**
     * 先实现二级嵌套json解析
     * 
     * @return
     * @throws Exception
     */
    @Override
    public String source2Text() throws Exception {
        JsonNode jsonNode = JSONUtil.parse2Node(source());
        StringBuilder builder = new StringBuilder();
        if (jsonNode instanceof ObjectNode) {
            ObjectNode node = (ObjectNode)jsonNode;
            Iterator<Map.Entry<String, JsonNode>> ite = node.fields();
            while (ite.hasNext()) {
                Map.Entry<String, JsonNode> entry = ite.next();
                String key = entry.getKey();
                JsonNode value = entry.getValue();
                if (value instanceof ValueNode) {
                    builder.append(key).append(":{").append(value.asText()).append("}. ");
                } else if (value instanceof ContainerNode) {
                    if (value instanceof ArrayNode) {
                        ArrayNode arrayNode = (ArrayNode)value;
                        if (!arrayNode.isEmpty()) {
                            builder.append(key).append(":{");
                            Iterator<JsonNode> eleIte = arrayNode.elements();
                            while (eleIte.hasNext()) {
                                JsonNode next = eleIte.next();
                                String text = next.asText();
                                if (text == null || text.isEmpty()) {
                                    continue;
                                }
                                builder.append(text).append('|');
                            }
                            builder.setCharAt(builder.length() - 1, '}');
                            builder.append(". ");
                        }
                    } else if (value instanceof ObjectNode) {
                        ObjectNode objectNode = (ObjectNode)value;
                        if (!objectNode.isEmpty()) {
                            builder.append(key).append(":{");
                            Iterator<Map.Entry<String, JsonNode>> fieldIte = objectNode.fields();
                            while (fieldIte.hasNext()) {
                                Map.Entry<String, JsonNode> fieldEntry = fieldIte.next();
                                String text = fieldEntry.getValue().asText();
                                if (text == null || text.isEmpty()) {
                                    continue;
                                }
                                builder.append(fieldEntry.getKey()).append(':').append(text).append(',');
                            }
                            builder.setCharAt(builder.length() - 1, '}');
                            builder.append(". ");
                        }
                    }
                }
            }
        } else {
            builder.append(jsonNode.asText());
        }
        return builder.toString();
    }

    /**
     *
     * @return
     */
    @Override
    public String toLogText() throws Exception {
        return new StringBuilder(remoteAddress).append(' ').append(source2Text()).toString();
    }

    @Override
    public String toLogText(String createTimeFormat) throws Exception {
        String text = toLogText();
        if (SystemConfig.getInstance().isDevMode()) {
            text = createTimeFormat + " " + text;
        }
        return text;
    }
}
