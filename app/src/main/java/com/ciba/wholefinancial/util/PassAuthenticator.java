package com.ciba.wholefinancial.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by Administrator on 2017/3/30 0030.
 */
public class PassAuthenticator extends Authenticator
{
    public PasswordAuthentication getPasswordAuthentication()
    {
        /**
         * 这个地方需要添加上自己的邮箱的账号和密码
         */
        String username = "xumingda@ccfax.cn";
        String pwd = "829475Hong";
        return new PasswordAuthentication(username, pwd);
    }
}
