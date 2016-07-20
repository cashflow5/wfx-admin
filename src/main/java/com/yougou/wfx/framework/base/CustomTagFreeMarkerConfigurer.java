/**
 * Company:优购网
 * @author JH
 * @date 2015年1月27日 下午1:12:33
 * @version V1.0
 */
package com.yougou.wfx.framework.base;

import java.io.IOException;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.jagregory.shiro.freemarker.ShiroTags;

import freemarker.template.TemplateException;

/**
 * @author JH
 * 2015年1月27日-下午1:12:33
 */
public class CustomTagFreeMarkerConfigurer extends FreeMarkerConfigurer {
	 @Override
     public void afterPropertiesSet() throws IOException, TemplateException {
        super.afterPropertiesSet();
        this.getConfiguration().setSharedVariable("shiro", new ShiroTags());
     }
}
