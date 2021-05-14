package com.petkov.spr_final_1.web;

import com.petkov.spr_final_1.model.binding.document.ArticleAddBindingModel;
import com.petkov.spr_final_1.model.service.document.ArticleServiceModel;
import com.petkov.spr_final_1.service.*;
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
import java.io.IOException;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final ATASubChapterService ataSubChapterService;
    private final ATAChapterService ataChapterService;
    private final DocumentService documentService;
    private final DocumentSubChapterService documentSubChapterService;
    private final ModelMapper modelMapper;

    public ArticleController(ArticleService articleService, ATASubChapterService ataSubChapterService, ATAChapterService ataChapterService, DocumentService documentService, DocumentSubChapterService documentSubChapterService, ModelMapper modelMapper) {
        this.articleService = articleService;
        this.ataSubChapterService = ataSubChapterService;
        this.ataChapterService = ataChapterService;
        this.documentService = documentService;
        this.documentSubChapterService = documentSubChapterService;
        this.modelMapper = modelMapper;
    }


    @ModelAttribute("articleAddBindingModel")
    public ArticleAddBindingModel articleAddBindingModel() {
        return new ArticleAddBindingModel();
    }

    @GetMapping("")
    private String addArticle(Model model) {

        if (!model.containsAttribute("articleExistsError")) {
            model.addAttribute("articleExistsError", false);
        }

        if (!model.containsAttribute("seedOk")) {
            model.addAttribute("seedOk", false);
        }

        model.addAttribute("documentDbList", documentService.findAllDocumentsSortedByNameDesc());
        model.addAttribute("chapterDBList", ataChapterService.findAllATAChaptersSortedByATADesc());


        return "articles";
    }

    @PostMapping("")
    public String addArticleConfirm(@Valid ArticleAddBindingModel articleAddBindingModel,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("articleAddBindingModel", articleAddBindingModel);
            redirectAttributes
                    .addFlashAttribute
                            ("org.springframework.validation.BindingResult.articleAddBindingModel",
                                    bindingResult);

            return "redirect:/articles";
        }

        if (articleService.articleExistsByTitle(articleAddBindingModel.getTitle())) {

            redirectAttributes.addFlashAttribute("articleAddBindingModel", articleAddBindingModel);

            redirectAttributes
                    .addFlashAttribute
                            ("org.springframework.validation.BindingResult.articleAddBindingModel",
                                    bindingResult);

            redirectAttributes.addFlashAttribute("articleExistsError", true);

            return "redirect:/articles";
        }

        articleAddBindingModel
                .setChapterRef(articleAddBindingModel.getChapterRef().split(" ")[0]);

        ArticleServiceModel articleServiceModel = modelMapper.map(
                articleAddBindingModel,
                ArticleServiceModel.class);


        articleService.seedArticleToDb(articleServiceModel);

        redirectAttributes.addFlashAttribute("seedOk", true);

        return "redirect:/articles";
    }
}
