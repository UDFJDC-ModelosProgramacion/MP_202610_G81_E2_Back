package co.edu.udistrital.mdp.pets.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
class AdopterEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private AdopterEntity adopterEntity;

    @BeforeEach
    void setUp() {
        adopterEntity = factory.manufacturePojo(AdopterEntity.class);
    }

    @Test
    void testCreateAdopter() {
        AdopterEntity saved = entityManager.persistAndFlush(adopterEntity);
        AdopterEntity found = entityManager.find(AdopterEntity.class, saved.getId());

        assertNotNull(found);
        assertEquals(adopterEntity.getAddress(), found.getAddress());
        assertEquals(adopterEntity.getCity(), found.getCity());
        assertEquals(adopterEntity.getHousingType(), found.getHousingType());
    }

    @Test
    void testAdopterInheritsUserFields() {
        AdopterEntity saved = entityManager.persistAndFlush(adopterEntity);
        AdopterEntity found = entityManager.find(AdopterEntity.class, saved.getId());

        assertNotNull(found);
        assertEquals(adopterEntity.getEmail(), found.getEmail());
        assertEquals(adopterEntity.getPassword(), found.getPassword());
        assertEquals(adopterEntity.getPhoneNumber(), found.getPhoneNumber());
        assertEquals(adopterEntity.getProfileImageUrl(), found.getProfileImageUrl());
        assertEquals(adopterEntity.getRegisterDate(), found.getRegisterDate());
    }
}
