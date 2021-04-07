package io.github.floriansw.ondus.api.model.blue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
public class Parameters {
    @JsonProperty("variant")
    @Getter
    private Integer variant;
    @JsonProperty("carbon_hardness")
    private Integer carbonHardness;
    @JsonProperty("filter_type")
    private Integer filterType;

    public void updateFilterType(FilterType type) {
        this.filterType = type.apiValue;
    }

    public FilterType filterType() {
        return FilterType.of(filterType);
    }

    public void updateCarbonHardness(CarbonHardness hardness) {
        this.carbonHardness = hardness.apiValue;
    }

    public CarbonHardness carbonHardness() {
        return CarbonHardness.of(carbonHardness);
    }

}
