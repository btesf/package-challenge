package com.mobiquityinc.packer;

import com.mobiquityinc.packer.domain.PackageItem;
import com.mobiquityinc.packer.domain.PackingOption;
import com.mobiquityinc.packer.exception.APIException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Parser {

    static int MAX_ALLOWED_NUMBER_OF_PACKAGES = 15;
    /**
     * Splits string/text by new line and return the resulting values in array of Strings
     *
     * @param text
     * @return
     * @throws APIException
     */
    static String[] getStringLines(String text) throws APIException {

        if (text == null || text.trim().equals("")) {

            throw new APIException("Empty file content.");
        }

        return text.split("[\\r?\\n]+");//ignores multiple new lines
    }

    /**
     * Validates the string confirms to the following sample format:
     * <p>
     * 81.2:(1,53.38,45)(2,88.62,98)(3,78.48,3)(4,72.30,76)(5,30.18,9)(6,46.34,48)
     *
     * @param packageListing
     */
    static void validatePackageListingString(String packageListing) throws APIException {

        boolean isValid = packageListing.matches("^[0-9.]+:(\\([0-9]+,[0-9.]+,[0-9.]+\\))*$");

        if (!isValid) {

            throw new APIException("Package listing is not formatted correctly.");
        }
    }

    /**
     * removes spaces and Euro (€) symbols. Any cleanup should be done here
     *
     * @param packageListing
     * @return
     */
    static String cleanupPackageListingString(String packageListing) {

        return packageListing.trim().replaceAll("[\\s€]", "");
    }

    /**
     * Separates multiple packageItem in a string into individual package items
     *
     * e.g. (1,53.38,45)(2,88.62,98)(3,78.48,3) will be: [(1,53.38,45), (2,88.62,98), (3,78.48,3)]
     *
     * @param packageItems
     * @return
     */
    static List<String> separatePackageItems(String packageItems) {

        List<String> packageItemList = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\([0-9]+,[0-9.]+,[0-9.]+\\))?+");
        Matcher matcher = pattern.matcher(packageItems);

        while (matcher.find()) {

            String matchedGroup = matcher.group(1);

            //matcher by default matches an empty string at the end of a line. Avoid that.
            if(matchedGroup != null){

                packageItemList.add(matcher.group(1));
            }
        }

        return packageItemList;
    }

    static PackageItem getPackageItemFromString(String packageItemString) throws APIException {

        //remove braces and split by commas (,)
        packageItemString = packageItemString.replaceAll("[()]", "");
        String[] itemParameters = packageItemString.split(",");

        if(itemParameters.length != 3){

            throw new APIException("Package item parameter is missing.");
        }

        int index = Integer.valueOf(itemParameters[0]);
        double weight = Double.valueOf(itemParameters[1]);
        double cost = Double.valueOf(itemParameters[2]);

        PackageItem packageItem = new PackageItem(index, weight, cost);

        return packageItem;
    }

    /**
     * Split a packing list line into maximum weight and list of package items by ':' (colon) and
     * Separate individual package items and create list of PackageItems
     *
     * e.g.
     *
     * 81.2:(1,53.38,45)(2,88.62,98) will be converted to:
     *
     * PackagingOption(maximumWeight: 81.2, [PackageItem(index: 1, weight: 53.38, cost: 45), PackageItem(index: 2, weight: 88.62, cost: 98)]);
     *
     * @param line
     * @return
     */
    static PackingOption getPackingOptionFromString(String line) throws APIException {
        //split line to max weight and package item list sections:
        String[] sections = line.split(":");

        if(sections.length != 2){

            throw new APIException("Package list line has incorrect format. Should be: <max_weight> : (<index>, <weight>, <cost>)...");
        }

        double maxWeight = Double.valueOf(sections[0]);
        PackingOption packingOption = new PackingOption(maxWeight);
        List<String> packageItemParts = separatePackageItems(sections[1]);
        List<PackageItem> packageItems = new ArrayList<>();

        for(String part : packageItemParts){

            PackageItem packageItem = getPackageItemFromString(part);
            packageItems.add(packageItem);
        }

        if(packageItems.size() > MAX_ALLOWED_NUMBER_OF_PACKAGES){

            throw new APIException("Maximum number of allowed package items are 15");
        }

        packingOption.setPackageItems(packageItems);

        return packingOption;
    }

    /**
     * parses a text into the following form:
     *
     * List<PackingOption>
     *
     * @param text
     * @return
     * @throws APIException
     */
    static List<PackingOption> getPackagingOptions(String text) throws APIException {

        List<PackingOption> packingOptions = new ArrayList<>();

        String[] lines = getStringLines(text);

        for(String line : lines){

            String cleanedUpString = cleanupPackageListingString(line);

            validatePackageListingString(cleanedUpString);
            packingOptions.add(getPackingOptionFromString(cleanedUpString));
        }

        return packingOptions;
    }


    static List<PackingOption> parse(String fileName) throws APIException {

        String text = getTextFromFile(fileName);

        return getPackagingOptions(text);
    }

    /**
     * Reads file content and return as String
     *
     * @return
     */
    static String getTextFromFile(String fileName){


        String lines ="81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)\n" +
                "8 : (1,15.3,€34)\n" +
                "75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)\n" +
                "56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)";

        return lines;
    }
}
