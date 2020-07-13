package io.github.jamsesso.jsonlogic.ast;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class JsonLogicParser {
    private static ObjectMapper PARSER = null;

    private JsonLogicParser() {
        // Utility class has no public constructor.
    }

    public static JsonLogicNode parse(String json) throws JsonLogicParseException {
        try {
            if(PARSER==null)
                PARSER = new ObjectMapper();
            return parse(PARSER.readTree(json));
        } catch (Exception e) {
            throw new JsonLogicParseException(e);
        }
    }

    public static JsonLogicNode parse(JsonNode root) throws JsonLogicParseException {
        // Handle null
        if (root.isNull()) {
            return JsonLogicNull.NULL;
        }

        if (root.isArray()) {
            List<JsonLogicNode> elements = new ArrayList<>(root.size());
            for (JsonNode element : root) {
                elements.add(parse(element));
            }
            return new JsonLogicArray(elements);
        } else if (root.isTextual())
            return new JsonLogicString(root.asText());
        else if (root.isBoolean()) {
            if (root.asBoolean())
                return JsonLogicBoolean.TRUE;
            else
                return JsonLogicBoolean.FALSE;
        } else if (root.isNumber())
            return new JsonLogicNumber(root.asDouble());
        else if (root.isNull())
            return JsonLogicNull.NULL;
        else {
            // Handle objects & variables
            ObjectNode object = (ObjectNode) root;

            if (object.size()!=1) {
                throw new JsonLogicParseException("objects must have exactly 1 key defined, found " + object.size());
            }
            Map.Entry<String, JsonNode> field = object.fields().next();
            String key = field.getKey();
            JsonLogicNode argumentNode = parse(object.get(key));
            JsonLogicArray arguments;

            // Always coerce single-argument operations into a JsonLogicArray with a single element.
            if (argumentNode instanceof JsonLogicArray) {
                arguments = (JsonLogicArray) argumentNode;
            } else {
                arguments = new JsonLogicArray(Collections.singletonList(argumentNode));
            }

            // Special case for variable handling
            if ("var".equals(key)) {
                JsonLogicNode defaultValue = arguments.size() > 1 ? arguments.get(1) : JsonLogicNull.NULL;
                return new JsonLogicVariable(arguments.size() < 1 ? JsonLogicNull.NULL : arguments.get(0), defaultValue);
            }

            // Handle regular operations
            return new JsonLogicOperation(key, arguments);
        }
    }
}
