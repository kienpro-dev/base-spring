package com.example.projectbase.constant;

public class UrlConstant {
  public static final String HOME = "/home";

  public static class Auth {
    private static final String PRE_FIX = "/auth";

    public static final String LOGIN = PRE_FIX + "/login";
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
