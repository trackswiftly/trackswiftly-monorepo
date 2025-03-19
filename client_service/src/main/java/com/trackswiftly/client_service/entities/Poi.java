package com.trackswiftly.client_service.entities;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.jdbc.JsonJdbcType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.databind.JsonNode;


@Data @EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "pois", uniqueConstraints = {
        @UniqueConstraint(name = "pois_tenant_id_name_key", columnNames = {"tenant_id", "name"})
})
public class Poi extends AbstractBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 64)
    private String tenantId;

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

    
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> payload;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}