package com.trackswiftly.vehicle_service.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "home_locations" ,
    indexes = {
            @Index(columnList = "tenantId" , name = "home_location_tenantId_idx")
    },
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tenantId" , "name"})
    }
)
@Data @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class HomeLocation extends AbstractBaseEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "home_location_seq")
    @SequenceGenerator(name = "home_location_seq" , sequenceName = "home_location_id_seq"  , allocationSize = 1)
    private Long id ;


    @Column(nullable = false, length = 255)
    private String name;


    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;


    @OneToMany(mappedBy = "homeLocation")
    @JsonBackReference
    private List<Vehicle> vehicles ;


    @CreationTimestamp
    private LocalDateTime createdAt ;

    @UpdateTimestamp
    private LocalDateTime updatedAt ;
    
}
