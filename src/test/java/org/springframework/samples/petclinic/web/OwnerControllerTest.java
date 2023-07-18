package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(locations = {
        "classpath:spring/mvc-test-config.xml",
        "classpath:spring/mvc-core-config.xml",
})
class OwnerControllerTest {

    private static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";

    @Autowired
    OwnerController ownerController;

    @Autowired
    ClinicService clinicService;

    @Captor
    ArgumentCaptor<String> captor;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    @AfterEach
    void tearDown() {
        reset(clinicService);
    }

    @Test
    void testUpdateOwnerPost_valid() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit", 1)
                        .param("address", "address")
                        .param("city", "city")
                        .param("telephone", "123456789")
                        .param("firstName", "firstName")
                        .param("lastName", "lastName")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/{ownerId}"));
    }

    @Test
    void testUpdateOwnerPost_notValid() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit", 1)
                        .param("firstName", "firstName")
                        .param("lastName", "lastName")
                )
                .andExpect(status().isOk())
                .andExpect(view().name(OWNERS_CREATE_OR_UPDATE_OWNER_FORM));
    }

    @Test
    void testNewOwnerPost_valid() throws Exception {
        mockMvc.perform(post("/owners/new")
                        .param("address", "address")
                        .param("city", "city")
                        .param("telephone", "123456789")
                        .param("firstName", "firstName")
                        .param("lastName", "lastName")
                )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testNewOwnerPost_notValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                        .param("address", "address")
                        .param("city", "city") // not all params
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
                .andExpect(model().attributeHasFieldErrors("owner", "firstName"))
                .andExpect(model().attributeHasFieldErrors("owner", "lastName"))
                .andExpect(view().name(OWNERS_CREATE_OR_UPDATE_OWNER_FORM));
    }

    @Test
    void testFindByName_list() throws Exception {
        given(clinicService.findOwnerByLastName("")).willReturn(List.of(new Owner(), new Owner()));

        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"));

        then(clinicService).should().findOwnerByLastName(captor.capture());

        assertThat(captor.getValue()).isEqualTo("");

    }

    @Test
    void testFindByName_notFound() throws Exception {
        mockMvc.perform(get("/owners").param("lastName", "Do NOT find me!"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void testFindByName_single() throws Exception {
        Owner owner = new Owner();
        owner.setId(1);
        given(clinicService.findOwnerByLastName(anyString())).willReturn(List.of(owner));

        mockMvc.perform(get("/owners").param("lastName", "UniqueOwner"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/" + owner.getId()));

        then(clinicService).should().findOwnerByLastName(anyString());
    }


    @Test
    void initCreationFormTest() throws Exception {
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name(OWNERS_CREATE_OR_UPDATE_OWNER_FORM));
    }
}