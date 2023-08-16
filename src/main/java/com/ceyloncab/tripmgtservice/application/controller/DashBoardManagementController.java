package com.ceyloncab.tripmgtservice.application.controller;

import com.ceyloncab.tripmgtservice.application.transport.request.CommissionHistoryRequest;
import com.ceyloncab.tripmgtservice.application.transport.request.RevenueHistoryRequest;
import com.ceyloncab.tripmgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.tripmgtservice.domain.service.DashBoardManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("${base-url.context}/getDashboard")
public class DashBoardManagementController extends BaseController {

    @Autowired
    DashBoardManagementService dashBoardManagementService;

    @PostMapping(value = "/revenue/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getRevenueHistory(@RequestBody(required = true) RevenueHistoryRequest revenueHistoryRequest, HttpServletRequest request){
        CommonResponse response = dashBoardManagementService.getRevenueHistory(revenueHistoryRequest);
        return getResponseEntity(response.getResponseHeader().getResponseCode(),response);
    }

    @PostMapping(value = "/commission/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCommissionHistory(@RequestBody(required = true) CommissionHistoryRequest commissionHistoryRequest, HttpServletRequest request){
        CommonResponse response = dashBoardManagementService.getCommissionHistory(commissionHistoryRequest);
        return getResponseEntity(response.getResponseHeader().getResponseCode(),response);
    }

    @PostMapping(value = "/get/summery", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getSummery( HttpServletRequest request){
        CommonResponse response = dashBoardManagementService.getSummery();
        return getResponseEntity(response.getResponseHeader().getResponseCode(),response);
    }

}
