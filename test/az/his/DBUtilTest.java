package az.his;

import junit.framework.TestCase;
import org.junit.Test;

public class DBUtilTest extends TestCase {
    @Test
    public void testCurrencyInt() {
        Object[][] tests = {
                {15600l, "156"},
                //{12010l,   "120.10"},
                {2000001l, "20\u00A0000,01"},
                {10000000l, "100\u00A0000"},
                {0l, "0"},
                {-2000l, "-20"},
                {5l, "0,05"},
                {-1234l, "-12,34"}
        };
        for (Object[] test : tests) {
            assertEquals(test[1], DBUtil.formatCurrency((Long) test[0]));
        }
    }
}
