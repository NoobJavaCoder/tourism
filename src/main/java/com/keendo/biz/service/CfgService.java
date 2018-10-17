package com.keendo.biz.service;

import com.keendo.architecture.utils.Log;
import com.keendo.biz.mapper.GlobalConfigMapper;
import com.keendo.biz.model.GlobalConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 全局配置表接口
 *
 * @author hebo
 */
@Service
public class CfgService {

    // 配置表接口
    @Autowired
    private GlobalConfigMapper globalConfigMapper;

    // 配置缓存
    private static Map<String, String> gCfgCache = new HashMap<String, String>();

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        Log.i("Init CfgBusiness, Call Update GlobalConfig...");

        updateGlobalConfigCache();
    }


    public static class Constants{

        //COS
        public final static String COS_SECRET_ID_KEY = "COS_SECRET_ID";
        public final static String COS_SECRET_KEY_KEY = "COS_SECRET_KEY";
        public final static String COS_APP_ID_KEY = "COS_APP_ID";
        public final static String COS_BUCKET_NAME_ORDINARY_KEY = "COS_BUCKET_NAME_ORDINARY";
        public final static String COS_BUCKET_NAME_SENSITIVE_KEY = "COS_BUCKET_NAME_SENSITIVE";
        public final static String COS_REGION_KEY = "COS_REGION";

        //SMS
        public final static String SMS_SIGN_KEY = "SMS_SIGN";//签名
        public final static String SMS_TPL_ID_USER_INFO_KEY = "SMS_TPL_ID_USER_INFO";//模板id("我的资料"手机验证码)

        //小程序
        public final static String MINIAPP_APP_ID_KEY = "MINIAPP_APP_ID";//小程序appid
        public final static String MINIAPP_SECRET_KEY = "MINIAPP_SECRET";//小程序secret

        //微信商户
        public final static String MCH_ID_KEY = "MCH_ID";//商户id
        public final static String MCH_KEY_KEY = "MCH_KEY";//签名算法秘钥
        public final static String CERT_PATH_KEY = "CERT_PATH";//证书路径
    }

    /**
     * 更新配置缓存
     */
    public void updateGlobalConfigCache() {
        Log.d("into updateGlobalConfigCache()");

        //gCfgCache.clear();

        List<GlobalConfig> cfgList = globalConfigMapper.selectAll();

        if (cfgList != null) {
            Iterator<GlobalConfig> it = cfgList.iterator();
            while (it.hasNext()) {
                GlobalConfig gcfg = it.next();
                gCfgCache.put(gcfg.getKey(), gcfg.getValue());
            }
        }
    }

    /**
     * 查询所有配置信息
     */
    public List<GlobalConfig> selectAll() {
        List<GlobalConfig> cfgList = globalConfigMapper.selectAll();
        return cfgList;
    }

    /**
     * 获取配置
     *
     * @param key
     * @return
     */
    public String get(String key) {
        // 如果缓存数据为空，则初始化
        if (gCfgCache.size() == 0) {
            updateGlobalConfigCache();
        }

        String value = (String) gCfgCache.get(key);

        Log.d("Get GlobalConfig, key=[" + key + "], value=[" + value + "].");

        // 返回配置值
        return value;
    }

    /**
     * 获取配置整型数值
     *
     * @param key
     * @return
     */
    public Integer getInteger(String key) {
        String value = this.get(key);

        if (!StringUtils.isBlank(value)) {
            Log.d("Get GlobalConfig, key=[" + key + "], value=[" + value + "].");
            try {
                Integer iValue = Integer.parseInt(value);
                return iValue;
            } catch (NumberFormatException nfe) {
                Log.e("Change config value [" + value + "] to Integer exception.");
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 获取配置长整型数值
     *
     * @param key
     * @return
     */
    public Long getLong(String key) {
        String value = this.get(key);

        if (!StringUtils.isBlank(value)) {
            Log.d("Get GlobalConfig, key=[" + key + "], value=[" + value + "].");
            try {
                Long lValue = Long.parseLong(value);
                return lValue;
            } catch (NumberFormatException nfe) {
                Log.d("Change config value [" + value + "] to Long exception.");
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 获取配置浮点型数值
     *
     * @param key
     * @return
     */
    public Float getFloat(String key) {
        String value = this.get(key);

        if (!StringUtils.isBlank(value)) {
            Log.d("Get GlobalConfig, key=[" + key + "], value=[" + value + "].");
            try {
                Float fValue = Float.parseFloat(value);
                return fValue;
            } catch (NumberFormatException nfe) {
                Log.e("Change config value [" + value + "] to Float exception.");
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 保存配置
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        // value没变化
        if (value.equals(this.get(key))) {
            return;
        }

        this.set(key, value, null);
    }

    /**
     * 保存配置
     *
     * @param key
     * @param value
     */
    public void set(String key, String value, String desc) {
        // value没变化
        if (value.equals(this.get(key))) {
            return;
        }

        // 更新DB
        GlobalConfig gcfg = new GlobalConfig();
        gcfg.setKey(key);
        gcfg.setValue(value);
        gcfg.setDesc(desc);

        // 缓存如果存在该配置项，则认为表中也存在，使用update，否则insert
        if (gCfgCache.get(key) != null) {
            this.globalConfigMapper.updateByPrimaryKeySelective(gcfg);
        } else {
            this.globalConfigMapper.insert(gcfg);
        }

        // 更新缓存 (注：因为gcfg.setXXX()作了trim()操作，所以这里反取出来）
        gCfgCache.put(gcfg.getKey(), gcfg.getValue());
    }


}
