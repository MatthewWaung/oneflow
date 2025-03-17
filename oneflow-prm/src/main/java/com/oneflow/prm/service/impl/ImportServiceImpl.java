package com.oneflow.prm.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.oneflow.prm.entity.excel.Country;
import com.oneflow.prm.entity.excel.Currency;
import com.oneflow.prm.entity.excel.ForeignRegion;
import com.oneflow.prm.listener.ExcelListener;
import com.oneflow.prm.mapper.ImportMapper;
import com.oneflow.prm.service.IImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ImportServiceImpl implements IImportService {

    @Autowired
    ImportMapper importMapper;


    public void importExcel(MultipartFile file) {

        ExcelListener<Country> excelListener = new ExcelListener<>();
        // 一个文件一个reader
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(file.getInputStream(), Country.class, excelListener).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            // 读取一个sheet
            excelReader.read(readSheet);

            List<Country> excelList = excelListener.getData();
            log.info("excel导入的数据为：{}", excelList);

            importMapper.importExcel(excelList);

            /** 多线程实现导入。通过应用ExecutorService 建立了固定的线程数，然后根据线程数目进行分组，批量依次导入。
             * 一方面可以缓解数据库的压力，另一个面线程数目多了，一定程度会提高程序运行的时间。
            int nThreads = 50;
            int size = enrollStudentEntityList.size();
            ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
            List<Future<Integer>> futures = new ArrayList<Future<Integer>>(nThreads);

            for (int i = 0; i < nThreads; i++) {
                final List<EnrollStudentEntity> EnrollStudentEntityImputList = enrollStudentEntityList.subList(size / nThreads * i, size / nThreads * (i + 1));
                final List<StudentEntity> studentEntityImportList = studentEntityList.subList(size / nThreads * i, size / nThreads * (i + 1));
                final List<AllusersEntity> allusersEntityImportList = allusersEntityList.subList(size / nThreads * i, size / nThreads * (i + 1));

                Callable<Integer> task1 = () -> {
                    studentSave.saveStudent(EnrollStudentEntityImputList,studentEntityImportList,allusersEntityImportList);
                    return 1;
                };
                futures.add(executorService.submit(task1));
            }
            executorService.shutdown();
            if (!futures.isEmpty() && futures != null) {
                System.out.println("导入成功");
            }
            System.out.println("");
            */

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (excelReader != null){
                // 这里千万别忘记关闭
                excelReader.finish();
            }
        }
    }

    public void importForeignRegion(MultipartFile file) {
        ExcelListener<ForeignRegion> excelListener = new ExcelListener<>();
        // 一个文件一个reader
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(file.getInputStream(), ForeignRegion.class, excelListener).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            // 读取一个sheet
            excelReader.read(readSheet);

            List<ForeignRegion> foreignList = excelListener.getData();
            log.info("excel导入的数据为：{}", foreignList);
            log.info("导入的数据条数为：{}", foreignList.size());

            importMapper.importForeignRegion(foreignList);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (excelReader != null){
                // 这里千万别忘记关闭
                excelReader.finish();
            }
        }
    }


    public void importCurrency(MultipartFile file) {
        ExcelListener<Currency> excelListener = new ExcelListener<>();
        // 一个文件一个reader
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(file.getInputStream(), Currency.class, excelListener).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            // 读取一个sheet
            excelReader.read(readSheet);

            List<Currency> currencyList = excelListener.getData();
            log.info("excel导入的数据为：{}", currencyList);
            log.info("导入的数据条数为：{}", currencyList.size());

            importMapper.importCurrency(currencyList);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (excelReader != null){
                // 这里千万别忘记关闭
                excelReader.finish();
            }
        }
    }
}
