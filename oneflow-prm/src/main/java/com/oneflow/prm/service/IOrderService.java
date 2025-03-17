package com.oneflow.prm.service;

import com.oneflow.prm.core.common.R;

import javax.servlet.http.HttpServletResponse;

public interface IOrderService {

    R exportPiFile(HttpServletResponse response, String id, String fileType);

    R exportCnFile(HttpServletResponse response, String id, String fileType);

    R exportDevicePdf(HttpServletResponse response);

    R exportOrderPdf(String id, HttpServletResponse response);

}
