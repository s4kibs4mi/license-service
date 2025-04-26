package ninja.sakib.licenseservice.shared.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiSuccessResp<T> {
    @JsonProperty("data")
    private T data;
}
