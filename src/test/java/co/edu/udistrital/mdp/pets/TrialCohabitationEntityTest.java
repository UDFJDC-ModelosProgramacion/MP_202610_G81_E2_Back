package co.edu.udistrital.mdp.pets;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.entities.ReturnCaseEntity;
import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
class TrialCohabitationEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private TrialCohabitationEntity trialEntity;

    @BeforeEach
    void setUp() {
        trialEntity = factory.manufacturePojo(TrialCohabitationEntity.class);
    }

    @Test
    void testCreateTrialCohabitation() {
        TrialCohabitationEntity saved = entityManager.persistAndFlush(trialEntity);
        TrialCohabitationEntity found = entityManager.find(TrialCohabitationEntity.class, saved.getId());

        assertNotNull(found);
        assertEquals(trialEntity.getStartDate(), found.getStartDate());
        assertEquals(trialEntity.getEndDate(), found.getEndDate());
        assertEquals(trialEntity.getApproved(), found.getApproved());
    }

    @Test
    void testTrialCohabitationInheritsProcessFields() {
        TrialCohabitationEntity saved = entityManager.persistAndFlush(trialEntity);
        TrialCohabitationEntity found = entityManager.find(TrialCohabitationEntity.class, saved.getId());

        assertNotNull(found);
        assertEquals(trialEntity.getCreationDate(), found.getCreationDate());
        assertEquals(trialEntity.getStatus(), found.getStatus());
    }

    @Test
    void testTrialCohabitationWithAdoptionRequest() {
        AdoptionRequestEntity request = factory.manufacturePojo(AdoptionRequestEntity.class);
        entityManager.persistAndFlush(request);

        trialEntity.setAdoptionRequest(request);
        TrialCohabitationEntity saved = entityManager.persistAndFlush(trialEntity);

        TrialCohabitationEntity found = entityManager.find(TrialCohabitationEntity.class, saved.getId());
        assertNotNull(found);
        assertNotNull(found.getAdoptionRequest());
        assertEquals(request.getId(), found.getAdoptionRequest().getId());
    }

    @Test
    void testTrialCohabitationWithReturnCase() {
        TrialCohabitationEntity saved = entityManager.persistAndFlush(trialEntity);

        ReturnCaseEntity returnCase = factory.manufacturePojo(ReturnCaseEntity.class);
        returnCase.setAdoptionProcess(saved);
        entityManager.persistAndFlush(returnCase);

        entityManager.clear();

        TrialCohabitationEntity found = entityManager.find(TrialCohabitationEntity.class, saved.getId());
        assertNotNull(found);
        assertNotNull(found.getReturnCase());
        assertEquals(returnCase.getReason(), found.getReturnCase().getReason());
    }
}
