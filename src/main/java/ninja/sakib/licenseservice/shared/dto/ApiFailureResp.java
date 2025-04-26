package ninja.sakib.licenseservice.shared.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import ninja.sakib.licenseservice.shared.errorcodes.ErrorCode;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ApiFailureResp {
    @JsonProperty("error_message")
    private String errorMessage;
    @JsonProperty("error_code")
    private ErrorCode errorCode;
    private Map<String, List<String>> errors;
}
