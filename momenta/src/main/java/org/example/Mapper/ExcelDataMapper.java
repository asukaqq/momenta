package org.example.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.entity.ExcelData;

import java.util.List;


@Mapper
public interface ExcelDataMapper {
    void insertExcelDataBatch(List<ExcelData> excelDataList);
}