/**
 * 
 */
package com.lic.epgs.gratuity.common.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Kathiravan
 * @Created Date : Wed Sep 23 19:33:18 IST 2020
 */
public class NumericUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Integer convertStringToInteger(String value) {
		try {
			return StringUtils.isNotBlank(value) ? Integer.valueOf(value) : 0;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Integer stringToInteger(String value) {
		try {
			return StringUtils.isBlank(value) ? null : Integer.valueOf(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Long convertStringToLong(String value) {
		try {
			return StringUtils.isBlank(value) ? null : Long.valueOf(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Long stringToLong(String value) {
		try {
			return StringUtils.isBlank(value) ? null : Long.valueOf(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static String convertLongToString(Long value) {
		try {
			return value == null ? "" : String.valueOf(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Double stringToDouble(String value) {
		try {
			if (StringUtils.isNotBlank(value) && !value.equalsIgnoreCase("null")) {
				return Double.valueOf(value);
			}
		} catch (NumberFormatException e) {
		}
		return null;
	}

	public static Double convertStringToDouble(String value) {
		try {
			if (StringUtils.isNotBlank(value) && !value.equalsIgnoreCase("null")) {
				return Double.valueOf(value);
			}
		} catch (NumberFormatException e) {
		}
		return 0.0;
	}

	public static String convertIntegerToString(Integer value) {
		String val = null;
		if (value != null) {
			val = String.valueOf(value);
		} else {
			val = "";
		}

		return val;
	}

	public static String convertDoubleToString(Double value) {
		String val = null;
		if (value != null) {
			val = String.valueOf(value);
		} else {
			val = "";
		}

		return val;
	}

	public static String convertBooleanToString(Boolean value) {
		String val = null;
		if (value != null) {
			val = String.valueOf(value);
		} else {
			val = "";
		}

		return val;
	}

	public static Boolean convertStringToBoolean(String value) {
		Boolean val = null;
		if (StringUtils.isNotBlank(value)) {
			val = Boolean.valueOf(value);
		} else {
			val = true;
		}
		return val;
	}

	public static Long sumOfExtingAndCurrentAmount(Long newCount, Long exstingCount) {

		if (exstingCount != null) {
			return exstingCount + newCount;
		}

		return newCount;
	}

	public static List<Long> convertStringToListOfLong(String value) {
		try {
			List<Long> listofLong = Arrays.asList(value.split(",")).stream().map(obj -> Long.parseLong(obj.trim()))
					.collect(Collectors.toList());
			return listofLong;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static String getStringVal(String value) {
		try {
			if (value != null) {
				if (StringUtils.isNotBlank(String.valueOf(value))) {
					if (value.equalsIgnoreCase("null")) {
						return null;
					} else {
						return value;
					}
				}
			}
		} catch (NumberFormatException e) {
		}
		return value;
	}

	public static String getNotNullStringVal(String value) {
		try {
			if (value != null) {
				if (StringUtils.isNotBlank(String.valueOf(value)) && !String.valueOf(value).equalsIgnoreCase("null")) {
					return String.valueOf(value);
				}
			}
		} catch (NumberFormatException e) {
		}
		return null;
	}

	public static String objectToString(Object value) {
		try {
			if (value != null) {
				if (StringUtils.isNotBlank(String.valueOf(value)) && !String.valueOf(value).equalsIgnoreCase("null")) {
					return String.valueOf(value);
				}
			}
		} catch (NumberFormatException e) {
		}
		return null;
	}

	public static boolean isStringNotNull(String value) {
		return StringUtils.isNotBlank(String.valueOf(value)) && !String.valueOf(value).equalsIgnoreCase("null");
	}

	public static Double convertObjectToDouble(Object ob) {
		try {
			if (ob != null) {
				String value = String.valueOf(ob);
				if (StringUtils.isNotBlank(value) && !value.equalsIgnoreCase("null")) {
					return Double.valueOf(value);
				}
			}
		} catch (NumberFormatException e) {
		}
		return 0.0;
	}

	public static Integer convertObjectToInteger(Object ob) {
		try {
			if (ob != null) {
				String value = String.valueOf(ob);
				if (StringUtils.isNotBlank(value) && !value.equalsIgnoreCase("null")) {
					return Integer.valueOf(value);
				}
			}
		} catch (NumberFormatException e) {
		}
		return 0;
	}

	public static Long convertObjectToLong(Object ob) {
		try {
			if (ob != null) {
				String value = String.valueOf(ob);
				if (StringUtils.isNotBlank(value) && !value.equalsIgnoreCase("null")) {
					return Long.valueOf(value);
				}
			}
		} catch (NumberFormatException e) {
		}
		return 0l;
	}

	public static BigDecimal convertObjectToBigDecimal(Object ob) {
		try {
			return BigDecimal.valueOf(convertObjectToDouble(ob));
		} catch (NumberFormatException e) {
		}
		return BigDecimal.ZERO;
	}

	public static Date convertObjectToDate(Object ob) {
		try {
			if (ob != null) {
				String value = String.valueOf(ob);
				if (StringUtils.isNotBlank(value) && !value.equalsIgnoreCase("null")) {
					return DateUtils.convertStringToDateDMYYYYSLASH(value);
				}
			}
		} catch (NumberFormatException e) {
		}
		return null;
	}

	public static Boolean convertObjectToBoolean(Object ob) {
		try {
			if (ob != null) {
				String value = String.valueOf(ob);
				if (StringUtils.isNotBlank(value) && !value.equalsIgnoreCase("null")) {
					return Boolean.valueOf(value);
				}
			}
		} catch (NumberFormatException e) {
		}
		return false;
	}

	public static String convertBigDecimalToString(BigDecimal ob) {
		try {
			return ob != null ? String.valueOf(ob) : "0";
		} catch (NumberFormatException e) {
		}
		return "0";
	}
	
}
