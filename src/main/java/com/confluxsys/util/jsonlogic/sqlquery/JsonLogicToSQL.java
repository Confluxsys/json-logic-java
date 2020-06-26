package com.confluxsys.util.jsonlogic.sqlquery;

import com.confluxsys.util.jsonlogic.BaseJsonLogic;
import com.confluxsys.util.jsonlogic.sqlquery.expression.EqualityExpression;
import com.confluxsys.util.jsonlogic.sqlquery.expression.InExpression;
import com.confluxsys.util.jsonlogic.sqlquery.expression.LogicExpression;
import com.confluxsys.util.jsonlogic.sqlquery.expression.StrictEqualityExpression;

public class JsonLogicToSQL extends BaseJsonLogic<JsonLogicToSQLEvaluator> {
    public JsonLogicToSQL() {
        super(expression -> new JsonLogicToSQLEvaluator(expression));
        //Adding default operations
        this.addOperation(EqualityExpression.INSTANCE);
//        this.addOperation(InExpression.INSTANCE);
        this.addOperation(LogicExpression.AND);
        this.addOperation(LogicExpression.OR);
//        this.addOperation(StrictEqualityExpression.INSTANCE);
    }
}
