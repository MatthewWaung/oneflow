package com.oneflow.prm.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.oneflow.prm.core.constant.Constants;
import com.oneflow.prm.core.utils.EasyExcelUtils;
import com.oneflow.prm.core.utils.ExcelUtil;
import com.oneflow.prm.entity.excel.Country;
import com.oneflow.prm.entity.excel.Currency;
import com.oneflow.prm.entity.excel.ForeignRegion;
import com.oneflow.prm.entity.vo.request.order.CreditOrderExcelReq;
import com.oneflow.prm.entity.vo.request.order.OrderAddReq;
import com.oneflow.prm.entity.vo.response.order.CreditOrderImportVO;
import com.oneflow.prm.entity.vo.response.order.CreditItemVO;
import com.oneflow.prm.listener.ExcelListener;
import com.oneflow.prm.mapper.ImportMapper;
import com.oneflow.prm.service.IImportService;
import com.oneflow.prm.service.validate.CreditOrderValidate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ImportServiceImpl implements IImportService {

    @Autowired
    ImportMapper importMapper;

    @Resource
    private CreditOrderValidate creditOrderValidate;


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


    @Override
    public CreditItemVO handleCreditOrderImport(MultipartFile file) {

        // 贷款凭证导入
        CreditItemVO creditItemVO = new CreditItemVO();

        // 1. 解析excel
        List<CreditOrderExcelReq> creditOrderExcelReqList = parseCreditOrderExcel(file);

        // 2.查询商品数据方便后续校验与使用
//        List<ProductDO> productDOList = findProductBySku(creditOrderExcelReqList);

        // 2. 数据校验
        creditOrderValidate.validate(creditOrderExcelReqList);

        // 3. 组装数据
        Map<String, OrderAddReq> orderAddReqMap = assembleAddOrderData(creditOrderExcelReqList);

        // 4. 批量保存数据
        List<CreditOrderImportVO> CreditOrderImportVOList = batchAddOrder(orderAddReqMap, creditOrderExcelReqList);

        // 5. 处理返回值
        handleReturnData(creditItemVO, CreditOrderImportVOList, creditOrderExcelReqList, file);

        return creditItemVO;
    }

    private void handleReturnData(CreditItemVO creditItemVO, List<CreditOrderImportVO> creditOrderImportVOList, List<CreditOrderExcelReq> creditOrderExcelReqList, MultipartFile file) {

        /**
         * 导入的结果以一个excel展示，在导入excel模板基础上第一列添加 结果 列，如果导入成功，结果为 导入成功；如果导入失败，结果为 失败原因（异常信息）
         */

        List<String> errorList = creditOrderExcelReqList.stream().flatMap(e -> e.getErrors().stream()).collect(Collectors.toList());

        // 设置tip和是否成功标记
        if (CollectionUtils.isNotEmpty(errorList)) {
            creditItemVO.setTip("操作成功，详细结果请下载导入结果进行查看");
            creditItemVO.setPassFlag(Constants.YesOrNoFlag.N.getCode());
        } else {
            creditItemVO.setTip("全部导入成功，详细结果请下载导入结果进行查看");
        }

        // 将校验不通过的数据写入行中，返回excel下载地址
        String fileName = "SKU映射导入结果-" + DateUtil.today() + "-" + RandomUtil.randomString(5);
        String writeToExcelUrl = EasyExcelUtils.extractAndWriteToExcel(creditOrderExcelReqList, fileName, file);
        creditItemVO.setErrorExcelDownloadUrl(writeToExcelUrl);

    }

    private List<CreditOrderImportVO> batchAddOrder(Map<String, OrderAddReq> orderAddReqMap, List<CreditOrderExcelReq> creditOrderExcelReqList) {
        // 申明返回值,并发安全的list
        List<CreditOrderImportVO> orderList = new ArrayList<>();

        // 调用新增订单方法，有异常等错误信息记录到CreditOrderExcelReq中

        return orderList;
    }

    private Map<String, OrderAddReq> assembleAddOrderData(List<CreditOrderExcelReq> creditOrderExcelReqList) {
        // 申明返回值
        Map<String, OrderAddReq> orderAddReqMap = new HashMap<>();

        // 将导入的数据转换为订单请求实体OrderAddReq

        return orderAddReqMap;
    }

    private List<CreditOrderExcelReq> parseCreditOrderExcel(MultipartFile file) {
        return ExcelUtil.parseFirstSheet(CreditOrderExcelReq.class, file, 1);
    }


}
