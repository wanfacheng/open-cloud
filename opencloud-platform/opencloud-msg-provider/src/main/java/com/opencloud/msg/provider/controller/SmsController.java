package com.opencloud.msg.provider.controller;

import com.opencloud.common.model.ResultBody;
import com.opencloud.msg.client.api.SmsRemoteApi;
import com.opencloud.msg.client.model.SmsNotify;
import com.opencloud.msg.provider.dispatcher.MessageDispatcher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 推送通知
 *
 * @author woodev
 */
@RestController
@Api(value = "短信", tags = "短信")
public class SmsController implements SmsRemoteApi {


    @Autowired
    private MessageDispatcher dispatcher;

    /**
     * 短信通知
     *
     * @param phoneNumber
     * @param templateCode
     * @param signName
     * @param params
     * @return
     */
    @ApiOperation(value = "发送短信", notes = "发送短信")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phoneNumber", value = "手机号码", required = true, paramType = "form"),
            @ApiImplicitParam(name = "templateCode", value = "模板编号", required = true, paramType = "form"),
            @ApiImplicitParam(name = "signName", value = "短信签名", required = true, paramType = "form"),
            @ApiImplicitParam(name = "params", value = "模板参数:json字符串", required = false, paramType = "form"),
    })
    @PostMapping("/sms")
    @Override
    public ResultBody<String> sendSms(@RequestParam(value = "phoneNumber", required = true) String phoneNumber,
                                      @RequestParam(value = "templateCode", required = true) String templateCode,
                                      @RequestParam(value = "signName", required = true) String signName,
                                      @RequestParam(value = "params", required = false) String params) {
        SmsNotify smsNotification = new SmsNotify();
        smsNotification.setPhoneNumber(phoneNumber);
        smsNotification.setTemplateCode(templateCode);
        smsNotification.setSignName(signName);
        smsNotification.setParams(params);
        this.dispatcher.dispatch(smsNotification);
        return ResultBody.ok();
    }
}
