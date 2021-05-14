package com.petkov.spr_final_1.web;

import com.petkov.spr_final_1.model.binding.document.DocumentAddBindingModel;
import com.petkov.spr_final_1.model.service.document.DocumentServiceModel;
import com.petkov.spr_final_1.service.DocumentService;
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
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final ModelMapper modelMapper;

    public DocumentController(DocumentService documentService, ModelMapper modelMapper) {
        this.documentService = documentService;
        this.modelMapper = modelMapper;
    }


    @ModelAttribute("documentAddBindingModel")
    public DocumentAddBindingModel documentAddBindingModel() {
        return new DocumentAddBindingModel();
    }

    @GetMapping("")
    private String addDocument(Model model) {

        if (!model.containsAttribute("documentExistsError")) {
            model.addAttribute("documentExistsError", false);
        }

        if (!model.containsAttribute("seedOk")) {
            model.addAttribute("seedOk", false);
        }

        return "documents";
    }

    @PostMapping("/add")
    public String addDocumentConfirm(@Valid DocumentAddBindingModel documentAddBindingModel,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("documentAddBindingModel", documentAddBindingModel);
            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.documentAddBindingModel", bindingResult);

            return "redirect:/documents";
        }

        if (documentService.documentExists(documentAddBindingModel.getName())) {

            redirectAttributes.addFlashAttribute("documentAddBindingModel", documentAddBindingModel);
            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.documentAddBindingModel", bindingResult);

            redirectAttributes.addFlashAttribute("documentExistsError", true);

            return "redirect:/documents";
        }

        DocumentServiceModel documentServiceModel = modelMapper.map(documentAddBindingModel, DocumentServiceModel.class);

        documentService.seedDocumentToDb(documentServiceModel);

        redirectAttributes.addFlashAttribute("seedOk", true);

        return "redirect:/documents";
    }

}
