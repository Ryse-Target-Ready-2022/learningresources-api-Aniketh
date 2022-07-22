package com.tgt.rysetii.learningresourcesapi.service;

import com.tgt.rysetii.learningresourcesapi.entity.LearningResource;
import com.tgt.rysetii.learningresourcesapi.entity.LearningResourceStatus;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LearningResourceService {
    public List<LearningResource> getLearningResources(){
        File file = new File("LearningResources.csv");
        List<LearningResource> learningResources = loadLearningResourceFromCsv(file);
        return learningResources;
    }


    private List<LearningResource> loadLearningResourceFromCsv(File file) {
        List<LearningResource> learningResources = new ArrayList<>();
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line = null;
            line = bufferedReader.readLine();
            while (line!=null){
                String[] attributes = line.split(",");
                LearningResource learningResource = createLearningResource(attributes);
                learningResources.add(learningResource);
                line = bufferedReader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return learningResources;
    }

    private LearningResource createLearningResource(String[] attributes) {
        Integer id = Integer.parseInt(attributes[0].replaceAll("\\D+", ""));
        String resourceName = attributes[1];
        Double costPrice = Double.parseDouble(attributes[2]);
        Double sellingPrice = Double.parseDouble(attributes[3]);
        LearningResourceStatus learningResourceStatus = LearningResourceStatus.valueOf(attributes[4]);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate createdDate = LocalDate.parse(attributes[5],dateTimeFormatter);
        LocalDate publishedDate = LocalDate.parse(attributes[6],dateTimeFormatter);
        LocalDate retiredDate = LocalDate.parse(attributes[7],dateTimeFormatter);
        LearningResource learningResource = new LearningResource(id,resourceName,costPrice,sellingPrice,learningResourceStatus,createdDate,publishedDate,retiredDate);
        return learningResource;
    }
}