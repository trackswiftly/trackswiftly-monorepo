package com.trackswiftly.client_service.entities;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.trackswiftly.client_service.dtos.routing.PlanningResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "planning_responses")

@Data  @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor @AllArgsConstructor @Builder
public class PlanningEntity extends AbstractBaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "planning_response_seq")
    @SequenceGenerator(name = "planning_response_seq" , sequenceName = "planning_response_id_seq"  , allocationSize = 50)
    private Long id;


    @Column(nullable = false, name = "planning_data")
    @JdbcTypeCode(SqlTypes.JSON)
    private  PlanningResponse planningData;


    @CreationTimestamp
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    private OffsetDateTime updatedAt;
}
