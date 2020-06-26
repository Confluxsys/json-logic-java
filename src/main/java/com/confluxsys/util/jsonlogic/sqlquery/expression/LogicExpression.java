package com.confluxsys.util.jsonlogic.sqlquery.expression;

import io.github.jamsesso.jsonlogic.JsonLogic;
import io.github.jamsesso.jsonlogic.ast.JsonLogicArray;
import io.github.jamsesso.jsonlogic.ast.JsonLogicNode;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluator;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicExpression;

public class LogicExpression implements JsonLogicExpression {
  public static final LogicExpression AND = new LogicExpression(true);
  public static final LogicExpression OR = new LogicExpression(false);

  private final boolean isAnd;

  private LogicExpression(boolean isAnd) {
    this.isAnd = isAnd;
  }

  @Override
  public String key() {
    return isAnd ? "and" : "or";
  }

  @Override
  public Object evaluate(JsonLogicEvaluator evaluator, JsonLogicArray arguments, Object data)
    throws JsonLogicEvaluationException {
    if (arguments.size() < 1) {
      throw new JsonLogicEvaluationException("and operator expects at least 1 argument");
    }

    boolean notTheFirstOne = false;
    StringBuffer expression = new StringBuffer("(");
    for (JsonLogicNode element : arguments) {
      Object result = evaluator.evaluate(element, data);
        if(notTheFirstOne) {
          if(isAnd)
            expression.append(" AND ");
          else
            expression.append(" OR ");
          expression.append(result.toString());
        }
        else {
          notTheFirstOne = true;
          expression.append(result);
        }
    }
    expression.append(")");
    return expression.toString();
  }
}
