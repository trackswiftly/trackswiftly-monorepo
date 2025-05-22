package com.trackswiftly.vehicle_service.test.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.trackswiftly.vehicle_service.dtos.GroupRequest;
import com.trackswiftly.vehicle_service.dtos.GroupResponse;
import com.trackswiftly.vehicle_service.dtos.OperationResult;
import com.trackswiftly.vehicle_service.dtos.PageDTO;
import com.trackswiftly.vehicle_service.services.GroupService;
import com.trackswiftly.vehicle_service.web.GroupController;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GroupControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController groupController;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(groupController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }



    // ========== CREATE GROUPS ==========
    @Test
    void createGroupsValidRequestReturns200() throws Exception {

        GroupRequest request = GroupRequest.builder()
                .name("Test Group")
                .description("Group Description")
                .build();

        GroupResponse response = GroupResponse.builder()
                .id(1L)
                .name("Test Group")
                .description("Group Description")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        when(groupService.createGroups(anyList())).thenReturn(List.of(response));

        mockMvc.perform(post("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(request))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Group"));
    }


    // ========== DELETE GROUPS ==========
    @Test
    void deleteGroupsValidIdsReturns200() throws Exception {
        OperationResult result = new OperationResult(3, "Deleted successfully");
        when(groupService.deleteGroups(anyList())).thenReturn(result);

        mockMvc.perform(delete("/groups/1,2,3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.affectedRecords").value(3));
    }


    // ========== GET GROUPS BY IDS ==========
    @Test
    void findGroupsValidIdsReturns200() throws Exception {

        GroupResponse response = GroupResponse.builder()
                .id(1L)
                .name("Test Group")
                .description("Group Description")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        
        when(groupService.findGroups(anyList())).thenReturn(List.of(response));

        mockMvc.perform(get("/groups/1,2,3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }


    // ========== GET PAGINATED GROUPS ==========
    @Test
    void getGroupsWithPaginationValidParamsReturns200() throws Exception {
        
        GroupResponse response = GroupResponse.builder()
                .id(1L)
                .name("Test Group")
                .description("Group Description")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        PageDTO<GroupResponse> page = new PageDTO<>(List.of(response) , 0, 10, 1, 1);

        when(groupService.getGroupsWithPagination(anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/groups?page=0&pageSize=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test Group"));
    }


    // ========== UPDATE GROUPS ==========
    @Test
    void updateGroupsInBatchValidRequestReturns200() throws Exception {
        GroupRequest request = new GroupRequest("Updated Name", "Updated Desc");

        OperationResult result = new OperationResult(3, "Updated successfully");
        
        when(groupService.updategroupsInBatch(anyList(), any(GroupRequest.class))).thenReturn(result);

        mockMvc.perform(put("/groups/1,2,3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.affectedRecords").value(3));
    }
    
}
