package com.oneflow.oms.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oneflow.oms.bo.order.OrderExportBO;
import com.oneflow.oms.entity.order.OrderDO;
import com.oneflow.oms.mapper.OrderMapper;
import com.oneflow.oms.service.IExportService;
import com.oneflow.oms.service.IOrderService;
import com.oneflow.oms.vo.request.order.OrderListReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ExportServiceImpl implements IExportService {

    @Resource
    private IOrderService orderService;

    @Resource
    private OrderMapper orderMapper;


    @Override
    public void exportOrderList(OrderListReq req, HttpServletResponse response) {
        /**
         * 由于订单量很大，一次性无法直接导出，所以需要分批分页导出，以下是一个示例，实际开发中需要根据业务情况调整分页大小和导出逻辑
         */
        orderService.getSubTablesCondition(req);
        final int perSheetRows = 200000;
        final int perWriteRows = 50000;
        Page<OrderDO> iPage = new Page<>(1, perWriteRows);
        iPage.setMaxLimit(-1L);

        File tempFile = null;

        try {
            tempFile = File.createTempFile(exportFileInfoDO.getFileName(), ".xlsx");

            //必须放到循环外，否则会刷新流
            ExcelWriter excelWriter = EasyExcel.write(tempFile).build();
            List<OrderExportBO> orderExportBOList = new ArrayList<>();

            Long total = orderMapper.selectCount(orderMapper.buildQuery(req));
            Integer totalCount = Math.toIntExact(total);
            log.info("ExportService exportOrderList totalCount: {}", totalCount);

            Map<Long, String> taxPlanMap = taxPlanService.getTaxPlanMap();

            // 1. 计算导出sheet页数和循环导出次数

            //每一个Sheet存放多少条数据
            Integer sheetDataRows = perSheetRows;
            //每次写入的数据量
            Integer writeDataRows = perWriteRows;
            //计算需要的Sheet数量
            Integer sheetNum = totalCount % sheetDataRows == 0 ? (totalCount / sheetDataRows) : (totalCount / sheetDataRows + 1);
            //计算一般情况下每一个Sheet需要写入的次数(一般情况不包含最后一个sheet,因为最后一个sheet不确定会写入多少条数据)
            Integer oneSheetWriteCount = sheetDataRows / writeDataRows;
            //计算最后一个sheet需要写入的次数
            Integer lastSheetWriteCount = totalCount % sheetDataRows == 0 ? oneSheetWriteCount :
                    (totalCount % sheetDataRows % writeDataRows == 0 ? (totalCount % sheetDataRows / writeDataRows) : (totalCount % sheetDataRows / writeDataRows + 1));

            // 2. 组装导出数据
            for (int i = 0; i < sheetNum; i++) {
                for (int j = 0; j < (i != sheetNum - 1 ? oneSheetWriteCount : lastSheetWriteCount); j++) {

                    iPage.setCurrent(j + 1 + oneSheetWriteCount * i);
                    IPage<OrderDO> selectPage = orderMapper.selectPage(iPage, orderMapper.buildQuery(req));

                    List<OrderDO> OrderDOList = selectPage.getRecords();
                    List<Long> orderIdList = OrderDOList.stream().map(OrderDO::getId).collect(Collectors.toList());

                    if (Func.isNotEmpty(orderIdList)) {
                        Map<Long, List<OmsOrderProductDO>> orderProductMap = orderProductMapper.getByOrderIds(orderIdList).stream().collect(Collectors.groupingBy(OmsOrderProductDO::getOrderId));
                        Map<Long, OmsOrderFinanceDO> orderFinanceMap = orderFinanceMapper.getByOrderIds(orderIdList).stream().collect(Collectors.toMap(OmsOrderFinanceDO::getOrderId, t -> t));
                        Map<Long, List<OmsOrderDeliveryDO>> orderDeliveryMap = orderDeliveryMapper.getByOrderId(orderIdList, true).stream().collect(Collectors.groupingBy(OmsOrderDeliveryDO::getOrderId));
                        Map<Long, OmsOrderReceiverDO> orderReceiverMap = orderReceiverMapper.getByOrderIds(orderIdList).stream().collect(Collectors.toMap(OmsOrderReceiverDO::getOrderId, t -> t));

                        orderExportBOList.clear();

                        for (OrderDO order : OrderDOList) {
                            OrderExportBO orderExportBO = OmsOrderConvert.INSTANCE.orderDOListToExportBO(order);
                            orderExportBO.setTaxPlan(Func.isNotEmpty(order.getTaxPlanId()) ? taxPlanMap.get(order.getTaxPlanId()) : null);

                            OmsOrderFinanceDO orderFinanceDO = orderFinanceMap.getOrDefault(order.getId(), new OmsOrderFinanceDO());
                            List<OmsOrderDeliveryDO> orderDeliveryDOList = orderDeliveryMap.get(order.getId());
                            OmsOrderReceiverDO receiverDO = orderReceiverMap.getOrDefault(order.getId(), new OmsOrderReceiverDO());

                            setFinanceInfo(orderExportBO, orderFinanceDO);
                            setReceiverInfo(orderExportBO, receiverDO);
                            setDeliveryInfo(orderExportBO, orderDeliveryDOList);

                            List<OmsOrderProductDO> omsOrderProductDOList = orderProductMap.getOrDefault(order.getId(), Collections.emptyList());

                            if (CollectionUtils.isEmpty(omsOrderProductDOList)) {
                                orderExportBOList.add(orderExportBO);
                            } else {
                                omsOrderProductDOList.forEach(omsOrderProductDO -> {
                                    OrderExportBO orderExportProductBO = BeanUtil.copyProperties(orderExportBO, orderExportBO.getClass());
                                    setOrderProductInfo(orderExportProductBO, omsOrderProductDO);
                                    orderExportBOList.add(orderExportProductBO);
                                });
                            }
                        }

                        WriteSheet writeSheet = EasyExcel.writerSheet(i, "order" + (i + 1))
                                .head(OrderExportBO.class)
                                .includeColumnFieldNames(req.getColumnList())
                                .build();
                        excelWriter.write(orderExportBOList, writeSheet);
                    }
                }
            }
            excelWriter.finish();

            // 3. 将导出的文件上传到服务器
            uploadAndSetFileUrl(tempFile, exportFileInfoDO);

        } catch (Exception e) {
            log.error("exportOrderList fail: ", e);
            exportFileMapper.updateById(exportFileInfoDO.setStatus("2"));
            throw new RuntimeException(e);
        } finally {
            if (tempFile != null) {
                tempFile.delete();
            }
        }
    }


}
