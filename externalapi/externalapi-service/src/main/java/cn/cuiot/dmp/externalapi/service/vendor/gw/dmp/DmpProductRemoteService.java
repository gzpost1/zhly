package cn.cuiot.dmp.externalapi.service.vendor.gw.dmp;

import cn.cuiot.dmp.common.bean.external.GWCurrencyBO;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.req.*;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.BaseDmpResp;
import cn.cuiot.dmp.externalapi.service.vendor.gw.bean.resp.ProductInfoResp;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 格物第三方 产品相关接口
 *
 * @Author: zc
 * @Date: 2024-09-06
 */
@Service
public class DmpProductRemoteService {

    @Autowired
    private DmpApiService dmpApiService;
    
    /**
     * 查询产品列表
     */
    public BaseDmpResp<ProductInfoResp> listProducts(@RequestBody BaseDmpPageReq req, GWCurrencyBO bo) {
        String gateway = "api/listProducts/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<ProductInfoResp>>() {
        });
    }

    /**
     * 查询指定产品详细信息
     */
    public BaseDmpResp<ProductInfoResp> getProduct(@RequestBody GetProductReq req, GWCurrencyBO bo) {
        String gateway = "api/getProduct/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<ProductInfoResp>>() {
        });
    }

    /**
     * 查询产品标签的产品列表
     */
    public BaseDmpResp<ProductInfoResp> listProductsByTag(@RequestBody ListProductsByTagReq req, GWCurrencyBO bo) {
        String gateway = "api/listProductsByTag/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<ProductInfoResp>>() {
        });
    }

    /**
     * 新建产品
     */
    public BaseDmpResp<ProductInfoResp> createProduct(@RequestBody CreateProductReq req, GWCurrencyBO bo) {
        String gateway = "api/createProduct/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<ProductInfoResp>>() {
        });
    }

    /**
     * 修改产品信息
     */
    public BaseDmpResp<Object> updateProduct(@RequestBody UpdateProductReq req, GWCurrencyBO bo) {
        String gateway = "api/updateProduct/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<Object>>() {
        });
    }

    /**
     * 删除产品
     */
    public BaseDmpResp<ProductInfoResp> deleteProduct(@RequestBody GetProductReq req, GWCurrencyBO bo) {
        String gateway = "api/deleteProduct/V1/1Main/vV1.1";
        return dmpApiService.postRequest(gateway, req, bo, new TypeReference<BaseDmpResp<ProductInfoResp>>() {
        });
    }
}
