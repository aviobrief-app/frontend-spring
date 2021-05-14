package com.petkov.spr_final_1.service.impl;

import com.google.gson.Gson;
import com.petkov.spr_final_1.model.aviation_library_entity.ATAChapterEntity;
import com.petkov.spr_final_1.model.aviation_library_entity.ATASubChapterEntity;
import com.petkov.spr_final_1.model.service.document.ATASubChapterServiceModel;
import com.petkov.spr_final_1.model.view.ATASubChapterViewModel;
import com.petkov.spr_final_1.repository.ATASubChapterRepository;
import com.petkov.spr_final_1.service.ATAChapterService;
import com.petkov.spr_final_1.service.ATASubChapterService;
import com.petkov.spr_final_1.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class ATASubChapterServiceImpl implements ATASubChapterService {


    private final ModelMapper modelMapper;
    private final ATASubChapterRepository subChapterRepository;
    private final ATAChapterService chapterService;
    private final Resource subChaptersInitFile;
    private final ValidationUtil validationUtil;
    private final Gson gson;


    public ATASubChapterServiceImpl(ModelMapper modelMapper,
                                    ATASubChapterRepository subChapterRepository,
                                    ATAChapterService chapterService,
                                    @Value("classpath:init/sub-chapters-init.json") Resource subChaptersInitFile,
                                    ValidationUtil validationUtil, Gson gson) {
        this.modelMapper = modelMapper;
        this.subChapterRepository = subChapterRepository;
        this.chapterService = chapterService;
        this.subChaptersInitFile = subChaptersInitFile;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }


    @Override
    @Transactional
    public void seedATASubchapterToDb(ATASubChapterServiceModel subChapterServiceModel) {

        //
//        TypeMap<ATASubChapterServiceModel, ATASubChapterEntity> typeMap =
//                modelMapper.createTypeMap(ATASubChapterServiceModel.class, ATASubChapterEntity.class);
//
//        typeMap.addMappings(mapper -> mapper.skip(ATASubChapterEntity::setAtaSubCode));
//
//        ATASubChapterEntity ataSubChapterEntity =
//                modelMapper.map(ataSubChapterServiceModel, ATASubChapterEntity.class);

        ATASubChapterEntity subChapterEntity = new ATASubChapterEntity();

        subChapterEntity.setAtaSubCode(subChapterServiceModel.getAtaSubCode());

        int ataChapterCode = Integer.parseInt(subChapterServiceModel.
                getAtaChapterRefInput());

        ATAChapterEntity chapterEntity =
                modelMapper.map(this.chapterService
                        .findChapterByAtaCode(ataChapterCode), ATAChapterEntity.class);

        subChapterEntity.setAtaChapter(chapterEntity);
        subChapterEntity.setName(subChapterServiceModel.getName());

        this.subChapterRepository.saveAndFlush(subChapterEntity);

    }

    @Override
    @Transactional
    public boolean subChapterCodeExists(int ataSubCode, int chapterRef) {

        ATAChapterEntity chapter
                = modelMapper.map(chapterService.findChapterByAtaCode(chapterRef), ATAChapterEntity.class);

        //todo subChapterCodeExists - implement
        return true;
    }

    @Override
    public void initSeedSubchaptersFromJson() {
        if (subChapterRepository.count() == 0) {

            try {
                ATASubChapterServiceModel[] subChapterServiceModels =
                        gson.fromJson(Files.readString(Path.of(subChaptersInitFile.getURI())), ATASubChapterServiceModel[].class);

                Arrays
                        .stream(subChapterServiceModels)
                        .forEach(this::seedSubChaptersIfValidOrPrintError);

            } catch (IOException e) {
                throw new IllegalStateException("IO error from file 'init/sub-chapters-init.json'!");
            }
        }
    }

    @Override
    public ATASubChapterServiceModel findByChapterAndSubchapterAta(int ataChapterCode,
                                                                   int ataSubchapterCode) throws IllegalArgumentException {

        ATAChapterEntity chapter =
                modelMapper.map(chapterService.findChapterByAtaCode(ataChapterCode), ATAChapterEntity.class);

        ATASubChapterEntity subChapterEntity = subChapterRepository
                .findByAtaSubCode(ataSubchapterCode)
                .orElseThrow(() -> new IllegalArgumentException("ATA SubChapter could not be found in DB"));

        return modelMapper.map(subChapterEntity, ATASubChapterServiceModel.class);

        //todo findByChapterAndSubchapterAta - refactor
    }

    @Override
    public List<ATASubChapterViewModel> getAllSortedByATADesc() {

        return subChapterRepository.findAll()
                .stream()
                .map(ataSubChapterEntity ->
                        modelMapper.map(ataSubChapterEntity, ATASubChapterViewModel.class))
                .sorted(Comparator
                        .comparing(ATASubChapterViewModel::getAtaChapter)
                        .thenComparing(ATASubChapterViewModel::getAtaSubCode))
                .collect(Collectors.toList());
    }

    @Override
    @Async
    public CompletableFuture<List<ATASubChapterViewModel>> getAllSortedByATAAsync() {
        return CompletableFuture
                .supplyAsync(this::getAllSortedByATADesc)
                .orTimeout(30, TimeUnit.SECONDS);
    }

    private void seedSubChaptersIfValidOrPrintError(ATASubChapterServiceModel subChapterServiceModel) {

        if (this.validationUtil.isValid(subChapterServiceModel)) {

            seedATASubchapterToDb(subChapterServiceModel);

        } else {
            //chapterServiceModel is NOT valid -> print messages
            System.out.println(String.format("Chapter init seed errors from file 'init/sub-chapters-init.json' %n: "));

            this.validationUtil.getViolations(subChapterServiceModel)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);

            System.out.printf("For chapter '%s'-'%s' - '%s' %n",
                    subChapterServiceModel.getAtaChapterRefInput(),
                    subChapterServiceModel.getAtaSubCode(),
                    subChapterServiceModel.getName());
        }
    }

}
