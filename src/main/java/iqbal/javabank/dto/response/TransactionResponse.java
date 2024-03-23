package iqbal.javabank.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {

    private String id;

    @JsonProperty("transaction_date")
    private Date transactionDate;

    private BigDecimal amount;

    @JsonProperty("transaction_type")
    private String types;

    @JsonProperty("number_rekening")
    private Long noRek;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;
}
