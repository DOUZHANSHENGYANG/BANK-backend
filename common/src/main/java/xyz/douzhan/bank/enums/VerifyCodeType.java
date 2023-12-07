package xyz.douzhan.bank.enums;

import lombok.Getter;

/**
 * 一些声明信息
 * Description:
 * date: 2023/11/25 22:25
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Getter
public enum VerifyCodeType
{
    //图像验证码
    PIC,
    //短信验证码
    SMS,

    //线段干扰的验证码
    LINE,
    //圆圈干扰验证码
    CIRCLE,
    //扭曲干扰验证码
    SHEAR,
    //自定义纯数字验证码
    PURENUM;
}
