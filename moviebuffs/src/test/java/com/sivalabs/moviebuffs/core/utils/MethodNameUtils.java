package com.sivalabs.moviebuffs.core.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MethodNameUtils {

	public static void main(String[] args) {
		List<String> names = new ArrayList<>();

		for (Method declaredMethod : MethodNameUtils.class.getDeclaredMethods()) {
			if (declaredMethod.getDeclaredAnnotation(Test.class) != null) {
				names.add(declaredMethod.getName());
			}
		}

		for (String name : names) {
			System.out.println(camelCaseToUnderscore(name));
		}
	}

	static String camelCaseToUnderscore(String methodName) {
		String regex = "([a-z])([A-Z]+)";
		String replacement = "$1_$2";
		String result = methodName.replaceAll(regex, replacement).toLowerCase();
		// System.out.println("result: "+result);
		return result;
	}

}
