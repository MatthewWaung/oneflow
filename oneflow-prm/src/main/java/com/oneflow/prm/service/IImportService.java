package com.oneflow.prm.service;

import com.oneflow.prm.entity.vo.response.order.CreditItemVO;
import org.springframework.web.multipart.MultipartFile;

public interface IImportService {

    void importExcel(MultipartFile file);

    void importForeignRegion(MultipartFile file);

    void importCurrency(MultipartFile file);

    /**
     *
     * @param file
     * @return
     */
    CreditItemVO handleCreditOrderImport(MultipartFile file);

}
