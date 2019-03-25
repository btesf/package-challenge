package com.mobiquityinc.packer;

import com.mobiquityinc.packer.domain.PackageItem;
import com.mobiquityinc.packer.domain.SubSet;
import com.mobiquityinc.packer.exception.APIException;

import java.util.ArrayList;
import java.util.List;

public interface Packer {

    public static final double MAXIMUM_ITEM_WEIGHT = 100d;
    public static final double MAXIMUM_ITEM_COST = 100d;

    public static String pack(String filePath) throws APIException {

        return "";
    }

    static List<SubSet> getPowerSet (List<PackageItem> packageItemList) {

        int size = packageItemList.size();

        /* powerset of a set/list of size n = 2^(n-1) */
        long powerSetSize = (long)Math.pow(2, size);

        List<SubSet> powerSet = new ArrayList<>();

        for(int i = 0; i < powerSetSize; i++) {

            SubSet subSet = new SubSet();

            for(int j = 0; j < size; j++)
            {
                /* If jth bit in i is set then add jth element from set */
                if((i & (1 << j)) > 0){

                    subSet.addToPackages(packageItemList.get(j));
                }
            }

            if(subSet.getPackageItems().size() > 0){

                subSet.calculateTotals();
                powerSet.add(subSet);
            }
        }

        return powerSet;
    }

    static void filterOutNonPackableItems(List<PackageItem> packageItemList){

        for (PackageItem packageItem : new ArrayList<>(packageItemList)) {

            if (packageItem.getCost() > MAXIMUM_ITEM_COST || packageItem.getWeight() > MAXIMUM_ITEM_WEIGHT) {

                packageItemList.remove(packageItem);
            }
        }
    }
}
