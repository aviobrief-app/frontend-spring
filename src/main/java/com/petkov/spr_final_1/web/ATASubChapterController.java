package com.petkov.spr_final_1.web;

import com.petkov.spr_final_1.model.binding.document.ATASubChapterAddBindingModel;
import com.petkov.spr_final_1.model.service.document.ATASubChapterServiceModel;
import com.petkov.spr_final_1.service.ATAChapterService;
import com.petkov.spr_final_1.service.ATASubChapterService;
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
@RequestMapping("/ata-subchapters")
public class ATASubChapterController {

    private final ATASubChapterService subchapterService;
    private final ATAChapterService chapterService;
    private final ModelMapper modelMapper;

    public ATASubChapterController(ATASubChapterService subchapterService, ATAChapterService chapterService, ModelMapper modelMapper) {
        this.subchapterService = subchapterService;
        this.chapterService = chapterService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute("subChapterAddBindingModel")
    public ATASubChapterAddBindingModel subChapterAddBindingModel() {
        return new ATASubChapterAddBindingModel();
    }

    @GetMapping("")
    private String addSubChapter(Model model) {

        if (!model.containsAttribute("ataSubChapterExistsError")) {
            model.addAttribute("ataSubChapterExistsError", false);
        }

        if (!model.containsAttribute("seedOk")) {
            model.addAttribute("seedOk", false);
        }

        model.addAttribute("ataChapterDBList", chapterService.findAllATAChaptersSortedByATADesc());

        return "ata-subchapters";
    }

    @PostMapping("")
    public String addSubChapterConfirm(@Valid ATASubChapterAddBindingModel subChapterAddBindingModel,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("subChapterAddBindingModel", subChapterAddBindingModel);
            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.subChapterAddBindingModel", bindingResult);

            return "redirect:/ata-subchapters";
        }

        if (subchapterService
                .subChapterCodeExists(
                        subChapterAddBindingModel.getAtaSubCode(),
                        Integer.parseInt(subChapterAddBindingModel.getAtaChapterRefInput().split(" - ")[0]))) {

            redirectAttributes.addFlashAttribute("subChapterAddBindingModel", subChapterAddBindingModel);

            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.subChapterAddBindingModel", bindingResult);

            redirectAttributes.addFlashAttribute("ataSubChapterExistsError", true);

            return "redirect:/ata-subchapters";
        }

        subChapterAddBindingModel
                .setAtaChapterRefInput(subChapterAddBindingModel.getAtaChapterRefInput().split(" ")[0]);

        ATASubChapterServiceModel subChapterServiceModel = modelMapper.map(
                subChapterAddBindingModel,
                ATASubChapterServiceModel.class);

        subchapterService.seedATASubchapterToDb(subChapterServiceModel);

        redirectAttributes.addFlashAttribute("seedOk", true);

        return "redirect:/ata-subchapters";
    }
}
