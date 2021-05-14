package com.petkov.spr_final_1.service;

import com.petkov.spr_final_1.model.binding.test.SubmitTestBindingModel;
import com.petkov.spr_final_1.model.app_entity.ActiveTestEntity;
import com.petkov.spr_final_1.model.service.test.CompletedTestServiceModel;

public interface CompletedTestService {

    CompletedTestServiceModel scoreAndArchiveTest(ActiveTestEntity activeTestEntity, SubmitTestBindingModel submitTestBindingModel);

}
