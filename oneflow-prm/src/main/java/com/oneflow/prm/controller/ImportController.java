package com.oneflow.prm.controller;

import com.oneflow.prm.core.common.R;
import com.oneflow.prm.core.constant.RedisKeyConstants;
import com.oneflow.prm.core.utils.MD5Util;
import com.oneflow.prm.entity.vo.response.order.CreditItemVO;
import com.oneflow.prm.service.IImportService;
import io.swagger.v3.oas.annotations.Operation;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;

/**
 * 此类用于统一格式的导入数据，统一格式的导入结果返回
 */
@RestController
@RequestMapping(value = "/import")
public class ImportController {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private IImportService importService;


    @Operation(summary = "贷项凭证订单导入")
    @PostMapping("/importDXOrder")
    public R<CreditItemVO> importDXOrder(@RequestParam("file") MultipartFile file) throws IOException {

        // 1. 对file进行md5加密，之后分布式锁控制同一文件同一时间只能有一个处理
        String fileKey = RedisKeyConstants.CREDIT_ITEM_IMPORT + MD5Util.md5(Arrays.toString(file.getBytes()));

        // 2. 加锁处理
        RLock lock = redissonClient.getLock(fileKey);
        if (lock.tryLock()) {
            try {
                CreditItemVO creditItemVO = importService.handleCreditOrderImport(file);
                return R.ok(creditItemVO);
            } finally {
                lock.unlock();
            }
        } else {
            return R.fail("文件正在处理中，请稍后再试");
        }
    }

}
