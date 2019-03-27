import com.mobiquityinc.packer.Parser;
import com.mobiquityinc.packer.exception.APIException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testGetStringLines(){

        final String text = "one\ntwo";
        final String[] expectedLines = new String[] {"one", "two"};

        try {

            assertArrayEquals(Parser.getStringLines(text), expectedLines);

        } catch (APIException e) {

            fail("Exception thrown while text is not empty"); //shouldn't come here
        }
    }

    @Test
    public void testGetStringLines_EmptyString(){

        String text = "";

        try {

            Parser.getStringLines(text);
            fail("Should throw APIException at this point"); //shouldn't come here

        } catch(APIException e){
            // --
        }
    }

    @Test
    public void testValidatePackageListingString(){

        final String sampleLine = "81.2:(1,53.38,45)";

        try {

            Parser.validatePackageListingString(sampleLine);

        } catch (APIException e) {

            fail("Exception thrown while sample line is not empty"); //shouldn't come here
        }
    }

    @Test
    public void testValidatePackageListingString_EmptyString(){

        final String sampleLine = "";

        try {

            Parser.validatePackageListingString(sampleLine);
            fail("Should throw APIException at this point"); //shouldn't come here

        } catch (APIException e) {
            // --
        }
    }

    @Test
    public void testCleanupPackageListingString(){

        final String input = "(1, 53.38,45€)  ( 2,  88.62  ,98€ )";
        final String expected = "(1,53.38,45)(2,88.62,98)";

        assertEquals(Parser.cleanupPackageListingString(input), expected);
    }

    @Test
    public void testSeparatePackageItems(){

        final String text = "(1,53.38,45)(2,88.62,98)";
        List<String> expectedList = new ArrayList<>();

        expectedList.add("(1,53.38,45)");
        expectedList.add("(2,88.62,98)");

        assertThat(Parser.separatePackageItems(text) , is(expectedList));
    }
}
