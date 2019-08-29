package com.slabs.exchange.service.fore;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;

public interface IInvitationRecordService {
    ResponseBean list(PageParamDto pageParamDto);

    ResponseBean getDetail(int userId);

    ResponseBean getMyInvitation(PageParamDto pageParamDto);
}
