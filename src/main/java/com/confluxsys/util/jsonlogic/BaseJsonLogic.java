package com.confluxsys.util.jsonlogic;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.jamsesso.jsonlogic.JsonLogicException;
import io.github.jamsesso.jsonlogic.ast.JsonLogicNode;
import io.github.jamsesso.jsonlogic.ast.JsonLogicParser;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.PreEvaluatedArgumentsExpression;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class BaseJsonLogic <E extends JsonLogicEvaluator> {
    private final List<JsonLogicExpression> expressions;
    private final Map<String, JsonLogicNode> parseCache;
    private final Function<List<JsonLogicExpression> , E> evaluatorConstructor;
    private E evaluator;

    public BaseJsonLogic(Function<List<JsonLogicExpression> , E> evaluatorConstructor) {
        this.expressions = new ArrayList<>();
        this.parseCache = new ConcurrentHashMap<>();
        this.evaluatorConstructor = evaluatorConstructor;
    }

    public BaseJsonLogic addOperation(String name, Function<Object[], Object> function) {
        return addOperation(new PreEvaluatedArgumentsExpression() {
            @Override
            public Object evaluate(List arguments, Object data) {
                return function.apply(arguments.toArray());
            }

            @Override
            public String key() {
                return name;
            }
        });
    }

    public BaseJsonLogic addOperation(JsonLogicExpression expression) {
        expressions.add(expression);
        evaluator = null;
        return this;
    }

    public Object apply(JsonNode json, Object data) throws JsonLogicException {
        String key = json.toString();
        if (!parseCache.containsKey(key)) {
            parseCache.put(key, JsonLogicParser.parse(json));
        }

        if (evaluator == null) {
            evaluator = evaluatorConstructor.apply(expressions);
        }

        return evaluator.evaluate(parseCache.get(key), data);
    }
    public Object apply(String json, Object data) throws JsonLogicException {
        if (!parseCache.containsKey(json)) {
            parseCache.put(json, JsonLogicParser.parse(json));
        }

        if (evaluator == null) {
            evaluator = evaluatorConstructor.apply(expressions);
        }

        return evaluator.evaluate(parseCache.get(json), data);
    }

    public static boolean truthy(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof Boolean) {
            return (boolean) value;
        }

        if (value instanceof Number) {
            if (value instanceof Double) {
                Double d = (Double) value;

                if (d.isNaN()) {
                    return false;
                }
                else if (d.isInfinite()) {
                    return true;
                }
            }

            if (value instanceof Float) {
                Float f = (Float) value;

                if (f.isNaN()) {
                    return false;
                }
                else if (f.isInfinite()) {
                    return true;
                }
            }

            return ((Number) value).doubleValue() != 0.0;
        }

        if (value instanceof String) {
            return !((String) value).isEmpty();
        }

        if (value instanceof Collection) {
            return !((Collection) value).isEmpty();
        }

        if (value.getClass().isArray()) {
            return Array.getLength(value) > 0;
        }

        return true;
    }
}
