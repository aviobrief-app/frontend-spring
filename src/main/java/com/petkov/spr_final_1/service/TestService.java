package com.petkov.spr_final_1.service;

import com.petkov.spr_final_1.model.app_entity.ActiveTestEntity;
import com.petkov.spr_final_1.model.service.test.TestServiceModel;
import com.petkov.spr_final_1.model.view.TestThumbnailViewModel;

import java.util.List;

public interface TestService {

    ActiveTestEntity buildActiveTest(Long id);

    List<TestThumbnailViewModel> getAllUpcomingTestsView();

    TestServiceModel seedTestToDb(TestServiceModel testServiceModel);

}
