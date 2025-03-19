package com.trackswiftly.client_service.entities;

import java.time.Instant;
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
        name = "poi_types" ,
        indexes = {
            @Index(columnList = "tenantId" , name = "poi_type_tenantid_idx")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"tenantId" , "name"})
        }
)
@Data  @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor @AllArgsConstructor @Builder
public class PoiType extends AbstractBaseEntity{
    

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "poi_type_seq")
    @SequenceGenerator(name = "poi_type_seq" , sequenceName = "poi_type_id_seq"  , allocationSize = 1)
    private Long id ;


    @Column(nullable = false)
    private String name ;

    private String description ;


    @OneToMany(mappedBy = "type")
    @JsonBackReference
    private List<Poi> pois ;

    @CreationTimestamp
    private Instant createdAt ;

    @UpdateTimestamp
    private Instant updatedAt ;
}