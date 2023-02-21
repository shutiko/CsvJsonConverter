package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.*;

import java.lang.reflect.Type;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String jsonFileName = "data.json";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(jsonFileName, json);


    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = null;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
             CSVReader csvReader = new CSVReader(bufferedReader)) {
            ColumnPositionMappingStrategy<Employee> columnPositionMappingStrategy = new ColumnPositionMappingStrategy<>();
            columnPositionMappingStrategy.setType(Employee.class);
            columnPositionMappingStrategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(columnPositionMappingStrategy)
                    .build();
            list = csv.parse();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public static <T> String listToJson(List<T> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        String json = gson.toJson(list, listType);

        //System.out.println(json);
        return json;
    }

    public static void writeString(String filename, String jsonString) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename));
        ) {
            JsonObject jsonObject = new JsonObject();

            bufferedWriter.write(jsonString);
            //bufferedWriter.write(String.valueOf(jsonObject.getAsJsonArray(jsonString)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}