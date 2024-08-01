package com.snake.admin.common.security.sm3;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SM3Utils {

    private static Invocable invocable = null;

    static {
        try {
            InputStream inputStream = SM3Utils.class.getClassLoader().getResourceAsStream("sm3.js");
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            assert inputStream != null;
            engine.eval(new BufferedReader(new InputStreamReader(inputStream)));
            invocable = (Invocable) engine;
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    /**
     * 杂凑
     *
     * @param msg 明文
     * @return 杂凑
     * @throws ScriptException Scripting通用异常
     */
    public static String sm3(String msg) throws ScriptException {
        if (msg == null || msg.trim().isEmpty()) {
            return "";
        }
        String hashData = null;
        try {
            hashData = (String) invocable.invokeFunction("constructSm3", msg);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return hashData;
    }
}
