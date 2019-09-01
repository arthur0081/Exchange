package com.slabs.exchange.controller.fore;


import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.service.fore.IInvitationRecordService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("invitation-record")
@Api(value = "邀请记录模块", description = "邀请记录：邀请记录相关接口")
public class InvitationRecordController {

    @Resource
    IInvitationRecordService iInvitationRecordService;

    /**
     * 邀请列表
     */
    @PostMapping("back/list")
    public ResponseBean list(@RequestBody PageParamDto pageParamDto) {
        return iInvitationRecordService.list(pageParamDto);
    }

    /**
     * 具体某个用户的邀请详情
     */
    @GetMapping("back/detail")
    public ResponseBean getDetail(@RequestParam int userId) {
        return iInvitationRecordService.getDetail(userId);
    }

    /**
     * 我的邀请列表
     */
    @PostMapping("fore/my-list")
    public ResponseBean getMyInvitation(@RequestBody PageParamDto pageParamDto) {
        return iInvitationRecordService.getMyInvitation(pageParamDto);
    }


}
