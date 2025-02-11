package com.trackswiftly.vehicle_service.entities;



import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.trackswiftly.vehicle_service.enums.EngineType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
        name = "vehicles" ,
        indexes = {
            @Index(columnList = "tenantId" , name = "vehicle_tenantId_idx")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"tenantId" , "vin"})
        }
)
@Data  @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicle extends AbstractBaseEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "vehicle_seq")
    @SequenceGenerator(name = "vehicle_seq" , sequenceName = "vehicle_id_seq"  , allocationSize = 1)
    private Long id ;


    @Column(nullable = false)
    private String vin ;


    private String licensePlate ;


    @Column(nullable = false)
    private int mileage ;


    private Instant purchaseDate ;


    
    @JoinColumn(nullable = false)
    private Group group ;


    @JoinColumn(nullable = false)
    private VehicleType type ;


    @JoinColumn(nullable = false)
    private Model model ;



    @CreationTimestamp
    private Instant createdAt ;

    @UpdateTimestamp
    private Instant updatedAt ;






    
}
