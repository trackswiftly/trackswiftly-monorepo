package com.trackswiftly.vehicle_service.entities;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.trackswiftly.vehicle_service.enums.EngineType;
import com.trackswiftly.vehicle_service.enums.FuelType;

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
    name = "models" ,
    indexes = {
            @Index(columnList = "tenantId" , name = "model_tenantId_idx")
    },
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tenantId" , "name"})
    }
)
@Data @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Model extends AbstractBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "model_seq")
    @SequenceGenerator(name = "model_seq" , sequenceName = "model_id_seq"  , allocationSize = 1)
    private Long id ;

    @Column(nullable = false)
    private String name ;

    @Column(nullable = false)
    private String make ;

    private Date year ;

    private EngineType engineType ;

    private FuelType fuelType ;

    private String transmission ;


    @Column(nullable = true)
    private double maxPayloadWeight ;

    @Column(nullable = true)
    private double maxVolume ;

    @OneToMany(mappedBy = "model")
    @JsonBackReference
    private List<Vehicle> vehicles ;



    @CreationTimestamp
    private Instant createdAt ;

    @UpdateTimestamp
    private Instant updatedAt ;
}
