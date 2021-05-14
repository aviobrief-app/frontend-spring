package com.petkov.spr_final_1.web;

import com.petkov.spr_final_1.service.TestService;
import com.petkov.spr_final_1.utils.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TestRestController.class)
@EnableWebMvc
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class TestAddRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TestService testService;
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private ValidationUtil validationUtil;
    @Autowired
    private WebApplicationContext webApplicationContext;


    @Test
    @WithMockUser(value = "pesho", roles = {"USER", "ADMIN"})
    public void testApiAddReturns_BadRequest() throws Exception {

        String objAsString =
                """
                {
                "name":"",
                "dueDate":"",
                "questionIds":[],
                "testTagEnums":[]
                }
                """;
            mockMvc
                .perform(post("/tests/api")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objAsString)
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(value = "pesho", roles = {"USER", "ADMIN"})
    public void testApiAddReturns_ResponseOk_with_responseBody() throws Exception {

        String objAsString =
                """
                {
                "name":"TEST",
                "dueDate":"2035-10-17",
                "questionIds":["6760c290-926a-4321-a599-d0db95a4dd3e"],
                "testTagEnums":["AFL"]
                }
                """;

        MvcResult result = mockMvc
                .perform(post("/tests/api")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objAsString)
                )
                .andExpect(status().isCreated())
                .andReturn();

//        System.out.println(result.getResponse().getContentAsString());

    }
}
