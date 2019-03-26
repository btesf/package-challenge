package com.mobiquityinc.packer;

import com.mobiquityinc.packer.domain.PackageItem;
import com.mobiquityinc.packer.domain.PackingOption;
import com.mobiquityinc.packer.domain.SubSet;
import com.mobiquityinc.packer.exception.APIException;

import java.util.ArrayList;
import java.util.List;

public interface Packer {

    public static double MAXIMUM_ITEM_WEIGHT = 100d;
    public static double MAXIMUM_ITEM_COST = 100d;

    /**
     *
     * Purpose: Filling a package with things where:
     *
     * - Each thing (PackageItem) has such parameters as index number, weight and cost.
     * - The package has a weight limit. The goal is to determine which things to put into the package so that
     *      the total weight is less than or equal to the package limit and the total cost is as large as possible.
     * - It is preferred to send a package which weights less in case there is more than one package with the same price.
     *
     * Constraints:
     *  - Max weight that a package can take is ≤ 100
     *  - There might be up to 15 items you need to choose from
     *  - Max weight and cost of an item is ≤ 100
     *
     * @param filePath
     * @return
     * @throws APIException
     */
    public static String pack(String filePath) throws APIException {

        StringBuilder builder = new StringBuilder("");
        String text = FileUtil.getTextFromFile(filePath);
        List<PackingOption> packingOptions = Parser.parse(text);

        for(PackingOption packingOption : packingOptions){

            double maxWeight = packingOption.getMaximumWeight();

            SubSet topRankedSet = Packer.getTopRankedPackage(packingOption.getPackageItems(), maxWeight);

            if(topRankedSet != null){


                int itemsSize = topRankedSet.getPackageItems().size();

                for(int i = 0; i < itemsSize; i++){

                    builder.append(topRankedSet.getPackageItems().get(i).getIndex());
                    //append comma (,) if there are remaining package items
                    if(i < itemsSize -1) {

                        builder.append(",");
                    }
                }

            } else {

                builder.append("-");
            }

            builder.append(System.lineSeparator()); //OS/System independent line separator
        }

        return removeLastNewLine(builder.toString());
    }

    /**
     *
     * Creates a power set of (all possible combinations of) PackageItems.
     *
     * e.g. for two PackageItems in a set {(1,53.38,$45)(2,88.62,$98)} the power set will be:
     *
     * {(1,53.38,$45)},
     * {(2,88.62,$98)},
     * {(1,53.38,$45)(2,88.62,$98)}
     *
     *
     * @param packageItemList
     * @return
     */
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

    /**
     * Returns a subSet (topRankedSet) whose total weight is less than or equal to the package limit and the total cost is as large as possible
     *
     * @param packageItems
     * @param maxWeightAllowedInPackage
     * @return
     */
    static SubSet getTopRankedPackage(List<PackageItem> packageItems, double maxWeightAllowedInPackage){

        Packer.filterOutNonPackableItems(packageItems);
        List<SubSet> powerSet = Packer.getPowerSet(packageItems);

        SubSet topRankedSet = null;

        for(SubSet subSet : powerSet){

            if(subSet.getTotalWeight() <= maxWeightAllowedInPackage && subSet.getTotalWeight() <= MAXIMUM_ITEM_WEIGHT){

                if(topRankedSet == null){

                    topRankedSet = subSet;

                } else {

                    //In case of same cost packages, choose a lesser weight package
                    if(subSet.getTotalCost().doubleValue() == topRankedSet.getTotalCost().doubleValue()){

                        if(subSet.getTotalWeight() < topRankedSet.getTotalWeight()){

                            topRankedSet = subSet;
                        }

                    } else if(subSet.getTotalCost() > topRankedSet.getTotalCost()){

                        topRankedSet = subSet;
                    }
                }
            }
        }

        return topRankedSet;
    }

    /**
     * Non-Packable items are items which are either their cost > MAXIMUM_ITEM_COST and/or their weight > MAXIMUM_ITEM_WEIGHT
     *
     * @param packageItemList
     */
    static void filterOutNonPackableItems(List<PackageItem> packageItemList){

        for (PackageItem packageItem : new ArrayList<>(packageItemList)) {

            if (packageItem.getCost() > MAXIMUM_ITEM_COST || packageItem.getWeight() > MAXIMUM_ITEM_WEIGHT) {

                packageItemList.remove(packageItem);
            }
        }
    }

    /**
     *
     * @param string
     * @return
     */
    static String removeLastNewLine(String string){

        int lastIndex = string.lastIndexOf(System.lineSeparator());

        if (lastIndex >= 0) {

             return string.substring(0, lastIndex);
        }

        return string;
    }
}
