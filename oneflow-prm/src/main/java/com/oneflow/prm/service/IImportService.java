package com.oneflow.prm.service;

import org.springframework.web.multipart.MultipartFile;

public interface IImportService {

    void importExcel(MultipartFile file);

    void importForeignRegion(MultipartFile file);

    void importCurrency(MultipartFile file);
}
