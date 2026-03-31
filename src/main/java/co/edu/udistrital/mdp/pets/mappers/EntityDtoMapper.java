package co.edu.udistrital.mdp.pets.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import co.edu.udistrital.mdp.pets.dto.AdopterDetailDTO;
import co.edu.udistrital.mdp.pets.dto.AdopterDTO;
import co.edu.udistrital.mdp.pets.dto.AdoptionDTO;
import co.edu.udistrital.mdp.pets.dto.MessageDTO;
import co.edu.udistrital.mdp.pets.dto.NotificationDTO;
import co.edu.udistrital.mdp.pets.dto.PetDetailDTO;
import co.edu.udistrital.mdp.pets.dto.PetDTO;
import co.edu.udistrital.mdp.pets.dto.RelationSummaryDTO;
import co.edu.udistrital.mdp.pets.dto.ShelterDetailDTO;
import co.edu.udistrital.mdp.pets.dto.ShelterDTO;
import co.edu.udistrital.mdp.pets.dto.VeterinarianDTO;
import co.edu.udistrital.mdp.pets.dto.VeterinarianDetailDTO;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.MessageEntity;
import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;

public final class EntityDtoMapper {
    private EntityDtoMapper() {
    }

    public static PetDTO toDto(PetEntity entity) {
        if (entity == null) {
            return null;
        }
        PetDTO dto = new PetDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setBreed(entity.getBreed());
        dto.setBornDate(entity.getBornDate());
        dto.setSex(entity.getSex());
        dto.setSize(entity.getSize());
        dto.setTemperament(entity.getTemperament());
        dto.setSpecificNeeds(entity.getSpecificNeeds());
        dto.setIsRescued(entity.getIsRescued());
        dto.setStatus(entity.getStatus());
        if (entity.getShelter() != null) {
            dto.setShelterId(entity.getShelter().getId());
            dto.setShelterName(entity.getShelter().getShelterName());
        }
        if (entity.getMedicalHistory() != null) {
            dto.setMedicalHistoryId(entity.getMedicalHistory().getId());
        }
        dto.setAdoptionRequestsCount(entity.getAdoptionRequests() == null ? 0 : entity.getAdoptionRequests().size());
        return dto;
    }

    public static PetEntity toEntity(PetDTO dto) {
        if (dto == null) {
            return null;
        }
        PetEntity entity = new PetEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setBreed(dto.getBreed());
        entity.setBornDate(dto.getBornDate());
        entity.setSex(dto.getSex());
        entity.setSize(dto.getSize());
        entity.setTemperament(dto.getTemperament());
        entity.setSpecificNeeds(dto.getSpecificNeeds());
        entity.setIsRescued(dto.getIsRescued());
        entity.setStatus(dto.getStatus());
        if (dto.getShelterId() != null) {
            ShelterEntity shelter = new ShelterEntity();
            shelter.setId(dto.getShelterId());
            entity.setShelter(shelter);
        }
        return entity;
    }

    public static List<PetDTO> toPetDtoList(List<PetEntity> entities) {
        return entities.stream().map(EntityDtoMapper::toDto).collect(Collectors.toList());
    }

    public static PetDetailDTO toDetailDto(PetEntity entity) {
        if (entity == null) {
            return null;
        }
        PetDetailDTO dto = new PetDetailDTO();
        PetDTO base = toDto(entity);
        dto.setId(base.getId());
        dto.setName(base.getName());
        dto.setBreed(base.getBreed());
        dto.setBornDate(base.getBornDate());
        dto.setSex(base.getSex());
        dto.setSize(base.getSize());
        dto.setTemperament(base.getTemperament());
        dto.setSpecificNeeds(base.getSpecificNeeds());
        dto.setIsRescued(base.getIsRescued());
        dto.setStatus(base.getStatus());
        dto.setShelterId(base.getShelterId());
        dto.setShelterName(base.getShelterName());
        dto.setMedicalHistoryId(base.getMedicalHistoryId());
        dto.setAdoptionRequestsCount(base.getAdoptionRequestsCount());

        if (entity.getAdoptionRequests() != null) {
        dto.setAdoptionRequests(entity.getAdoptionRequests().stream()
                .map(ar -> summary(ar.getId(), String.valueOf(ar.getStatus()))) 
                .collect(Collectors.toList()));
    }
        if (entity.getAdoptions() != null) {
            dto.setAdoptions(entity.getAdoptions().stream()
                    .map(a -> summary(a.getId(), a.getStatus() == null ? null : a.getStatus().name()))
                    .collect(Collectors.toList()));
        }
        if (entity.getMedicalHistory() != null && entity.getMedicalHistory().getMedicalEvents() != null) {
            dto.setMedicalEvents(entity.getMedicalHistory().getMedicalEvents().stream()
                    .map(e -> summary(e.getId(), e.getEventType()))
                    .collect(Collectors.toList()));
        }
        if (entity.getMedicalHistory() != null && entity.getMedicalHistory().getVaccineEntries() != null) {
            dto.setVaccines(entity.getMedicalHistory().getVaccineEntries().stream()
                    .map(v -> summary(v.getId(), v.getVaccineName()))
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public static AdopterDTO toDto(AdopterEntity entity) {
        if (entity == null) {
            return null;
        }
        AdopterDTO dto = new AdopterDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setProfileImageUrl(entity.getProfileImageUrl());
        dto.setRegisterDate(entity.getRegisterDate());
        dto.setAddress(entity.getAddress());
        dto.setCity(entity.getCity());
        dto.setHousingType(entity.getHousingType());
        dto.setStatus(entity.getStatus());
        dto.setAdoptionRequestsCount(entity.getAdoptionRequests() == null ? 0 : entity.getAdoptionRequests().size());
        dto.setReviewsCount(entity.getReviews() == null ? 0 : entity.getReviews().size());
        dto.setSentMessagesCount(entity.getSentMessages() == null ? 0 : entity.getSentMessages().size());
        dto.setReceivedMessagesCount(entity.getReceivedMessages() == null ? 0 : entity.getReceivedMessages().size());
        dto.setNotificationsCount(entity.getNotifications() == null ? 0 : entity.getNotifications().size());
        return dto;
    }

    public static AdopterEntity toEntity(AdopterDTO dto) {
        if (dto == null) {
            return null;
        }
        AdopterEntity entity = new AdopterEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setProfileImageUrl(dto.getProfileImageUrl());
        entity.setRegisterDate(dto.getRegisterDate());
        entity.setAddress(dto.getAddress());
        entity.setCity(dto.getCity());
        entity.setHousingType(dto.getHousingType());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public static List<AdopterDTO> toAdopterDtoList(List<AdopterEntity> entities) {
        return entities.stream().map(EntityDtoMapper::toDto).collect(Collectors.toList());
    }

    public static AdopterDetailDTO toDetailDto(AdopterEntity entity) {
        if (entity == null) {
            return null;
        }
        AdopterDetailDTO dto = new AdopterDetailDTO();
        AdopterDTO base = toDto(entity);
        dto.setId(base.getId());
        dto.setName(base.getName());
        dto.setEmail(base.getEmail());
        dto.setPhoneNumber(base.getPhoneNumber());
        dto.setProfileImageUrl(base.getProfileImageUrl());
        dto.setRegisterDate(base.getRegisterDate());
        dto.setAddress(base.getAddress());
        dto.setCity(base.getCity());
        dto.setHousingType(base.getHousingType());
        dto.setStatus(base.getStatus());
        dto.setAdoptionRequestsCount(base.getAdoptionRequestsCount());
        dto.setReviewsCount(base.getReviewsCount());
        dto.setSentMessagesCount(base.getSentMessagesCount());
        dto.setReceivedMessagesCount(base.getReceivedMessagesCount());
        dto.setNotificationsCount(base.getNotificationsCount());

        if (entity.getAdoptions() != null) {
            dto.setAdoptions(entity.getAdoptions().stream()
                    .map(a -> summary(a.getId(), a.getStatus() == null ? null : a.getStatus().name()))
                    .collect(Collectors.toList()));
        }
        if (entity.getAdoptionRequests() != null) {
            dto.setAdoptionRequests(entity.getAdoptionRequests().stream()
                    .map(ar -> summary(ar.getId(), ar.getStatus()))
                    .collect(Collectors.toList()));
        }
        if (entity.getReviews() != null) {
            dto.setReviews(entity.getReviews().stream()
                    .map(r -> summary(r.getId(), r.getComment()))
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public static ShelterDTO toDto(ShelterEntity entity) {
        if (entity == null) {
            return null;
        }
        ShelterDTO dto = new ShelterDTO();
        dto.setId(entity.getId());
        dto.setShelterName(entity.getShelterName());
        dto.setNit(entity.getNit());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());
        dto.setStatus(entity.getStatus());
        dto.setCity(entity.getCity());
        dto.setLocationDetails(entity.getLocationDetails());
        dto.setDescription(entity.getDescription());
        dto.setWebsiteUrl(entity.getWebsiteUrl());
        dto.setVeterinariansCount(entity.getVeterinarians() == null ? 0 : entity.getVeterinarians().size());
        dto.setMediaItemsCount(entity.getMediaItems() == null ? 0 : entity.getMediaItems().size());
        dto.setReviewsCount(entity.getReviews() == null ? 0 : entity.getReviews().size());
        return dto;
    }

    public static ShelterEntity toEntity(ShelterDTO dto) {
        if (dto == null) {
            return null;
        }
        ShelterEntity entity = new ShelterEntity();
        entity.setId(dto.getId());
        entity.setShelterName(dto.getShelterName());
        entity.setNit(dto.getNit());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setAddress(dto.getAddress());
        entity.setStatus(dto.getStatus());
        entity.setCity(dto.getCity());
        entity.setLocationDetails(dto.getLocationDetails());
        entity.setDescription(dto.getDescription());
        entity.setWebsiteUrl(dto.getWebsiteUrl());
        return entity;
    }

    public static List<ShelterDTO> toShelterDtoList(List<ShelterEntity> entities) {
        return entities.stream().map(EntityDtoMapper::toDto).collect(Collectors.toList());
    }

    public static ShelterDetailDTO toDetailDto(ShelterEntity entity) {
        if (entity == null) {
            return null;
        }
        ShelterDetailDTO dto = new ShelterDetailDTO();
        ShelterDTO base = toDto(entity);
        dto.setId(base.getId());
        dto.setShelterName(base.getShelterName());
        dto.setNit(base.getNit());
        dto.setPhoneNumber(base.getPhoneNumber());
        dto.setAddress(base.getAddress());
        dto.setStatus(base.getStatus());
        dto.setCity(base.getCity());
        dto.setLocationDetails(base.getLocationDetails());
        dto.setDescription(base.getDescription());
        dto.setWebsiteUrl(base.getWebsiteUrl());
        dto.setVeterinariansCount(base.getVeterinariansCount());
        dto.setMediaItemsCount(base.getMediaItemsCount());
        dto.setReviewsCount(base.getReviewsCount());

        if (entity.getVeterinarians() != null) {
            dto.setVeterinarians(entity.getVeterinarians().stream()
                    .map(v -> summary(v.getId(), v.getName()))
                    .collect(Collectors.toList()));
        }
        if (entity.getMediaItems() != null) {
            dto.setMediaItems(entity.getMediaItems().stream()
                    .map(m -> summary(m.getId(), m.getMediaType()))
                    .collect(Collectors.toList()));
        }
        if (entity.getReviews() != null) {
            dto.setReviews(entity.getReviews().stream()
                    .map(r -> summary(r.getId(), r.getComment()))
                    .collect(Collectors.toList()));
        }
        if (entity.getAdoptions() != null) {
            dto.setAdoptions(entity.getAdoptions().stream()
                    .map(a -> summary(a.getId(), a.getStatus() == null ? null : a.getStatus().name()))
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public static AdoptionDTO toDto(AdoptionEntity entity) {
        if (entity == null) {
            return null;
        }
        AdoptionDTO dto = new AdoptionDTO();
        dto.setId(entity.getId());
        dto.setOfficialDate(entity.getOfficialDate());
        dto.setContractSigned(entity.getContractSigned());
        dto.setStatus(entity.getStatus() == null ? null : entity.getStatus().name());
        if (entity.getAdopter() != null) {
            dto.setAdopterId(entity.getAdopter().getId());
            dto.setAdopterName(entity.getAdopter().getName());
        }
        if (entity.getPet() != null) {
            dto.setPetId(entity.getPet().getId());
            dto.setPetName(entity.getPet().getName());
        }
        if (entity.getShelter() != null) {
            dto.setShelterId(entity.getShelter().getId());
            dto.setShelterName(entity.getShelter().getShelterName());
        }
        if (entity.getTrialCohabitation() != null) {
            dto.setTrialCohabitationId(entity.getTrialCohabitation().getId());
        }
        return dto;
    }

    public static AdoptionEntity toEntity(AdoptionDTO dto) {
        if (dto == null) {
            return null;
        }
        AdoptionEntity entity = new AdoptionEntity();
        entity.setId(dto.getId());
        entity.setOfficialDate(dto.getOfficialDate());
        entity.setContractSigned(dto.getContractSigned());
        if (dto.getAdopterId() != null) {
            AdopterEntity adopter = new AdopterEntity();
            adopter.setId(dto.getAdopterId());
            entity.setAdopter(adopter);
        }
        if (dto.getPetId() != null) {
            PetEntity pet = new PetEntity();
            pet.setId(dto.getPetId());
            entity.setPet(pet);
        }
        if (dto.getShelterId() != null) {
            ShelterEntity shelter = new ShelterEntity();
            shelter.setId(dto.getShelterId());
            entity.setShelter(shelter);
        }
        if (dto.getTrialCohabitationId() != null) {
            TrialCohabitationEntity trial = new TrialCohabitationEntity();
            trial.setId(dto.getTrialCohabitationId());
            entity.setTrialCohabitation(trial);
        }
        return entity;
    }

    public static List<AdoptionDTO> toAdoptionDtoList(List<AdoptionEntity> entities) {
        return entities.stream().map(EntityDtoMapper::toDto).collect(Collectors.toList());
    }

    public static VeterinarianDTO toDto(VeterinarianEntity entity) {
        if (entity == null) {
            return null;
        }
        VeterinarianDTO dto = new VeterinarianDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setProfileImageUrl(entity.getProfileImageUrl());
        dto.setRegisterDate(entity.getRegisterDate());
        dto.setLicenseNumber(entity.getLicenseNumber());
        dto.setAvailabilitySchedule(entity.getAvailabilitySchedule());
        if (entity.getShelter() != null) {
            dto.setShelterId(entity.getShelter().getId());
            dto.setShelterName(entity.getShelter().getShelterName());
        }
        dto.setSpecialitiesCount(entity.getSpecialities() == null ? 0 : entity.getSpecialities().size());
        dto.setMedicalHistoriesCount(entity.getMedicalHistories() == null ? 0 : entity.getMedicalHistories().size());
        dto.setMedicalEventsCount(entity.getMedicalEvents() == null ? 0 : entity.getMedicalEvents().size());
        dto.setSentMessagesCount(entity.getSentMessages() == null ? 0 : entity.getSentMessages().size());
        dto.setReceivedMessagesCount(entity.getReceivedMessages() == null ? 0 : entity.getReceivedMessages().size());
        dto.setNotificationsCount(entity.getNotifications() == null ? 0 : entity.getNotifications().size());
        return dto;
    }

    public static VeterinarianEntity toEntity(VeterinarianDTO dto) {
        if (dto == null) {
            return null;
        }
        VeterinarianEntity entity = new VeterinarianEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setProfileImageUrl(dto.getProfileImageUrl());
        entity.setRegisterDate(dto.getRegisterDate());
        entity.setLicenseNumber(dto.getLicenseNumber());
        entity.setAvailabilitySchedule(dto.getAvailabilitySchedule());
        if (dto.getShelterId() != null) {
            ShelterEntity shelter = new ShelterEntity();
            shelter.setId(dto.getShelterId());
            entity.setShelter(shelter);
        }
        return entity;
    }

    public static VeterinarianDetailDTO toDetailDto(VeterinarianEntity entity) {
        if (entity == null) {
            return null;
        }
        VeterinarianDetailDTO dto = new VeterinarianDetailDTO();
        VeterinarianDTO base = toDto(entity);
        dto.setId(base.getId());
        dto.setName(base.getName());
        dto.setEmail(base.getEmail());
        dto.setPhoneNumber(base.getPhoneNumber());
        dto.setProfileImageUrl(base.getProfileImageUrl());
        dto.setRegisterDate(base.getRegisterDate());
        dto.setLicenseNumber(base.getLicenseNumber());
        dto.setAvailabilitySchedule(base.getAvailabilitySchedule());
        dto.setShelterId(base.getShelterId());
        dto.setShelterName(base.getShelterName());
        dto.setSpecialitiesCount(base.getSpecialitiesCount());
        dto.setMedicalHistoriesCount(base.getMedicalHistoriesCount());
        dto.setMedicalEventsCount(base.getMedicalEventsCount());
        dto.setSentMessagesCount(base.getSentMessagesCount());
        dto.setReceivedMessagesCount(base.getReceivedMessagesCount());
        dto.setNotificationsCount(base.getNotificationsCount());

        if (entity.getSpecialities() != null) {
            dto.setSpecialities(entity.getSpecialities().stream()
                    .map(s -> summary(s.getId(), s.getName()))
                    .collect(Collectors.toList()));
        }
        if (entity.getMedicalHistories() != null) {
            dto.setMedicalHistories(entity.getMedicalHistories().stream()
                    .map(h -> summary(h.getId(), h.getCreatedDate() == null ? null : h.getCreatedDate().toString()))
                    .collect(Collectors.toList()));
        }
        if (entity.getMedicalEvents() != null) {
            dto.setMedicalEvents(entity.getMedicalEvents().stream()
                    .map(e -> summary(e.getId(), e.getEventType()))
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public static List<VeterinarianDTO> toVeterinarianDtoList(List<VeterinarianEntity> entities) {
        return entities.stream().map(EntityDtoMapper::toDto).collect(Collectors.toList());
    }

    public static MessageDTO toDto(MessageEntity entity) {
        if (entity == null) return null;
        MessageDTO dto = new MessageDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setSentAt(entity.getSentAt());
        dto.setIsRead(entity.getIsRead());
        dto.setActive(entity.getActive());
        
        if (entity.getSender() != null) {
            dto.setSenderId(entity.getSender().getId());
            dto.setSenderName(entity.getSender().getName());
        }
        if (entity.getReceiver() != null) {
            dto.setReceiverId(entity.getReceiver().getId());
            dto.setReceiverName(entity.getReceiver().getName());
        }
        return dto;
    }

    public static NotificationDTO toDto(NotificationEntity entity) {
    if (entity == null) {
        return null;
    }
    NotificationDTO dto = new NotificationDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setIsRead(entity.getIsRead());
        
        // Mapeo seguro del usuario
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUserName(entity.getUser().getName());
        }
        return dto;
    }

    public static List<NotificationDTO> toNotificationDtoList(List<NotificationEntity> entities) {
        if (entities == null) return new ArrayList<>();
        return entities.stream()
                    .map(EntityDtoMapper::toDto)
                    .collect(Collectors.toList());
    }

    private static RelationSummaryDTO summary(Long id, String name) {
        return new RelationSummaryDTO(id, name);
    }
}
