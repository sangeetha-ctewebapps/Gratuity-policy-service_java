/**
\ * @author Kathiravan
 */
package com.lic.epgs.gratuity.common.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {

	static {
		/**
		 * 
		 * TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
		 * 
		 **/
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));

	}

	private static String formatMMDDYYYHHMM = "yyyy-MM-dd hh:mm:ss aa";
	private static DecimalFormat decimalFormatHHHMMM = new DecimalFormat("######");
	private static final String DDMMYYYY = "dd/MM/yyyy";
	private static final String DDMMYYYYHMMA = "dd/MM/yyyy H:mm a";
	private static final String DDMMYYYYHHMMSS = "dd/MM/yyyy HH:mm:ss";
	private static String YYYYMMDD_HHMMSS_HYHEN = "yyyy-MM-dd hh:mm:ss";

	// public static final String DMYYYY_SLASH = "M/d/yyyy";

	public static final String DMYYYY_SLASH = "yyyy-MM-dd";

	public static Date sysDate() {
		return new Date();
	}

	public static Date convertStringToDateDMYYYYSLASH(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat(DMYYYY_SLASH);
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {

			}
		}
		return null;
	}

	public static Date convertStringToDate(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat(DDMMYYYY);
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Date convertStringToDateYYYYMMDD_HHMMSS_HYHEN(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat(YYYYMMDD_HHMMSS_HYHEN);
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String dateToStringDDMMYYYY(Date value) {
		String val = "";
		if (value != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(DDMMYYYY);
			val = sdf.format(value);
		}
		return val;
	}

	public static String dateToStringFormatYyyyMmDdHhMmSsSlash(Date value) {
		String val = "";
		if (value != null) {

			SimpleDateFormat sdf = new SimpleDateFormat(DDMMYYYYHHMMSS);
			val = sdf.format(value);
		}
		return val;
	}

	public static String dateToStringFormatHhMmSs(Date value) {
		String val = "";
		if (value != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			val = sdf.format(value);
		}
		return val;
	}

	public static Date convertStringToDateTimeWithPeriod(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat(DDMMYYYYHMMA);
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				/***
				 * Error
				 */
			}
		}
		return null;
	}

	public static String convertDateTimeToStringWithPeriod(Date dateValue) {
		if (dateValue != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(DDMMYYYYHMMA);
				return sdf.format(dateValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String dateToStringFormatYyyyMmDdHhMmSsSAlash(Date value) {
		String val = "";
		if (value != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
			val = sdf.format(value);
		}
		return val;
	}

	public static Map<String, String> findTwoDaysDiffHourMinitsSecendsMilliseconds(Date fromDate, Date toDate) {
		Map<String, String> map = new HashMap<>();
		if (fromDate != null && toDate != null) {
			try {
				long diff = fromDate.getTime() - toDate.getTime();
				int years = (int) (diff / (24 * 60 * 60 * 1000)) / 365;
				map.put("Age", String.valueOf(years));
				int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
				map.put("Days", String.valueOf(diffDays));
				int diffhours = (int) (diff / (60 * 60 * 1000));
				map.put("hours", decimalFormatHHHMMM.format(diffhours));
				int diffmin = (int) (diff / (60 * 1000));
				map.put("Minitues", decimalFormatHHHMMM.format(diffmin));
				int diffsec = (int) (diff / (1000));
				map.put("Secends", decimalFormatHHHMMM.format(diffsec));
				map.put("Milliseconds", decimalFormatHHHMMM.format(diff));
			} catch (Exception e) {
				/**
				 * Error
				 */
			}
		}
		return map;
	}

	/**
	 * (d/m/yyyy h:mm:ss tt", "dd/MM/yyyy hh:mm:ss", "d/M/yyyy hh:mm tt", "d/m/yyyy
	 * h:mm tt", "d/M/yyyy h:mm:ss", "d/M/yyyy hh tt", "d/M/yyyy h:mm", "d/M/yyyy
	 * h:mm","dd/MM/yyyy hh:mm", "dd/M/yyyy hh:mm)
	 **/
	public static Boolean validateIsDateFormatInHHMM(String dateValue) {
		boolean isValue = false;
		if (StringUtils.isNotEmpty(dateValue)) {
			isValue = true;
		}
		return isValue;
	}

	public static void main(String[] args) {

		/***
		 * 
		 */
	}

	public static String uniqueNoYYYMMYDDMilli() {
		SimpleDateFormat dfFormat = new SimpleDateFormat("yyyMMyddkkmmssSS");
		return dfFormat.format(new Date());
	}

	public static String uniqueNoMilli() {
		SimpleDateFormat dfFormat = new SimpleDateFormat("ddMMyyyykkmm");
		return dfFormat.format(new Date());
	}

	public static String uniqueNoYYYY() {
		SimpleDateFormat dfFormat = new SimpleDateFormat("yyyy");
		return dfFormat.format(new Date());
	}

	public static String currentMonth() {
		SimpleDateFormat dfFormat = new SimpleDateFormat("MM");
		return dfFormat.format(new Date());
	}

	public static String currentDay() {
		SimpleDateFormat dfFormat = new SimpleDateFormat("dd");
		return dfFormat.format(new Date());
	}

	public static String uniqueNoYYYYMMDD() {
		SimpleDateFormat dfFormat = new SimpleDateFormat("yyyyMMdd");
		return dfFormat.format(new Date());
	}

	public static long dateDifferenceInHour(Date fromDate, Date toDate) {
		LocalDateTime fromDateTime = dateToLocalDateTime(fromDate);
		LocalDateTime toDateTime = dateToLocalDateTime(toDate);
		/**
		 * long diffInDays = ChronoUnit.DAYS.between(fromDateTime, toDateTime); long
		 * diffInMin = ChronoUnit.MINUTES.between(fromDateTime, toDateTime); long
		 * diffInSec = ChronoUnit.SECONDS.between(fromDateTime, toDateTime); long
		 * diffInHours = ChronoUnit.HOURS.between(fromDateTime, toDateTime);
		 */
		return ChronoUnit.HOURS.between(fromDateTime, toDateTime);
	}

	public static LocalDateTime dateToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	public static boolean isDatebetweenTwoDates(Date fromDate, Date toDate, Date currentDate) {
		return currentDate.after(fromDate) && currentDate.before(toDate);
	}

	public static List<LocalDate> getDatesBetweenUsingJava8(LocalDate startDate, LocalDate endDate) {
		long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
		return IntStream.iterate(0, i -> i + 1).limit(numOfDaysBetween).mapToObj(startDate::plusDays)
				.collect(Collectors.toList());
	}

	public static String convertLdtToString(LocalDate localDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DDMMYYYY);
		return localDate.format(formatter);
	}

	/*
	 * request format is convertStringToDBDateWithHHMMSSA("02/16/2021 1:36:00 pm")
	 */
	/* written from Kathiravan */
	@SuppressWarnings("deprecation")
	public static Date convertStringToDBDateWithHHMMSSA(String date) {
		try {
			if (StringUtils.isNotBlank(date) && !date.equalsIgnoreCase("null")) {
				return new Date(String.format(date));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String uniqueNoYYYYMM() {
		SimpleDateFormat dfFormat = new SimpleDateFormat("yyyyMM");
		return dfFormat.format(new Date());
	}

	public static Date convertStringYYYYMMDDTHHMMSSZ(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Date convertStringDDMMYYYYYHHMMSS(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Date convertStringYYYYMMDDTHHMMSSSSZ(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Date getDateOnly() {
		Date today = sysDate();
		DateFormat formatter = new SimpleDateFormat(DDMMYYYY);
		try {
			return formatter.parse(formatter.format(today));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static boolean validateCaseCreationDateFormat(String dateValue) {
		boolean isValue = false;

		String[] permissFormats = new String[] { "d/m/yyyy h:mm:ss tt", "d/m/yyyy h:mm tt", "dd/MM/yyyy hh:mm:ss",
				"d/M/yyyy h:mm:ss", "d/M/yyyy hh:mm tt", "d/M/yyyy hh tt", "d/M/yyyy h:mm", "d/M/yyyy h:mm",
				"dd/MM/yyyy hh:mm", "dd/M/yyyy hh:mm", DDMMYYYY, "dd/M/yyyy HH:mm" };
		for (int i = 0; i < permissFormats.length; i++) {
			try {
				SimpleDateFormat sdfObj = new SimpleDateFormat(permissFormats[i]);
				sdfObj.setLenient(false);
				sdfObj.parse(dateValue);
				isValue = true;

				break;
			} catch (Exception e) {
				isValue = false;
			}
		}
		if (!isValue) {
			isValue = isValidDate1(dateValue);
		} else {
			isValue = isValue;
		}
		return isValue;
	}

	public static boolean isValidDate1(String d) {
		String regex = "^([1-9])/([1-9])/[0-9]{4} [0-9]{2} [A-Z][A-Z]$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(d);
		return matcher.matches();
	}

	public static Date convertStringToDateYYYYMMDD(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Long dateDifferenceInMinutes(Date fromDate, Date toDate) {
		LocalDateTime fromDateTime = dateToLocalDateTime(fromDate);
		LocalDateTime toDateTime = dateToLocalDateTime(toDate);
		return ChronoUnit.MINUTES.between(fromDateTime, toDateTime);
	}

	public static Date convertStringToDateDDMMYYYYYHHMMSS(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat(DDMMYYYYHHMMSS);
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Long stringDateDifferenceInDays(String fromDate, String toDate) {
		LocalDateTime fromDateTime = dateToLocalDateTime(convertStringToDate(fromDate));
		LocalDateTime toDateTime = dateToLocalDateTime(convertStringToDate(toDate));
		return ChronoUnit.DAYS.between(fromDateTime, toDateTime);
	}

	public static Long dateDifferenceInDays(Date fromDate, Date toDate) {
		if (fromDate != null && toDate != null) {
			LocalDateTime fromDateTime = dateToLocalDateTime((fromDate));
			LocalDateTime toDateTime = dateToLocalDateTime((toDate));
			return ChronoUnit.DAYS.between(fromDateTime, toDateTime);
		}
		return null;
	}

	public static Date convertStringToDateTimeWithSecsPeriod(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat("dd/MM/yyyy H:mm:ss a");
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				/***
				 * error
				 */
			}
		}
		return null;
	}

	public static Date convertStringToDate1(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Date convertStringToDate2(String dateValue) {
		if (StringUtils.isNotEmpty(dateValue)) {
			DateFormat dfFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			try {
				return dfFormat.parse(dateValue);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String getFinancialYear(Date date) {
		Calendar today = Calendar.getInstance();
		today.setTime(date);

		if (today.get(Calendar.MONTH) <= 2) {
			return (today.get(Calendar.YEAR) - 1) + "-" + (today.get(Calendar.YEAR));
		} else {
			return today.get(Calendar.YEAR) + "-" + (today.get(Calendar.YEAR) + 1);
		}
	}

	
	public static String getPreviousYear(Date date) {
		Calendar today = Calendar.getInstance();
		today.setTime(date);
		today.set(Calendar.MONTH,Calendar.APRIL);
		today.add(Calendar.YEAR, -1);

		if (today.get(Calendar.MONTH) <= 2) {
			return (today.get(Calendar.YEAR) - 1) + "-" + (today.get(Calendar.YEAR));
		} else {
			return today.get(Calendar.YEAR) + "-" + (today.get(Calendar.YEAR) + 1);
		}
	}

	public static String currentMonthName() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM");
		return sdf.format(new Date());
	}
	
	public static String GSTInvoiceMonthCode() {
		Map<Integer, String> map = new HashMap<>();
		//coding is based on financial year
		map.put(4, "A");
		map.put(5, "B");
		map.put(6, "C");
		map.put(7, "D");
		map.put(8, "E");
		map.put(9, "F");
		map.put(10, "G");
		map.put(11, "H");
		map.put(12, "I");
		map.put(1, "J");
		map.put(2, "K");
		map.put(3, "L");
		Calendar today = Calendar.getInstance();
		return map.get(today.get(Calendar.MONTH)+1);
	}

	public static Date stringToDateYYYYMMDDHHMMSSHyphen(String valueOf) {
		if (StringUtils.isNotEmpty(valueOf)) {
			DateFormat dfFormat = new SimpleDateFormat("YYYY_MM_DD_HHMMSS_HYPHEN");
			try {
				return dfFormat.parse(valueOf);
			} catch (ParseException e) {
				System.out.println("Parse Exception::" + e);
			}
		}
		return null;
	}

	public static String getFinancialQuarterIdentifier(Date date) {
		Calendar today = Calendar.getInstance();
		today.setTime(date);

		if (today.get(Calendar.MONTH) <= 2) { // jan-mar
			return "Q4";
		} else if (today.get(Calendar.MONTH) <= 5) { // apr-jun
			return "Q1";
		} else if (today.get(Calendar.MONTH) <= 8) { // jul-sep
			return "Q2";
		} else { // oct-dec
			return "Q3";
		}
	}

	static final String input_date_format = "yyyy-MM-dd HH:mm:ss.S";
	static final String output_date_format = "dd-MM-yyyy";

	public static String convertObjectToDate(Object ob) {
		if (ob != null) {
			try {
				String value = String.valueOf(ob);
				if (value != null && !value.equalsIgnoreCase("null") && !value.trim().isEmpty()) {
					DateFormat inputDateFormat = new SimpleDateFormat(input_date_format);
					DateFormat outputDateFormat = new SimpleDateFormat(output_date_format);
					Date parsedDate = inputDateFormat.parse(value);
					return outputDateFormat.format(parsedDate);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Date convertStringYYYYMMDDTHHMMSSSSZ1(String date) {
		String[] split = date.split("T");

		return DateUtils.convertStringToDateYYYYMMDD(split[0]);

	}
	
	public static String uniqueNo() {
		return new SimpleDateFormat("yyMdkkmmssSSS").format(sysDate());
	}
}
