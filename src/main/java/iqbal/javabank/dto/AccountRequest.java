package iqbal.javabank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequest {

    @JsonProperty("customer_id")
    @NotEmpty(message = "Customer Id not be empty!")
    private String customerId;

    @NotNull(message = "Balance not be null!")
    @DecimalMin(value = "100000.00", message = "Balance must be at least 100000.00")
    private BigDecimal balance;

    @NotEmpty(message = "Account Type not be empty!")
    @JsonProperty("account_type")
    private String type;
}
