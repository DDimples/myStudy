package com.mystudy.web.common.util;

import java.lang.Character.UnicodeBlock;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class StringUtil extends StringUtils {

	/**
	 * char '_'(\u005f)
	 */
	public static final char UNDERLINE = '_';

	/**
	 * 是否为汉字 汉字范围为 19968 - 40869
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isChineseCharacter(String src) {
		boolean ret = true;
		if (src == null || src.trim().length() == 0) {
			return ret;
		}

		char[] charArr = src.toCharArray();
		for (int i = 0; i < charArr.length; i++) {
			char ch = charArr[i];
			if (ch < 19968 || ch > 40869) {
				ret = false;
				break;
			}
		}
		return ret;
	}

	/**
	 * 获取字符长度：汉、日、韩文字符长度为2，ASCII码等字符长度为1
	 * 
	 * @param c
	 *            字符
	 * @return 字符长度
	 */
	@SuppressWarnings("unused")
	private static int getSpecialCharLength(char c) {
		if (isLetter(c)) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
	 * 
	 * @param c, 需要判断的字符
	 * @return boolean, 返回true,Ascill字符
	 */
	private static boolean isLetter(char c) {
		return c >> 7 == 0;
	}

	/**
	 * 去除回车换行符
	 * 
	 * @param src
	 * @return
	 */
	public static String deleteReturn(String src) {
		if (src == null || src.trim().length() == 0) {
			return "";
		}
		src = src.replaceAll("\r", " ");
		src = src.replaceAll("\n", " ");
		src = src.replaceAll("\t", " ");
		return src;
	}

	/**
	 * 替换特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceCharacter(String str) {
		if (str == null || str.trim().length() == 0) {
			return "";
		}
		str = str.replaceAll("\r", "");
		str = str.replaceAll("\n", "");
		str = str.replaceAll("\t", "");
		return str;
	}

	public static boolean containChineseChar(String str) {
		if (str == null || str.trim().length() == 0) {
			return false;
		}
		char[] charArr = str.toCharArray();
		for (int i = 0; i < charArr.length; i++) {
			if (!isLetter(charArr[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 过滤掉字符串中的特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static String specialCharacterfilter(String str) {
		if (str == null) {
			return null;
		}
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\\\\\ ]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	public static String filterSpecialChar4Xss(String xssStr) {
		if (xssStr == null) {
			return null;
		}

		xssStr = xssStr.replaceAll("[''\\[\\]<>/“\\\\\\ ]", "");
		return xssStr;
	}

	private static final Pattern PHONE_NUMBER = Pattern.compile("^1[0-9]{10}$");

	/**
	 * 校验手机号是否正确
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPhoneNumber(String str) {
		return str == null ? false : PHONE_NUMBER.matcher(str).matches();
	}

	private static final Pattern ALL_DIGIT = Pattern.compile("^\\d*$");

	public static boolean isInteger(String str) {
		return ALL_DIGIT.matcher(str).matches();
	}

	/**
	 * 获取字符串里的第一个字符
	 */
	public static String getFirstCharacter(String value) {
		return value.substring(0, 1);
	}

	// 身份证正则表达式[15位或18位]
	private static Pattern idNumber = Pattern
			.compile("(^[1-9]\\d{13}[0-9a-zA-Z]$)|(^[1-9]\\d{16}[0-9a-zA-Z]$)");

	private static int getDaysInMonth(int year, int month) {

		int days = 0;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			days = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			days = 30;
			break;
		case 2:
			if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
				days = 29;
			} else { // 不是闰年
				days = 28;
			}
			break;
		default:
			break;
		}

		return days; // 返回一个月份的天数
	}

	/**
	 * 验证身份证的合法性
	 * 
	 * @param idCardNumber
	 * @return boolean
	 */
	public static boolean isIDCard(String idCardNumber) {

		boolean flag = true; // 验证身份证的合法性的标志位
		if (!idNumber.matcher(idCardNumber).matches()) { // 获取解密后的IdNumber并进行合法性校验
			flag = false; // 身份证格式不合法
		} else { // 身份证格式合法, 判断出生日期
			String year = "", month = "", day = "";
			int intYear = 0, intMonth = 0, intDay = 0, days = 0;

			if ((15 == idCardNumber.length())) {
				year = idCardNumber.substring(6, 8);
				month = idCardNumber.substring(8, 10);
				day = idCardNumber.substring(10, 12);
				intYear = Integer.parseInt(year) + 1900;
				intMonth = Integer.parseInt(month);
				intDay = Integer.parseInt(day);
				days = getDaysInMonth(intYear, intMonth);
				if ((intMonth > 12) || (intDay > days)) {
					flag = false; // 输入的出生日期不正确
				}
			} else { // 18 == decryptIdNumber.length()
				Calendar cal = Calendar.getInstance();
				int nowYear = cal.get(Calendar.YEAR);
				year = idCardNumber.substring(6, 10);
				month = idCardNumber.substring(10, 12);
				day = idCardNumber.substring(12, 14);
				intYear = Integer.parseInt(year);
				intMonth = Integer.parseInt(month);
				intDay = Integer.parseInt(day);
				days = getDaysInMonth(intYear, intMonth);
				if ((intYear > nowYear) || (intMonth > 12) || (intDay > days)) {
					flag = false; // 输入的出生日期不正确
				}
			}
		}

		return flag; // 返回验证身份证的合法性的标志位
	}

	public static String utf8ToUnicode(String inStr) {
		char[] cs = inStr.toCharArray();
		StringBuffer sb = new StringBuffer(cs.length);
		for (char c : cs) {
			UnicodeBlock ub = UnicodeBlock.of(c);
			if (ub == UnicodeBlock.BASIC_LATIN) {
				// 英文及数字等
				sb.append(c);
			} else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
				// 全角半角字符
				int j = (int) c - 65248;
				sb.append((char) j);
			} else {
				// 汉字
				String hexS = Integer.toHexString(c);
				sb.append("\\u").append(hexS);
			}
		}
		return sb.toString();
	}

	public static String utf8ChineseToUnicode(String inStr) {
		char[] cs = inStr.toCharArray();
		StringBuffer sb = new StringBuffer(cs.length);
		for (char c : cs) {
			UnicodeBlock ub = UnicodeBlock.of(c);
			if (ub == UnicodeBlock.BASIC_LATIN) {
				// 英文及数字等
				sb.append(c);
			} else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
				// 全角半角字符
				sb.append(c);
			} else {
				// 汉字
				String hexS = Integer.toHexString(c);
				sb.append("\\u").append(hexS);
			}
		}
		return sb.toString();
	}

	public static String unicodeToUtf8(String source) {
		char aChar;
		char[] cs = source.toCharArray();
		int len = cs.length;
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = cs[x++];
			if (aChar == '\\') {
				aChar = cs[x++];
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = cs[x++];
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + aChar - ('a' - 10);
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + aChar - ('A' - 10);
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed /uxxxx encoding:ascii["
											+ (int) aChar + "]");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

	/**
	 * change \\uxxxx to a single char, ignore \\t,\\n etc.
	 * 
	 * @param source
	 * @return
	 */
	public static String unicodeChineseToUtf8(String source) {
		char aChar;
		char[] cs = source.toCharArray();
		int len = cs.length;
		StringBuffer outBuffer = new StringBuffer(len);
		int x = 0;
		while (x < len) {
			aChar = cs[x++];
			if (aChar == '\\') {
				aChar = cs[x++];
				if (aChar == 'u') {
					// Read the xxxx
					outBuffer.append(unicodeToChar(cs[x++], cs[x++], cs[x++],
							cs[x++]));
				} else {
					outBuffer.append('\\').append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

	private static char unicodeToChar(char c1, char c2, char c3, char c4) {
		return (char) ((radixF(c1) << 12) + (radixF(c2) << 8)
				+ (radixF(c3) << 4) + radixF(c4));
	}

	private static int radixF(char c) {
		switch (c) {
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			return c - '0';
		case 'a':
		case 'b':
		case 'c':
		case 'd':
		case 'e':
		case 'f':
			return c - ('a' - 10);
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
			return c - ('A' - 10);
		default:
			throw new IllegalArgumentException(
					"Malformed /uxxxx encoding:ascii[" + (int) c + "]");
		}
	}

	public static String trimEnd(String str, char c) {
		if (StringUtil.isEmpty(str))
			return StringUtil.EMPTY;
		int index = str.length() - 1;
		if (str.charAt(index) == c)
			str = str.substring(0, index);
		return str;
	}

	/**
	 * join strings with {@link #UNDERLINE StringUtil.UNDERLINE}
	 * 
	 * @param strs
	 * @return
	 */
	public static String joinString(String... strs) {
		return joinString(UNDERLINE, strs);
	}

	/**
	 * join strings with joinChar
	 * 
	 * @param joinChar
	 * @param strs
	 * @return
	 */
	public static String joinString(char joinChar, String... strs) {
		if (strs.length == 0)
			return StringUtil.EMPTY;
		StringBuilder joinStr = new StringBuilder(
				Math.max(strs.length << 3, 16));
		for (String str : strs) {
			if (StringUtil.isNotEmpty(str))
				joinStr.append(joinChar).append(str);
		}
		if (StringUtil.isNotEmpty(joinStr))
			return joinStr.substring(1);
		return StringUtil.EMPTY;
	}

	/**
	 * Copy the given Collection into a String array. The Collection must
	 * contain String elements only.
	 * 
	 * @param collection
	 *            the Collection to copy
	 * @return the String array ({@code null} if the passed-in Collection was
	 *         {@code null})
	 */
	public static String[] toStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return collection.toArray(new String[collection.size()]);
	}

	/**
	 * 给定一个内容和正则串返回匹配的子串组
	 * 
	 * @param content
	 *            (匹配内容)
	 * @param regX
	 *            （正则串）
	 * @return
	 */
	public static List<Map<Integer, String>> getRegMatchList(String content,
			String regX) {
		if (content == null || content.equals(""))
			return null;
		if (regX == null || regX.equals(""))
			return null;

		List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
		Map<Integer, String> mp = null;
		Matcher m = Pattern.compile(regX).matcher(content);
		while (m.find()) {
			mp = new HashMap<Integer, String>();

			int gc = m.groupCount();
			if (gc > 0) {
				for (int k = 1; k <= gc; k++) {
					mp.put(k, m.group(k));
				}
				list.add(mp);
			}
		}
		return list;
	}

	/**
	 * 功能描述：验证URL的合法性
	 * 
	 * @param url
	 *            需要验证的URL
	 * @return 返回：合法的URL则返回true，否则返回false
	 */
	public static boolean validateURL(String url) {
		if (isEmpty(url))
			return false;
		Pattern p = Pattern.compile(RegexpConstants.URL_REGEXP);
		Matcher m = p.matcher(url);
		if (m.matches())
			return true;

		return false;
	}

	/**
	 * 功能描述：验证的合法性
	 * 
	 * @param url
	 *            需要验证的URL
	 * @param regexp
	 *            正则串
	 * @return 返回：则返回true，否则返回false
	 */
	public static boolean validate(String url, final String regexp) {
		if (isEmpty(url) || isEmpty(regexp))
			return false;
		Pattern p = Pattern.compile(regexp);
		Matcher m = p.matcher(url);
		if (m.matches())
			return true;

		return false;
	}

	public static boolean match(final String orginalStr,
			final String modelStr[]) {
		boolean flag = false;
		for (String str : modelStr) {
			if (StringUtil.contains(orginalStr, str)) {
				flag = true;
				break;
			}
		}
		return flag;

	}
}
