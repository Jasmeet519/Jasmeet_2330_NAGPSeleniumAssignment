package com.nagarro.utils;

import org.testng.annotations.DataProvider;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CsvDataProviders {

    /**
     * Helps to pass the parameters in the test function after reading from CSV filed
     * @param method provides information about, and access to, a single method on a class or interface
     * @return  Iterator<Object[]>
     */
    @DataProvider(name = "csvReader")
    public static Iterator<Object[]> csvReader(Method method) {
        Helper helper = new Helper();
        List<Object[]> list = new ArrayList<Object[]>();
        String pathname = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "dataproviders" + File.separator + method.getDeclaringClass().getSimpleName() + File.separator
                + method.getName() + ".csv";

        List<Map<String, String>> csvData = helper.readCSVFile(pathname);
        for (Map<String, String> testData :
                csvData) {
            list.add(new Object[]{testData});
        }
        return list.iterator();
    }

}
