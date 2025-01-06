package org.example.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.example.Mapper.ExcelDataMapper;
import org.example.entity.ExcelData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
@Slf4j
public class ExcelDataService {

    // 核心线程数为 5
    // 最大线程数为 10（5 个核心线程 + 5 个救急线程）
    // 当任务无法处理时，抛出 RejectedExecutionException
    private ExecutorService executorService = new ThreadPoolExecutor(
            5,
            10,
            30L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );
    private List<ExcelData> dataList = new ArrayList<>();
    @Autowired
    private ExcelDataMapper excelDataMapper;

    // 自定义监听器，用于处理读取 Excel 时的数据
    class ExcelDataListener extends AnalysisEventListener<ExcelData> {
        private int count = 0;

        @Override
        public void invoke(ExcelData data, AnalysisContext context) {
            // 拼接 k1~kn 列的数据
            StringBuilder descriptionBuilder = new StringBuilder();
            for (int i = 4; i < context.readRowHolder().getCellMap().size() - 1; i++) {
                String key = "k" + (i - 3);
                String value = context.readRowHolder().getCellMap().get(i).toString();
                if (value!= null) {
                    descriptionBuilder.append(key).append(":").append(value).append(",");
                }
            }
            // 去除最后的逗号
            String description = descriptionBuilder.toString().replaceAll(",$", "");
            data.setDescription(description);
            dataList.add(data);
            count++;
            if (count % 1000 == 0) {
                processDataInBatch();
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            if (!dataList.isEmpty()) {
                processDataInBatch();
            }
            log.info("数据读取完成,一共 " +  count + "条数据");
        }

        private void processDataInBatch() {
            List<ExcelData> tempList = new ArrayList<>(dataList);
            dataList.clear();
            executorService.execute(() -> {
                excelDataMapper.insertExcelDataBatch(tempList);
            });
        }
    }

    public void importExcelData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), ExcelData.class, new ExcelDataListener()).sheet().doRead();
        } catch (IOException e) {
            log.error("文件读取错误");
            e.printStackTrace();
        }
    }
}