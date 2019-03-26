package com.mobiquityinc.packer;

import com.mobiquityinc.packer.domain.PackageItem;
import com.mobiquityinc.packer.domain.PackingOption;
import com.mobiquityinc.packer.domain.SubSet;
import com.mobiquityinc.packer.exception.APIException;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

public class Bereket {

    // Driver program to test printPowerSet
    public static void main (String[] args) throws APIException, URISyntaxException {

        URL resource = Bereket.class.getClassLoader().getResource("packing_options.txt");
        Paths.get(resource.toURI()).toFile();
        System.out.println(Paths.get(resource.toURI()).toFile());
    }

}