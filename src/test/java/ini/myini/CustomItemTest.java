package ini.myini;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomItemTest {

    @Test
    void testToString() {
        CustomItem item = new CustomItem();
        item.setKey("key");
        item.setValue("value");
        item.setComment("My first comment");

        String expected = String.join(System.lineSeparator(),
                "# My first comment",
                "key = value");
        String actual = item.toString();

        Assertions.assertEquals(expected, actual);
    }

}
