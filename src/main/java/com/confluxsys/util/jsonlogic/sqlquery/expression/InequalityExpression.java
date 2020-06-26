package com.confluxsys.util.jsonlogic.sqlquery.expression;

import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;
import io.github.jamsesso.jsonlogic.evaluator.expressions.EqualityExpression;

public class InequalityExpression implements JsonLogicExpression {
  public static final InequalityExpression INSTANCE = new InequalityExpression(io.github.jamsesso.jsonlogic.evaluator.expressions.EqualityExpression.INSTANCE);

  private final io.github.jamsesso.jsonlogic.evaluator.expressions.EqualityExpression delegate;

  private InequalityExpression(EqualityExpression delegate) {
    this.delegate = delegate;
  }

  @Override
  public String key() {
    return "!=";
  }

  @Override
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data)
    throws JsonLogicEvaluationException {
    boolean result = (boolean) delegate.evaluate(evaluator, arguments, data);

    return !result;
  }
}
