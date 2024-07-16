package org.example.resumeorg.config;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.resumeorg.entity.Technology;
import org.example.resumeorg.repo.TechnologyRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final TechnologyRepo technologyRepo;
    @Override
    public void run(String... args) throws Exception {
//        getTechnologies();
    }
    public void getTechnologies(){
        String filePath = "C:\\Users\\Бахтиёр Садуллоев\\Desktop\\Java_Projects\\modul_9\\resumeOrg\\src\\main\\resources\\static\\technology\\technologies.xlsx";
        List<Technology> technologies = new ArrayList<>();

        try (
                FileInputStream fis = new FileInputStream(new File(filePath));
                Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getLastRowNum();
            for (int rowIdx = 0; rowIdx <= rowCount; rowIdx++) {
                Row row = sheet.getRow(rowIdx);
                if (row != null && row.getCell(0) != null) {
                    String cellValue = row.getCell(0).getStringCellValue();
                    if (!cellValue.isEmpty()) {
                        Technology technology = Technology.builder()
                                .name(cellValue)
                                .build();
                        technologies.add(technology);
                    }
                }
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        technologyRepo.saveAll(technologies);
    }


}
