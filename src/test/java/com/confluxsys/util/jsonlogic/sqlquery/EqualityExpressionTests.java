package com.confluxsys.util.jsonlogic.sqlquery;

import com.confluxsys.util.jsonlogic.BaseJsonLogic;
import io.github.jamsesso.jsonlogic.JsonLogicException;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EqualityExpressionTests {
    private static final BaseJsonLogic jsonLogic = new JsonLogicToSQL();

    @Test
    public void testWithoutData() throws JsonLogicException {
        assertEquals("((col_a = 'hello' AND col_b = 'world' AND col_c = col_d) OR col_e = 34.0)", jsonLogic.apply("{\"or\":[{\"and\":[{\"==\":[{\"var\":\"col_a\"},\"hello\"]},{\"==\":[{\"var\":\"col_b\"},\"world\"]},{\"==\":[{\"var\":\"col_c\"},{\"var\":\"col_d\"}]}]},{\"==\":[{\"var\":\"col_e\"},34]}]}"
                , null));
    }

    @Test
    public void testWithData() throws JsonLogicException {
        assertEquals("((col_a = 'hello' AND col_b = 'world' AND col_c = col_d) OR col_e = 34.0)", jsonLogic.apply(
               "{\"or\":[{\"and\":[{\"==\":[{\"var\":0},\"hello\"]},{\"==\":[{\"var\":1},\"world\"]},{\"==\":[{\"var\":2},{\"var\":3}]}]},{\"==\":[{\"var\":4},34]}]}"
                , new String[] {"col_a", "col_b", "col_c", "col_d", "col_e"}

        ));
    }

//    @Test
//    public void testSameValueDifferentType() throws JsonLogicException {
//        assertEquals(true, jsonLogic.apply("{\"==\": [1, 1]}", null));
//    }
//
//    @Test
//    public void testDifferentValueDifferentType() throws JsonLogicException {
//        assertEquals(true, jsonLogic.apply("{\"==\": [[], false]}", null));
//    }
//
//    @Test
//    public void testEmptyStringAndZeroComparison() throws JsonLogicException {
//        assertEquals(true, jsonLogic.apply("{\"==\": [\" \", 0]}", null));
//    }
}
