package com.petkov.spr_final_1.service.impl;

import com.google.gson.Gson;
import com.petkov.spr_final_1.model.aviation_library_entity.DocumentEntity;
import com.petkov.spr_final_1.model.service.document.DocumentServiceModel;
import com.petkov.spr_final_1.model.view.DocumentViewModel;
import com.petkov.spr_final_1.repository.DocumentRepository;
import com.petkov.spr_final_1.service.DocumentSubChapterService;
import com.petkov.spr_final_1.utils.ValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.petkov.spr_final_1.constants.GlobalMessages.*;
import static org.mockito.Mockito.when;


@SpringBootTest
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ComponentScan.class)})
@ExtendWith(MockitoExtension.class)
public class DocumentServiceImplTest {


    @InjectMocks
    private DocumentServiceImpl serviceToTest;
    private DocumentViewModel docA, docB, docC;

    @Mock
    DocumentRepository documentRepository;

    @Value(DOCUMENTS_INIT_FILE_PATH)
    private Resource initFile;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private Gson gson;
    @Autowired
    private ValidationUtil validationUtil;
    @Autowired
    private DocumentSubChapterService documentSubChapterService;

    @BeforeEach
    public void init() {
        docA = new DocumentViewModel();
        docB = new DocumentViewModel();
        docC = new DocumentViewModel();
        docA.setName("aaa");
        docB.setName("bbb");
        docC.setName("ccc");


        serviceToTest = new DocumentServiceImpl(modelMapper, documentRepository, initFile, gson, validationUtil, documentSubChapterService);
    }

    @Test
    public void initSeedDocumentsFromJson_works() {
        List<DocumentViewModel> entered = List.of(docB, docC, docA);
        List<DocumentEntity> expectedFromRepo = entered
                .stream()
                .sorted(Comparator.comparing(DocumentViewModel::getName))
                .map(documentViewModel -> modelMapper.map(documentViewModel, DocumentEntity.class))
                .collect(Collectors.toList());

        when(documentRepository.count()).thenReturn(0L);
        when(documentRepository
                .findAll(Sort.by(Sort.Direction.DESC, "documentName")))
                .thenReturn(expectedFromRepo);

        serviceToTest.initSeedDocumentsFromJson();
        List<String> result = serviceToTest.findAllDocumentsSortedByNameDesc()
                .stream()
                .map(DocumentViewModel::getName)
                .collect(Collectors.toList());

        Assertions.assertEquals(result, List.of(docA.getName(), docB.getName(), docC.getName()));
    }

    @Test
    public void findDocumentByName_works(){
        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setName("aaa");
        when(documentRepository.findByName("aaa")).thenReturn(Optional.of(documentEntity));

        DocumentServiceModel result = serviceToTest.findDocumentByName("aaa");

        Assertions.assertEquals(result.getName(), documentEntity.getName());

    }

    @Test
    public void findDocumentByName_throws(){
        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setName("aaa");

        when(documentRepository.findByName("aaa")).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> serviceToTest.findDocumentByName("aaa"));

        Assertions.assertEquals(DOCUMENT_NOT_FOUND_MESSAGE, exception.getMessage());

    }



}
