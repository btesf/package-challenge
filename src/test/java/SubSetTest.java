import com.mobiquityinc.packer.domain.PackageItem;
import com.mobiquityinc.packer.domain.SubSet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SubSetTest {

    @Test
    public void testCalculateTotals(){

        List<PackageItem> packageItems = new ArrayList<>();
        SubSet subSet = new SubSet();

        packageItems.add(new PackageItem(1, 10d, 15d));
        packageItems.add(new PackageItem(1, 10d, 15d));
        packageItems.add(new PackageItem(1, 10d, 15d));
        subSet.setPackageItems(packageItems);

        subSet.calculateTotals();

        assertEquals(subSet.getTotalCost().doubleValue(), 45d, 0);
        assertEquals(subSet.getTotalWeight().doubleValue(), 30d, 0);

    }
}
