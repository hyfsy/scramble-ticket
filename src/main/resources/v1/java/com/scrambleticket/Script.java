package com.scrambleticket;

import java.nio.file.Files;
import java.nio.file.Paths;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

public class Script {
    public static void main(String[] args) throws Exception {

        // String url = "https://mobile.12306.cn/otsmobile/antcaptcha/suite1608722853171.js";
        // String url = "https://kyfw.12306.cn/otn/resources/js/framework/SM4.js";

        // String scriptFile = "C:\\Users\\user\\Desktop\\test\\project\\frame-parent\\frame-examples\\frame-scramble-ticket\\src\\main\\resources\\suite.js";
        // String scriptFile = "C:\\Users\\user\\Desktop\\passport\\suite.js";
        String scriptFile = "C:\\Users\\user\\Desktop\\passport\\SM4.js";

        // ScriptEngineManager manager = new ScriptEngineManager();
        // ScriptEngine engine = manager.getEngineByName("js"); // nashorn

        NashornScriptEngineFactory nashornScriptEngineFactory = new NashornScriptEngineFactory();
        NashornScriptEngine engine = (NashornScriptEngine) nashornScriptEngineFactory.getScriptEngine("-doe", "-d=C:\\Users\\user\\Desktop\\test\\project\\frame-parent\\frame-examples\\frame-scramble-ticket\\src\\main\\resources\\SuiteJs_" + System.currentTimeMillis() + ".class");

        String script = new String(Files.readAllBytes(Paths.get(scriptFile)));
        engine.eval(script);
        engine.eval("");

        // {
        //     Object eval = engine.eval("'a'");
        //     System.out.println(eval);
        //
        //     engine.eval("var window = {};window.json_ua = {};c=[];");
        //     engine.eval("!function acMain() {" +
        //             "window['json_ua'].toString = function() {\n" +
        //             // "                    return 'bbb'" +
        //             "                    return c[1721] = \"\",\n" +
        //             "                    c[1721] = c[1721].split(\"\"),\n" +
        //             "                    c[1721] = c[1721].reverse(),\n" +
        //             "                    c[1721] = c[1721].join(\"\"),\n" +
        //             "                    c[1722] = c[1721],\n" +
        //             "                    c[1062] = 'success',\n" +
        //             // "                    be(Wa, 5).r(),\n" +
        //             "                    c[1722] = c[1062],\n" +
        //             "                    c[1722]\n" +
        //             "                }" +
        //             "}();");
        //
        //     engine.eval("function aaa() {return 'a';}");
        //     Object eval1 = engine.eval("aaa()");
        //     System.out.println(eval1);
        //
        //     Object eval2 = engine.eval("window['json_ua'].toString()");
        //     System.out.println(eval2);
        //     Object eval3 = engine.eval("window['json_ua'].toString");
        //     System.out.println(eval3);
        // }

        // String script = new String(Files.readAllBytes(Paths.get(scriptFile)));
        //
        // Object eval = engine.eval("var window = {};\n" + "window.json_ua = {};");
        // System.out.println(eval);
        //
        // // 执行JavaScript代码
        // Object eval1 = engine.eval(script);
        // System.out.println(eval1);
        //
        // System.out.println(engine.eval("window._a"));
        // System.out.println(engine.eval("window._b"));
        // System.out.println(engine.eval("window.a"));
        //
        // engine.eval("function getType(obj) {\n" +
        //         "  return Object.prototype.toString.call(obj).slice(8, -1);\n" +
        //         "}");
        // engine.eval("function getEncryptedData() {return window.json_ua.toString();}");
        //
        // Thread.sleep(3_000);
        //
        // Object result = engine.eval("window.json_ua.toString");
        // System.out.println(result);
        // Object result2 = engine.eval("window.json_ua.toString()");
        // System.out.println(result2);
        // System.out.println("==========");

        // Invocable invocable = (Invocable) engine;
        // Object o = invocable.invokeFunction("getEncryptedData");
        // System.out.println(o);

        // Object eval = engine.eval("getType(window.json_ua.toString())");
        // System.out.println(eval);
        // Object eval2 = engine.eval("typeof window.json_ua.toString()");
        // System.out.println(eval2);
        // Object result = engine.eval("window.json_ua.toString()");
        // System.out.println(result);
        // Object result2 = engine.eval("JSON.stringify(window.json_ua.toString())");
        // System.out.println(result2);

        // ScriptObjectMirror result2 = (ScriptObjectMirror) engine.eval("window.json_ua.valueOf()");
        // System.out.println(result2);

    }
}
