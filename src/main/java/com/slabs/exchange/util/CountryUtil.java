package com.slabs.exchange.util;

import com.slabs.exchange.model.dto.CountryDto;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.locale.provider.ResourceBundleBasedAdapter;
import sun.util.resources.OpenListResourceBundle;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 单例模式。懒汉模式。
 */
@Data
public class CountryUtil {
    public   static List<CountryDto> countryDtos;

    private  static CountryUtil countryUtil;

    public CountryUtil(List<CountryDto> countryDtos) {
        this.countryDtos = countryDtos;
    }


    public static CountryUtil getInstance() {
        if (countryUtil == null) {
            synchronized (CountryUtil.class) {
                if (countryUtil == null) {
                    countryDtos = getAllCountries();
                    countryUtil = new CountryUtil(countryDtos);
                }
            }
        }
        return countryUtil;
    }

    /**
     * 得到所有国家 编码和名称
     */
    public static List<CountryDto> getAllCountries() {
        List<CountryDto> countryDtos = new ArrayList<>();
        ResourceBundleBasedAdapter resourceBundleBasedAdapter = ((ResourceBundleBasedAdapter) LocaleProviderAdapter.forJRE());
        OpenListResourceBundle resource = resourceBundleBasedAdapter.getLocaleData().getLocaleNames(Locale.CHINA);
        Set<String> data = resource.keySet();
        List<String> twoCodes = data.stream()
                // 提取出国家的二字码，长度为2和全是大写
                .filter(code -> code.length() == 2 && StringUtils.isAllUpperCase(code))
                .collect(Collectors.toList());
        twoCodes.sort(Comparator.naturalOrder());

        twoCodes.forEach(twoCode -> {
            Locale locale = new Locale("", twoCode);
            String threeCode = null;
            try {
                // 获取国家的三字码
                threeCode = locale.getISO3Country();
            } catch (Exception e) {}
            Formatter formatter = new Formatter();
            String str = formatter.format("%-1s %-1s %-1s", twoCode, threeCode, resource.getString(twoCode)).toString();
            String[] arrStr = str.split(" ");
            String code = arrStr[1];
            String name = arrStr[2];
            CountryDto countryDto = new CountryDto();
            countryDto.setCode(code);
            countryDto.setName(name);
            countryDtos.add(countryDto);
        });
        return countryDtos;
    }
}