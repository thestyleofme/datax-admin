package com.github.thestyleofme.datax.server.domain.entity;

/**
 * <p>
 * description
 * </p>
 *
 * @author thestyleofme 2020/12/14 11:12
 * @since 1.0.0
 */
public class RegisterDataxInfo {

    /**
     * 如http://10.211.144.85:9999
     */
    private String url;
    /**
     * 当前节点，默认权重为1，权重1-10，越高越有可能负载到此节点
     */
    private int weight;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
