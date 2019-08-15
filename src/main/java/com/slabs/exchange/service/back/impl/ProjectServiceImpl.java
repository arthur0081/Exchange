package com.slabs.exchange.service.back.impl;

import com.slabs.exchange.common.enums.*;
import com.slabs.exchange.common.exception.ExchangeException;
import com.slabs.exchange.mapper.back.*;
import com.slabs.exchange.mapper.ext.back.BoughtAmountExtMapper;
import com.slabs.exchange.mapper.ext.back.ProjectExtMapper;
import com.slabs.exchange.mapper.ext.fore.ForeProjectExtMapper;
import com.slabs.exchange.mapper.fore.UserFundMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.*;
import com.slabs.exchange.model.entity.*;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.back.IProjectService;
import com.slabs.exchange.util.ExchangePreconditions;
import com.slabs.exchange.util.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class ProjectServiceImpl extends BaseService implements IProjectService {
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private AttachFileMapper attachFileMapper;
    @Resource
    private ProjectExtMapper projectExtMapper;
    @Resource
    private SymbolMapper symbolMapper;
    @Resource
    private ForeProjectExtMapper foreProjectExtMapper;
    @Resource
    private BoughtAmountExtMapper boughtAmountExtMapper;
    @Resource
    private WalletMapper walletMapper;
    @Resource
    private UserFundMapper userFundMapper;
    @Resource
    private BoughtAmountMapper boughtAmountMapper;

    @Override
    public ResponseBean insert(ProjectDto projectDto) {
        // 项目表新增（需要获取到项目id）
        Project project = map(projectDto, Project.class);
        if (ProjectStatusEnum.SAVE.getKey().equals(projectDto.getClickType())) {
            // 可编辑状态
            project.setStatus(ProjectStatusEnum.SAVE.getKey());
        } else {
            // 待审核状态
            project.setStatus(ProjectStatusEnum.SUBMIT.getKey());
        }
        // 根据币对得到项目是 保本区  或者  创新区
        Symbol symbol = symbolMapper.selectByPrimaryKey(project.getSymbol().intValue());
        String coin = symbol.getName().split("/")[1];
        if (CoinEnum.USDT.getKey().equals(coin)) {//保本区
            project.setAreaType(Integer.valueOf(AreaEnum.BREAK_EVEN.getKey()));
        }
        if (CoinEnum.HOS.getKey().equals(coin)) {//创意区
            project.setAreaType(Integer.valueOf(AreaEnum.CREATION.getKey()));
        }
        projectMapper.insert(project);

        // 构建附件信息
        List<AttachFile> list = buildAttachFile(project.getId(), projectDto);
        // 批量插入附件信息
        attachFileMapper.batchInsert(list);

        return new ResponseBean(200, "", null);
    }

    /**
     * 构建附件信息
     */
    private List<AttachFile> buildAttachFile(Long id, ProjectDto projectDto) {
        List<AttachFile> attachFiles = new ArrayList<>();
        Map<String, List<AttachFileDto>> map = projectDto.getAttachFileMap();
        for(Map.Entry<String, List<AttachFileDto>> entry : map.entrySet()){
            String mapKey = entry.getKey();
            List<AttachFileDto> mapValue = entry.getValue();
            for (AttachFileDto afd: mapValue) {
                AttachFile af = new AttachFile();
                af.setRefId(id);
                af.setType(mapKey);
                af.setFilePath(afd.getFilePath());
                af.setFileName(afd.getFileName());
                af.setIsDel(YNEnum.N.getKey());
                attachFiles.add(af);
            }
        }

        return attachFiles;
    }

    /**
     * 预修改，回显
     */
    @Override
    public ResponseBean preUpdate(Long projectId) {
        // 根据主键查询项目基础信息。
        Project project = projectMapper.selectByPrimaryKey(projectId);

        List<Map<String, List<AttachFile>>> attachFiles = new ArrayList<>();
        // 根据项目主键 + 附件类型  得到所有附件
        getAttacheFiles(projectId, attachFiles);
        // 响应数据
        ProjectDto projectDto = map(project, ProjectDto.class);
        projectDto.setAttachList(attachFiles);

        return new ResponseBean(200, "", projectDto);
    }

    /**
     * 根据项目主键 + 附件类型  得到所有附件
     */
    private void getAttacheFiles(Long projectId, List<Map<String, List<AttachFile>>> attachFiles) {
        for (int i = 0; i < 5; i++) {// 写死5个类型
            switch (i) {
                case 0:
                    List<AttachFile> proPicture = attachFileMapper.selectByTypeAndRefId(AttachEnum.PRO_PICTURE.getKey(), projectId);
                    Map<String, List<AttachFile>> map = new HashMap<>();
                    map.put(AttachEnum.PRO_PICTURE.getKey(), proPicture);
                    attachFiles.add(map);
                    break;
                case 1:
                    List<AttachFile> struPicture = attachFileMapper.selectByTypeAndRefId(AttachEnum.STRUCTURE_PICTURE.getKey(), projectId);
                    Map<String, List<AttachFile>> map2 = new HashMap<>();
                    map2.put(AttachEnum.STRUCTURE_PICTURE.getKey(), struPicture);
                    attachFiles.add(map2);
                    break;
                case 2:
                    List<AttachFile> proRefFile = attachFileMapper.selectByTypeAndRefId(AttachEnum.PRO_REF_FILE.getKey(), projectId);
                    Map<String, List<AttachFile>> map3 = new HashMap<>();
                    map3.put(AttachEnum.PRO_REF_FILE.getKey(), proRefFile);
                    attachFiles.add(map3);
                    break;
                case 3:
                    List<AttachFile> proRefLawFile = attachFileMapper.selectByTypeAndRefId(AttachEnum.PRO_REF_LAW_FILE.getKey(), projectId);
                    Map<String, List<AttachFile>> map4 = new HashMap<>();
                    map4.put(AttachEnum.PRO_REF_LAW_FILE.getKey(), proRefLawFile);
                    attachFiles.add(map4);
                    break;
                case 4:
                    List<AttachFile> proBuyInvestContract = attachFileMapper.selectByTypeAndRefId(AttachEnum.PRO_BUY_INVEST_CONTRACT.getKey(), projectId);
                    Map<String, List<AttachFile>> map5 = new HashMap<>();
                    map5.put(AttachEnum.PRO_BUY_INVEST_CONTRACT.getKey(), proBuyInvestContract);
                    attachFiles.add(map5);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 更新
     */
    @Override
    public ResponseBean update(ProjectDto projectDto) {
        //TODO  对页面传来的关键信息必须要作判断

        Project project = map(projectDto, Project.class);
        if (ProjectStatusEnum.SAVE.getKey().equals(projectDto.getClickType())) {
            // 可编辑状态
            project.setStatus(ProjectStatusEnum.SAVE.getKey());
        } else {
            // 待审核状态
            project.setStatus(ProjectStatusEnum.SUBMIT.getKey());
        }
        // 得到保本区或者创意区
        Symbol symbol = symbolMapper.selectByPrimaryKey(project.getSymbol().intValue());
        String coin = symbol.getName().split("_")[1];
        if (CoinEnum.USDT.getKey().equals(coin)) {//保本区
            project.setAreaType(Integer.valueOf(AreaEnum.BREAK_EVEN.getKey()));
        }
        if (CoinEnum.HOS.getKey().equals(coin)) {//创意区
            project.setAreaType(Integer.valueOf(AreaEnum.CREATION.getKey()));
        }

        // 更新
        projectMapper.updateByPrimaryKeySelective(project);

        // 删除附件  refId + type (is_del = 'N')
        deleteAttachFile(project.getId());
        // 构建附件信息
        List<AttachFile> list = buildAttachFile(project.getId(), projectDto);
        // 批量插入附件信息
        attachFileMapper.batchInsert(list);

        return new ResponseBean(200, "", null);
    }

    /**
     * 删除附件信息
     */
    private void deleteAttachFile(Long projectId) {
        // 因为有关项目的附件一共有5个类型
        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 0:
                    attachFileMapper.deleteByTypeAndRefId(AttachEnum.PRO_PICTURE.getKey(), projectId);
                    break;
                case 1:
                    attachFileMapper.deleteByTypeAndRefId(AttachEnum.STRUCTURE_PICTURE.getKey(), projectId);
                    break;
                case 2:
                    attachFileMapper.deleteByTypeAndRefId(AttachEnum.PRO_REF_FILE.getKey(), projectId);
                    break;
                case 3:
                    attachFileMapper.deleteByTypeAndRefId(AttachEnum.PRO_REF_LAW_FILE.getKey(), projectId);
                    break;
                case 4:
                    attachFileMapper.deleteByTypeAndRefId(AttachEnum.PRO_BUY_INVEST_CONTRACT.getKey(), projectId);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 列表查询
     */
    @Override
    public ResponseBean list(PageParamDto pageParamDto) {
        int total = projectExtMapper.count(pageParamDto);
        int start = (pageParamDto.getCurrentPage() - 1) * pageParamDto.getPageSize();
        pageParamDto.setStart(start);
        List<ProjectListDto> projectListDtos = projectExtMapper.list(pageParamDto);
        // 构建返回信息
        ResponseBean res = BuildResponseBusinessDto(pageParamDto, total, projectListDtos);

        return res;
    }

    /**
     * 构建返回信息
     */
    private ResponseBean BuildResponseBusinessDto(PageParamDto pageParamDto, int total, List<ProjectListDto> projectList) {
        Map<String, Object> data = new HashMap<>();
        data.put("list", projectList);
        data.put("total", total);
        data.put("currentPage", pageParamDto.getCurrentPage());
        data.put("pageSize", pageParamDto.getPageSize());
        data.put("code", CodeEnum.OK.getKey());

        return new ResponseBean(200, "", data);
    }

    /**
     * 超级管理员进行审核
     */
    @Override
    public ResponseBean audit(AuditDto auditDto) {
        ExchangePreconditions.notNull(auditDto,"参数不能为空！");
        // 参数是Y 或者 N
        Project project = new Project();
        project.setId(auditDto.getProjectId());
        if(YNEnum.Y.getKey().equals(auditDto.getReject())) {
            // 拒绝发布
            project.setStatus(ProjectStatusEnum.REJECT.getKey());
        } else{
            Project p = projectMapper.selectByPrimaryKey(auditDto.getProjectId());
            // 审核通过，根据时间判断有两个类别
            if (new Date().before(p.getStartTime())) {// 当前时间在项目开始时间之前
                //预售中
                project.setStatus(ProjectStatusEnum.PRE_SALE.getKey());
            } else {
                //认购中
                project.setStatus(ProjectStatusEnum.ON_SALE.getKey());
            }
        }
        // 更新项目状态
        projectMapper.updateByPrimaryKeySelective(project);

        return new ResponseBean(200, "", null);
    }

    /**
     * 前台： 项目的列表查询
     * 需求： 认购中  和  认购结束
     */
    @Override
    public ResponseBean getForeProjectList(PageParamDto pageParamDto) {
        // 如果这个项目在 预售中，应该什么时候，修改状态呢？(挂单的时候)
        // 凡是在 预售中 的状态都需要多写一个时间的判断

        int total = foreProjectExtMapper.count(pageParamDto);
        int start = (pageParamDto.getCurrentPage() - 1) * pageParamDto.getPageSize();
        pageParamDto.setStart(start);
        List<ForeProjectDto> foreProjectList = foreProjectExtMapper.list(pageParamDto);

        // 计算出认购金额 (认购金额表中求和)
        // 对每个项目id都要 求和

        List<BoughtAmountDto> boughtAmountDtos = boughtAmountExtMapper.getBoughtAmount(foreProjectList);

        // 构建返回信息

        for (ForeProjectDto foreProjectDto : foreProjectList) {
            // 构建认购倒计时  认购周期减去当前时间 认购倒计时可以做成页面动态变化的时钟

            //todo 首发价(在币对表中) （项目表中冗余一个首发价字段）

            for (BoughtAmountDto boughtAmountDto: boughtAmountDtos) {
                if (foreProjectDto.getId().intValue() == boughtAmountDto.getProjectId()) {
                    foreProjectDto.setBoughtAmount(boughtAmountDto.getAmount());
                }
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("currentPage", pageParamDto.getCurrentPage());
        data.put("pageSize", pageParamDto.getPageSize());
        data.put("list", foreProjectList);

        return new ResponseBean(200, "", data);
    }

    /**
     * 前台： 项目详情
     */
    @Override
    public ResponseBean getForeProjectDetail(Long projectId) {
        // 根据主键查询项目基础信息。
        Project project = projectMapper.selectByPrimaryKey(projectId);

        List<Map<String, List<AttachFile>>> attachFiles = new ArrayList<>();
        // 根据项目主键 + 附件类型  得到所有附件
        getAttacheFiles(projectId, attachFiles);
        // 响应数据
        ProjectDto projectDto = map(project, ProjectDto.class);
        projectDto.setAttachList(attachFiles);

        // 得到当前登陆用户的id
        Integer userId = ShiroUtils.getUserId();

        // 得到当前登陆用户的账户余额(user_fund表)
        UserFund userFund = userFundMapper.selectByUserIdAndUsdt(userId);

        // 得到当前登陆用户的钱包地址(wallet表)
        Wallet wallet = walletMapper.selectByUserId(userId);


        Map<String, Object> data = new HashMap<>();
        data.put("projectDto", projectDto);
        data.put("userFund", userFund);
        data.put("wallet", wallet);

        return new ResponseBean(200, "", data);
    }

    /**
     * 前台：买入逻辑
     */
    @Override
    public ResponseBean buy(BuyDto buyDto) {
        ExchangePreconditions.notNull(buyDto, "参数不能为空！");
        // 求得当前项目总额
        Project project = projectMapper.selectByPrimaryKey(buyDto.getProjectId());
        // 求得已经认购的总额度
        BoughtAmountDto boughtAmountDto = boughtAmountExtMapper.getAmountByProjectId(buyDto.getProjectId());
        // 获取时间
        LocalDateTime now = LocalDateTime.now();
        //当前时间减去投资天数
        now = now.minus(project.getInvestPeriod(), ChronoUnit.DAYS);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = now.toLocalDate().atStartOfDay(zoneId);
        Date nowDate = Date.from(zdt.toInstant());

        // 判断项目是否已经失败

        // 认购额度不满而且认购时间没有了
        if (boughtAmountDto.getAmount().compareTo(project.getCollectAmount()) == -1 &&
                project.getStartTime().before(nowDate) ) {
            // 更新状态(项目失败)
            project.setStatus(ProjectStatusEnum.INVALID.getKey());
            projectMapper.updateByPrimaryKeySelective(project);

            // 一旦失效，将要撤回这个项目的所有挂单
            // todo 调用第三方接口，撤回挂单

            // 如果调用第三方逻辑异常，则不更新当前挂单为撤回状态
            //todo boughtAmount的withdraw字段为1（默认是0）

            throw new ExchangeException("项目已经失效了！");
        }

        // 认购金额 >= 项目总额度
        if (boughtAmountDto.getAmount().compareTo(project.getCollectAmount()) != -1) {

            throw new ExchangeException("认购已满额！请认购其他项目！");
        }

        // 预售中(虽然没有更新状态，但是依然可以购买)
        if (ProjectStatusEnum.PRE_SALE.getKey().equals(project.getStatus())) {
            if (project.getStartTime().after(new Date())
                && project.getStartTime().after(nowDate)) {
                //在预售中，但是项目开始时间大于当前时间 且 项目开始时间  >  当前时间 - 认购天数
                // todo 满足认购条件后去调用第三方挂单逻辑接口

                // todo 捕获到异常就不添加认购记录
            }
        }

        // 认购中
        if (ProjectStatusEnum.ON_SALE.getKey().equals(project.getStatus())) {
            // 项目开始时间  >  当前时间 - 认购周期
            if (project.getStartTime().after(nowDate)) {
                // 认购额度 + 当前认购额度 <= 项目总金额
                if (project.getCollectAmount().compareTo(boughtAmountDto.getAmount().add(buyDto.getBoughtAmount())) != 1) {
                    // todo 满足认购条件后去调用第三方挂单接口

                    // todo 捕获到异常就不添加认购记录

                } else {
                    // 更新项目状态
                    project.setStatus(ProjectStatusEnum.END_SALE.getKey());
                    projectMapper.updateByPrimaryKey(project);
                    throw new ExchangeException("此项目认购结束，请认购其他项目！");
                }
            } else {
                // 项目结束
                project.setStatus(ProjectStatusEnum.PROJECT_END.getKey());
                projectMapper.updateByPrimaryKey(project);

                //todo 更新币对状态为2（设计：0是无效的，1是可以进行币币交易的，2是项目方回购的状态？）

                // todo 项目方，以初始价格进行回购  （做法：用户主动挂卖单，项目方挂买单吃了就行了）

                throw new ExchangeException("项目结束！");
            }
        }

        // 状态变更分析
            //1，在认购期内，完成资金募集    ----  认购结束（此时是计息状态）
            //2，在认购期内，未达到认购额度  ---- invalid
            //3，认购期结束，项目结束。      ----  end

        // 当进行插入的时候应该上锁，这样保证库存不会被并发影响

        return new ResponseBean(200, "", "成功买入");
    }

}
