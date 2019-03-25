package com.mobiquityinc.packer;

import com.mobiquityinc.packer.domain.PackageItem;
import com.mobiquityinc.packer.domain.SubSet;

import java.util.ArrayList;
import java.util.List;

public class Bereket {

   /* public static void main(String[] args) {

        String lines ="81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)\n" +
                "8 : (1,15.3,€34)\n" +
                "75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)\n" +
                "56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)";

        System.out.println(lines);
    }*/

   static List<PackageItem> packageItemList;

    static {

        packageItemList = new ArrayList<>();
        /*packageItemList.add(new PackageItem(1,53.38,45d));
        packageItemList.add(new PackageItem(2,88.62,98d));
        packageItemList.add(new PackageItem(3,78.48,3d));
        packageItemList.add(new PackageItem(4,72.30,76d));
        packageItemList.add(new PackageItem(5,30.18,9d));
        packageItemList.add(new PackageItem(6,46.34,48d));*/

        /*packageItemList.add(new PackageItem(1,15.3,34d));*/

        /*
        packageItemList.add(new PackageItem(1,85.31,29d));
        packageItemList.add(new PackageItem(2,14.55,74d));
        packageItemList.add(new PackageItem(3,3.98,16d));
        packageItemList.add(new PackageItem(4,26.24,55d));
        packageItemList.add(new PackageItem(5,63.69,52d));
        packageItemList.add(new PackageItem(6,76.25,75d));
        packageItemList.add(new PackageItem(7,60.02,74d));
        packageItemList.add(new PackageItem(8,93.18,35d));
        packageItemList.add(new PackageItem(9,89.95,78d));
        */

        packageItemList.add(new PackageItem(1,90.72,13d));
        packageItemList.add(new PackageItem(2,33.80,40d));
        packageItemList.add(new PackageItem(3,43.15,10d));
        packageItemList.add(new PackageItem(4,37.97,16d));
        packageItemList.add(new PackageItem(5,46.81,36d));
        packageItemList.add(new PackageItem(6,48.77,79d));
        packageItemList.add(new PackageItem(7,81.80,45d));
        packageItemList.add(new PackageItem(8,19.36,79d));
        packageItemList.add(new PackageItem(9,6.76,64d));
    }

    // Driver program to test printPowerSet
    public static void main (String[] args)
    {

        double maxWeight = 56d;

        Packer.filterOutNonPackableItems(packageItemList);
        List<SubSet> powerSet = Packer.getPowerSet(packageItemList);

        printPowerSet(powerSet);

        SubSet topRankedSet = null;

        for(SubSet subSet : powerSet){

            if(subSet.getTotalWeight() <= maxWeight && subSet.getTotalWeight() <= 100d){

                if(topRankedSet == null){

                    topRankedSet = subSet;

                } else {

                    if(subSet.getTotalCost() > topRankedSet.getTotalCost()){

                        topRankedSet = subSet;
                        System.out.println(subSet.getTotalCost() + ", " + subSet.getTotalWeight());
                    }
                }
            }
        }

        if(topRankedSet != null){

            System.out.print("(");
            for(int i = 0; i < topRankedSet.getPackageItems().size(); i++){
                System.out.print(topRankedSet.getPackageItems().get(i).getIndex() + ", ");
            }
            System.out.println(") Total Weight: " + topRankedSet.getTotalWeight() + ", Total Cost: " + topRankedSet.getTotalCost());
        } else {
            System.out.println(" - ");
        }
    }

    public static void printPowerSet(List<SubSet> powerSet){

        for(SubSet subSet : powerSet){

            System.out.print("(");
            for(PackageItem packageItem : subSet.getPackageItems()){
                System.out.print("("+ packageItem.getIndex() + ", " + packageItem.getWeight() + ", " +  packageItem.getCost() + "), ");
            }
            System.out.println(": Total Weight: " + subSet.getTotalWeight() + ", Total Cost: " + subSet.getTotalCost());
        }
    }
}
