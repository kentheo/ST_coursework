/** Kendeas Theofanous s1317642 */
/** Demetra Charalambous s1452672 */

import org.junit.Before;
import org.junit.Test;
import st.EntryMap;
import st.TemplateEngine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class Task21 {

    private EntryMap map;

    private TemplateEngine engine;

    @Before
    public void setUp() throws Exception {
        map = new EntryMap();
        engine = new TemplateEngine();
    }

    /*
        EntryMap tests
     */

    // Testing Spec1
    // Case: A runtime exception is thrown if template is null
    @Test (expected = RuntimeException.class)
    public void TestStore1_1() {
        map.store(null, "Adam", false);
    }

    // Testing Spec1
    // Case: A runtime exception is thrown if template is empty
    @Test (expected = RuntimeException.class)
    public void TestStore1_2() {
        map.store("", "Adam", false);
    }

    // Testing Spec2
    // Case: A runtime exception is thrown if value is null
    @Test (expected = RuntimeException.class)
    public void TestStore2_1() {
        map.store("name", null, false);
    }

    // Testing Spec3
    // Case: Case sensitive flag = True
    @Test
    public void TestCaseSensitive3_1() {
        map.store("name", "Adam", true);
        String result = engine.evaluate("Hello ${Name}", map,"delete-unmatched");
        assertNotEquals("Hello Adam", result);
    }

    // Testing Spec3
    // Case: Case sensitive flag = False
    @Test
    public void TestCaseSensitive3_2() {
        map.store("name", "Adam", false);
        String result = engine.evaluate("Hello ${Name}", map,"delete-unmatched");
        assertEquals("Hello Adam", result);
    }

    // Testing Spec3
    // Case: Case sensitive flag = null, by default it's case insensitive
    // Test throws NullPointerException
    @Test
    public void TestCaseSensitive3_3() {
        map.store("name", "Adam", null);
        String result = engine.evaluate("Hello ${Name}", map,"delete-unmatched");
        assertEquals("Hello Adam", result);
    }

    // Testing Spec4
    // Case: Entries must follow the order in which they appear in the program
    // The template name corresponds to the first "name" stored in the map so "John" asserts not equal
    @Test
    public void TestOrder4_1() {
        map.store("name", "Adam", false);
        map.store("name", "John", false);
        String result = engine.evaluate("Hello ${name}", map,"delete-unmatched");
        assertNotEquals("Hello John", result);
    }

    // Testing Spec4
    // Case: Entries must follow the order in which they appear in the program
    // The template name corresponds to the first "name" stored in the map so "John" must not be used
    // either in the first or second occurence of ${name}
    @Test
    public void TestOrder4_2() {
        map.store("name", "Adam", false);
        map.store("name", "John", false);
        map.store("surname", "Dykes", false);
        String result = engine.evaluate("Hello ${name} ${surname}, is your name truly ${name} ?", map,"delete-unmatched");
        assertEquals("Hello Adam Dykes, is your name truly Adam ?", result);
    }

    // Testing Spec5
    // Case: Entries that already exist cannot be stored again
    // name/Adam is only stored once in the map
    @Test
    public void TestUnique5_1() {
        map.store("name", "Adam", false);
        map.store("name", "Adam", false);
        assertEquals(map.getEntries().size(), 1);
    }

    // Testing Spec5
    // Case: Entries that already exist cannot be stored again
    // name/Adam is stored twice because it has different case sensitivity
    @Test
    public void TestUnique5_2() {
        map.store("name", "Adam", true);
        map.store("name", "Adam", false);
        boolean result = map.getEntries().size() == 2 && engine.evaluate("Hello ${Name}", map,"delete-unmatched").equals("Hello Adam");
        assertEquals(result, true);
    }

    /*
        TemplateEngine tests
     */

    // Testing non-null values for template and value of EntryMap
    // Testing evaluate method of TemplateEngine
    @Test
    public void Test1() {
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        String result = engine.evaluate("Hello ${name} ${surname}", map,"delete-unmatched");
        assertEquals("Hello Adam Dykes", result);
    }

    @Test
    public void Test2() {
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        map.store("age", "29", false);
        String result = engine.evaluate("Hello ${name}, is your age ${age ${symbol}}", map,"delete-unmatched");
        assertEquals("Hello Adam, is your age 29", result);
    }

    // Testing Spec1
    // Case: Template string is empty
    @Test
    public void TestEvaluate1_1() {
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        map.store("age", "29", false);
        String result = engine.evaluate("", map,"delete-unmatched");
        assertEquals("", result);
    }

    // Testing Spec1
    // Case: Template string is null
    @Test
    public void TestEvaluate1_2() {
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        map.store("age", "29", false);
        String result = engine.evaluate(null, map,"delete-unmatched");
        assertEquals(null, result);
    }

    // Testing Spec2
    // Case: EntryMap is null
    @Test
    public void TestEvaluate2_1() {
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        map.store("age", "29", false);
        String result = engine.evaluate("Hello ${name}, is your age ${age ${symbol}}", null,"delete-unmatched");
        assertEquals("Hello ${name}, is your age ${age ${symbol}}", result);
    }

    // Testing Spec2
    // Case: EntryMap is null
    // Since the EntryMap is null, the templates should not be replaced
    @Test
    public void TestEvaluate2_2() {
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        String result = engine.evaluate("Hello ${name} ${surname}", null,"delete-unmatched");
        assertNotEquals("Hello Adam Dykes", result);
    }

    // Testing spec3-erroneous case
    // Case: "If matching mode NULL or other value, it defaults to "delete-unmatched""
    // matching mode = null
    // ***Test FAILS WITH NULL POINTER EXCEPTION!!!***
    @Test
    public void TestEvaluate3_1(){
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        map.store("age", "29", false);
        String result = engine.evaluate("Hello ${name}, is your age ${age ${symbol}}", map, null);
        assertEquals("Hello Adam, is your age 29", result);
    }

    // Testing spec3-boundary case
    // Case: "If matching mode NULL or other value, it defaults to "delete-unmatched""
    // matching mode = ""
    @Test
    public void TestEvaluate3_2(){
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        map.store("age", "29", false);
        String result = engine.evaluate("Hello ${name}, is your age ${age ${symbol}}", map, "");
        assertEquals("Hello Adam, is your age 29", result);
    }

    // Testing spec3-boundary case
    // Case: "If matching mode NULL or other value, it defaults to "delete-unmatched""
    // matching mode = "random_string"
    @Test
    public void TestEvaluate3_3(){
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        map.store("age", "29", false);
        String result = engine.evaluate("Hello ${name}, is your age ${age ${symbol}}", map, "random_string");
        assertEquals("Hello Adam, is your age 29", result);
    }

    // Testing spec3-normal case
    // Case: "Matching mode must be one of the possible values ("keep-unmatched" and "delete-unmatched")"
    // matching mode = "keep-unmatched"
    @Test
    public void TestEvaluate3_4(){
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        map.store("age", "29", false);
        String result = engine.evaluate("Hello ${name}, is your age ${age ${symbol}}", map, "keep-unmatched");
        assertEquals("Hello Adam, is your age ${age ${symbol}}", result);
    }

    // Testing spec3-normal case
    // Case: "Matching mode must be one of the possible values ("keep-unmatched" and "delete-unmatched")"
    // matching mode = "delete-unmatched"
    @Test
    public void TestEvaluate3_5(){
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        map.store("age", "29", false);
        String result = engine.evaluate("Hello ${name}, is your age ${age ${symbol}}", map, "delete-unmatched");
        assertEquals("Hello Adam, is your age 29", result);
    }

    // Testing spec4-normal case
    // Case: "Templates in a template string occur between "${" and "}""
    @Test
    public void TestEvaluate4_1(){
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        map.store("age", "29", false);
        String result = engine.evaluate("Hello ${name} ${surname}, is your age ${age}", map, "delete-unmatched");
        assertEquals("Hello Adam Dykes, is your age 29", result);
    }

    // Testing spec4
    // Case: "Templates in a template string occur between "${" and "}""
    // Reversing order of stored strings
    // Checking case where template string does not occur between "${" and "}" (age)
    @Test
    public void TestEvaluate4_2(){
        map.store("age", "29", false);
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        String result = engine.evaluate("Hello ${name} ${surname}, is your age ${age}", map, "keep-unmatched");
        assertEquals("Hello Adam Dykes, is your age 29", result);
    }

    // Testing spec4
    // Case: "Templates in a template string occur between "${" and "}""
    // Checking if first closing bracket is for the template "${}" and it remains unmatched
    @Test
    public void TestEvaluate4_3(){
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        map.store("age", "29", false);
        String result = engine.evaluate("Hello ${name} ${surname}, is your age ${age${}", map, "delete-unmatched");
        assertEquals("Hello Adam Dykes, is your age ${age", result);
    }

    // Testing Spec5
    // Case: Non visible characters do not affect the result
    @Test
    public void TestEvaluate5_1() {
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        String result = engine.evaluate("Hello ${na me} ${surname}", map,"delete-unmatched");
        assertEquals("Hello Adam Dykes", result);
    }

    // Testing Spec5
    // Case: Non visible characters do not affect the result
    @Test
    public void TestEvaluate5_2() {
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        String result = engine.evaluate("Hello ${na\tme} ${surname}", map,"delete-unmatched");
        assertEquals("Hello Adam Dykes", result);
    }

    // Testing Spec5
    // Case: Non visible characters do not affect the result
    @Test
    public void TestEvaluate5_3() {
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        String result = engine.evaluate("Hello ${na\nme} ${surname}", map,"delete-unmatched");
        assertEquals("Hello Adam Dykes", result);
    }

    // Testing Spec5
    // Case: Non visible characters do not affect the result
    @Test
    public void TestEvaluate5_4() {
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        String result = engine.evaluate("Hello ${na\rme} ${surname}", map,"delete-unmatched");
        assertEquals("Hello Adam Dykes", result);
    }

    // Testing Spec6
    // Case: Every "${" and "}" occurrence acts as a boundary of at most one template
    @Test
    public void TestEvaluate6_1() {
        map.store("add your John", "Adam", false);
        map.store("name", "John", false);
        map.store("surname", "Dykes", false);
        String result = engine.evaluate("Hello ${add your ${name}} ${surname}", map,"delete-unmatched");
        assertEquals("Hello Adam Dykes", result);
    }

    // Testing Spec6
    // Case: Every "${" and "}" occurrence acts as a boundary of at most one template
    @Test
    public void TestEvaluate6_2() {
        map.store("add your John", "Adam", false);
        map.store("name", "John", false);
        map.store("surname", "Dykes", false);
        map.store("Adam Dykes", "Alex", false);
        String result = engine.evaluate("Hello } ${ ${add your ${name}} ${surname} }", map,"delete-unmatched");
        assertEquals("Hello } Alex", result);
    }

    // Testing Spec7
    // Case: Different templates are ordered according to their length
    // Same length templates are first replaced in this order: ${cd} and then ${fg}
    // Then template "${ab Alex}" is processed and finally "$ab Alex John"
    @Test
    public void TestEvaluate7_1() {
        map.store("cd", "Alex", false);
        map.store("fg", "John", false);
        map.store("ab Alex John", "Adam", false);
        map.store("ab Alex", "Dykes", false);
        String result = engine.evaluate("Hello ${ab ${cd} ${fg}} ${ab ${cd}}", map,"delete-unmatched");
        assertEquals("Hello Adam Dykes", result);
    }

    // Testing Spec7
    // Case: Different templates are ordered according to their length
    // ${abcd} is replaced accordingly, but the other template first replaces ${f} and then the ${abcdf}
    @Test
    public void TestEvaluate7_2() {
        map.store("abcd", "Adam", false);
        map.store("f", "f", false);
        map.store("abcdf", "Dykes", false);
        String result = engine.evaluate("Hello ${abcd} ${abcd${f}}", map,"delete-unmatched");
        assertEquals("Hello Adam Dykes", result);
    }

    // Testing spec8
    // Case: "The engine processes one template at a time and attempts to match it against the keys of the EntryMap
    // entries until there is a match or the entry list is exhausted."
    // "1 - The template (including its boundaries) in the template string is replaced by the value of the matched entry."
    // "2 - The same replace happens to all other templates which include the replaced template."
    // Testing if one template can be used more than one time
    @Test
    public void TestEvaluate8_1(){
        map.store("name", "Adam", false);
        String result = engine.evaluate("Hello ${name}, is your name actually ${name}", map, "delete-unmatched");
        assertEquals("Hello Adam, is your name actually Adam", result);
    }

    // Testing spec8
    // Case: "If the entry list is exhausted and no match found for the current template:
    // 1 - The template engine just moves on to the next template if matching the mode is "keep-unmatched"."
    // Test ignores "${nomatchingtemplate}" and moves on to the template "${name}"
    @Test
    public void TestEvaluate8_2(){
        map.store("name", "Adam", false);
        String result = engine.evaluate("Hello ${nomatchingtemplate}, is your name actually ${name}", map, "keep-unmatched");
        assertEquals("Hello ${nomatchingtemplate}, is your name actually Adam", result);
    }

    // Testing spec8
    // Case: "If the entry list is exhausted and no match found for the current template:
    // 2 - The engine deletes the unmatched template from the template string and all other templates which include it.
    // Test deletes ALL "${nomatchingtemplate}" but replaces the template "${name}"
    @Test
    public void TestEvaluate8_3(){
        map.store("name", "Adam", false);
        String result = engine.evaluate("Hello ${nomatchingtemplate}, is your name actually ${name} ${nomatchingtemplate}", map, "delete-unmatched");
        assertEquals("Hello , is your name actually Adam ", result);
    }

    // Task 2
    /* Line 134-throw new RuntimeException() never reached
     * A template  cannot be null as templates are stored in a String and a String cannot contain a null value by default
     * The only way is that the String will be null
     */

    /* Line 190 currentTemplate.getEndIndex() == instancedString.length()
     * never reached as index starts counting from 0 while length starts from 1
     */

    /* Lines 255-267 never reached
     * equals is a method of the class Template which cannot be accessed on its own. The only reason it's used here
     * it's because it's an override of the existing equals method for objects
     */

    /* Line 291 getTemplatesReplaced() never used/called in the code
     */

    // Test for reaching line 184 when template starts from the beginning of the String
    @Test
    public void test1() {
        map.store("name", "Adam", false);
        map.store("surname", "Dykes", false);
        String result = engine.evaluate("${name} ${surname}, hello", map,"delete-unmatched");
        boolean x = result.equals("Adam Dykes, hello");
        assertEquals(x, true);
    }
}
