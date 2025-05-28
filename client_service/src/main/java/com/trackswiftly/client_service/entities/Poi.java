package com.trackswiftly.client_service.entities;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.trackswiftly.utils.enums.Capacity;
import com.trackswiftly.utils.enums.CapacityType;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "pois", uniqueConstraints = {
        @UniqueConstraint(name = "pois_tenant_id_name_key", columnNames = {"tenant_id", "name"})
})

@Data  @EqualsAndHashCode(callSuper = false)
@NoArgsConstructor @AllArgsConstructor @Builder
public class Poi extends AbstractBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "poi_seq")
    @SequenceGenerator(name = "poi_seq" , sequenceName = "poi_id_seq"  , allocationSize = 100)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false , name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(nullable = false , name = "type_id")
    private PoiType type;

    @Column(length = 255)
    private String address;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @Embedded
    private Capacity capacity;

    @Enumerated(EnumType.STRING)
    private CapacityType defaultCapacityType;

    @Column(name = "service_duration_seconds" , nullable = false)
    private Integer serviceDurationSeconds;

    @JdbcTypeCode(SqlTypes.JSON)
    private  Map<String, Object> payload;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}