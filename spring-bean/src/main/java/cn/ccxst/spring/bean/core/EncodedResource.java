package cn.ccxst.spring.bean.core;

/**
 * @author zwd
 * @date 2018/12/17 11:07
 * @Email stephen.zwd@gmail.com
 */
public class EncodedResource {

    private final Resource resource;

    public EncodedResource(Resource resource) {
        this.resource = resource;
         //todo
    }

    public final Resource getResource() {
        return this.resource;
    }
}
