package co.edu.udistrital.mdp.pets.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

    @Test
    void createPetWithValidData_ReturnsCreated() throws Exception {
        ShelterEntity shelter = new ShelterEntity();
        shelter.setShelterName("Test Shelter");
        shelter = shelterRepository.save(shelter);

        PetDTO validPetDTO = new PetDTO();
        validPetDTO.setName("Firulais");
        validPetDTO.setSpecies("Dog");
        validPetDTO.setBreed("Golden Retriever");
        validPetDTO.setSex("Male");
        validPetDTO.setSize("Large");
        validPetDTO.setTemperament("Friendly");
        validPetDTO.setPhoto("http://example.com/photo.jpg");
        validPetDTO.setShelterId(shelter.getId());

        mockMvc.perform(post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validPetDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Firulais"))
                .andExpect(jsonPath("$.status").value("IN_SHELTER"));
    }

    @Test
    void createPetMissingMandatoryFields_ReturnsBadRequest() throws Exception {
        // Enviar un DTO completamente vacío
        PetDTO invalidPetDTO = new PetDTO();

        mockMvc.perform(post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPetDTO)))
                .andExpect(status().isBadRequest());
                // Validamos que se lance error 400 ya que faltan los campos obligatorios
    }

    @Test
    void createPetMissingSpecificField_ReturnsBadRequest() throws Exception {
        ShelterEntity shelter = new ShelterEntity();
        shelter.setShelterName("Test Shelter");
        shelter = shelterRepository.save(shelter);

        PetDTO invalidPetDTO = new PetDTO();
        invalidPetDTO.setName("Firulais");
        invalidPetDTO.setSpecies("Dog");
        // Falta breed, sex, size, temperament, photo
        invalidPetDTO.setShelterId(shelter.getId());

        mockMvc.perform(post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPetDTO)))
                .andExpect(status().isBadRequest());
    }
}
