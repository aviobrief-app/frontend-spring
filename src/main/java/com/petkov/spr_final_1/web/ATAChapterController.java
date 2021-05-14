package com.petkov.spr_final_1.web;

import com.petkov.spr_final_1.model.binding.document.ATAChapterAddBindingModel;
import com.petkov.spr_final_1.model.service.document.ATAChapterServiceModel;
import com.petkov.spr_final_1.service.ATAChapterService;
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
@RequestMapping("/ata-chapters")
public class ATAChapterController {

    private final ATAChapterService chapterService;
    private final ModelMapper modelMapper;

    public ATAChapterController(ATAChapterService chapterService, ModelMapper modelMapper) {
        this.chapterService = chapterService;
        this.modelMapper = modelMapper;
    }


    @ModelAttribute("chapterAddBindingModel")
    public ATAChapterAddBindingModel chapterAddBindingModel() {
        return new ATAChapterAddBindingModel();
    }

    @GetMapping("")
    private String addChapter(Model model) {

        if (!model.containsAttribute("seedOk")) {
            model.addAttribute("seedOk", "false");
        }

        if (!model.containsAttribute("chapterExistsError")) {
            model.addAttribute("chapterExistsError", false);
        }

        if (!model.containsAttribute("chapterExistsError")) {
            model.addAttribute("chapterNameExistsError", false);
        }

        return "ata-chapters";
    }

    @PostMapping("")
    public String addChapterConfirm(@Valid ATAChapterAddBindingModel chapterAddBindingModel,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("chapterAddBindingModel", chapterAddBindingModel);
            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.chapterAddBindingModel", bindingResult);

            return "redirect:/ata-chapters";
        }

        if (chapterService.chapterAtaCodeExists(chapterAddBindingModel.getAtaCode())) {

            redirectAttributes.addFlashAttribute("chapterAddBindingModel", chapterAddBindingModel);

            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.chapterAddBindingModel", bindingResult);

            redirectAttributes.addFlashAttribute("chapterExistsError", true);

            return "redirect:/ata-chapters";
        }

        if (chapterService.chapterAtaNameExists(chapterAddBindingModel.getName())) {

            redirectAttributes.addFlashAttribute("chapterAddBindingModel", chapterAddBindingModel);

            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.chapterAddBindingModel", bindingResult);

            redirectAttributes.addFlashAttribute("chapterNameExistsError", true);

            return "redirect:/ata-chapters";
        }

        ATAChapterServiceModel chapterServiceModel = modelMapper.map(
                chapterAddBindingModel,
                ATAChapterServiceModel.class);

        chapterService.addChapterToDB(chapterServiceModel);

        redirectAttributes.addFlashAttribute("seedOk", true);

        return "redirect:/ata-chapters";
    }


}
