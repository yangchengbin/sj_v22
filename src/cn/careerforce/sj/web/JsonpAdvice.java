/**
 * 项目名称：mod-moxtra
 * 包名称：cn.careerforce.module.moxtra.web
 * 文件名称：JsonpAdvice.java
 * 创建者：yangdh
 * 创建时间：2015年11月1日 下午4:16:57
 * 版本：1.0
 */
package cn.careerforce.sj.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * @author yangdh
 * @since 1.0.0
 */
@ControllerAdvice
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
    public JsonpAdvice() {
        super("callback");
    }
}