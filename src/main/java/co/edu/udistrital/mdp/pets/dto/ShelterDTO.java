package co.edu.udistrital.mdp.pets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for ShelterEntity.
 * Prevents circular references by using counts/IDs instead of full object lists.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShelterDTO {
    private Long id;
    private String shelterName;
    private String nit;
    private String phoneNumber;
    private String address;
    private String status;
    private String city;
    private String locationDetails;
    private String description;
    private String websiteUrl;
    
    // Use counts instead of full lists to prevent cycles
    private Integer veterinariansCount = 0;
    private Integer mediaItemsCount = 0;
    private Integer reviewsCount = 0;
}
