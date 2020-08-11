package ini.myini;


import ini.IIniSection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomIIniSectionTest {

    @Test
    void testGetValueWhichNotExistReturnNull_OrPutValue() {
        IIniSection section = new CustomIIniSection("section");
        section.putValue("new_key", "new_value");

        Assertions.assertNull(section.getValue("unknown_key"));
    }

    @Test
    void testGetValueEmptyReturnNull_OrPutValue() {
        IIniSection section = new CustomIIniSection("section");
        section.putValue("new_key", "new_value");

        Assertions.assertNull(section.getValue(""));
    }

    @Test
    void testContainsKeyExist_OrContainsKey() {
        IIniSection section = new CustomIIniSection("section");
        section.putValue("new_key", "new_value");

        Assertions.assertTrue(section.hasItem("new_key"));
    }

    @Test
    void testContainsKeyNotExistReturnNull_OrContainsKey() {
        IIniSection section = new CustomIIniSection("section");
        section.putValue("new_key", "new_value");

        Assertions.assertFalse(section.hasItem("unknown_key"));
    }

    @Test
    void testPutValueNewContainNewValue_OrGetValue() {
        IIniSection section = new CustomIIniSection("section");
        section.putValue("new_key", "new_value");

        Assertions.assertEquals("new_value", section.getValue("new_key"));
    }

    @Test
    void testPutValueAlreadyContainUpdateValue_OrGetValue() {
        IIniSection section = new CustomIIniSection("section");
        section.putValue("new_key", "new_value");
        section.putValue("new_key", "replace_value");

        Assertions.assertEquals("replace_value", section.getValue("new_key"));
    }

    @Test
    void testPutValueEmptyAlreadyContainUpdateValueToEmpty_OrGetValue() {
        IIniSection section = new CustomIIniSection("section");
        section.putValue("new_key", "new_value");
        section.putValue("new_key", "");

        Assertions.assertEquals("", section.getValue("new_key"));
    }

    @Test
    void testPutSectionCommentAlreadyHasOne_OrGetComment() {
        IIniSection section = new CustomIIniSection("section");
        section.putSectionComment("This is the first section header comment.");
        section.putSectionComment("This is the second section header comment.");

        Assertions.assertEquals("This is the second section header comment.",
                                section.getSectionComment()
        );
    }

    @Test
    void testPutCommentItemExistHasComment_OrGetCommentPutValue() {
        IIniSection section = new CustomIIniSection("section");
        section.putValue("new_key", "new_value");
        section.putComment("new_key", "This is the item comment.");

        Assertions.assertEquals("This is the item comment.", section.getComment("new_key"));
    }

    @Test
    void testPutCommentItemNotExistHasComment_OrGetComment() {
        IIniSection section = new CustomIIniSection("section");
        section.putComment("new_key", "This is the item comment.");
        Assertions.assertEquals("This is the item comment.", section.getComment("new_key"));
    }

    @Test
    void testGetCommentNotExistingItemReturnEmptyString_OrPutValuePutComment() {
        IIniSection section = new CustomIIniSection("section");
        section.putValue("new_key", "new_value");
        section.putComment("new_key", "This is the item comment.");

        Assertions.assertEquals("", section.getComment("unknown_key"));
    }

    @Test
    void testToString() {
        IIniSection section = new CustomIIniSection("section");
        section.putValue("first_key", "first_value");
        section.putComment("first_key", "This is the first item comment.");

        section.putValue("second_key", "second_value");
        section.putComment("second_key", "This is the second item comment.");

        section.putSectionComment("The section comment");

        String expected = String.join(System.lineSeparator(), "# The section comment", "[section]",
                                      "# This is the first item comment.",
                                      "first_key = first_value", "",
                                      "# This is the second item comment.",
                                      "second_key = second_value"
        );
        expected += System.lineSeparator();
        String actual = section.toString();

        Assertions.assertEquals(expected, actual);
    }

}
