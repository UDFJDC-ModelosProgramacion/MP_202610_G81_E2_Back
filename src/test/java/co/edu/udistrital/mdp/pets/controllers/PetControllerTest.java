package co.edu.udistrital.mdp.pets.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.udistrital.mdp.pets.dto.PetDTO;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShelterRepository shelterRepository;

    private Long shelterId;

    @BeforeEach
    void setUp() {
        ShelterEntity shelter = new ShelterEntity();
        shelter.setShelterName("Test Shelter");
        shelterId = shelterRepository.save(shelter).getId();
    }

    @Test
    void createPetWithValidData_ReturnsCreated() throws Exception {
        PetDTO validPetDTO = new PetDTO();
        validPetDTO.setName("Firulais");
        validPetDTO.setSpecies("Dog");
        validPetDTO.setBreed("Golden Retriever");
        validPetDTO.setSex("Male");
        validPetDTO.setSize("Large");
        validPetDTO.setTemperament("Friendly");
        validPetDTO.setPhoto("http://example.com/photo.jpg");
        validPetDTO.setShelterId(shelterId);

        mockMvc.perform(post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validPetDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Firulais"))
                .andExpect(jsonPath("$.status").value("IN_SHELTER"));
    }

    @Test
    void createPetMissingMandatoryFields_ReturnsBadRequest() throws Exception {
        // Enviar un DTO completamente vacío → debe fallar validación JSR-303
        PetDTO invalidPetDTO = new PetDTO();

        mockMvc.perform(post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPetDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPetMissingSpecificField_ReturnsBadRequest() throws Exception {
        // Solo nombre y especie, faltan breed, sex, size, temperament, photo
        PetDTO invalidPetDTO = new PetDTO();
        invalidPetDTO.setName("Firulais");
        invalidPetDTO.setSpecies("Dog");
        invalidPetDTO.setShelterId(shelterId);

        mockMvc.perform(post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPetDTO)))
                .andExpect(status().isBadRequest());
    }
}
