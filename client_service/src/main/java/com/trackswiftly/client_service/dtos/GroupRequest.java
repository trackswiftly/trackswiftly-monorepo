package com.trackswiftly.client_service.dtos;




import com.trackswiftly.client_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.client_service.dtos.interfaces.UpdateValidationGroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupRequest {

    @NotNull(groups = CreateValidationGroup.class, message = "Name is required for create operation")
    @NotBlank(groups = {CreateValidationGroup.class}, message = "Name must not be blank")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name must only contain alphabetic characters", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    private String name ;

    @Size(min = 20, max = 255, message = "Description must be between 20 and 255 characters", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    @Pattern(regexp = "^[A-Za-z0-9\\s.,!?;:'\"()-]+$", message = "Description contains invalid characters", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    private String description ;
    
}
