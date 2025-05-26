package com.trackswiftly.vehicle_service.test.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.trackswiftly.vehicle_service.dtos.VehicleRequest;
import com.trackswiftly.vehicle_service.dtos.VehicleResponse;
import com.trackswiftly.vehicle_service.entities.Group;
import com.trackswiftly.vehicle_service.entities.HomeLocation;
import com.trackswiftly.vehicle_service.entities.Model;
import com.trackswiftly.vehicle_service.entities.Vehicle;
import com.trackswiftly.vehicle_service.entities.VehicleType;
import com.trackswiftly.vehicle_service.enums.Capacity;
import com.trackswiftly.vehicle_service.enums.EngineType;
import com.trackswiftly.vehicle_service.enums.FuelType;
import com.trackswiftly.vehicle_service.enums.VehicleTypeEnum;
import com.trackswiftly.vehicle_service.mappers.VehicleMapper;

class VehicleMapperTest {

    private VehicleMapper vehicleMapper;

    @BeforeEach
    void setUp() {
        vehicleMapper = new VehicleMapper();
    }
    

    @Test
    @DisplayName("Should map Vehicle to VehicleResponse successfully")
    void testToVehicleResponse() {
        // Given
        Vehicle vehicle = createTestVehicle();

        // When
        VehicleResponse response = vehicleMapper.toVehicleResponse(vehicle);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("1HGBH41JXMN109186", response.getVin());
        assertEquals("ABC-123", response.getLicensePlate());
        assertEquals(50000, response.getMileage());
        assertEquals(LocalDate.of(2023, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant(), response.getPurchaseDate());
        assertEquals(VehicleTypeEnum.HOV, response.getVehicleType());

        // Verify model mapping
        assertNotNull(response.getModel());
        assertEquals(1L, response.getModel().getId());
        assertEquals("Toyota Camry", response.getModel().getName());
        assertNotNull(response.getModel().getCapacity());
        assertEquals(2000.0, response.getModel().getCapacity().getMaxWeightKg());
        assertEquals(12.5, response.getModel().getCapacity().getMaxVolumeM3());
        assertEquals(10, response.getModel().getCapacity().getMaxBoxes());
        assertEquals(2, response.getModel().getCapacity().getMaxPallets());
        assertEquals(FuelType.DIESEL, response.getModel().getFuelType());
        assertEquals(EngineType.FUEL_CELL, response.getModel().getEngineType());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(response.getModel().getYear());
        assertEquals(2023, calendar.get(Calendar.YEAR));

        // Verify group mapping (note: typo in original code "vhicleGroup")
        assertNotNull(response.getVhicleGroup());
        assertEquals(1L, response.getVhicleGroup().getId());
        assertEquals("Fleet A", response.getVhicleGroup().getName());

        // Verify home location mapping
        assertNotNull(response.getHomeLocation());
        assertEquals(1L, response.getHomeLocation().getId());
        assertEquals("Main Depot", response.getHomeLocation().getName());
        assertEquals(40.7128, response.getHomeLocation().getLatitude());
        assertEquals(-74.0060, response.getHomeLocation().getLongitude());
        
    }

    @Test
    @DisplayName("Should map Vehicle to VehicleResponse with null home location")
    void testToVehicleResponseWithNullHomeLocation() {
        // Given
        Vehicle vehicle = createTestVehicle();
        vehicle.setHomeLocation(null);

        // When
        VehicleResponse response = vehicleMapper.toVehicleResponse(vehicle);

        // Then
        assertNotNull(response);
        assertNull(response.getHomeLocation());
        // Verify other fields are still mapped correctly
        assertEquals("1HGBH41JXMN109186", response.getVin());
        assertNotNull(response.getModel());
        assertNotNull(response.getVhicleGroup());
    }


    @Test
    @DisplayName("Should map VehicleRequest to Vehicle with null home location")
        void testToVehicleWithNullHomeLocation() {
        // Given
        VehicleRequest request = createTestVehicleRequest();
        request.setHomeLocationId(null);

        // When
        Vehicle vehicle = vehicleMapper.toVehicle(request);

        // Then
        assertNotNull(vehicle);
        assertNull(vehicle.getHomeLocation());
        // Verify other fields are still mapped correctly
        assertEquals("1HGBH41JXMN109186", vehicle.getVin());
        assertNotNull(vehicle.getModel());
        assertNotNull(vehicle.getVehicleGroup());
    }

    @Test
    @DisplayName("Should map list of Vehicles to VehicleResponse list")
    void testToVehicleResponseList() {
        // Given
        List<Vehicle> vehicles = Arrays.asList(
                createTestVehicle(),
                createSecondTestVehicle()
        );

        // When
        List<VehicleResponse> responses = vehicleMapper.toVehicleResponseList(vehicles);

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.size());
        
        // Verify first vehicle
        VehicleResponse first = responses.get(0);
        assertEquals("1HGBH41JXMN109186", first.getVin());
        assertEquals("ABC-123", first.getLicensePlate());
        
        // Verify second vehicle
        VehicleResponse second = responses.get(1);
        assertEquals("2HGBH41JXMN109187", second.getVin());
        assertEquals("DEF-456", second.getLicensePlate());
    }


    @Test
    @DisplayName("Should map list of VehicleRequests to Vehicle list")
    void testToVehicleList() {
        // Given
        List<VehicleRequest> requests = Arrays.asList(
                createTestVehicleRequest(),
                createSecondTestVehicleRequest()
        );

        // When
        List<Vehicle> vehicles = vehicleMapper.toVehicleList(requests);

        // Then
        assertNotNull(vehicles);
        assertEquals(2, vehicles.size());
        
        // Verify first vehicle
        Vehicle first = vehicles.get(0);
        assertEquals("1HGBH41JXMN109186", first.getVin());
        assertEquals("ABC-123", first.getLicensePlate());
        
        // Verify second vehicle
        Vehicle second = vehicles.get(1);
        assertEquals("2HGBH41JXMN109187", second.getVin());
        assertEquals("DEF-456", second.getLicensePlate());
    }

    @Test
    @DisplayName("Should handle empty lists")
    void testEmptyLists() {
            // Test empty vehicle list
            List<VehicleResponse> emptyResponses = vehicleMapper.toVehicleResponseList(Arrays.asList());
            assertNotNull(emptyResponses);
            assertTrue(emptyResponses.isEmpty());

            // Test empty request list
            List<Vehicle> emptyVehicles = vehicleMapper.toVehicleList(Arrays.asList());
            assertNotNull(emptyVehicles);
            assertTrue(emptyVehicles.isEmpty());
    }



    

    // Helper methods to create test objects
    private Vehicle createTestVehicle() {
        Model model = Model.builder()
                .id(1L)
                .name("Toyota Camry")
                .capacity(Capacity.builder()
                        .maxWeightKg(2000.0)
                        .maxVolumeM3(12.5)
                        .maxBoxes(10)
                        .maxPallets(2)
                        .build())
                .fuelType(FuelType.DIESEL)
                .engineType(EngineType.FUEL_CELL)
                .year(createDate(2023))
                .build();

        Group group = Group.builder()
                .id(1L)
                .name("Fleet A")
                .build();

        HomeLocation homeLocation = HomeLocation.builder()
                .id(1L)
                .name("Main Depot")
                .latitude(40.7128)
                .longitude(-74.0060)
                .build();

        return Vehicle.builder()
                .id(1L)
                .vin("1HGBH41JXMN109186")
                .licensePlate("ABC-123")
                .mileage(50000)
                .purchaseDate(LocalDate.of(2023, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant())
                .vehicleType(VehicleTypeEnum.HOV)
                .model(model)
                .vehicleGroup(group)
                .homeLocation(homeLocation)
                .build();
    }

    private Vehicle createSecondTestVehicle() {
        Model model = Model.builder()
                .id(2L)
                .name("Honda Accord")
                .capacity(Capacity.builder()
                        .maxWeightKg(1800.0)
                        .maxVolumeM3(10.0)
                        .maxBoxes(8)
                        .maxPallets(1)
                        .build())
                .fuelType(FuelType.CNG)
                .engineType(EngineType.INTERNAL_COMBUSTION)
                .year(createDate(2022))
                .build();

        Group group = Group.builder()
                .id(2L)
                .name("Fleet B")
                .build();

        return Vehicle.builder()
                .id(2L)
                .vin("2HGBH41JXMN109187")
                .licensePlate("DEF-456")
                .mileage(30000)
                .purchaseDate(LocalDate.of(2022, 6, 10).atStartOfDay(ZoneId.systemDefault()).toInstant())
                .vehicleType(VehicleTypeEnum.MOTORCYCLE)
                .model(model)
                .vehicleGroup(group)
                .homeLocation(null)
                .build();
    }

    private VehicleRequest createTestVehicleRequest() {
        VehicleRequest request = new VehicleRequest();
        request.setVin("1HGBH41JXMN109186");
        request.setLicensePlate("ABC-123");
        request.setMileage(50000);
        request.setPurchaseDate(LocalDate.of(2023, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        request.setVehicleType(VehicleTypeEnum.HOV);
        request.setModelId(1L);
        request.setVehicleGroupId(1L);
        request.setHomeLocationId(1L);
        return request;
    }

    private VehicleRequest createSecondTestVehicleRequest() {
        VehicleRequest request = new VehicleRequest();
        request.setVin("2HGBH41JXMN109187");
        request.setLicensePlate("DEF-456");
        request.setMileage(30000);
        request.setPurchaseDate(LocalDate.of(2022, 6, 10).atStartOfDay(ZoneId.systemDefault()).toInstant());
        request.setVehicleType(VehicleTypeEnum.MOTORCYCLE);
        request.setModelId(2L);
        request.setVehicleGroupId(2L);
        request.setHomeLocationId(null);
        return request;
    }

    private Date createDate(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

}
