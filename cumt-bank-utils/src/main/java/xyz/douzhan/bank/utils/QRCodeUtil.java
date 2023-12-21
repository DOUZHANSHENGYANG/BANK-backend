package xyz.douzhan.bank.utils;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * Description:
 * date: 2023/12/18 15:39
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class QRCodeUtil {
    public static String genCode(String json) {
        QrConfig qrConfig = new QrConfig(300, 300);
        qrConfig.setErrorCorrection(ErrorCorrectionLevel.H);
        return QrCodeUtil.generateAsBase64(json,qrConfig,null,ResourceUtil.getResourceObj("cumt.jpg").readBytes());
    }
}
