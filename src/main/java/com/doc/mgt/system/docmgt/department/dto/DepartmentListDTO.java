package com.doc.mgt.system.docmgt.department.dto;

import com.doc.mgt.system.docmgt.general.dto.PageableResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DepartmentListDTO extends PageableResponseDTO {

    @JsonProperty("departments")
    private List<DepartmentDTO> departmentDTOList;
}
