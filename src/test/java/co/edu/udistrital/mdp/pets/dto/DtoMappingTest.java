package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ReturnCaseEntity;
import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;

class DtoMappingTest {

    @Test
    void adoptionProcessDtoMapsReferencedIds() {
        AdoptionProcessDTO dto = new AdoptionProcessDTO();
        dto.setId(10L);
        dto.setCreationDate(LocalDate.of(2026, 4, 5));
        dto.setStatus("PENDING");
        dto.setAdoptionRequestId(20L);
        dto.setPetId(30L);

        AdoptionProcessEntity entity = dto.toEntity();

        assertInstanceOf(TrialCohabitationEntity.class, entity);
        assertEquals(10L, entity.getId());
        assertEquals(LocalDate.of(2026, 4, 5), entity.getCreationDate());
        assertEquals("PENDING", entity.getStatus());
        assertNotNull(entity.getAdoptionRequest());
        assertEquals(20L, entity.getAdoptionRequest().getId());
        assertNotNull(entity.getPet());
        assertEquals(30L, entity.getPet().getId());
    }

    @Test
    void returnCaseDtoMapsAdoptionProcessId() {
        ReturnCaseDTO dto = new ReturnCaseDTO();
        dto.setId(11L);
        dto.setReturnDate(LocalDate.of(2026, 4, 6));
        dto.setReason("Test");
        dto.setDetails("Details");
        dto.setAdoptionProcessId(21L);

        ReturnCaseEntity entity = dto.toEntity();

        assertEquals(11L, entity.getId());
        assertEquals(LocalDate.of(2026, 4, 6), entity.getReturnDate());
        assertEquals("Test", entity.getReason());
        assertEquals("Details", entity.getDetails());
        assertNotNull(entity.getAdoptionProcess());
        assertEquals(21L, entity.getAdoptionProcess().getId());
    }

    @Test
    void veterinarianDtoMapsShelterId() {
        VeterinarianDTO dto = new VeterinarianDTO();
        dto.setId(12L);
        dto.setName("Vet Test");
        dto.setEmail("vet@test.com");
        dto.setPassword("secret");
        dto.setPhoneNumber("3000000000");
        dto.setProfileImageUrl("https://example.org/vet.png");
        dto.setRegisterDate(LocalDateTime.of(2026, 4, 5, 10, 0));
        dto.setLicenseNumber("LIC-123");
        dto.setAvailabilitySchedule("9-5");
        dto.setShelterId(31L);

        VeterinarianEntity entity = dto.toEntity();

        assertEquals(12L, entity.getId());
        assertEquals("Vet Test", entity.getName());
        assertEquals("LIC-123", entity.getLicenseNumber());
        assertNotNull(entity.getShelter());
        assertEquals(31L, entity.getShelter().getId());
    }

    @Test
    void adoptionRequestDtoMapsReferencedIds() {
        AdoptionRequestDTO dto = new AdoptionRequestDTO();
        dto.setId(13L);
        dto.setRequestDate(LocalDate.of(2026, 4, 7));
        dto.setStatus("PENDING");
        dto.setComments("Test");
        dto.setAdopterId(22L);
        dto.setPetId(32L);

        AdoptionRequestEntity entity = dto.toEntity();

        assertEquals(13L, entity.getId());
        assertEquals(LocalDate.of(2026, 4, 7), entity.getRequestDate());
        assertEquals("PENDING", entity.getStatus());
        assertEquals("Test", entity.getComments());
        assertNotNull(entity.getAdopter());
        assertEquals(22L, entity.getAdopter().getId());
        assertNotNull(entity.getPet());
        assertEquals(32L, entity.getPet().getId());
    }

    @Test
    void petDtoMapsShelterId() {
        PetDTO dto = new PetDTO();
        dto.setId(14L);
        dto.setName("Pet Test");
        dto.setBreed("Breed");
        dto.setBornDate(LocalDate.of(2026, 1, 1));
        dto.setSex("F");
        dto.setSize("M");
        dto.setTemperament("Calm");
        dto.setSpecificNeeds("None");
        dto.setIsRescued(Boolean.TRUE);
        dto.setStatus("IN_SHELTER");
        dto.setShelterId(33L);

        PetEntity entity = dto.toEntity();

        assertEquals(14L, entity.getId());
        assertEquals("Pet Test", entity.getName());
        assertEquals("Breed", entity.getBreed());
        assertNotNull(entity.getShelter());
        assertEquals(33L, entity.getShelter().getId());
    }
}