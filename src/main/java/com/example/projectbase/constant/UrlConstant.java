package com.example.projectbase.constant;

import org.springframework.security.core.parameters.P;

public class UrlConstant {
    public static final String HOME = "/home";
    public static final String ABOUT = "/about";
    public static final String CONTACT = "/contact";

    public static class Admin {
        private static final String PRE_FIX = "/admin";

        public static final String ADMIN_HOME = PRE_FIX + "/home";
        public static final String USERS_MANAGEMENT = PRE_FIX + "/users";
        public static final String DELETE_USER = PRE_FIX + "/delete/{id}";
        public static final String VIEW_USER = PRE_FIX + "/view/{id}";
        public static final String VIEW_ADMIN = PRE_FIX + "/view-admin/{id}";
        public static final String UPDATE_INFOR = PRE_FIX + "/update";
        public static final String LOGOUT_ADMIN = PRE_FIX + "/logout";
        public static final String CARS_MANAGEMENT = PRE_FIX + "/cars";

        private Admin() {

        }
    }

    public static class Auth {
        private static final String PRE_FIX = "/auth";

        public static final String LOGIN = PRE_FIX + "/login";
        public static final String ADMIN_LOGIN = PRE_FIX + "/admin/login";

        public static final String LOGOUT = PRE_FIX + "/logout";
        public static final String REGISTER = PRE_FIX + "/register";
        public static final String CHANGE_PASSWORD = PRE_FIX + "/change-password";
        public static final String FORGOT_PASSWORD = PRE_FIX + "/forgot-password";
        public static final String RESET_PASSWORD = FORGOT_PASSWORD + "/reset";
        public static final String SUBMIT_RESET = RESET_PASSWORD + "/submit";

        private Auth() {
        }
    }

    public static class User {
        private static final String PRE_FIX = "/user";

        public static final String GET_USERS = PRE_FIX;
        public static final String GET_USER = PRE_FIX + "/{userId}";
        public static final String GET_CURRENT_USER = PRE_FIX + "/current";

        private User() {
        }
    }

}
