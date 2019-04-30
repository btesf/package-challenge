import com.bereketT.packer.Packer;
import com.bereketT.packer.domain.PackageItem;
import com.bereketT.packer.domain.SubSet;
import com.bereketT.packer.exception.APIException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class PackerTest {

    @Parameterized.Parameter(0)
    public List<PackageItem> _packageItems;
    @Parameterized.Parameter(1)
    public double _maxAllowedWeight;
    @Parameterized.Parameter(2)
    public SubSet _subSet;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {

        SubSet subSet1 = new SubSet();
        subSet1.setPackageItems(new ArrayList<>(Arrays.asList(new PackageItem(4,72.30d,76d))));
        subSet1.calculateTotals();

        SubSet subSet2 = new SubSet();
        subSet2.setPackageItems(new ArrayList<>(Arrays.asList(new PackageItem(2,14.55d,74d), new PackageItem(7,60.02d,74d))));
        subSet2.calculateTotals();

        SubSet subSet3 = new SubSet();
        subSet3.setPackageItems(new ArrayList<>(Arrays.asList(new PackageItem(8,19.36d,79d), new PackageItem(9,6.76d,64d))));
        subSet3.calculateTotals();


        Object[][] data = new Object[][] {
                {
                    new ArrayList<PackageItem>(Arrays.asList(new PackageItem(1,53.38d,45d), new PackageItem(2,88.62d,98d), new PackageItem(3,78.48d,3d), new PackageItem(4,72.30d,76d), new PackageItem(5,30.18d,9d), new PackageItem(6,46.34d,48d))),
                    81d,
                    subSet1
                },
                {
                    new ArrayList<PackageItem>(Arrays.asList(new PackageItem(1,85.31d,29d), new PackageItem(2,14.55d,74d), new PackageItem(3,3.98d,16d), new PackageItem(4,26.24d,55d), new PackageItem(5,63.69d,52d), new PackageItem(6,76.25d,75d), new PackageItem(7,60.02d,74d), new PackageItem(8,93.18d,35d), new PackageItem(9,89.95d,78d))),
                    75d,
                    subSet2
                },
                {
                    new ArrayList<PackageItem>(Arrays.asList(new PackageItem(1,90.72d,13d), new PackageItem(2,33.80d,40d), new PackageItem(3,43.15d,10d), new PackageItem(4,37.97d,16d), new PackageItem(5,46.81d,36d), new PackageItem(6,48.77d,79d), new PackageItem(7,81.80d,45d), new PackageItem(8,19.36d,79d), new PackageItem(9,6.76d,64d))),
                    56d,
                    subSet3
                }
        };
        return Arrays.asList(data);
    }

    @Test
    public void testPack() throws URISyntaxException, APIException {

        final String expectedText = "4\n" +
                "-\n" +
                "2,7\n" +
                "8,9";

        URL resource = PackerTest.class.getClassLoader().getResource("packing_options.txt");
        Paths.get(resource.toURI()).toFile();
        String fileName = Paths.get(resource.toURI()).toString();

        assertEquals(Packer.pack(fileName), expectedText);
    }

    @Test
    public void testGetPowerSet(){

        PackageItem packageItem1 = new PackageItem(1, 53.38d, 45d);
        PackageItem packageItem2 = new PackageItem(2, 88.62d, 98d);
        List<PackageItem> packageItems = new ArrayList<>();
        packageItems.add(packageItem1);
        packageItems.add(packageItem2);
        //Powerset of the above two items contains 2^2 -1 sets = 3.
        List<PackageItem> list1 = new ArrayList<>(Arrays.asList(packageItem1));
        List<PackageItem> list2 = new ArrayList<>(Arrays.asList(packageItem2));
        List<PackageItem> list3 = new ArrayList<>(Arrays.asList(packageItem1, packageItem2));

        List<SubSet> subSets = new ArrayList<>();
        SubSet subSet1 = new SubSet();
        subSet1.setPackageItems(list1);
        subSet1.calculateTotals();
        subSets.add(subSet1);

        SubSet subSet2 = new SubSet();
        subSet2.setPackageItems(list2);
        subSet2.calculateTotals();
        subSets.add(subSet2);

        SubSet subSet3 = new SubSet();
        subSet3.setPackageItems(list3);
        subSet3.calculateTotals();
        subSets.add(subSet3);

        List<SubSet> powerSet = Packer.getPowerSet(packageItems);
        //check if the no of subsets in the power set are 3
        assertThat(powerSet.size(), is(subSets.size()));

        for(int i = 0; i < subSets.size(); i++){

            SubSet expectedSubset = subSets.get(i);
            SubSet computedSubset = powerSet.get(i);
            //assert the number of elements in subset list are equal
            assertThat(expectedSubset.getPackageItems().size(), is(computedSubset.getPackageItems().size()));
            assertThat(expectedSubset.getPackageItems(), is(computedSubset.getPackageItems()));
        }
    }

    @Test
    public void testGetTopRankedPackage(){

        SubSet computedSubset = Packer.getTopRankedPackage(_packageItems, _maxAllowedWeight);

        assertEquals(computedSubset.getTotalWeight(), _subSet.getTotalWeight(), 0);
        assertEquals(computedSubset.getTotalCost(), _subSet.getTotalCost(), 0);
        assertThat(computedSubset.getPackageItems().size(), is(_subSet.getPackageItems().size()));
        assertEquals(computedSubset.getPackageItems(), _subSet.getPackageItems());
    }

    @Test
    public void testGetTopRankedPackage_EmptyResult(){

        List<PackageItem> packageItems = new ArrayList<>(Arrays.asList(new PackageItem(1,15.3d,34d)));
        SubSet computedSubset = Packer.getTopRankedPackage(packageItems, 8);

        assertNull(computedSubset);
    }

    /**
     * Two items have either their cost exceeds 100 Euro, or weight exceed 100Kg - they will be removed
     */
    @Test
    public void testFilterOutNonPackableItems(){

        List<PackageItem> packageItems = new ArrayList<>();

        packageItems.add(new PackageItem(1, 53.38d, 45d));
        packageItems.add(new PackageItem(2, 53.38d, 45d));
        //non-packable items
        packageItems.add(new PackageItem(3, 53.38d, 1001d));
        packageItems.add(new PackageItem(4, 101d, 45d));

        Packer.filterOutNonPackableItems(packageItems);
        assertThat(packageItems.size(), is(2));

    }
}
