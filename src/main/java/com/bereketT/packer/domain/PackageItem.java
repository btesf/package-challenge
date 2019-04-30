package com.bereketT.packer.domain;

import java.util.Objects;

public class PackageItem {

    private Integer index;
    private Double weight;
    private Double cost;

    public PackageItem(Integer index, Double weight, Double cost){

        this.index = index;
        this.weight = weight;
        this.cost = cost;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj instanceof PackageItem){

            PackageItem secondPackageItem = (PackageItem) obj;

            return secondPackageItem.index.equals(this.index)
                    && secondPackageItem.getWeight().equals(this.weight)
                    && secondPackageItem.getCost().equals(this.cost);

        } else {

            return false;
        }
    }

    @Override
    public int hashCode() {

        return Objects.hash(index, weight, cost);
    }
}
