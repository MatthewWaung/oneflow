package com.oneflow.prm.mapper;

import com.oneflow.prm.entity.excel.Country;
import com.oneflow.prm.entity.excel.Currency;
import com.oneflow.prm.entity.excel.ForeignRegion;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportMapper {
    void importExcel(List<Country> excelList);

    void importForeignRegion(List<ForeignRegion> foreignList);

    void importCurrency(List<Currency> currencyList);
}
