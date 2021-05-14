package com.petkov.spr_final_1.service.impl;

import com.google.gson.Gson;
import com.petkov.spr_final_1.model.aviation_library_entity.ATAChapterEntity;
import com.petkov.spr_final_1.model.service.document.ATAChapterServiceModel;
import com.petkov.spr_final_1.model.view.ATAChapterViewModel;
import com.petkov.spr_final_1.repository.ATAChapterRepository;
import com.petkov.spr_final_1.service.ATAChapterService;
import com.petkov.spr_final_1.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ATAChapterServiceImpl implements ATAChapterService {

    private final ModelMapper modelMapper;
    private final ATAChapterRepository chapterRepository;
    private final Gson gson;
    private final Resource chaptersInitFile;
    private final ValidationUtil validationUtil;

    public ATAChapterServiceImpl(ModelMapper modelMapper,
                                 ATAChapterRepository chapterRepository,
                                 Gson gson,
                                 @Value("classpath:init/chapters-init.json") Resource chaptersInitFile,
                                 ValidationUtil validationUtil) {
        this.modelMapper = modelMapper;
        this.chapterRepository = chapterRepository;
        this.gson = gson;
        this.chaptersInitFile = chaptersInitFile;
        this.validationUtil = validationUtil;
    }

    @Override
    public void initSeedChaptersFromJson() {
        if (chapterRepository.count() == 0) {
            try {
                ATAChapterServiceModel[] chapterServiceModels =
                        gson.fromJson(Files.readString(Path.of(chaptersInitFile.getURI())), ATAChapterServiceModel[].class);

                Arrays
                        .stream(chapterServiceModels)
                        .forEach(this::seedChaptersIfValidOrPrintError);

            } catch (IOException e) {
                throw new IllegalStateException("IO error from file 'init/chapters-init.json'!");
            }
        }
    }

    @Override
    @Async
    public CompletableFuture<List<ATAChapterViewModel>> findAllATAChaptersSortedByAtaDescAsync() {

        return CompletableFuture
                .supplyAsync(this::findAllATAChaptersSortedByATADesc)
                .orTimeout(30, TimeUnit.SECONDS);
    }

    private void seedChaptersIfValidOrPrintError(ATAChapterServiceModel chapterServiceModel) {

        if (this.validationUtil.isValid(chapterServiceModel)) {

            ATAChapterEntity chapter = modelMapper.map(chapterServiceModel, ATAChapterEntity.class);
            chapterRepository.saveAndFlush(chapter);

        } else {
            //todo seedChaptersIfValidOrPrintError - Log errors io printing
            System.out.println(String.format("Chapter init seed errors from file 'init/chapters-init.json' %n: "));

            validationUtil.getViolations(chapterServiceModel)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            System.out.printf("For chapter '%s' - '%s' %n", chapterServiceModel.getAtaCode(), chapterServiceModel.getName());
        }
    }

    @Override
    public ATAChapterServiceModel addChapterToDB(ATAChapterServiceModel chapterServiceModel) {

        ATAChapterEntity chapterEntity = this.modelMapper.map(chapterServiceModel, ATAChapterEntity.class);

        return modelMapper
                .map(chapterRepository.saveAndFlush(chapterEntity), ATAChapterServiceModel.class);
    }

    @Override
    public List<ATAChapterViewModel> findAllATAChaptersSortedByATADesc() {
        return chapterRepository
                .findAll()
                .stream()
                .map(chapterEntity -> modelMapper.map(chapterEntity, ATAChapterViewModel.class))
                .sorted(Comparator.comparing(ATAChapterViewModel::getAtaCode))
                .collect(Collectors.toList());
    }


    @Override
    public ATAChapterServiceModel findChapterByAtaCode(Integer ataCode) throws IllegalArgumentException {

        ATAChapterEntity chapterEntity =
                chapterRepository.findByAtaCode(ataCode)
                        .orElseThrow(() -> new IllegalArgumentException("Chapter could not be found in DB"));

        return modelMapper.map(chapterEntity, ATAChapterServiceModel.class);

    }

    @Override
    public ATAChapterServiceModel findChapterById(Long id) throws IllegalArgumentException {

        ATAChapterEntity chapterEntity =
                chapterRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Chapter could not be found in DB"));

        return modelMapper.map(chapterEntity, ATAChapterServiceModel.class);
    }

    @Override
    public boolean chapterAtaCodeExists(Integer ataCode) {
        return chapterRepository.findByAtaCode(ataCode).isPresent();
    }

    @Override
    public boolean chapterAtaNameExists(String name) {
        return chapterRepository.findByName(name).isPresent();
    }
}
