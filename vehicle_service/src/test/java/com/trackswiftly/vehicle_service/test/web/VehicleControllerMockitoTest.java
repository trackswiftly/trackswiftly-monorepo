package com.trackswiftly.vehicle_service.test.web;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.trackswiftly.vehicle_service.dtos.OperationResult;
import com.trackswiftly.vehicle_service.dtos.PageDTO;
import com.trackswiftly.vehicle_service.dtos.VehicleRequest;
import com.trackswiftly.vehicle_service.dtos.VehicleResponse;
import com.trackswiftly.vehicle_service.enums.VehicleTypeEnum;
import com.trackswiftly.vehicle_service.services.VehicleService;
import com.trackswiftly.vehicle_service.web.VehicleController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisabledInNativeImage
class VehicleControllerMockitoTest {


    private MockMvc mockMvc;

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    private final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule()); // Critical for Instant support


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).build();
    }


    // ====== CREATE VEHICLES ======
    @Test
    void createVehiclesValidRequestReturns200() throws Exception {

        VehicleRequest request = VehicleRequest.builder()
            .vin("1HGCM82633A123456")
            .licensePlate("ABC123")
            .mileage(10000)
            .purchaseDate(Instant.now())
            .vehicleType(VehicleTypeEnum.TRUCK)
            .modelId(1L)
            .homeLocationId(1L)
            .vehicleGroupId(1L)
            .build();

        VehicleResponse response = VehicleResponse.builder()
            .vin("1HGCM82633A123456")
            .licensePlate("ABC123")
            .mileage(10000)
            .purchaseDate(Instant.now())
            .vehicleType(VehicleTypeEnum.TRUCK)
            .model(null)
            .homeLocation(null)
            .vhicleGroup(null)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();

        when(vehicleService.createVehicles(anyList())).thenReturn(List.of(response));

        mockMvc.perform(post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(request))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].vin").value("1HGCM82633A123456"))
            .andExpect(jsonPath("$[0].vehicleType").value("TRUCK"));
    }
    


    // ====== GET VEHICLES ======
    @Test
    void getVehiclesValidIdsReturns200() throws Exception {
        VehicleResponse response = VehicleResponse.builder()
            .id(1L)
            .vin("1HGCM82633A123456")
            .licensePlate("ABC123")
            .mileage(10000)
            .purchaseDate(Instant.now())
            .vehicleType(VehicleTypeEnum.TRUCK)
            .model(null)
            .homeLocation(null)
            .vhicleGroup(null)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();

        when(vehicleService.findVehicles(anyList())).thenReturn(List.of(response));

        mockMvc.perform(get("/vehicles/1,2,3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L));
    }


    // ====== DELETE VEHICLES ======
    @Test
    void deleteVehiclesValidIdsReturns200() throws Exception {
        when(vehicleService.deleteVehicles(anyList()))
            .thenReturn(OperationResult.of(3));

        mockMvc.perform(delete("/vehicles/1,2,3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.affectedRecords").value(3));
    }


    // ====== PAGINATION ======
    @Test
    void getVehiclesWithPaginationValidParamsReturns200() throws Exception {
        VehicleResponse response = VehicleResponse.builder()
            .id(1L)
            .vin("1HGCM82633A123456")
            .licensePlate("ABC123")
            .mileage(10000)
            .purchaseDate(Instant.now())
            .vehicleType(VehicleTypeEnum.TRUCK)
            .model(null)
            .homeLocation(null)
            .vhicleGroup(null)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
        

        PageDTO<VehicleResponse> page = new PageDTO<>(List.of(response) , 0, 10, 1, 1);
        
        when(vehicleService.getVehiclesWithPagination(anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/vehicles?page=0&pageSize=10"))
            .andExpect(status().isOk());
    }
}
