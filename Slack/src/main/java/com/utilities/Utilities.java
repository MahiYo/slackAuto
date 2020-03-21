package com.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.Assert;

public class Utilities {

	public static Properties loadProperty(String propertyFilePath)
			throws FileNotFoundException, IOException, Exception {

		Properties environmentProperty = new Properties();
		FileInputStream inputFile = null;
		try {
			inputFile = new FileInputStream(propertyFilePath);

			try {
				environmentProperty.load(inputFile);
			} catch (Exception e) {
				Assert.assertNull(e);
			}
		} catch (Exception e) {
			Assert.assertNull(e);
		}

		return environmentProperty;

	}

}
