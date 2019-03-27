package com.mobiquityinc.packer.domain;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds a subset of the power set of all possible combination of package items
 */
public class SubSet {

    private Double totalCost;
    private Double totalWeight;
    List<PackageItem> packageItems;

    public SubSet(){

        packageItems = new ArrayList<>();
    }

    public void addToPackages(PackageItem packageItem){

        packageItems.add(packageItem);
    }

    public void calculateTotals(){

        //calculate total cost and weight
        double totalCost = 0d;
        double totalWeight = 0d;

        for(PackageItem packageItem : packageItems){

            totalCost += packageItem.getCost();
            totalWeight += packageItem.getWeight();
        }

        //round and assign values
        DecimalFormat df = new DecimalFormat("#.##");

        this.totalCost = Double.valueOf(df.format(totalCost));
        this.totalWeight = Double.valueOf(df.format(totalWeight)); ;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public List<PackageItem> getPackageItems() {
        return packageItems;
    }

    public void setPackageItems(List<PackageItem> packageItems) {
        this.packageItems = packageItems;
    }
}
