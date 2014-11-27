package org.ginga.tools.util;

import java.util.Map;

public class EnvironmentUtil {

	public static void printEnv() {
	  Map<String, String> env = System.getenv();
	  for (String envName : env.keySet()) {
	      System.out.format("%s=%s%n", envName, env.get(envName));
	  }
	}
}
