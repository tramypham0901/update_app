package com.example.contactbook.remote;

import com.example.contactbook.service.AttendanceService;
import com.example.contactbook.service.AuthenticateService;
import com.example.contactbook.service.ClassService;
import com.example.contactbook.service.MarkService;
import com.example.contactbook.service.PostService;
import com.example.contactbook.service.ScheduleService;
import com.example.contactbook.service.UserService;

public class APIUtils {

    public static final String API_URL = "http://192.168.1.124:8080/";

    public APIUtils() {
    }

    public static UserService getUserService() {
        return RetrofitClient.getClient(API_URL).create(UserService.class);
    }

    public static AuthenticateService getAuthenticateService() {
        return RetrofitClient.getClient(API_URL).create(AuthenticateService.class);
    }

    public static MarkService getMarkService() {
        return RetrofitClient.getClient(API_URL).create(MarkService.class);
    }

    public static ScheduleService getScheduleService() {
        return RetrofitClient.getClient(API_URL).create(ScheduleService.class);
    }

    public static ClassService getClassService() {
        return RetrofitClient.getClient(API_URL).create(ClassService.class);
    }

    public static AttendanceService getAttendanceService() {
        return RetrofitClient.getClient(API_URL).create(AttendanceService.class);
    }

    public static PostService getPostService() {
        return RetrofitClient.getClient(API_URL).create(PostService.class);
    }
}
