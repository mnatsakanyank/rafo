package com.testaccounting.app;

import com.opencsv.CSVReader;
import com.testaccounting.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class AppRunner implements CommandLineRunner {
    private final CsvService csvService;

    private final TestaccountingConfig config;

    public AppRunner(CsvService csvService, TestaccountingConfig config) {
        this.csvService = csvService;
        this.config = config;
    }

    @Override
    public void run(String... args) throws Exception {
        if(config.isReloadFilesOnStartup()&&args.length>0){
            String csvFolder = args[0];
            Path csvFolderPath = Paths.get(csvFolder);
            if(Files.isDirectory(csvFolderPath)){
                Files.list(csvFolderPath).filter(path -> {
                            return path.toString().endsWith(".csv");
                        }).
                        forEach(this::_processCsv);
            }
        }
    }
    private void _processCsv(Path csvFile){
        String fileName = csvFile.getFileName().toString();
        if(fileName.startsWith("EmployeeSalary")){
            try(CSVReader reader = new CSVReader(Files.newBufferedReader(csvFile))){
                csvService.parseCsvAndSaveEmployeesAndSalaries(fileName, reader);
            }catch (Exception e){ throw new RuntimeException(e);}
        }else if(fileName.equals("HistoryExchangeReport.csv")){
            try(CSVReader reader = new CSVReader(Files.newBufferedReader(csvFile))){
                csvService.parseCsvAndSaveCurrencyRates(reader);
            }catch (Exception e){ throw new RuntimeException(e);}
        }
    }
}
