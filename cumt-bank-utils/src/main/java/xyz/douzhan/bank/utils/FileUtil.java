package xyz.douzhan.bank.utils;


import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import xyz.douzhan.bank.constants.ThirdAPIConfigConstant;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description:
 * date: 2023/12/15 19:34
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Slf4j
public class FileUtil {
    private static LinkedHashMap<String, Map<String,String>> thirdAPIConfMap;
    public static String getFileTypeSuffixWithDot(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 通过yaml文件获取第三方配置
     * @param key
     * @return
     */
    public static Map<String, String> getConfig(String key) {
        if(!StrUtil.equals(key,ThirdAPIConfigConstant.ALIYUN)&&!StrUtil.equals(key,ThirdAPIConfigConstant.BAIDU)){
            return null;
        }
        if ( !getYamlMap()){
            return null;
        }
        try{
             return thirdAPIConfMap.get(key);

        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     *加载yaml配置文件
     * @return
     */
    private static Boolean getYamlMap(){
        try (InputStream in=new FileInputStream(ThirdAPIConfigConstant.FILE_PATH)){
            Yaml yaml = new Yaml();
            thirdAPIConfMap= (LinkedHashMap<String, Map<String, String>>) yaml.loadAs(in,LinkedHashMap.class).get(ThirdAPIConfigConstant.BANK);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

}
