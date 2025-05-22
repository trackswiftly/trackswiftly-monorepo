package com.trackswiftly.vehicle_service.test.mappers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.trackswiftly.vehicle_service.dtos.CapacityRequest;
import com.trackswiftly.vehicle_service.dtos.ModelRequest;
import com.trackswiftly.vehicle_service.dtos.ModelResponse;
import com.trackswiftly.vehicle_service.entities.Model;
import com.trackswiftly.vehicle_service.enums.Capacity;
import com.trackswiftly.vehicle_service.enums.CapacityType;
import com.trackswiftly.vehicle_service.enums.EngineType;
import com.trackswiftly.vehicle_service.enums.FuelType;
import com.trackswiftly.vehicle_service.mappers.ModelMapper;

class ModelMapperTest {

    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
    }


    @Test
    void toModelResponseWithCompleteModelReturnsCorrectResponse() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.parse("2023-01-01T00:00:00Z"), ZoneId.systemDefault());
        LocalDateTime updatedAt = LocalDateTime.ofInstant(Instant.parse("2023-06-01T00:00:00Z"), ZoneId.systemDefault());


        Date yearDate = Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        Model model = Model.builder()
                .id(1L)
                .name("Tesla Model 3")
                .make("Tesla")
                .fuelType(FuelType.ELECTRIC)
                .engineType(EngineType.ELECTRIC)
                .defaultCapacityType(CapacityType.BOX_COUNT)
                .capacity(Capacity.builder()
                        .maxWeightKg(500.0)
                        .maxVolumeM3(2.5)
                        .maxBoxes(10)
                        .maxPallets(0)
                        .build())
                .year(yearDate)
                .transmission("Automatic")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Act
        ModelResponse response = modelMapper.toModelResponse(model);

        // Assert
        assertAll(
                () -> assertEquals(1L, response.getId()),
                () -> assertEquals("Tesla Model 3", response.getName()),
                () -> assertEquals("Tesla", response.getMake()),
                () -> assertEquals(FuelType.ELECTRIC, response.getFuelType()),
                () -> assertEquals(EngineType.ELECTRIC, response.getEngineType()),
                () -> assertEquals(CapacityType.BOX_COUNT, response.getDefaultCapacityType()),
                () -> assertEquals(500.0, response.getCapacity().getMaxWeightKg()),
                () -> assertEquals(2.5, response.getCapacity().getMaxVolumeM3()),
                () -> assertEquals(10, response.getCapacity().getMaxBoxes()),
                () -> assertEquals(0, response.getCapacity().getMaxPallets()),
                () -> assertEquals(yearDate, response.getYear()),
                () -> assertEquals("Automatic", response.getTransmission()),
                () -> assertEquals(createdAt, response.getCreatedAt()),
                () -> assertEquals(updatedAt, response.getUpdatedAt())
        );
    }


    // ========== LIST MAPPING TESTS ==========

    @Test
    void toModelResponseList_WithMultipleModels_ReturnsCorrectResponses() {
        // Arrange
        List<Model> models = List.of(
                Model.builder().name("Model A").build(),
                Model.builder().name("Model B").build()
        );

        // Act
        List<ModelResponse> responses = modelMapper.toModelResponseList(models);

        // Assert
        assertEquals(2, responses.size());
        assertEquals("Model A", responses.get(0).getName());
        assertEquals("Model B", responses.get(1).getName());
    }


    @Test
    void toModelListWithMultipleRequestsReturnsCorrectEntities() {
        // Arrange
        List<ModelRequest> requests = List.of(
                // new ModelRequest("Model X", null, null, null, null, 0, null),
                // new ModelRequest("Model Y", null, null, null, null, 0, null)
                ModelRequest.builder()
                        .name("Model X")
                        .make("Make X")
                        .year(Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                        .engineType(EngineType.HYBRID)
                        .fuelType(FuelType.DIESEL)
                        .transmission("Automatic")

                        .capacity(CapacityRequest.builder()
                                .maxWeightKg(1000.0)
                                .maxVolumeM3(5.0)
                                .maxBoxes(20)
                                .maxPallets(10)
                                .build())
                        .defaultCapacityType(CapacityType.BOX_COUNT)
                        .build() ,
                ModelRequest.builder()
                        .name("Model Y")
                        .make("Make Y")
                        .year(Date.from(LocalDate.of(2023, 3, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                        .engineType(EngineType.ELECTRIC)
                        .fuelType(FuelType.PETROL)
                        .transmission("Manual")
                        .capacity(CapacityRequest.builder()
                                .maxWeightKg(1500.0)
                                .maxVolumeM3(7.5)
                                .maxBoxes(30)
                                .maxPallets(15)
                                .build())
                        .defaultCapacityType(CapacityType.BOX_COUNT)
                        .build()
        );

        // Act
        List<Model> models = modelMapper.toModelList(requests);

        // Assert
        assertEquals(2, models.size());

        // Verify first model
        Model modelX = models.get(0);
        assertAll("Model X fields",
            () -> assertEquals(EngineType.HYBRID, modelX.getEngineType()),
            () -> assertEquals(FuelType.DIESEL, modelX.getFuelType()),
            () -> assertEquals("Automatic", modelX.getTransmission()),
            () -> assertEquals(CapacityType.BOX_COUNT, modelX.getDefaultCapacityType()),
            () -> assertEquals(1000.0, modelX.getCapacity().getMaxWeightKg()),
            () -> assertEquals(5.0, modelX.getCapacity().getMaxVolumeM3()),
            () -> assertEquals(20, modelX.getCapacity().getMaxBoxes()),
            () -> assertEquals(10, modelX.getCapacity().getMaxPallets()),
            () -> assertNull(modelX.getId()), // Should not be set from request
            () -> assertNull(modelX.getCreatedAt()),
            () -> assertNull(modelX.getUpdatedAt())
        );


        // Verify second model
        Model modelY = models.get(1);
        assertAll("Model Y fields",
            () -> assertEquals("Model Y", modelY.getName()),
            () -> assertEquals("Make Y", modelY.getMake()),
            () -> assertEquals(Date.from(LocalDate.of(2023, 3, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), modelY.getYear()),
            () -> assertEquals(EngineType.ELECTRIC, modelY.getEngineType()),
            () -> assertEquals(FuelType.PETROL, modelY.getFuelType()),
            () -> assertEquals(CapacityType.BOX_COUNT, modelY.getDefaultCapacityType()),
            () -> assertEquals(1500.0, modelY.getCapacity().getMaxWeightKg()),
            () -> assertEquals(7.5, modelY.getCapacity().getMaxVolumeM3()),
            () -> assertEquals(30, modelY.getCapacity().getMaxBoxes()),
            () -> assertEquals(15, modelY.getCapacity().getMaxPallets())
        );

        // Verify the models are different instances
        assertNotSame(models.get(0), models.get(1));
    }


     // ========== EDGE CASES ==========

    @Test
    void toModelResponse_WithNullInput_ReturnsNull() {
        assertNull(modelMapper.toModelResponse(null));
    }

    @Test
    void toModel_WithNullInput_ReturnsNull() {
        assertNull(modelMapper.toModel(null));
    }

    @Test
    void toModelResponseList_WithEmptyList_ReturnsEmptyList() {
        assertTrue(modelMapper.toModelResponseList(List.of()).isEmpty());
    }
}
