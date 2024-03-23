package iqbal.javabank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
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
public class TransferRequest {

    @NotNull(message = "Account Id not be null!")
    @JsonProperty("account_id")
    private String accountId;

    @NotNull(message = "Number Rekening not be null!")
    @JsonProperty("number_rekening")
    private Long noRek;

    @NotNull(message = "Amount not be null!")
    @DecimalMin(value = "10000.00", message = "Amount must be at least 10000.00")
    private BigDecimal amount;
}
