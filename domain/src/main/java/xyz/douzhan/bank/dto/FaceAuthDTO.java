package xyz.douzhan.bank.dto;

import lombok.Data;
import lombok.Getter;

/**
 * 一些声明信息
 * Description:
 * date: 2023/11/26 0:00
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
public class FaceAuthDTO {
    //姓名
    private String name;
    //身份证号
    private String ICNum;
}
