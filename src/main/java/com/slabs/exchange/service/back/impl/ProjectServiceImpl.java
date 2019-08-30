package com.slabs.exchange.service.back.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slabs.exchange.common.config.ExchangeApiProperties;
import com.slabs.exchange.common.enums.*;
import com.slabs.exchange.common.exception.ExchangeException;
import com.slabs.exchange.mapper.UserMapper;
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
import com.slabs.exchange.util.JWTUtil;
import com.slabs.exchange.util.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class ProjectServiceImpl extends BaseService implements IProjectService {
    private static final Gson gson = new GsonBuilder().create();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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
    private UserMapper userMapper;
    @Resource
    private UserFundMapper userFundMapper;
    @Resource
    private BoughtAmountMapper boughtAmountMapper;
    @Resource
    private ExchangeApiProperties exchangeApiProperties;

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
        // 根据币对得到项目是 稳定区  或者  创新区
        Symbol symbol = symbolMapper.selectByPrimaryKey(project.getSymbol().intValue());
        String coin = symbol.getName().split("_")[1];
        if (CoinEnum.USDT.getKey().equals(coin)) {//稳定区
            project.setAreaType(Integer.valueOf(AreaEnum.BREAK_EVEN.getKey()));
        }
        if (CoinEnum.HOS.getKey().equals(coin)) {//创新区
            project.setAreaType(Integer.valueOf(AreaEnum.CREATION.getKey()));
        }
        // todo ShiroUtil.getUserId();
        project.setCreateTime(new Date());
        project.setCoinId(symbol.getCommodity());
        projectMapper.insert(project);

        // 跟项目管理的币对这里设置成无效的
        Symbol sym = new Symbol();
        sym.setId(symbol.getId());
        sym.setValid(false);
        symbolMapper.updateByPrimaryKeySelective(sym);

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
                af.setRefId(id.intValue());
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
    public ResponseBean preUpdate(Integer projectId) {
        // 根据主键查询项目基础信息。
        Project project = projectMapper.selectByPrimaryKey(projectId.longValue());

        List<Map<String, List<AttachFile>>> attachFiles = new ArrayList<>();
        // 根据项目主键 + 附件类型  得到所有附件
        getAttacheFiles(projectId.longValue(), attachFiles);
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
        Project project = map(projectDto, Project.class);
        if (ProjectStatusEnum.SAVE.getKey().equals(projectDto.getClickType())) {
            // 可编辑状态
            project.setStatus(ProjectStatusEnum.SAVE.getKey());
        } else {
            // 待审核状态
            project.setStatus(ProjectStatusEnum.SUBMIT.getKey());
        }
        // 得到稳定区或者创新区
        Symbol symbol = symbolMapper.selectByPrimaryKey(project.getSymbol().intValue());
        String coin = symbol.getName().split("_")[1];
        if (CoinEnum.USDT.getKey().equals(coin)) {//稳定区
            project.setAreaType(Integer.valueOf(AreaEnum.BREAK_EVEN.getKey()));
        }
        if (CoinEnum.HOS.getKey().equals(coin)) {//创新区
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
        ResponseBean res = buildResponseBusinessDto(pageParamDto, total, projectListDtos);

        return res;
    }

    /**
     * 构建返回信息
     */
    private ResponseBean buildResponseBusinessDto(PageParamDto pageParamDto, int total, List<ProjectListDto> projectList) {
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
        // 凡是在 预售中 的状态都需要多写一个时间的判断

        int total = foreProjectExtMapper.count(pageParamDto);
        int start = (pageParamDto.getCurrentPage() - 1) * pageParamDto.getPageSize();
        pageParamDto.setStart(start);
        List<ForeProjectDto> foreProjectList = foreProjectExtMapper.list(pageParamDto);

        // 构建返回信息
        for (ForeProjectDto foreProjectDto : foreProjectList) {
            // 项目开始时间
            Instant instant = foreProjectDto.getStartTime().toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime startTime = instant.atZone(zoneId).toLocalDateTime();
            // 计算项目截止时间
            LocalDateTime endTime = startTime.plus(foreProjectDto.getInvestPeriod(), ChronoUnit.DAYS);
            ZonedDateTime et = endTime.toLocalDate().atStartOfDay(zoneId);
            Date endTimeDate = Date.from(et.toInstant());
            foreProjectDto.setEndTime(endTimeDate);
            // 获取项目图片
            List<AttachFile> files = attachFileMapper.selectByTypeAndRefId(AttachEnum.PRO_PICTURE.getKey(), foreProjectDto.getId());
            foreProjectDto.setFileList(files);
        }

        // 计算出认购金额 (认购金额表中求和)
        // 对每个项目id都要 求和
        if (foreProjectList.size() > 0) {
            List<BoughtAmountDto> boughtAmountDtos = boughtAmountExtMapper.getBoughtAmount(foreProjectList);
            // 构建返回信息
            for (ForeProjectDto foreProjectDto : foreProjectList) {
                for (BoughtAmountDto boughtAmountDto: boughtAmountDtos) {
                    if (foreProjectDto.getId().intValue() == boughtAmountDto.getProjectId()) {
                        foreProjectDto.setBoughtAmount(boughtAmountDto.getAmount());
                    }
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
    public ResponseBean getForeProjectDetail(Integer projectId) {
        // 根据主键查询项目基础信息。
        Project project = projectMapper.selectByPrimaryKey(projectId.longValue());

        List<Map<String, List<AttachFile>>> attachFiles = new ArrayList<>();
        // 根据项目主键 + 附件类型  得到所有附件
        getAttacheFiles(projectId.longValue(), attachFiles);
        // 响应数据
        ProjectDto projectDto = map(project, ProjectDto.class);
        projectDto.setAttachList(attachFiles);

        // 得到当前登陆用户的id
        Integer userId = ShiroUtils.getUserId();

        // 得到当前登陆用户的账户余额(user_fund表)
        UserFund userFund = userFundMapper.selectByUserIdAndCoinName(userId, CoinEnum.USDT.getKey());

        // 得到当前登陆用户的钱包地址(wallet表)
        User user = userMapper.selectByPrimaryKey(userId);

        Map<String, Object> data = new HashMap<>();
        data.put("projectDto", projectDto);
        data.put("userFund", userFund);
        data.put("walletAddr", user.getWalletAddr());

        return new ResponseBean(200, "", data);
    }

    /**
     * 前台：买入逻辑
     */
    @Override
    public ResponseBean buy(BuyDto buyDto) {
        ExchangePreconditions.notNull(buyDto, "参数不能为空！");
        //验证用户余额和购买量是否合理
        Long projectId = buyDto.getProjectId().longValue();
        BigDecimal initPrice = buyDto.getInitPrice();
        BigDecimal boughtAmount = buyDto.getBoughtAmount();
        BigDecimal amount = initPrice.multiply(boughtAmount);
        UserFund userFund = userFundMapper.selectByUserIdAndCoinName(ShiroUtils.getUserId(), CoinEnum.USDT.getKey());

        // 计算出可用余额
        BigDecimal availableAmount = userFund.getAmount().subtract(userFund.getOrderLocked()).subtract(userFund.getWithdrawLocked());

        if (userFund.getAmount() == null || availableAmount.compareTo(amount) < 0) {
            throw new ExchangeException("用户余额不足！");
        }

        // 求得当前项目总额
        Project project = projectMapper.selectByPrimaryKey(projectId);
        // 求得已经认购的总额度
        BoughtAmountDto boughtAmountDto = boughtAmountExtMapper.getAmountByProjectId(projectId);
        // 获取时间
        LocalDateTime now = LocalDateTime.now();
        //当前时间减去投资天数
        now = now.minus(project.getInvestPeriod(), ChronoUnit.DAYS);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = now.toLocalDate().atStartOfDay(zoneId);
        Date nowDate = Date.from(zdt.toInstant());

        // 判断项目是否已经失败

        // 认购额度不满而且认购时间没有了
        if (boughtAmountDto.getAmount().compareTo(project.getCollectAmount()) < 0 &&
                project.getStartTime().before(nowDate) ) {
            if (!project.getStatus().equals(ProjectStatusEnum.INVALID.getKey())) {
                // 更新状态(项目失败)
                project.setStatus(ProjectStatusEnum.INVALID.getKey());
                projectMapper.updateByPrimaryKeySelective(project);
                // 把此项目的认购记录更新为 需要撤回状态(withdraw = 2)
                boughtAmountMapper.updateWithdrawByProjectId(projectId.intValue(), Integer.valueOf(WithdrawStatusEnum.WITHDRAW_NEED.getKey()));
            }

            throw new ExchangeException("项目已经失效了！");
        }

        // 认购金额 >= 项目总额度
        if (boughtAmountDto.getAmount().compareTo(project.getCollectAmount()) > 0) {
            throw new ExchangeException("认购已满额！请认购其他项目！");
        }

        // 预售中(虽然没有更新状态，但是依然可以购买)
        if (ProjectStatusEnum.PRE_SALE.getKey().equals(project.getStatus())) {
            if (project.getStartTime().after(new Date())
                && project.getStartTime().after(nowDate)) {
                // 在预售中，但是项目开始时间大于当前时间 且 项目开始时间  >  当前时间 - 认购天数
                //  /limit/bvp_usdt post {"side":"buy","price":初始价,"amount":12}
                //  响应：{"id":"string"}
                if (project.getCollectAmount().compareTo(boughtAmountDto.getAmount().add(boughtAmount)) >= 0) {
                    holdOrder(buyDto, project);
                }
            }
        }

        // 认购中
        if (ProjectStatusEnum.ON_SALE.getKey().equals(project.getStatus())) {
            // 项目开始时间  >  当前时间 - 认购周期
            if (project.getStartTime().after(nowDate)) {
                // 认购额度 + 当前认购额度 <= 项目总金额
                if (project.getCollectAmount().compareTo(boughtAmountDto.getAmount().add(boughtAmount)) >= 0) {
                    holdOrder(buyDto, project);
                } else {
                    // 认购结束
                    // 项目方挂当前项目对应的币的所有卖单
                    projectThirdOrderSell(project);
                    throw new ExchangeException("此项目认购结束，请认购其他项目！");
                }
            } else {
                // 项目结束
                projectThirdOrderBuy(project);
                throw new ExchangeException("项目结束！");
            }
        }

        // 状态变更分析
            //1，在认购期内，完成资金募集    ----  认购结束（此时是计息状态）
            //2，在认购期内，未达到认购额度  ---- invalid
            //3，认购期结束，项目结束。      ----  end
        return new ResponseBean(200, "", "成功买入");
    }

    /**
     * 项目结束，项目方挂买单
     */
    private void projectThirdOrderBuy(Project project) {
        // 找到这个项目的平台方用户，挂买单（回购）。
        OrderDto orderDto = new OrderDto();
        orderDto.setSide(BuyAndSellEnum.BUY.getKey());
        // 这个是创建项目的时候，填写此项目需要多少项目币
        orderDto.setAmount(project.getCollectCoinAmount());
        orderDto.setPrice(project.getInitPrice());
        String requestData = gson.toJson(orderDto);
        Symbol symbol = symbolMapper.selectByPrimaryKey(project.getSymbol().intValue());
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        Request request = new Request.Builder()
                .url(exchangeApiProperties.getHost() + exchangeApiProperties.getOrder() + symbol.getName())
                .post(RequestBody.create(mediaType, requestData))
                .header("Authorization", "Bearer" + " " + JWTUtil.encode(project.getUserId().toString()))//项目方用户
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        String resData = "";
        try {
            resData = okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            // 挂该项目的所有买单失败！(网络原因)
            log.error("network question: project third, side: sell, projectId:"+ project.getId() +  "ordered failed." + sdf.format(new Date()));
        }
        ExchangeApiResDto exchangeApiResDto = gson.fromJson(resData, ExchangeApiResDto.class);
        if (exchangeApiResDto.getId() == null) {
            // 挂该项目的所有买单失败！(业务原因)
            log.error("business question: project third, side: sell, projectId:"+ project.getId() +  "ordered failed." + sdf.format(new Date()));
        } else {
            // 项目结束
            project.setStatus(ProjectStatusEnum.PROJECT_END.getKey());
            projectMapper.updateByPrimaryKey(project);
            //币对失效（对于交易系统的挂单无任何影响）
            symbolMapper.updateValid(project.getSymbol().intValue(), false);
        }
    }

    /**
     * 项目方挂卖单
     */
    private void projectThirdOrderSell(Project project) {
        OrderDto orderDto = new OrderDto();
        orderDto.setSide(BuyAndSellEnum.SELL.getKey());
        orderDto.setAmount(project.getCollectCoinAmount());
        orderDto.setPrice(project.getInitPrice());
        String requestData = gson.toJson(orderDto);
        Symbol symbol = symbolMapper.selectByPrimaryKey(project.getSymbol().intValue());
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        Request request = new Request.Builder()
                .url(exchangeApiProperties.getHost() + exchangeApiProperties.getOrder() + symbol.getName())
                .post(RequestBody.create(mediaType, requestData))
                .header("Authorization", "Bearer" + " " + JWTUtil.encode(project.getUserId().toString()))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        String resData = "";
        try {
            resData = okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            // 挂该项目的所有卖单失败！网络原因
            log.error("network question: project third, side: sell, projectId:"+ project.getId() +  "ordered failed." + sdf.format(new Date()));
        }

        ExchangeApiResDto exchangeApiResDto = gson.fromJson(resData, ExchangeApiResDto.class);
        if (exchangeApiResDto.getId() == null) {
            //业务原因
            log.error("business question: project third, side: sell, projectId:"+ project.getId() +  "ordered failed." + sdf.format(new Date()));
        } else {
            project.setStatus(ProjectStatusEnum.END_SALE.getKey());
            projectMapper.updateByPrimaryKey(project);
            // 更新认购记录状态为1（表达此项目认购成功）
            boughtAmountMapper.updateWithdrawByProjectId(project.getId().intValue(), Integer.valueOf(WithdrawStatusEnum.SUCCEED.getKey()));
            // 更新币对为有效状态
            symbolMapper.updateValid(project.getSymbol().intValue(), true);
        }
    }


    /**
     *  调用挂单逻辑和插入认购份额表数据
     */
    private void holdOrder(BuyDto buyDto, Project project) {
        OrderDto orderDto = new OrderDto();
        orderDto.setSide(BuyAndSellEnum.BUY.getKey());
        orderDto.setAmount(buyDto.getBoughtAmount());
        orderDto.setPrice(project.getInitPrice());
        String requestData = gson.toJson(orderDto);
        Symbol symbol = symbolMapper.selectByPrimaryKey(project.getSymbol().intValue());
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        Request request = new Request.Builder()
                .url(exchangeApiProperties.getHost() + exchangeApiProperties.getOrder() + symbol.getName())
                .post(RequestBody.create(mediaType, requestData))
                .header("Authorization", "Bearer" + " " + JWTUtil.encode(ShiroUtils.getUserId().toString()))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        String resData = "";
        try {
            resData = okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("network question: user id:" + ShiroUtils.getUserId() + "ordered failed." + sdf.format(new Date()));
            throw new ExchangeException("购买失败，请重新购买！");
        }
        ExchangeApiResDto exchangeApiResDto = gson.fromJson(resData, ExchangeApiResDto.class);
        if (exchangeApiResDto.getId() == null) {
            log.info("business question: user id:" + ShiroUtils.getUserId() + "ordered failed." + sdf.format(new Date()));
            throw new ExchangeException("购买失败，请重新购买！");
        }

        // 购买成功
        BoughtAmount boughtAmount = new BoughtAmount();
        boughtAmount.setCreateTime(new Date());
        // 认购状态
        boughtAmount.setWithdraw(Integer.valueOf(WithdrawStatusEnum.DEFAULT.getKey()));
        // 挂单返回的id
        boughtAmount.setOrderId(exchangeApiResDto.getId());
        // 购买项目币的数量
        BigDecimal coinAmount = buyDto.getBoughtAmount();
        boughtAmount.setCoinAmount(coinAmount);
        // 购买的币种
        boughtAmount.setCoinId(project.getCoinId());
        // 购买的项目
        boughtAmount.setProjectId(buyDto.getProjectId());
        // 给的预期年化收益（手工转平台币）
        BigDecimal expectYearBonus =  project.getExpectYearBonus().divide(new BigDecimal(100), 6, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal hosAmount = buyDto.getBoughtAmount().multiply(buyDto.getInitPrice()).multiply(expectYearBonus).setScale(6, BigDecimal.ROUND_HALF_DOWN);
        boughtAmount.setHosAmount(hosAmount);
        boughtAmount.setSymbolId(project.getSymbol().intValue());
        boughtAmount.setUsdtAmount(coinAmount.multiply(project.getInitPrice()).setScale(6, BigDecimal.ROUND_HALF_DOWN));

        boughtAmountMapper.insert(boughtAmount);
    }

}
