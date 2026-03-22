package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.ShelterMediaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ShelterMediaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ShelterMediaService {

    @Autowired
    private ShelterMediaRepository shelterMediaRepository;

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
        return shelterMediaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El media con ID " + id + " no fue encontrado"));
    }

    @Transactional(readOnly = true)
    public List<ShelterMediaEntity> searchShelterMediasByShelterId(Long shelterId) {
        log.info("Searching shelter medias by shelter id: {}", shelterId);
        return shelterMediaRepository.findByShelterId(shelterId);
    }

    @Transactional
    public ShelterMediaEntity updateShelterMedia(Long id, ShelterMediaEntity updatedMedia)
            throws EntityNotFoundException {
        log.info("Updating shelter media with id: {}", id);
        ShelterMediaEntity existing = shelterMediaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("El media con ID " + id + " no fue encontrado"));

        if (updatedMedia.getDescription() != null) {
            existing.setDescription(updatedMedia.getDescription());
        }

        return shelterMediaRepository.save(existing);
    }

    @Transactional
    public void deleteShelterMedia(Long id) throws EntityNotFoundException, IllegalOperationException {
        log.info("Deleting shelter media with id: {}", id);
        ShelterMediaEntity existing = shelterMediaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("El media con ID " + id + " no fue encontrado"));

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
}
