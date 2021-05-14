package com.petkov.spr_final_1.web;

import com.petkov.spr_final_1.model.binding.document.DocumentSubchapterAddBindingModel;
import com.petkov.spr_final_1.model.service.document.DocumentSubchapterServiceModel;
import com.petkov.spr_final_1.service.DocumentService;
import com.petkov.spr_final_1.service.DocumentSubChapterService;
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
@RequestMapping("/document-subchapters")
public class DocumentSubChapterController {

    private final DocumentSubChapterService documentSubChapterService;
    private final ModelMapper modelMapper;
    private final DocumentService documentService;

    public DocumentSubChapterController(DocumentSubChapterService documentSubChapterService, ModelMapper modelMapper, DocumentService documentService) {
        this.documentSubChapterService = documentSubChapterService;
        this.modelMapper = modelMapper;
        this.documentService = documentService;
    }


    @ModelAttribute("documentSubchapterAddBindingModel")
    public DocumentSubchapterAddBindingModel documentSubchapterAddBindingModel() {
        return new DocumentSubchapterAddBindingModel();
    }

    @GetMapping("")
    private String addSubChapter(Model model) {

        if (!model.containsAttribute("documentSubChapterExistsError")) {
            model.addAttribute("documentSubChapterExistsError", false);
        }

        if (!model.containsAttribute("seedOk")) {
            model.addAttribute("seedOk", false);
        }

        model.addAttribute("documentDBList",
                documentService.findAllDocumentsSortedByNameDesc());

        return "document-subchapters";
    }

    @PostMapping("")
    public String addSubChapterConfirm(@Valid DocumentSubchapterAddBindingModel documentSubchapterAddBindingModel,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("documentSubchapterAddBindingModel", documentSubchapterAddBindingModel);
            redirectAttributes
                    .addFlashAttribute(
                            "org.springframework.validation.BindingResult.documentSubchapterAddBindingModel",
                            bindingResult);

            return "redirect:/document-subchapters";
        }

        if (documentSubChapterService.subChapterExistsInDocument(
                documentSubchapterAddBindingModel.getDocument(),
                documentSubchapterAddBindingModel.getName()
        )) {

            redirectAttributes
                    .addFlashAttribute("documentSubchapterAddBindingModel", documentSubchapterAddBindingModel);

            redirectAttributes
                    .addFlashAttribute(
                            "org.springframework.validation.BindingResult.documentSubchapterAddBindingModel",
                            bindingResult);

            redirectAttributes.addFlashAttribute("documentSubChapterExistsError", true);

            return "redirect:/document-subchapters";
        }

        DocumentSubchapterServiceModel documentSubchapterServiceModel = modelMapper.map(
                documentSubchapterAddBindingModel,
                DocumentSubchapterServiceModel.class);

        documentSubChapterService.seedDocumentSubchapterToDb(documentSubchapterServiceModel);

        redirectAttributes.addFlashAttribute("seedOk", true);

        return "redirect:/document-subchapters";
    }
}
