package com.confluxsys.util.jsonlogic.sqlquery;

import com.confluxsys.util.jsonlogic.BaseJsonLogicEvaluator;
import com.confluxsys.util.jsonlogic.sqlquery.types.QueryField;
import io.github.jamsesso.jsonlogic.ast.JsonLogicVariable;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;
import io.github.jamsesso.jsonlogic.utils.ArrayLike;

import java.util.Collection;
import java.util.Optional;

public class JsonLogicToSQLEvaluator extends BaseJsonLogicEvaluator {
    public JsonLogicToSQLEvaluator(Collection<JsonLogicExpression> expressions) {
        super(expressions);
    }

    @Override
    public Object evaluate(JsonLogicVariable variable, Object data) throws JsonLogicEvaluationException {
        String fieldName = null;

        Object key = evaluate(variable.getKey(), data);

        if (key == null)
            throw new JsonLogicEvaluationException("Variable Key cannot be null.");

        if (key instanceof Number) {
            int index = ((Number) key).intValue();

            if (ArrayLike.isEligible(data)) {
                ArrayLike list = new ArrayLike(data);

                if (index >= 0 && index < list.size()) {
                    fieldName = transform(list.get(index)).toString();
                }
            }
        } else if (key instanceof String) {
            String name = (String) key;

            if (name.isEmpty()) {
                throw new JsonLogicEvaluationException("Variable Name cannot empty string.");
            }
            fieldName = name;
        }
        if (fieldName != null)
            return new QueryField(fieldName);
        else
            throw new JsonLogicEvaluationException("var first argument must be null, number, or string");

    }
}
