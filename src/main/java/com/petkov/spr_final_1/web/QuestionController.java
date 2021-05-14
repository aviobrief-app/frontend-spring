package com.petkov.spr_final_1.web;

import com.petkov.spr_final_1.model.binding.test.QuestionAddBindingModel;
import com.petkov.spr_final_1.model.service.test.QuestionServiceModel;
import com.petkov.spr_final_1.service.ATAChapterService;
import com.petkov.spr_final_1.service.DocumentService;
import com.petkov.spr_final_1.service.QuestionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final ModelMapper modelMapper;
    private final DocumentService documentService;
    private final ATAChapterService ataChapterService;

    public QuestionController(QuestionService questionService, ModelMapper modelMapper, DocumentService documentService, ATAChapterService ataChapterService) {
        this.questionService = questionService;
        this.modelMapper = modelMapper;
        this.documentService = documentService;
        this.ataChapterService = ataChapterService;
    }


    @GetMapping("/add")
    private String addQuestion(Model model) {

        if (!model.containsAttribute("questionExistsError")) {
            model.addAttribute("questionExistsError", false);
        }

        if (!model.containsAttribute("seedOk")) {
            model.addAttribute("seedOk", false);
        }

        if (!model.containsAttribute("questionAddBindingModel")) {
            model.addAttribute("questionAddBindingModel", new QuestionAddBindingModel());
        }

        model.addAttribute("documentDbList", documentService.findAllDocumentsSortedByNameDesc());
        model.addAttribute("ataChapterDBList", ataChapterService.findAllATAChaptersSortedByATADesc());

        return "questions-add";
    }

    @PostMapping("/add")
    public String addQuestionConfirm(@Valid @ModelAttribute
                                             QuestionAddBindingModel questionAddBindingModel,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("questionAddBindingModel", questionAddBindingModel);

            redirectAttributes
                    .addFlashAttribute(
                            "org.springframework.validation.BindingResult.questionAddBindingModel",
                            bindingResult);

            return "redirect:add";
        }

        if (questionService.questionExistsByName(questionAddBindingModel.getName())) {

            redirectAttributes
                    .addFlashAttribute("questionAddBindingModel", questionAddBindingModel);

            redirectAttributes
                    .addFlashAttribute(
                            "org.springframework.validation.BindingResult.questionAddBindingModel",
                            bindingResult);

            redirectAttributes.addFlashAttribute("questionExistsError", true);

            return "redirect:add";
        }


        QuestionServiceModel questionServiceModel
                = modelMapper.map(questionAddBindingModel, QuestionServiceModel.class);

        questionServiceModel.setChapter(questionAddBindingModel.getChapter().split(" - ")[0]);
        questionServiceModel.setAtaSubChapter(questionAddBindingModel.getChapter().split(" - ")[0]);

        questionService.seedQuestionToDb(questionServiceModel);

        redirectAttributes.addFlashAttribute("seedOk", true);

        return "redirect:add";
    }
}
