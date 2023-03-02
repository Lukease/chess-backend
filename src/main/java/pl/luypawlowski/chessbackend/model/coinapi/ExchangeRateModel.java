package pl.luypawlowski.chessbackend.model.coinapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeRateModel {
    private LocalDate time;
    @JsonProperty(value = "asset_id_base")
    private String assetIdBase;
    @JsonProperty(value = "asset_id_quote")
    private String assetIdQuote;
    private double rate;
}