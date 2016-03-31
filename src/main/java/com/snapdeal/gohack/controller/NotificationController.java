package com.snapdeal.gohack.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.snapdeal.gohack.model.ApiResponseEntity;
import com.snapdeal.gohack.model.NotificationDataDTO;
import com.snapdeal.gohack.model.NotificationResponseDTO;
import com.snapdeal.gohack.model.ResponseMetaDataV1;
import com.snapdeal.gohack.model.ResponseMetaGeneratorV1;
import com.snapdeal.gohack.model.UserWebRegistration;
import com.snapdeal.gohack.model.UserWebRegistrationDTO;


@RestController
@RequestMapping(value = "/web")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @RequestMapping(value = "/user/register", method = RequestMethod.POST, produces = { "application/json" })
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ApiResponseEntity<UserWebRegistrationDTO> doUserWebRegistration(@RequestBody UserWebRegistration userReg, HttpServletRequest request) throws Exception {
        long startTime = System.currentTimeMillis();
        ApiResponseEntity<UserWebRegistrationDTO> response = new ApiResponseEntity<>();
        UserWebRegistrationDTO userRegistrationResponse =notificationService.doUserWebRegsitration(userReg);
        response.setResult(userRegistrationResponse);
        long endTime = System.currentTimeMillis();
        response.setResponseMetadata(ResponseMetaGeneratorV1.getResponseMetadata(endTime - startTime, true, userRegistrationResponse.getMessage(), request.getRequestURI()));
        return response;

    }
    
    @RequestMapping(value = "/user/push", method = RequestMethod.POST, produces = { "application/json" })
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ApiResponseEntity<NotificationResponseDTO> push(@RequestBody NotificationDataDTO pushData, HttpServletRequest request) throws Exception {
        long startTime = System.currentTimeMillis();
        ApiResponseEntity<NotificationResponseDTO> response = new ApiResponseEntity<>();
        NotificationResponseDTO notificationReponse =notificationService.push(pushData);
        response.setResult(notificationReponse);
        long endTime = System.currentTimeMillis();
        response.setResponseMetadata(ResponseMetaGeneratorV1.getResponseMetadata(endTime - startTime, true, notificationReponse.getMessage(), request.getRequestURI()));
        return response;
        
    }


        @RequestMapping(value = "/user/undelivered", method = RequestMethod.GET, produces = { "application/json" })
        @ResponseStatus(HttpStatus.OK)
        public @ResponseBody ApiResponseEntity<NotificationDataDTO> getNotificationDetails(@RequestParam String registrationId, HttpServletRequest request) throws Exception {
            long startTime = System.currentTimeMillis();
            ApiResponseEntity<NotificationDataDTO> response = new ApiResponseEntity<>();
            NotificationDataDTO notficationData =notificationService.getNotificationDetails(registrationId);
            response.setResult(notficationData);
            long endTime = System.currentTimeMillis();
            response.setResponseMetadata(ResponseMetaGeneratorV1.getResponseMetadata(endTime - startTime, true, "gets you notification data", request.getRequestURI()));
            return response;

        }
        
    
    
    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    ApiResponseEntity<UserWebRegistrationDTO> handleServiceException(ServiceException ex, HttpServletRequest request) {
        ApiResponseEntity<UserWebRegistrationDTO> response = new ApiResponseEntity<>();
        ResponseMetaDataV1 responseMetadata = ResponseMetaGeneratorV1.getResponseMetadata(0, false, ex.getMessage(), request.getRequestURI());
        response.setResponseMetadata(responseMetadata);
        return response;
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    ApiResponseEntity<ResponseMetaDataV1> handleException(Exception ex, HttpServletRequest request) {
        ApiResponseEntity<ResponseMetaDataV1> response = new ApiResponseEntity<>();
        ResponseMetaDataV1 responseMetadata = ResponseMetaGeneratorV1.getResponseMetadata(0, false, ex.getMessage(), request.getRequestURI());
        response.setResponseMetadata(responseMetadata);
        return response;
    }


}
