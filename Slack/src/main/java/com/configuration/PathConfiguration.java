/**
 * 
 */
package com.configuration;

import java.util.Properties;

import com.utilities.Utilities;

/**
 * To set the host endpoint based on env.
 * 
 */
public class PathConfiguration {

	public static String url,accesstoken;
	public static String env;
	public static Properties Path;

	static {
		try {
			Properties commonConfigPath = Utilities
					.loadProperty(System.getProperty("user.dir") + "/PropertiesFile/application.properties");
			env = commonConfigPath.getProperty("env").toLowerCase();
			Environment((commonConfigPath.getProperty("env")).toLowerCase());

		} catch (Exception e) {
			System.out.println("unable to load properties File not found");
			e.printStackTrace();

		}
	}

	public static void Environment(String name) throws Exception {

		Path = Utilities.loadProperty(System.getProperty("user.dir") + "/PropertiesFile/" + name + ".properties");
		url = Path.getProperty("url");
		accesstoken= Path.getProperty("token");

	}

}
