import com.mobiquityinc.packer.Parser;
import com.mobiquityinc.packer.domain.PackageItem;
import com.mobiquityinc.packer.domain.PackingOption;
import com.mobiquityinc.packer.exception.APIException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
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
            fail("Should throw APIException at this point - Empty input string"); //shouldn't come here

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
            fail("Should throw APIException at this point - Empty input string"); //shouldn't come here

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

    @Test
    public void testGetPackageItemFromString(){

        final String text = "(1,53.38,45)";
        PackageItem expectedItem = new PackageItem(1, 53.38d, 45d);

        try{

            PackageItem packageItem = Parser.getPackageItemFromString(text);
            assertEquals(packageItem, expectedItem);

        } catch(Exception e){

            fail("Exception thrown while package item parameters are three"); //shouldn't come here
        }
    }

    @Test
    public void testGetPackageItemFromString_InvalidNoOfComponents(){

        final String text = "(1,53.38)";

        try{

            PackageItem packageItem = Parser.getPackageItemFromString(text);
            fail("Should throw APIException at this point - Invalid number of components"); //shouldn't come here

        } catch(Exception e){
           // --
        }
    }

    @Test
    public void testGetPackingOptionFromString(){

        final String text = "80.2:(1,53.38,45)(2,88.62,98)";
        final double maximumWeight = 80.2d;
        PackingOption expectedPackingOption = new PackingOption(maximumWeight);
        List<PackageItem> packageItems = new ArrayList<>();

        packageItems.add(new PackageItem(1, 53.38d, 45d));
        packageItems.add(new PackageItem(2, 88.62d, 98d));
        expectedPackingOption.setPackageItems(packageItems);

        try {

            PackingOption packingOption = Parser.getPackingOptionFromString(text);

            assertEquals(packingOption.getMaximumWeight(), expectedPackingOption.getMaximumWeight());
            assertThat(packingOption.getPackageItems().size(), is(2));
            assertThat(packingOption.getPackageItems(), containsInAnyOrder(packageItems.get(0), packageItems.get(1)));

        } catch (APIException e) {

            fail("Exception thrown while proper packing list text is provided"); //shouldn't come here
        }
    }

    @Test
    public void testGetPackingOptionFromString_InvalidSectionLength(){

        final String text = ":(1,53.38,45)(2,88.62,98)";

        try{

            Parser.getPackingOptionFromString(text);
            fail("Should throw APIException at this poin - Invalid length of sections"); //shouldn't come here

        } catch(Exception e){
            // --
        }
    }

    @Test
    public void testGetPackingOptionFromString_ExceededMaximumItemLimit(){

        //16 package items are listed (after the colon). Maximum allowed is 15.
        final String text = "80.2:(1,53.38,45)(2,88.62,98)(3,88.62,98)(4,88.62,98)(5,88.62,98)(6,88.62,98)(7,88.62,98)" +
                "(8,88.62,98)(9,88.62,98)(10,88.62,98)(11,88.62,98)(12,88.62,98)(13,88.62,98)(14,88.62,98)(15,88.62,98)(16,88.62,98)";
        try{

            Parser.getPackingOptionFromString(text);
            fail("Should throw APIException at this point - maximum package item limit exceeded"); //shouldn't come here

        } catch(Exception e){
            // --
        }
    }

    @Test
    public void testGetPackagingOptions(){

        final String text = "80.2:(1,53.38,45)\n30:(2,10,20)";
        List<PackingOption> expectedPackingOptionList = new ArrayList<>();
        PackingOption packingOption1 = new PackingOption(80.2d);
        PackingOption packingOption2 = new PackingOption(30d);

        packingOption1.setPackageItems(Arrays.asList(new PackageItem(1, 53.38d, 45d)));
        packingOption2.setPackageItems(Arrays.asList(new PackageItem(2, 11d, 20d)));
        expectedPackingOptionList.add(packingOption1);
        expectedPackingOptionList.add(packingOption2);

        try {

            List<PackingOption> packingOptionList = Parser.getPackagingOptions(text);

            //check for first packing option in expectedPackingOptionList
            PackingOption packingOption = expectedPackingOptionList.get(0);
            PackingOption computedPackingOption = packingOptionList.get(0);
            assertEquals(computedPackingOption.getMaximumWeight(), packingOption.getMaximumWeight());
            assertThat(computedPackingOption.getPackageItems().size(), is(1));
    //assertThat(computedPackingOption.getPackageItems(), contains(packingOption.getPackageItems().get(0)));

            //check for first packing option in expectedPackingOptionList
            packingOption = expectedPackingOptionList.get(1);
            computedPackingOption = packingOptionList.get(1);
            assertEquals(computedPackingOption.getMaximumWeight(), packingOption.getMaximumWeight());
            assertThat(computedPackingOption.getPackageItems().size(), is(1));
    //assertThat(computedPackingOption.getPackageItems(), contains(packingOption.getPackageItems().get(0)));

        } catch (APIException e) {

            fail("Exception thrown while proper packing list text is provided"); //shouldn't come here
        }
    }

    /*
     only test the  empty input string part - The rest is covered by 'testGetPackagingOptions(..)' test case above.
     */
    @Test
    public void testParse(){

        final String text = "";
        try{

            Parser.parse(text);
            fail("Should throw APIException at this point - empty string provided"); //shouldn't come here

        } catch(Exception e){
            // --
        }
    }
}
