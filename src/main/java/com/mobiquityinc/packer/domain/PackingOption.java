package com.mobiquityinc.packer.domain;

import java.util.List;

/**
 * Holds all the possible package items to be shipped and the maximum allowed weight limit
 */
public class PackingOption {

    Double maximumWeight;
    List<PackageItem> packageItems;

    public PackingOption(Double maximumWeight){

        this.maximumWeight = maximumWeight;
    }

    public Double getMaximumWeight() {
        return maximumWeight;
    }

    public void setMaximumWeight(Double maximumWeight) {
        this.maximumWeight = maximumWeight;
    }

    public List<PackageItem> getPackageItems() {
        return packageItems;
    }

    public void setPackageItems(List<PackageItem> packageItems) {
        this.packageItems = packageItems;
    }

}
