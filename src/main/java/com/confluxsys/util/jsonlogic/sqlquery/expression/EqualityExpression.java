package com.confluxsys.util.jsonlogic.sqlquery.expression;

import com.confluxsys.util.jsonlogic.sqlquery.types.QueryField;
import io.github.jamsesso.jsonlogic.JsonLogic;
import io.github.jamsesso.jsonlogic.evaluator.JsonLogicEvaluationException;
import io.github.jamsesso.jsonlogic.evaluator.expressions.PreEvaluatedArgumentsExpression;

import java.util.List;

public class EqualityExpression implements PreEvaluatedArgumentsExpression {
    public static final EqualityExpression INSTANCE = new EqualityExpression();

    private EqualityExpression() {
        // Only one instance can be constructed. Use EqualityExpression.INSTANCE
    }

    @Override
    public String key() {
        return "==";
    }

    @Override
    public Object evaluate(List arguments, Object data) throws JsonLogicEvaluationException {
        if (arguments.size() != 2) {
            throw new JsonLogicEvaluationException("equality expressions expect exactly 2 arguments");
        }

        Object left = arguments.get(0);
        Object right = arguments.get(1);

        if (left == null || right == null) {
            throw new IllegalArgumentException("Invalid parameters. One of the operand is NULL. " + left + ", " + right);
        }

        String leftExp = getString(left);
        String rightExp = getString(right);

        String exp = null;
        if (leftExp != null && rightExp != null)
            return leftExp + " = " + rightExp;
        else if (leftExp == null && rightExp != null)
            return rightExp + " IS NULL";
        else if (leftExp != null && rightExp == null)
            return leftExp + " IS NULL";

        throw new JsonLogicEvaluationException("Invalid Expression");

    }

    private String getString(Object obj) {
        if (obj == null)
            return null;
        else if (obj instanceof QueryField)
            return ((QueryField) obj).getName();
        else if (obj instanceof String)
            return "'" + obj.toString() + "'";
        else
            return obj.toString();
    }


}
