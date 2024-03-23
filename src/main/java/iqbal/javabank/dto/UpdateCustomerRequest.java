package iqbal.javabank.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCustomerRequest {

    @JsonIgnore
    private String id;

    @NotEmpty(message = "First Name not be empty!")
    @JsonProperty("fist_name")
    private String firstName;

    @NotEmpty(message = "Last Name not be empty!")
    @JsonProperty("last_name")
    private String lastName;

    @NotEmpty(message = "Phone Number not be empty!")
    @JsonFormat(pattern = "^08[0-9]{8,11}$")
    @Size(max = 13, message = "phoneNumber max 13")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotEmpty(message = "Address not be empty!")
    private String address;
}
