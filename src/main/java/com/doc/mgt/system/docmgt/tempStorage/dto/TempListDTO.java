package com.doc.mgt.system.docmgt.tempStorage.dto;

import com.doc.mgt.system.docmgt.general.dto.PageableResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TempListDTO extends PageableResponseDTO {

    @JsonProperty("temps")
    private List<TempDTO> tempDTOList;
}
