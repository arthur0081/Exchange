package com.slabs.exchange.service.back.impl;

import com.slabs.exchange.common.enums.AttachEnum;
import com.slabs.exchange.common.enums.CodeEnum;
import com.slabs.exchange.common.enums.CoinEnum;
import com.slabs.exchange.common.enums.YNEnum;
import com.slabs.exchange.mapper.back.AttachFileMapper;
import com.slabs.exchange.mapper.ext.back.ProjectCoinExtMapper;
import com.slabs.exchange.mapper.back.ProjectCoinMapper;
import com.slabs.exchange.mapper.back.SymbolMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.AttachFileDto;
import com.slabs.exchange.model.dto.CoinDto;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.ProjectCoinDto;
import com.slabs.exchange.model.entity.AttachFile;
import com.slabs.exchange.model.entity.ProjectCoin;
import com.slabs.exchange.model.entity.Symbol;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.back.IProjectCoinService;
import com.slabs.exchange.util.ShiroUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ProjectCoinServiceImpl extends BaseService implements IProjectCoinService {
    @Resource
    private ProjectCoinMapper projectCoinMapper;
    @Resource
    private AttachFileMapper attachFileMapper;
    @Resource
    private SymbolMapper symbolMapper;
    @Resource
    private ProjectCoinExtMapper projectCoinExtMapper;


    @Override
    public ResponseBean insert(ProjectCoinDto projectCoinDto) {
        // 给项目币表存入基础信息
        ProjectCoin coin =  map(projectCoinDto, ProjectCoin.class);
       // coin.setPrecision(6L);
        // 关键点需要项目币表返回一个id
        projectCoinMapper.insert(coin);
        // 构建附件信息
        List<AttachFile> list = buildAttachFile(coin.getId(), projectCoinDto);
        // 批量插入附件信息
        attachFileMapper.batchInsert(list);

        return new ResponseBean(200, "新增成功",null);
    }

    /**
     * 构建附件信息
     */
    private List<AttachFile> buildAttachFile(Long coinId, ProjectCoinDto projectCoinDto) {
        List<AttachFileDto> list = projectCoinDto.getAttachFileList();
        List<AttachFile> attachFiles = new ArrayList<>();
        for (AttachFileDto afd: list) {
            AttachFile af = map(afd, AttachFile.class);
            af.setIsDel(YNEnum.N.getKey());
            af.setRefId(coinId.intValue());
            af.setType(AttachEnum.PRO_COIN.getKey());
            af.setCreateUser(ShiroUtils.getUserId().longValue());
            af.setCreateTime(new Date());
            attachFiles.add(af);
        }
        return attachFiles;
    }

    /**
     * 预修改
     */
    @Override
    public ResponseBean preUpdate(Integer coinId) {
        // 判断项目币是否被构建成币对
        Symbol symbol = symbolMapper.selectByCommodity(coinId);
        if (symbol != null) {
            return new ResponseBean(200, "该币已成币对，不能修改！", null);
        }

        // 根据coinId去project_coin中查询基础信息。
        ProjectCoin pc = projectCoinMapper.selectByPrimaryKey(coinId.longValue());
        ProjectCoinDto pcd = map(pc, ProjectCoinDto.class);
        // 根据coinId和type去附件表得到相关联的附件
        List<AttachFile> list = attachFileMapper.selectByTypeAndRefId(AttachEnum.PRO_COIN.getKey(), coinId.longValue());
        List<AttachFileDto> attachFileDtos = new ArrayList<>();
        for (AttachFile af: list) {
            AttachFileDto afd = map(af, AttachFileDto.class);
            attachFileDtos.add(afd);
        }
        pcd.setAttachFileList(attachFileDtos);

        return new ResponseBean(200, "", pcd);
    }

    /**
     * 修改
     */
    @Override
    public ResponseBean update(ProjectCoinDto projectCoinDto) {
        // 修改ProjectCoin表
        ProjectCoin pc = map(projectCoinDto, ProjectCoin.class);
        projectCoinMapper.updateByPrimaryKeySelective(pc);
        // 附件表处理
        // 给 type ref_id的数据进行逻辑删除
        attachFileMapper.deleteByTypeAndRefId(AttachEnum.PRO_COIN.getKey(), projectCoinDto.getId());
        // 批量插入
        List<AttachFile> list = buildAttachFile(projectCoinDto.getId(), projectCoinDto);
        attachFileMapper.batchInsert(list);
        return new ResponseBean(200, "", null);
    }

    /**
     * 项目币的列表查询
     */
    @Override
    public ResponseBean list(PageParamDto pageParamDto) {
        // 根据页面传递参数获取总数
        int total = projectCoinExtMapper.count();

        // 根据页面传递参数获取projectCoins
        int start = (pageParamDto.getCurrentPage() - 1) * pageParamDto.getPageSize();
        pageParamDto.setStart(start);
        List<CoinDto> projectCoins = projectCoinExtMapper.list(pageParamDto);

        // 构建返回信息
        ResponseBean res = BuildResponseBusinessDto(pageParamDto, total, projectCoins);

        return res;
    }

    /**
     * 构建返回信息
     */
    private ResponseBean BuildResponseBusinessDto(PageParamDto pageParamDto, int total, List<CoinDto> projectCoins) {
        Map<String, Object> data = new HashMap<>();
        data.put("list", projectCoins);
        data.put("total", total);
        data.put("currentPage", pageParamDto.getCurrentPage());
        data.put("pageSize", pageParamDto.getPageSize());
        data.put("code", CodeEnum.OK.getKey());

        return new ResponseBean(200, "", data);
    }

    /**
     * 得到所有的商品货币（本身是项目货币）
     */
    @Override
    public ResponseBean getProjectCoins() {
        List<ProjectCoin> list = projectCoinMapper.getProjectCoins();
        return new ResponseBean(200, "", list);
    }


    /**
     * 得到所有稳定币对
     */
    @Override
    public ResponseBean getStableSymbols() {
        List<CoinDto> list = projectCoinExtMapper.getSymbols(CoinEnum.USDT.getKey());
        return new ResponseBean(200, "", list);
    }

}
