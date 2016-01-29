/**
 * 正则表达常量
 */
package com.mystudy.web.common.util;

public final class RegexpConstants {

	public static final String MAIL_REGEXP = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";  
    public static final String HOMEPHONE_REGEXP = "^(?:0[0-9]{2,3}[-\\s]{1}|\\(0[0-9]{2,4}\\))[0-9]{6,8}$|^[1-9]{1}[0-9]{5,7}$|^[1-9]{1}[0-9]{10}$";  
      
    /** 
     *  
     * 匹配图象 <br> 
     * 格式: /相对路径/文件名.后缀 (后缀为gif,dmp,png) 
     * 匹配 : /forum/head_icon/admini20100830_ff.gif 或 admini20100830.dmp<br> 
     * 不匹配: D:/admins0830.gif 
     */  
    public static final String ICON_REGEXP = "^(/{0,1}\\w){1,}\\.(gif|dmp|png|jpg)$|^\\w{1,}\\.(gif|dmp|png|jpg)$";  
     
    /** 
     *  
     * 匹配email地址 <br> 
     * 格式: XXX@XXX.XXX.XX 
     * 匹配 : test@eye.com 或 test@iteye.com.cn <br> 
     * 不匹配: test@eye 或 $$$@bar.com 
     */  
    public static final String EMAIL_REGEXP = "(?:\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,3}$)";  
     
    /** 
     *  
     * 匹配匹配并提取url <br> 
     * 格式: XXXX://XXX.XXX.XXX.XX/XXX.XXX?XXX=XXX 
     * 匹配 : http://www.javaeyecom 或news://www<br> 
     * 提取(MatchResult matchResult=matcher.getMatch()): 
     * matchResult.group(0)= http://www.iteye.com:8080/index.html?login=true 
     * matchResult.group(1) = http 
     * matchResult.group(2) = www.iteye.com 
     * matchResult.group(3) = :8080 
     * matchResult.group(4) = /index.html?login=true 
     * 不匹配: c:\window 
     */  
    public static final String URL_REGEXP = "(\\w+)://([^/:]+)(:\\d*)?([^#\\s]*)";  
 
    /** 
     *  
     * 匹配并提取http <br> 
     * 格式: http://XXX.XXX.XXX.XX/XXX.XXX?XXX=XXX 或 ftp://XXX.XXX.XXX 或 https://XXX 
     * 匹配 : http://www.iteye.com:8080/index.html?login=true<br> 
     * 提取(MatchResult matchResult=matcher.getMatch()): 
     * matchResult.group(0)= http://www.iteye.com:8080/index.html?login=true 
     * matchResult.group(1) = http 
     * matchResult.group(2) = www.iteye.com 
     * matchResult.group(3) = :8080 
     * matchResult.group(4) = /index.html?login=true 
     * 不匹配: news://www 
     */  
    public static final String HTTP_REGEXP = "(http|https|ftp)://([^/:]+)(:\\d*)?([^#\\s]*)";  
    
    public static final String DOMIN_REGEXP ="(http)://((?:(?:[\\w]+)\\.)+\\w+)/(\\w+)";
 
    /** 
     *  
     * 匹配日期 <br> 
     * 格式(首位不为0): XXXX-XX-XX 或 XXXX XX XX 或 XXXX-X-X <br> 
     * 范围:1900--2099 <br> 
     * 匹配 : 2010-08-04 <br> 
     * 不匹配: 01-01-01 
     */  
    public static final String DATE_REGEXP = "^((((19){1}|(20){1})d{2})|d{2})[-\\s]{1}[01]{1}d{1}[-\\s]{1}[0-3]{1}d{1}$";// 匹配日期  
 
    /** 
     *  
     * 匹配电话 <br> 
     * 格式为: 0XXX-XXXXXX(10-13位首位必须为0) 或0XXX XXXXXXX(10-13位首位必须为0) 或 <br> 
     * (0XXX)XXXXXXXX(11-14位首位必须为0) 或 XXXXXXXX(6-8位首位不为0) 或 
     * XXXXXXXXXXX(11位首位不为0) <br> 
     * 匹配 : 0371-123456 或 (0371)1234567 或 (028)12345678 或 010-123456 或 
     * 010-12345678 或 12345678912 <br> 
     * 不匹配: 1111-134355 或 0123456789 
     s*/  
    public static final String PHONE_REGEXP = "^(?:0[0-9]{2,3}[-\\s]{1}|\\(0[0-9]{2,4}\\))[0-9]{6,8}$|^[1-9]{1}[0-9]{5,7}$|^[1-9]{1}[0-9]{10}$";  
 
    /** 
     *  
     * 匹配邮编代码 <br> 
     * 格式为: XXXXXX(6位) <br> 
     * 匹配 : 123456 <br> 
     * 不匹配: 012345 
     */  
    public static final String ZIP_REGEXP = "^[1-9]{6}$";  
 
    /** 
     * 不包括特殊字符的匹配 (字符串中不包括符号 数学次方号^ 单引号' 双引号" 分号; 逗号, 帽号: 数学减号- 右尖括号> 左尖括号< 反斜杠\ 即空格,制表符,回车符等 )<br> 
     * 格式为: x 或 一个一上的字符 <br> 
     * 匹配 : 012345 <br> 
     * 不匹配: 0123456 
     */  
    public static final String NON_SPECIAL_CHAR_REGEXP = "^[^'\"\\;,:-<>\\s].+$";// 匹配邮编代码  
 
    /** 
     * 匹配非负整数（正整数 + 0) 
     */  
    public static final String NON_NEGATIVE_INTEGERS_REGEXP = "^\\d+$";  
      
    /** 
     * 匹配不包括零的非负整数（正整数 > 0) 
     */  
    public static final String NON_ZERO_NEGATIVE_INTEGERS_REGEXP = "^[1-9]+\\d*$";  
 
    /** 
     * 匹配正整数 
     */  
    public static final String POSITIVE_INTEGER_REGEXP = "^[0-9]*[1-9][0-9]*$";  
 
    /** 
     * 匹配非正整数（负整数 + 0） 
     */  
    public static final String NON_POSITIVE_INTEGERS_REGEXP = "^((-\\d+)|(0+))$";  
 
    /** 
     * 匹配负整数 
     */  
    public static final String NEGATIVE_INTEGERS_REGEXP = "^-[0-9]*[1-9][0-9]*$";  
 
    /** 
     * 匹配整数 
     */  
    public static final String INTEGER_REGEXP = "^-?\\d+$";  
 
    /** 
     * 匹配非负浮点数（正浮点数 + 0） 
     */  
    public static final String NON_NEGATIVE_RATIONAL_NUMBERS_REGEXP = "^\\d+(\\.\\d+)?$";  
 
    /** 
     * 匹配正浮点数 
     */  
    public static final String POSITIVE_RATIONAL_NUMBERS_REGEXP = "^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";  
 
    /** 
     * 匹配非正浮点数（负浮点数 + 0） 
     */  
    public static final String NON_POSITIVE_RATIONAL_NUMBERS_REGEXP = "^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$";  
 
    /** 
     * 匹配负浮点数 
     */  
    public static final String NEGATIVE_RATIONAL_NUMBERS_REGEXP = "^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))$";  
 
    /** 
     * 匹配浮点数 
     */  
    public static final String RATIONAL_NUMBERS_REGEXP = "^(-?\\d+)(\\.\\d+)?$";  
 
    /** 
     * 匹配由26个英文字母组成的字符串 
     */  
    public static final String LETTER_REGEXP = "^[A-Za-z]+$";  
 
    /** 
     * 匹配由26个英文字母的大写组成的字符串 
     */  
    public static final String UPWARD_LETTER_REGEXP = "^[A-Z]+$";  
 
    /** 
     * 匹配由26个英文字母的小写组成的字符串 
     */  
    public static final String LOWER_LETTER_REGEXP = "^[a-z]+$";  
 
    /** 
     * 匹配由数字和26个英文字母组成的字符串 
     */  
    public static final String LETTER_NUMBER_REGEXP = "^[A-Za-z0-9]+$";  
 
    /** 
     * 匹配由数字、26个英文字母或者下划线组成的字符串 
     */  
    public static final String LETTER_NUMBER_UNDERLINE_REGEXP = "^\\w+$";  
      
    /** 
     * 匹配中文 
     */  
    public static final String ZH_REGEXP = "^[\u0391-\uFFE5]+$";  
      
    /** 
     * 匹配手机号码，以13、14、15、18开头的11位号码 
     */  
    public static final String  MOBILE_REGEXP = "(86)*0*1[3,4,5,8]\\d{9}";  	
}
