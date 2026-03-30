package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.ShelterMediaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ShelterMediaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShelterMediaService {

    private static final String MEDIA_ID_PREFIX = "El media con ID ";
    private static final String NOT_FOUND_SUFFIX = " no fue encontrado";
    private static final String MEDIA_ID_NULL_MSG = "Media id cannot be null";

    private final ShelterMediaRepository shelterMediaRepository;

    public ShelterMediaService(ShelterMediaRepository shelterMediaRepository) {
        this.shelterMediaRepository = shelterMediaRepository;
    }

    @Transactional
    public ShelterMediaEntity createShelterMedia(ShelterMediaEntity mediaItems, long fileSizeBytes)
            throws IllegalOperationException {
        log.info("Creating shelter media");

        if (mediaItems.getMediaUrl() == null || mediaItems.getMediaUrl().trim().isEmpty()) {
            throw new IllegalOperationException("La URL/nombre del archivo es obligatoria");
        }

        String url = mediaItems.getMediaUrl().toLowerCase();
        if (!url.endsWith(".jpg") && !url.endsWith(".png") && !url.endsWith(".mp4")) {
            throw new IllegalOperationException("Formato no permitido. Solo se permiten .jpg, .png y .mp4");
        }

        long maxSizeBytes = 5L * 1024L * 1024L;
        if (fileSizeBytes > maxSizeBytes) {
            throw new IllegalOperationException("El tamaño del archivo no debe exceder los 5MB");
        }

        return shelterMediaRepository.save(mediaItems);
    }

    @Transactional(readOnly = true)
    public ShelterMediaEntity searchShelterMedia(Long id) throws EntityNotFoundException {
        log.info("Searching shelter media with id: {}", id);
        Objects.requireNonNull(id, MEDIA_ID_NULL_MSG);
        return shelterMediaRepository.findById(id).orElseThrow(() -> notFound(id));
    }

    @Transactional(readOnly = true)
    public List<ShelterMediaEntity> searchShelterMediasByShelterId(Long shelterId) {
        log.info("Searching shelter medias by shelter id: {}", shelterId);
        Objects.requireNonNull(shelterId, "Shelter id cannot be null");
        return shelterMediaRepository.findByShelterId(shelterId);
    }

    @Transactional
    @SuppressWarnings("null")
    public ShelterMediaEntity updateShelterMedia(Long id, ShelterMediaEntity updatedMedia)
            throws EntityNotFoundException {
        log.info("Updating shelter media with id: {}", id);
        Objects.requireNonNull(id, MEDIA_ID_NULL_MSG);
        ShelterMediaEntity existing = shelterMediaRepository.findById(id).orElseThrow(() -> notFound(id));

        if (updatedMedia.getDescription() != null) {
            existing.setDescription(updatedMedia.getDescription());
        }

        return shelterMediaRepository.save(existing);
    }

    @Transactional
    public void deleteShelterMedia(Long id) throws EntityNotFoundException, IllegalOperationException {
        log.info("Deleting shelter media with id: {}", id);
        Objects.requireNonNull(id, MEDIA_ID_NULL_MSG);
        ShelterMediaEntity existing = shelterMediaRepository.findById(id).orElseThrow(() -> notFound(id));

        if ("Foto de Perfil".equalsIgnoreCase(existing.getMediaType())) {
            if (existing.getShelter() == null) {
                throw new IllegalOperationException("El media no tiene un refugio asociado válido.");
            }
            int count = shelterMediaRepository.countByShelterIdAndMediaType(existing.getShelter().getId(),
                    "Foto de Perfil");
            if (count <= 1) {
                throw new IllegalOperationException(
                        "Debe existir una imagen de respaldo antes de permitir la eliminación de la Foto de Perfil");
            }
        }

        shelterMediaRepository.deleteById(id);
    }

    private EntityNotFoundException notFound(Long id) {
        return new EntityNotFoundException(MEDIA_ID_PREFIX + id + NOT_FOUND_SUFFIX);
    }
}
