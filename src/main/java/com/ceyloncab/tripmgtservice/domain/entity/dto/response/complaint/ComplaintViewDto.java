package com.ceyloncab.tripmgtservice.domain.entity.dto.response.complaint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintViewDto {
    private String subject;
    private String description;
    private Date updatedDate;
}
