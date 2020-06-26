package io.github.jamsesso.jsonlogic.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JsonValueExtractor {
  private JsonValueExtractor() { }

  public static Object extract(JsonNode element) {
    if (element.isObject()) {
      Map<String, Object> map = new HashMap<>();
      ObjectNode object = (ObjectNode)element;

      object.fields().forEachRemaining(
             entry -> map.put(entry.getKey(), extract(entry.getValue()))
      );

      return map;
    }
    else if (element.isArray()) {
      List<Object> values = new ArrayList<>();

      for (JsonNode item : element) {
        values.add(extract(item));
      }

      return values;
    }
    else if (element.isNull()) {
      return null;
    }
    else  {
      JsonNode primitive = element;

      if (primitive.isBoolean()) {
        return primitive.asBoolean();
      }
      else if (primitive.isNumber()) {
        return primitive.asDouble();
      }
      else {
        return primitive.asText();
      }
    }
  }
}
