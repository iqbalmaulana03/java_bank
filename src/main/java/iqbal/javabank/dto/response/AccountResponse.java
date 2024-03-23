package iqbal.javabank.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {

    private String id;

    private BigDecimal balance;

    @JsonProperty("account_type")
    private String type;

    @JsonProperty("number_rekening")
    private Long noRek;

    @JsonProperty("customer_id")
    private String customerId;

    @JsonProperty("fist_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("phone_number")
    private String phoneNumber;
}
