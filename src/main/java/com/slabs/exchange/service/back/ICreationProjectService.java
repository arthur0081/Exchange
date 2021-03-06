package com.slabs.exchange.service.back;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.AuditDto;
import com.slabs.exchange.model.dto.BuyDto;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.ProjectDto;

public interface ICreationProjectService {
    ResponseBean insert(ProjectDto projectDto);

    ResponseBean preUpdate(Integer projectId);

    ResponseBean update(ProjectDto projectDto);

    ResponseBean list(PageParamDto pageParamDto);

    ResponseBean audit(AuditDto auditDto);

    ResponseBean getForeCreationProjectList(PageParamDto pageParamDto);

    ResponseBean getForeCreationProjectDetail(Long projectId);

    ResponseBean buy(BuyDto buyDto);
}
