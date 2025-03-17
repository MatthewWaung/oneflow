package com.oneflow.prm.feign;

import cn.hutool.core.util.ObjectUtil;
import com.oneflow.prm.entity.vo.request.sap.SapRequestVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

@Slf4j
@Service
public class SapRequestServiceImpl implements SapRequestService {
    private String defaultUrl = "";
    private String defaultCharset = "UTF-8";
//    @Value("${sap.username}")
    @Value("sb-b16946bb-e278-4299-84d9-d24ad4d855ad!b1065|it-rt-dev-jjkl4c9s!b39")
    private String username;    //sb-b16946bb-e278-4299-84d9-d24ad4d855ad!b1065|it-rt-dev-jjkl4c9s!b39

//    @Value("${sap.password}")
    @Value("994ef750-8bdb-4c2d-93e7-e3f95462f8e2$eX2DwDjQQpRpG9ubsuwjtrchkvTl2yR29cBOF6NUZLY=")
    private String password;   //994ef750-8bdb-4c2d-93e7-e3f95462f8e2$eX2DwDjQQpRpG9ubsuwjtrchkvTl2yR29cBOF6NUZLY=

    @Override
    public String requestSap(SapRequestVo reqVo) {
        try {
            //1 拼接url
            String url = reqVo.getUrl();
            if (StringUtils.isEmpty(url)) {
                url = defaultUrl;
            }
            if (ObjectUtil.isNotEmpty(reqVo.getParams())) {
                StringBuilder sb = new StringBuilder(url).append("?");
                reqVo.getParams().forEach((k, v) -> {
                    sb.append(k).append("=").append(v).append("&");
                });
                url = sb.toString();
            }
            //2 处理请求头
            HttpClient httpClient = new HttpClient();
            httpClient.getParams().setContentCharset(defaultCharset);
            PostMethod postMethod = new PostMethod(url);       //相对应的还有GetMethod
            postMethod.addRequestHeader("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString((username + ":" + password).getBytes()));
            if (reqVo.getHeads() != null && reqVo.getHeads().size() > 0) {
                reqVo.getHeads().forEach((k, v) -> postMethod.addRequestHeader(k, v));
            }

            postMethod.setRequestEntity(new StringRequestEntity(reqVo.getReqXml(), "application/xml", defaultCharset));

            log.info("请求SAP报文：{}", reqVo.getReqXml());
            //3 发起请求
            httpClient.executeMethod(postMethod);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = postMethod.getResponseBodyAsStream();
            int len;
            byte[] buf = new byte[1024];
            while((len=in.read(buf))!=-1){
                out.write(buf, 0, len);
            }
            String responseMsg = out.toString(defaultCharset);

            log.info("SAP响应报文：{}", responseMsg);
            return responseMsg;

        } catch (Exception e) {
            log.error("请求SAP失败", e);
        }
        return null;
    }
}
