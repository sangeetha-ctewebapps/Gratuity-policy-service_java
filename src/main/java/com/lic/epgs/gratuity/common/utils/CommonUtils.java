/**
 * 
 */
package com.lic.epgs.gratuity.common.utils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;


/**
 * @author Muruganandam
 *
 */
@Component
public class CommonUtils {

	public static boolean isNonEmptyArray(List<?> list) {
		return list != null && !list.isEmpty();
	}

	public static String getRootCauseStackTrace(final Throwable throwable) {

		List<String> list = Arrays.asList(ExceptionUtils.getRootCauseStackTrace(throwable));
		if (CommonUtils.isNonEmptyArray(list)) {
			if (list.size() > 2) {
				return list.subList(0, 2).toString();
			} else if (list.size() <= 2) {
				return list.subList(0, 1).toString();
			} else {
				return list.toString();
			}
		}
		return "Internal Server Error";

	}
	
	public static final String[] units = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
			"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen",
			"Nineteen" };
public static final String[] tens = { "", // 0
			"", // 1
			"Twenty", // 2
			"Thirty", // 3
			"Forty", // 4
			"Fifty", // 5
			"Sixty", // 6
			"Seventy", // 7
			"Eighty", // 8
			"Ninety" // 9
	};
	public String convertAmountToWords(final int n) {
		if (n < 0) {
			return "Minus " + convertAmountToWords(-n);
		}

		if (n < 20) {
			return units[n];
		}

		if (n < 100) {
			return tens[n / 10] + ((n % 10 != 0) ? " " : "") + units[n % 10];
		}

		if (n < 1000) {
			return units[n / 100] + " Hundred" + ((n % 100 != 0) ? " " : "") + convertAmountToWords(n % 100);
		}

		if (n < 100000) {
			return convertAmountToWords(n / 1000) + " Thousand" + ((n % 10000 != 0) ? " " : "")
					+ convertAmountToWords(n % 1000);
		}

		if (n < 10000000) {
			return convertAmountToWords(n / 100000) + " Lakh" + ((n % 100000 != 0) ? " " : "")
					+ convertAmountToWords(n % 100000);
		}

		return convertAmountToWords(n / 10000000) + " Crore" + ((n % 10000000 != 0) ? " " : "")
				+ convertAmountToWords(n % 10000000);
	}
	
	public static String convertformate(Double payment) {
		 if(payment < 1000) {
		        return format("###", payment);
		    } else {
		        double hundreds = payment % 1000;
		        int other = (int) (payment / 1000);
		        return format(",##", other) + ',' + format("000", hundreds);
		    }		   
//		return decimalFormat.format(payment);
}

private static String format(String pattern, Object value) {
   return new DecimalFormat(pattern).format(value);
}
}
