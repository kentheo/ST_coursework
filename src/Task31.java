/* Kendeas Theofanous s1317642 */
/* Demetra Charalambous s1452672 */

import org.junit.Before;
import org.junit.Test;
import st.EntryMap;
import st.TemplateEngine;

import static org.junit.Assert.assertEquals;


public class Task31 {

    private EntryMap map;
    private TemplateEngine engine;

    @Before
    public void setUp() throws Exception {
        map = new EntryMap();
        engine = new TemplateEngine();
    }

    // Case 1: where keep-unmatched replaces more templates than delete-unmatched
    // as keep-unmatched replaces 3 templates and delete-unmatched replaces only one
    // result for delete-unmatched should be "Hello , is your age 20?"
    // keep-unmatched should be selected and actual result must be "Hello Alex, is your age 30?"
    @Test
    public void TestEvaluate1() {
        map.store("name ${surname}", "Alex", false);
        map.store("age", "20?", false);
        map.store("number ${?}", "?", false);
        map.store("age?", "30?", false);
        String result = engine.evaluate("Hello ${name ${surname}}, is your age ${age ${number ${?}}}", map, "optimization");
        assertEquals("Hello Alex, is your age 30?", result);
    }

    // Case 2: where delete-unmatched replaces more templates than keep-unmatched
    // as delete-unmatched replaces 3 templates and keep-unmatched replaces none
    // result for keep-unmatched should be the same as "Hello ${name ${surname}}, is your age ${age ${number ${?}}}"
    // delete-unmatched should be selected and the actual result must be "Hello Adam, is your age 20?"
    // This test passes the existing implementation because delete-unmatched is set by default
    @Test
    public void TestEvaluate2() {
        map.store("name", "Adam", false);
        map.store("age", "20?", false);
        map.store("${?}", "?", false);
        String result = engine.evaluate("Hello ${name ${surname}}, is your age ${age ${number ${?}}}", map, "optimization");
        assertEquals("Hello Adam, is your age 20?", result);
    }

    // Case 3: where both keep-unmatched and delete-unmatched replace the same number of templates
    // both replace two templates, result for delete-unmatched should be "Hello Adam, is your age 20?"
    // keep-unmatched should be selected and the actual output should be "Hello Alex, is your age 30?"
    @Test
    public void TestEvaluate3() {
        map.store("name", "Adam", false);
        map.store("name ${surname}", "Alex", false);
        map.store("age ${number}", "30?", false);
        map.store("age", "20?", false);
        String result = engine.evaluate("Hello ${name ${surname}}, is your age ${age ${number}}", map, "optimization");
        assertEquals("Hello Alex, is your age 30?", result);
    }


}
