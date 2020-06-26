package com.confluxsys.util.jsonlogic;

import io.github.jamsesso.jsonlogic.ast.JsonLogicVariable;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;

import java.util.Collection;

public abstract class BaseJsonLogicEvaluator extends JsonLogicEvaluator {
    public BaseJsonLogicEvaluator(Collection<JsonLogicExpression> expressions) {
        super(expressions);
    }
    @Override
    public abstract Object evaluate(JsonLogicVariable variable, Object data) throws JsonLogicEvaluationException;
}
