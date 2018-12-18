package cn.ccxst.spring.bean.factory.xml;

import cn.ccxst.spring.bean.core.EncodedResource;
import cn.ccxst.spring.bean.core.Resource;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zwd
 * @date 2018/12/17 11:01
 * @Email stephen.zwd@gmail.com
 */
public class XmlBeanDefinitionReader {

    public int loadBeanDefinitions(Resource resource)  {
        return loadBeanDefinitions(new EncodedResource(resource));
    }

    public int loadBeanDefinitions(EncodedResource encodedResource) {

            InputStream inputStream = encodedResource.getResource().getInputStream();
    }
}
