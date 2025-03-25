package com.oneflow.prm.core.utils;


import java.security.MessageDigest;

/**
 * MD5加密工具类
 *
 * @author hezhao
 * @version v01.00.00 $Revision$
 * @date 2015年4月17日
 * @time 下午5:15:50
 */
public class MD5Util {

    private MD5Util() {
        // 私用构造主法.因为此类是工具类.
    }

    /**
     * 对字符串自行2次MD5加密MD5(MD5(s))
     *
     * @param s
     * @return
     * @author hezhao
     */
    public final static String md5x2(String s) {
        return md5(md5(s));
    }

    /**
     * MD5加密工具类
     *
     * @param s
     * @return
     * @author hezhao
     */
    public final static String md5(String s) {

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

}