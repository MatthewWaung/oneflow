package com.oneflow.prm.core.utils;

import com.oneflow.prm.core.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;

/**
 * 通用 http发送方法
 *
 * 参考 ruoyi
 */
public class HttpUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 向指定 URL 发送 GET 请求
     *
     * @param url 请求 URL
     * @return 响应结果
     */
    public static String sendGet(String url) {
        return sendGet(url, StringUtils.EMPTY);
    }

    /**
     * 向指定 URL 发送带参数的 GET 请求
     *
     * @param url   请求 URL
     * @param param 请求参数，name1=value1&name2=value2 形式。
     * @return 响应结果
     */
    public static String sendGet(String url, String param) {
        return sendGet(url, param, Constants.UTF8);
    }

    /**
     * 向指定 URL 发送 GET方法的请求
     *
     * @param url         发送请求的 URL
     * @param param       请求参数，name1=value1&name2=value2 形式。
     * @param contentType 编码类型
     * @return 响应结果
     */
    public static String sendGet(String url, String param, String contentType) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = StringUtils.isNotBlank(param) ? url + "?" + param : url;
            log.info("sendGet - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接，这里强转为具体HTTP请求的HttpsURLConnection，方便添加一些参数
            HttpsURLConnection connection = (HttpsURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 设置连接主机服务器的超时时间
            connection.setConnectTimeout(5000);
            // 设置读取远程返回的数据时间
            connection.setReadTimeout(10000);
            // 建立实际的连接
            connection.connect();
            // 获取响应状态码
            int responseCode = connection.getResponseCode();
            // 定义 BufferedReader 输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), contentType));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            log.info("response code - {}", responseCode);
        } catch (Exception e) {
            log.error("调用HttpsUtil.sendGet Exception, url=" + url + ",param=" + param, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    /**
     * 向指定 URL 发送 POST 请求
     *
     * @param url   请求URL
     * @param param 请求参数，与contentType有关，可以说json或表单（name1=value1&name2=value2）形式等
     * @return 响应结果
     */
    public static String sendPost(String url, String param) {
        return sendPost(url, param, Constants.CONTENT_TYPE_JSON);
    }

    /**
     * 向指定 URL 发送 POST方法的请求
     *
     * @param url         请求URL
     * @param param       请求参数，与contentType有关，可以说json或表单形式等
     * @param contentType 请求参数类型
     * @return 响应结果
     */
    public static String sendPost(String url, String param, String contentType) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            log.info("sendPost - {}", url);
            URL realUrl = new URL(url);
            // 这里强转为具体HTTP请求的HttpsURLConnection，方便添加一些参数
            HttpsURLConnection connection = (HttpsURLConnection) realUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("Content-Type", contentType);
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(60000);
            // 发送post请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            out = new PrintWriter(connection.getOutputStream());
            // 发送请求使用输出流将参数传递出去，接收响应结果使用输入流接收数据
            out.print(param);
            out.flush();
            // 获取响应状态码
            int responseCode = connection.getResponseCode();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            log.info("response code - {}", responseCode);
            log.info("receive - {}", result);
        } catch (Exception e) {
            log.error("调用HttpsUtil.sendPost Exception, url=" + url + ",param=" + param, e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    public static String sendSSLPost(String url, String param) {
        StringBuilder result = new StringBuilder();
        String urlNameString = url + "?" + param;
        try {
            log.info("sendSSLPost - {}", urlNameString);
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
            URL console = new URL(urlNameString);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Content-Type", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String ret = "";
            while ((ret = br.readLine()) != null) {
                if (ret != null && !"".equals(ret.trim())) {
                    result.append(new String(ret.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
                }
            }
            log.info("receive - {}", result);
            conn.disconnect();
            br.close();
        } catch (ConnectException e) {
            log.error("调用HttpUtil.sendSSLPost ConnectException, url=" + url + ",param=" + param, e);
        } catch (SocketTimeoutException e) {
            log.error("调用HttpUtil.sendSSLPost SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            log.error("调用HttpUtil.sendSSLPost IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            log.error("调用HttpsUtil.sendSSLPost Exception, url=" + url + ",param=" + param, e);
        }
        return result.toString();
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
