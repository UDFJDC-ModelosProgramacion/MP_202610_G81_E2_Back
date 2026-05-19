package co.edu.udistrital.mdp.pets.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.udistrital.mdp.pets.dto.PetDTO;
import co.edu.udistrital.mdp.pets.entities.MedicalHistoryEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.VaccineEntryEntity;
import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;
import co.edu.udistrital.mdp.pets.repositories.MedicalHistoryRepository;
import co.edu.udistrital.mdp.pets.repositories.MedicalEventRepository;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;
import co.edu.udistrital.mdp.pets.repositories.VaccineEntryRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    @Autowired
    private VaccineEntryRepository vaccineEntryRepository;

    @Autowired
    private MedicalEventRepository medicalEventRepository;

    private Long shelterId;
    private Long petId;

    @BeforeEach
    void setUp() {
        ShelterEntity shelter = new ShelterEntity();
        shelter.setShelterName("Test Shelter");
        shelter.setAddress("Calle 123");
        shelter.setCity("Bogotá");
        shelter.setPhoneNumber("3001234567");
        shelter = shelterRepository.save(shelter);
        shelterId = shelter.getId();

        PetEntity pet = new PetEntity();
        pet.setName("Firulais");
        pet.setSpecies("Dog");
        pet.setBreed("Golden Retriever");
        pet.setSex("Male");
        pet.setSize("Large");
        pet.setTemperament("Friendly");
        pet.setPhoto("http://example.com/photo.jpg");
        pet.setStatus("IN_SHELTER");
        pet.setShelter(shelter);
        pet = petRepository.save(pet);
        petId = pet.getId();

        // Crear historia clínica
        MedicalHistoryEntity medHistory = new MedicalHistoryEntity();
        medHistory.setCreatedDate(LocalDate.of(2025, 1, 1));
        medHistory.setLastUpdated(LocalDate.of(2025, 6, 1));
        medHistory.setPet(pet);
        medHistory = medicalHistoryRepository.save(medHistory);

        pet.setMedicalHistory(medHistory);
        petRepository.save(pet);

        // Crear vacunas
        VaccineEntryEntity vaccine1 = new VaccineEntryEntity();
        vaccine1.setVaccineName("Rabia");
        vaccine1.setAdminDate(LocalDate.of(2025, 2, 15));
        vaccine1.setNextDueDate(LocalDate.of(2026, 2, 15));
        vaccine1.setMedicalHistory(medHistory);
        vaccineEntryRepository.save(vaccine1);

        VaccineEntryEntity vaccine2 = new VaccineEntryEntity();
        vaccine2.setVaccineName("Parvovirus");
        vaccine2.setAdminDate(LocalDate.of(2025, 3, 10));
        vaccine2.setNextDueDate(LocalDate.of(2026, 3, 10));
        vaccine2.setMedicalHistory(medHistory);
        vaccineEntryRepository.save(vaccine2);

        // Crear evento médico
        MedicalEventEntity event = new MedicalEventEntity();
        event.setEventType("Consulta");
        event.setDescription("Revisión general de salud");
        event.setEventDate(LocalDate.of(2025, 4, 20));
        event.setMedicalHistory(medHistory);
        medicalEventRepository.save(event);

        // Flush and clear persistence context to force fresh DB reads
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void createPetWithValidData_ReturnsCreated() throws Exception {
        PetDTO validPetDTO = new PetDTO();
        validPetDTO.setName("Luna");
        validPetDTO.setSpecies("Cat");
        validPetDTO.setBreed("Persa");
        validPetDTO.setSex("Female");
        validPetDTO.setSize("Small");
        validPetDTO.setTemperament("Calm");
        validPetDTO.setPhoto("http://example.com/luna.jpg");
        validPetDTO.setShelterId(shelterId);

        mockMvc.perform(post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validPetDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Luna"))
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

    // ======================== HU09 – Tests de detalle de mascota ========================

    @Test
    void getPetDetail_ReturnsFullPetInfo() throws Exception {
        mockMvc.perform(get("/pets/{id}/detail", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(petId))
                .andExpect(jsonPath("$.name").value("Firulais"))
                .andExpect(jsonPath("$.species").value("Dog"))
                .andExpect(jsonPath("$.breed").value("Golden Retriever"))
                .andExpect(jsonPath("$.sex").value("Male"))
                .andExpect(jsonPath("$.size").value("Large"))
                .andExpect(jsonPath("$.temperament").value("Friendly"))
                .andExpect(jsonPath("$.status").value("IN_SHELTER"));
    }

    @Test
    void getPetDetail_ReturnsShelterInfo() throws Exception {
        mockMvc.perform(get("/pets/{id}/detail", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shelter").exists())
                .andExpect(jsonPath("$.shelter.id").value(shelterId))
                .andExpect(jsonPath("$.shelter.shelterName").value("Test Shelter"))
                .andExpect(jsonPath("$.shelter.address").value("Calle 123"))
                .andExpect(jsonPath("$.shelter.city").value("Bogotá"));
    }

    @Test
    void getPetDetail_ReturnsMedicalHistoryWithVaccines() throws Exception {
        mockMvc.perform(get("/pets/{id}/detail", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.medicalHistory").exists())
                .andExpect(jsonPath("$.medicalHistory.createdDate").value("2025-01-01"))
                .andExpect(jsonPath("$.medicalHistory.vaccineEntries").isArray())
                .andExpect(jsonPath("$.medicalHistory.vaccineEntries.length()").value(2))
                .andExpect(jsonPath("$.medicalHistory.vaccineEntries[0].vaccineName").exists());
    }

    @Test
    void getPetDetail_ReturnsMedicalEvents() throws Exception {
        mockMvc.perform(get("/pets/{id}/detail", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.medicalHistory.medicalEvents").isArray())
                .andExpect(jsonPath("$.medicalHistory.medicalEvents.length()").value(1))
                .andExpect(jsonPath("$.medicalHistory.medicalEvents[0].eventType").value("Consulta"))
                .andExpect(jsonPath("$.medicalHistory.medicalEvents[0].description").value("Revisión general de salud"));
    }

    @Test
    void getPetDetail_ReturnsAdoptionRequestIds() throws Exception {
        mockMvc.perform(get("/pets/{id}/detail", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.adoptionRequestIds").isArray());
    }

    @Test
    void getPetDetail_NotFound_Returns404() throws Exception {
        mockMvc.perform(get("/pets/{id}/detail", 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMedicalHistory_ReturnsFromDatabase() throws Exception {
        mockMvc.perform(get("/pets/{id}/medical-history", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createdDate").value("2025-01-01"))
                .andExpect(jsonPath("$.vaccineEntries").isArray())
                .andExpect(jsonPath("$.vaccineEntries.length()").value(2))
                .andExpect(jsonPath("$.medicalEvents").isArray())
                .andExpect(jsonPath("$.medicalEvents.length()").value(1));
    }

    @Test
    void getVaccines_ReturnsFromDatabase() throws Exception {
        mockMvc.perform(get("/pets/{id}/vaccines", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].vaccineName").exists())
                .andExpect(jsonPath("$[1].vaccineName").exists());
    }

    @Test
    void getPetById_ReturnsDetail() throws Exception {
        mockMvc.perform(get("/pets/{id}", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Firulais"))
                .andExpect(jsonPath("$.shelter").exists())
                .andExpect(jsonPath("$.medicalHistory").exists());
    }

    @Test
    void getPets_ReturnsList() throws Exception {
        mockMvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(1)));
    }
}
