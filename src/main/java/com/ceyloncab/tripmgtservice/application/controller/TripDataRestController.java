package com.ceyloncab.tripmgtservice.application.controller;

import com.ceyloncab.tripmgtservice.domain.service.TripManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${base-url.context}/trip")
public class TripDataRestController extends BaseController{
    @Autowired
    TripManagementService tripManagementService;
}
