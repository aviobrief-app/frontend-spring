package com.petkov.spr_final_1.web;

import com.petkov.spr_final_1.model.binding.test.SubmitTestBindingModel;
import com.petkov.spr_final_1.model.app_entity.ActiveTestEntity;
import com.petkov.spr_final_1.service.CompletedTestService;
import com.petkov.spr_final_1.utils.ActiveTestTransporter;
import com.petkov.spr_final_1.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tests")
public class TestController {

    private final TestService testService;
    private final CompletedTestService completedTestService;

    //todo ActiveTestTransporter - move in the service?
    private final ActiveTestTransporter activeTestTransporter;

    public TestController(TestService testService, CompletedTestService completedTestService, ActiveTestTransporter activeTestTransporter) {
        this.testService = testService;
        this.completedTestService = completedTestService;
        this.activeTestTransporter = activeTestTransporter;
    }


    @GetMapping("/upcoming")
    private String viewAllUpcomingTests(Model model) {

        //todo - make getAllUpcomingTestsView return Upcoming tests, not all tests
        model.addAttribute("upcomingTests", testService.getAllUpcomingTestsView());

        return "tests-upcomming";
    }

    @GetMapping("/active/{id}")
    public String activeTestShow(@PathVariable Long id, Model model) {

        ActiveTestEntity activeTestEntity = testService.buildActiveTest(id);

        model.addAttribute("activeTestEntity", activeTestEntity);

        model.addAttribute("submitTestBindingModel", new SubmitTestBindingModel());

        activeTestTransporter.setActiveTestViewModel(activeTestEntity);

        return "tests-active";
    }

    @PostMapping("/active/{id}")
    public ModelAndView activeTestSubmit(@ModelAttribute("submitTestBindingModel") SubmitTestBindingModel submitTestBindingModel,
                                   BindingResult bindingResult,
                                   ModelAndView modelAndView,
                                   RedirectAttributes redirectAttributes) {


        if(!bindingResult.hasErrors()){

            ActiveTestEntity activeTestEntity = activeTestTransporter.getActiveTestViewModel();

            redirectAttributes.addFlashAttribute("testResult",completedTestService
                    .scoreAndArchiveTest(activeTestEntity, submitTestBindingModel));

            modelAndView.setViewName("redirect:/tests/result");

            //todo activeTestSubmit() debugPoint
            System.out.println();
        } else{
            //todo - activeTestSubmit - handle SubmitTestBindingModel errors
        }


        return modelAndView;
    }

    @GetMapping("/result")
    public String showTestResult(Model model) {

        //todo showTestResult debug
        System.out.println();


        return "tests-result";
    }




    @GetMapping("/add")
    public String addTest() {
        return "tests-add";
    }

}
