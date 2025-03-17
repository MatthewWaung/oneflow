package com.oneflow.prm.controller;

import com.oneflow.prm.service.impl.ImportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/import")
public class ExcelImportController {

    @Autowired
    ImportServiceImpl importServiceImpl;

    @PostMapping("/importCountry")
    public void importCountry(@RequestParam("file") MultipartFile file){
        long start = System.currentTimeMillis();
        System.out.println(file.getOriginalFilename());
        importServiceImpl.importExcel(file);
        System.out.println("导入excel数据到数据库用时：" + (System.currentTimeMillis() - start) + "ms");

    }

    @PostMapping("/importForeignRegion")
    public void importForeignRegion(@RequestParam("file") MultipartFile file){
        long start = System.currentTimeMillis();
        System.out.println(file.getOriginalFilename());
        importServiceImpl.importForeignRegion(file);
        System.out.println("导入excel数据到数据库用时：" + (System.currentTimeMillis() - start) + "ms");

    }

    @PostMapping("/importCurrency")
    public void importCurrency(@RequestParam("file") MultipartFile file){
        long start = System.currentTimeMillis();
        System.out.println(file.getOriginalFilename());
        importServiceImpl.importCurrency(file);
        System.out.println("导入excel数据到数据库用时：" + (System.currentTimeMillis() - start) + "ms");

    }

}
