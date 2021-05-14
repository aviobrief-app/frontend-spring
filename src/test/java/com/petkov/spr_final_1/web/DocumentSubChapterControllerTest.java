package com.petkov.spr_final_1.web;

import com.petkov.spr_final_1.model.aviation_library_entity.DocumentEntity;
import com.petkov.spr_final_1.model.aviation_library_entity.DocumentSubchapterEntity;
import com.petkov.spr_final_1.repository.DocumentRepository;
import com.petkov.spr_final_1.repository.DocumentSubchapterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DocumentSubChapterControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentSubchapterRepository documentSubchapterRepository;


    @Test
    void getWorks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/document-subchapters")).
                andExpect(status().isOk()).
                andExpect(view().name("document-subchapters")).
                andExpect(model().attributeExists("documentDBList"));
    }

    @Test
    @WithMockUser(value = "pesho", roles = {"USER", "ADMIN"})
    void addDocumentSubchapter() throws Exception {

        //save doc
        String docName = String.format("Test Document - %s", LocalDateTime.now().toString());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/documents/add")
                .param("documentName", docName)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection());
        DocumentEntity savedDoc = documentRepository.findByName(docName).orElse(null);

        Assertions.assertNotNull(savedDoc);

        //save docSubchapter , related to doc
        String docSubchapterName = String.format("Test DocumentSubchapter - %s", LocalDateTime.now().toString());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/document-subchapters/add")
                .param("docSubchapterName", docSubchapterName)
                .param("documentRef", docName)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection());

        DocumentSubchapterEntity savedDocSubchapter = documentSubchapterRepository
                .findByDocumentAndName(savedDoc, docSubchapterName).orElse(null);

        Assertions.assertNotNull(savedDocSubchapter);
        Assertions.assertEquals(docSubchapterName, savedDocSubchapter.getName());


        documentSubchapterRepository.deleteById(savedDocSubchapter.getId());
        documentRepository.deleteById(savedDoc.getId());

    }


}
