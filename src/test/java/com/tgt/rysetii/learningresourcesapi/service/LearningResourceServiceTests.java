package com.tgt.rysetii.learningresourcesapi.service;

import com.tgt.rysetii.learningresourcesapi.entity.LearningResource;
import com.tgt.rysetii.learningresourcesapi.entity.LearningResourceStatus;
import com.tgt.rysetii.learningresourcesapi.repository.LearningResourceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LearningResourceServiceTests {

    @Mock
    LearningResourceRepository learningResourceRepository;

    @InjectMocks
    LearningResourceService learningResourceService;

    @Test
    void saveLearningResourcesTest() {
        List<LearningResource> learningResources = new ArrayList<>();
        LearningResource learningResource1 = new LearningResource(1234, "Test ResourceName 1", 111.0, 150.0, LearningResourceStatus.LIVE, LocalDate.now(), LocalDate.now().plusMonths(5), LocalDate.now().plusYears(2));
        LearningResource learningResource2 = new LearningResource(1111, "Test ResourceName 2", 220.0, 300.0, LearningResourceStatus.LIVE, LocalDate.now(), LocalDate.now().plusMonths(6), LocalDate.now().plusYears(3));
        LearningResource learningResource3 = new LearningResource(1515, "Test ResourceName 3", 150.0, 200.0, LearningResourceStatus.LIVE, LocalDate.now(), LocalDate.now().plusMonths(6), LocalDate.now().plusYears(3));

        learningResources.add(learningResource1);
        learningResources.add(learningResource2);
        learningResources.add(learningResource3);

        learningResourceService.saveLearningResources(learningResources);
        verify(learningResourceRepository, times(learningResources.size())).save(any(LearningResource.class));

    }

    @Test
    void getProfitMarginsTest() {
        List<LearningResource> learningResources = new ArrayList<>();
        LearningResource learningResource1 = new LearningResource(1234, "Test ResourceName 1", 111.0, 150.0, LearningResourceStatus.LIVE, LocalDate.now(), LocalDate.now().plusMonths(5), LocalDate.now().plusYears(2));
        LearningResource learningResource2 = new LearningResource(1111, "Test ResourceName 2", 220.0, 300.0, LearningResourceStatus.LIVE, LocalDate.now(), LocalDate.now().plusMonths(6), LocalDate.now().plusYears(3));
        LearningResource learningResource3 = new LearningResource(1515, "Test ResourceName 3", 150.0, 200.0, LearningResourceStatus.LIVE, LocalDate.now(), LocalDate.now().plusMonths(6), LocalDate.now().plusYears(3));

        learningResources.add(learningResource1);
        learningResources.add(learningResource2);
        learningResources.add(learningResource3);

        List<Double> expectedProfitMargins = learningResources.stream().map(e -> ((e.getSellingPrice()-e.getCostPrice())/e.getSellingPrice())).collect(Collectors.toList());
        when(learningResourceRepository.findAll()).thenReturn(learningResources);
        List<Double> actualProfitMargins = learningResourceService.getProfitMargin();
        assertEquals(expectedProfitMargins, actualProfitMargins, "Wrong profit margins");
    }

    @Test
    void sortLearningResourcesByProfitMarginInNonIncreasingOrderTest() {
        List<LearningResource> learningResources = new ArrayList<>();
        LearningResource learningResource1 = new LearningResource(1234, "Test ResourceName 1", 111.0, 150.0, LearningResourceStatus.LIVE, LocalDate.now(), LocalDate.now().plusMonths(5), LocalDate.now().plusYears(2));
        LearningResource learningResource2 = new LearningResource(1111, "Test ResourceName 2", 220.0, 300.0, LearningResourceStatus.LIVE, LocalDate.now(), LocalDate.now().plusMonths(6), LocalDate.now().plusYears(3));
        LearningResource learningResource3 = new LearningResource(1515, "Test ResourceName 3", 150.0, 200.0, LearningResourceStatus.LIVE, LocalDate.now(), LocalDate.now().plusMonths(6), LocalDate.now().plusYears(3));

        learningResources.add(learningResource1);
        learningResources.add(learningResource2);
        learningResources.add(learningResource3);

        learningResources.sort((lr1, lr2) -> {
            Double profitMargin1 = (lr1.getSellingPrice() - lr1.getCostPrice())/lr1.getSellingPrice();
            Double profitMargin2 = (lr2.getSellingPrice() - lr2.getCostPrice())/lr2.getSellingPrice();

            return profitMargin2.compareTo(profitMargin1) ;
        });

        when(learningResourceRepository.findAll()).thenReturn(learningResources);

        List<LearningResource> learningResourcesSorted = learningResourceService.sortLearningResourcesByProfitMargin();

        assertEquals(learningResources, learningResourcesSorted, "The learning resources are not sorted by profit margin");
    }

    @Test
    void deleteLearningResourceByIdTest() {
        int resourceId = 1234;
        learningResourceRepository.deleteById(resourceId);
        verify(learningResourceRepository, times(1)).deleteById(resourceId);
    }
}