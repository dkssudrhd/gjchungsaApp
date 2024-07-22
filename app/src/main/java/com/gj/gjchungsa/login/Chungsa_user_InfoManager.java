package com.gj.gjchungsa.login;

public class Chungsa_user_InfoManager {
    public static Chungsa_user chungsa_user;

    public static Chungsa_user getUserInfo(){
        return chungsa_user;
    }

    public static void setUserInfo(Chungsa_user user){
        chungsa_user = user;
    }
}
