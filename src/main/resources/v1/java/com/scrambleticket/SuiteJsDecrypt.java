
package com.scrambleticket;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class SuiteJsDecrypt {

    // window.json_ua.toString()
    //

    /*
    
    
            c[49] = SPqegH3xTF.9tmDXWJoukZndVQ_ClsU80B5GhyRY2NM1L76rAKbEfOvawzjipcI4 = c[49] + ... + c[64] // 固定值
    
    
            c[51] < 6
    
            c[50] = "",
            while (?) {
                c[50] = c[50] + c[49].charAt(63 & c[56]),
            }
            c[50] = c[50] + c[49].charAt(c[51]),
    
    
            c[66] = c[50],
            c[964] = c[66],
            c[1062] = c[964],
            c[1722] = c[1062],
            return c[1722];
    
    */
    public static void main(String[] args) throws Exception {
        String scriptFile = "C:\\Users\\user\\Desktop\\passport\\suite.js";
        String script = new String(Files.readAllBytes(Paths.get(scriptFile)));
        SuiteJsDecrypt decrypt = new SuiteJsDecrypt();
        SuiteJs suiteJs = decrypt.new SuiteJs(script);

        Entrance rootEntrance = suiteJs.getRootEntrance();
        EntranceOperator operator = new EntranceOperator(rootEntrance);

        // 找到方法调用的所有参数
        // Map<String, Set<Integer>> ids = new HashMap<>();
        // operator.op(d -> {
        //     ids.putIfAbsent(d._n, new HashSet<>());
        //     ids.get(d._n).add(d._e);
        // });

        // sa $a zr
        // 1638 1639 1646 1599 1
        // 找到方法调用的调用点
        Map<String, Integer> ids = new HashMap<>();
        for (Function _function : suiteJs._functions) {
            List<String> names = _function.entrances.stream().map(e -> e._n).distinct().collect(Collectors.toList());
            for (String name : names) {
                ids.compute(name, (k, v) -> {
                    if (v == null) {
                        return 1;
                    }
                    return v + 1;
                });
            }
        }
        ids.forEach((k, v) -> {
            if (v == 1) {
                System.out.println(k);
            }
        });

        // String scriptFile2 = "C:\\Users\\user\\Desktop\\test\\project\\frame-parent\\frame-examples\\frame-scramble-ticket\\src\\main\\resources\\temp.js";
        // String script2 = new String(Files.readAllBytes(Paths.get(scriptFile2)));
        // String[] lines = script2.split("\r\n");
        // List<String> fc_names = new LinkedList<>();
        // for (String line : lines) {
        //     if (line.startsWith("function ")) {
        //         int i = line.indexOf("function ");
        //         String substring = line.substring(i + "function ".length(), line.indexOf("(e"));
        //         fc_names.add(substring);
        //     }
        // }
        // for (String fcName : fc_names) {
        //     if ("be".equals(fcName)) continue;
        //     Set<Integer> integers = ids.get(fcName);
        //     System.out.println(fcName + " " + integers.stream().map(String::valueOf).collect(Collectors.joining(" ")));
        // }
        //
        // System.out.println();

        // String javaCodeFile =
        // "C:\\Users\\user\\Desktop\\test\\project\\frame-parent\\frame-examples\\frame-scramble-ticket\\src\\main\\java\\com\\scrambleticket\\a_gen\\"
        // + System.currentTimeMillis() + "\\Temp.java";
        // String javaCode = suiteJs.toJavaCode();
        // new File(javaCodeFile).getParentFile().mkdirs();
        // new File(javaCodeFile).createNewFile();
        // Files.write(new File(javaCodeFile).toPath(), javaCode.getBytes(StandardCharsets.UTF_8));

        System.out.println();

    }

    static class EntranceOperator {
        Entrance _root;

        public EntranceOperator(Entrance _root) {
            this._root = _root;
        }

        void op(EntranceConsumer consumer) {
            Set<Entrance> entrances = new HashSet<>();
            _op(_root, entrances, consumer);
        }

        void _op(Entrance entrance, Set<Entrance> entrances, EntranceConsumer consumer) {
            if (!entrances.add(entrance)) {
                return;
            }

            consumer.consume(new EntranceOperateData(entrance));
            Set<Entrance> _entrances = entrance._func.entrances;
            for (Entrance _entrance : _entrances) {
                _op(_entrance, entrances, consumer);
            }
        }
    }

    interface EntranceConsumer {
        void consume(EntranceOperateData data);
    }

    static class EntranceOperateData {
        Entrance entrance;
        String _n;
        int _e;
        boolean _new;
        Set<Entrance> _ch;

        public EntranceOperateData(Entrance entrance) {
            this.entrance = entrance;
            this._n = entrance._n;
            this._e = entrance._e;
            this._new = entrance._new;
            this._ch = new HashSet<>(entrance._func.entrances);
        }
    }

    // window.json_ua.toString()
    class SuiteJs {
        String _text;
        int _funcStartIdx;
        int _funcEndIdx;
        String _startText;
        String _endText;
        String[] _lines;
        List<Function> _functions;

        Map<String, Function> _functions_map;
        Function _root_func;
        Entrance _root_entrance;

        public SuiteJs(String text) {
            this._text = text;

            this._lines = text.split("\r\n");
            int[] functionsRangeIdxs = getFunctionsRangeIdx(_lines);
            this._funcStartIdx = functionsRangeIdxs[0];
            this._funcEndIdx = functionsRangeIdxs[1];
            this._startText = multiLineToText(_lines, 0, _funcStartIdx - 1);
            this._endText = multiLineToText(_lines, _funcEndIdx + 1, _lines.length - 1);

            this._functions = parseFunctions(_lines, _funcStartIdx, _funcEndIdx);
            this._functions_map = this._functions.stream().collect(Collectors.toMap(f -> f._name, f -> f));
            this._root_func = getRootFunc();
            this._root_entrance = getRootEntrance();
        }

        private int[] getFunctionsRangeIdx(String[] lines) {
            int acMainStartIdx = -1;
            int funcStartIdx = -1;
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (line.contains("acMain")) {
                    acMainStartIdx = i;
                    continue;
                }
                if (acMainStartIdx != -1 && line.contains("function")) {
                    funcStartIdx = i;
                    break;
                }
            }
            int acMainEndIdx = -1;
            int funcEndIdx = -1;
            for (int i = lines.length - 1; i >= 0; i--) {
                String line = lines[i];
                if (line.contains("}();")) {
                    acMainEndIdx = i;
                    continue;
                }
                if (acMainEndIdx != -1 && line.contains("}")) {
                    funcEndIdx = i;
                    break;
                }
            }

            if (funcStartIdx == -1 || funcEndIdx == -1) {
                throw new RuntimeException("文件解析错误");
            }

            return new int[] {funcStartIdx, funcEndIdx};
        }

        private List<Function> parseFunctions(String[] lines, int start, int end) {

            List<Function> functions = new LinkedList<>();

            for (int i = start; i <= end; i++) {
                String line = lines[i];
                if (line.startsWith("    function")) {
                    // if (line.startsWith(" function be(")) {
                    // continue; // apply 递归函数忽略
                    // }
                    int startIdx = i;
                    while (!lines[i].startsWith("    }"))
                        i++;
                    functions.add(new Function(lines, startIdx, i));
                }
            }

            linkFunctionEntrances(functions);
            return functions;
        }

        private String toJavaCode() {
            String startText = "\n" + "package com.scrambleticket;\n" + "\n" + "public class Temp {\n" + "\n"
                + "    Window window = new Window();\n" + "    Object[] c = new Object[2000];\n"
                + "    Object[] r = new Object[2000];\n" + "    Document document = new Document();\n" + "    \n"
                + "    static int toInt(Object o) {\n" + "        return Integer.parseInt(String.valueOf(o));\n"
                + "    }\n" + "    static boolean toBoolean(Object o) {\n"
                + "        return Boolean.parseBoolean(String.valueOf(o));\n" + "    }"
                + "    static String toString(Object o) {\n" + "        return String.valueOf(o);\n" + "    }\n"
                + "    \n" + "    class Document {\n" + "        public void createElement(Object o) {\n"
                + "            \n" + "        }\n" + "    }" + "\n" + "    class Window {\n"
                + "        UA_Opt_12306 ua_opt_12306 = new UA_Opt_12306();\n"
                + "        Json_Ua json_ua = new Json_Ua();\n" + "\n" + "        class UA_Opt_12306 {\n"
                + "            Runnable setToken = () -> {\n" + "            };\n"
                + "            Runnable reload = () -> {\n" + "            };\n" + "            String image =\n"
                + "                \"data:image/png;base64,Bl4XoyTN.d7N6CYWP0teP_YQvdqCAtPCZj_RpUFKlQt0gPSAg0hF3cBLC9WnqALsJ_IrjyDhNXldB2_Je0lxqdViDfv1vmBE0pEtupdrib2F5VRSj8YzgqVugEdW_35wd5prlowdHEhf3B26_eWfdH3yN8JSTqP0VibpajQuk4kMHJk45hZHEIzaszy82M9ctzokNjBkZXEMsdn8DBfsFw4.bhndKLQJ449benUIr7yV9qKfVGgKkvVEtOnGamK4Z5L18lNyQ_IWogGVW5gBV4JjU2zIXEcPCeVvnBRP0QBK7CdglGhzov2OmZpYYNjzXPwUTY2cXkspXGzqeV69B6aiIKQwXeKg2sas8HnYN4.TJgutI8xKqt86i5yhtGFn1HQnIM4_vYnkoh1U6sxJ_eSV3q5_ukadNs48U1FQnU7a71CPaUXGX6JblNVBYQvpPSTqP1aufvweGTbXbIG2Ti4ofRvPElOtryc8wundyjTVeSAk7HN_iMFJk8t1RF5Ew8acxqI_R1kqSWgeKRmGNoTJgutWqS0Vu5lY_lzYQjhle0won5f7sKwXeKxJzUkfCGlanW6T5TXlCRjbaRcrLu0lxqJE79uxJ5tGQCPVnDBl95zb7cCnVUKvsN1qVn3Gq0kHe4aEpAjDJf8FcY7iGuRJh_GOc4lq4xzI0RIj67kC0TqCFPTomBIq0NaacrVPewM.BuVC3bvhky9vle0woSnXGw22HPWpTzblrfIMM6kNRQRASVvmGSUDG2Y_CnvaDzqWMc45wGf4hN.7W_v7iXPwUT56ZO8z0gPSAHvG24jtJhC.YlXedy602X6jdZaYvUxBDPDgBFlyYzIjeSV3q7JM_1iEcXWxBgFqKNXgKHs0tePW6ceW_zFFos6vnOr7oqWhCgRNMUeY4rwQK3F5NyRB28BO7i8qIxKpB3vi6cQ4L19mUC3Kb9BldDJ.FoZrkq02UPnxBz9rIr2ZYp9qd8Nc8VscLiXDUiqAb4FzUVrdz6VKhldHZgU616yyri4_XBp8TQYPAZ5MZZd4EUr1F2a9bf_EOmJYYhcXfuTQ3orPiUfLqSWgeHWM.Yjnofrs2HPWp.aCva_xELzFE3QRMhDL1D42sxolBhmeKCKUd69Xj62yLsdZQFKDu7.OOcs5uqVR_2YFbkSs0wDUXQDu1LBhQ.ECwDSiNciUU8rApS7mMjrR0hHPAE3Zy_uo_nRij4GwgpYNjoPgT5FROU8YtNBtumJ9M.GGgVnHy33crsEeiDArse2ju2rTbgXy44p_4Yc40UPAlFeuzDWaHS2.x6QElzdKc8kg9PVnD7Q1_ifSSSSSyl8xkR6w6mtayN_Bw4EpxK2meJl7Rz6vA3P02tpN6WXQA.QKgSyLqjIhB0livtEr58hu_qpd3GoyT9KuHkZa2NJksJa.Kr.I9egEaq7yoqHjXE_P7uhOYBwcX2riXmvYJXx6tyC_o7gVuscQ76mbYW9uGDGoEZIdeHNJRXv_SntJqqsYe5fXzuUTCRpl7qmV97QvVWdEJa6Jcajj0P.qc22CkG5XHFbMRVvDCQe5hMOwZfqGJLD0dU3gYAQMcg9gtmPFnboN4R.B7abiL1HlFo5xILFcLiPzzLn6rK4PZUr9OgpQiWWMoOeCER_LsgmUj7ZDObCF0zmhwmiAMf3Z_8GNT6stMTncie1T4acAzSNEcJQ1StUwyrAmRvS\";\n"
                + "        }\n" + "\n" + "        class Json_Ua {\n" + "\n" + "        }\n" + "    }\n" + "\n"
                + "    public static void main(String[] args) {\n" + "        new Temp().init();\n" + "    }\n" + "\n"
                + "    public void init() {\n" + "        be(this::Re, 0).r();\n" + "    }\n" + "\n"
                + "    private Pivot_R be(PivotConsumer e, int a) {\n" + "        return new Pivot_R(e, a);\n"
                + "    }\n" + "    class Pivot_R implements PivotConsumer {\n" + "        private Object[] j;\n"
                + "        public Pivot_R(PivotConsumer e, int a) {\n" + "            this.j = new Object[]{e, a};\n"
                + "        }\n" + "\n" + "        private void r() {\n" + "            for (; null != j; ) {\n"
                + "                Object[] e = j;\n" + "                j = null;\n" + "                try {\n"
                + "                    ((PivotConsumer) e[0]).apply((int) e[1], this);\n"
                + "                } catch (Exception ex) {}\n" + "            }\n" + "        }\n" + "\n"
                + "        @Override\n" + "        public void i(PivotConsumer e, int a) {\n"
                + "            j = new Object[]{e, a};\n" + "        }\n" + "\n" + "        @Override\n"
                + "        public void apply(int e, PivotConsumer a) {\n" + "            // 仅实现接口，无实际功能\n"
                + "        }\n" + "    }\n" + "    interface PivotConsumer {\n"
                + "        void apply(int e, PivotConsumer a); // consumer 使用\n"
                + "        default void i(PivotConsumer e, int a) {\n" + "            // Pivot 使用\n" + "        }\n"
                + "    }\n";
            String endText = "\n" + "}";

            StringBuilder sb = new StringBuilder();
            sb.append(startText);
            for (Function function : _functions) {
                function.toJavaCode(sb);
            }
            sb.append(endText);
            return sb.toString();
        }

        private void linkFunctionEntrances(List<Function> functions) {
            Map<String, Function> _functions_map = functions.stream().collect(Collectors.toMap(f -> f._name, f -> f));
            for (Function function : functions) {
                for (Entrance entrance : function.entrances) {
                    entrance._func = _functions_map.get(entrance._n);
                }
            }
        }

        private Function getRootFunc() {
            return getFunc("Re");
        }

        private Function getFunc(String name) {
            return _functions_map.get(name);
        }

        private Entrance getRootEntrance() {
            Function rootFunc = getRootFunc();
            // be(Re, 0).r()
            Entrance rootEntrance = new Entrance(rootFunc._name, 0, true);
            rootEntrance._func = rootFunc;
            return rootEntrance;
        }
    }

    class Entrance {
        String _n;
        int _e;
        boolean _new;

        Function _func;

        public Entrance(String _n, int _e, boolean _new) {
            this._n = _n;
            this._e = _e;
            this._new = _new;
        }

        @Override
        public String toString() {
            // be(Hs, 1).r()
            // a.i(Ja, 0)
            return _new ? "be(" + _n + ", " + _e + ").r()" : "a.i(" + _n + ", " + _e + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Entrance entrance = (Entrance)o;
            return _e == entrance._e && _new == entrance._new && Objects.equals(_n, entrance._n)
                && Objects.equals(_func, entrance._func);
        }

        @Override
        public int hashCode() {
            return Objects.hash(_n, _e, _new, _func);
        }
    }

    class Function extends Text {
        String _name;
        List<String> _paramNames;
        Loop _loop;

        Set<Entrance> entrances = new LinkedHashSet<>();

        public Function(String[] lines, int start, int end) {
            super(lines, start, end);

            String funcSig = lines[start];
            this._name = funcSig.substring(funcSig.indexOf("function ") + "function ".length(), funcSig.indexOf("("));
            String[] paramNameArray = funcSig.substring(funcSig.indexOf("(") + 1, funcSig.indexOf(")")).split(",");
            this._paramNames = Arrays.stream(paramNameArray).map(String::trim).collect(Collectors.toList());

            if (!isPivotFunction(this._name)) { // 中枢方法不解析
                this._loop = new Loop(lines, start + 1, end - 1);
            }

            parseEntrances();
        }

        private boolean isPivotFunction(String name) {
            return "be".equals(name);
        }

        public void toJavaCode(StringBuilder sb) {
            if ("g".equals(_name)) {
                _loop._switch._cases.get(8)._block = null;
            }
            if (!isPivotFunction(this._name)) { // 中枢方法不解析
                sb.append("\tprivate void ").append(_name).append("(int e, PivotConsumer a) {\n");
                _loop.toJavaCode(sb);
                sb.append("\t}\n");
            }
        }

        private void parseEntrances() {
            if (_loop == null)
                return;

            for (Case _case : _loop._switch._cases) {
                if (_case._block == null)
                    continue;
                for (Statement _statement : _case._block._statements) {
                    // be(Hs, 1).r()
                    // a.i(Ja, 0)
                    String code = _statement._text;

                    String be_kw = "be(";

                    int contIdx = 0;
                    while (true) {
                        int be_idx = code.indexOf(be_kw, contIdx);
                        if (be_idx == -1) {
                            break;
                        }
                        int be_idx_end = code.indexOf(")", be_idx + be_kw.length());
                        String param = code.substring(be_idx + be_kw.length(), be_idx_end);
                        String[] params = param.split(", ");
                        entrances.add(new Entrance(params[0], Integer.parseInt(params[1]), true));
                        contIdx = be_idx_end;
                    }

                    int contIdx2 = 0;
                    while (true) {
                        String ai_kw = "a.i(";
                        int ai_idx = _statement._text.indexOf(ai_kw, contIdx2);
                        if (ai_idx == -1) {
                            break;
                        }
                        int ai_idx_end = code.indexOf(")", ai_idx + ai_kw.length());
                        String param = code.substring(ai_idx + ai_kw.length(), ai_idx_end);
                        String[] params = param.split(", ");
                        entrances.add(new Entrance(params[0], Integer.parseInt(params[1]), false));
                        contIdx2 = ai_idx_end;
                    }
                }
            }
        }
    }

    class Loop extends Text {
        Switch _switch;

        public Loop(String[] lines, int start, int end) {
            super(lines, start, end);

            this._switch = new Switch(lines, start + 1, end);
        }

        public void toJavaCode(StringBuilder sb) {
            sb.append("\t\tfor (; -1 < e; )").append("\n");
            _switch.toJavaCode(sb);
        }
    }

    class Switch extends Text {
        String _switch_text;
        List<Case> _cases;

        public Switch(String[] lines, int start, int end) {
            super(lines, start, end);

            String firstLine = lines[start];
            this._switch_text = firstLine.substring(firstLine.indexOf("(") + 1, firstLine.indexOf(")"));

            List<Case> cases = new LinkedList<>();
            int startCase = -1;
            for (int i = start; i <= end - 1; i++) {
                String line = lines[i];
                if (line.startsWith("            case")) {
                    if (startCase != -1) {
                        cases.add(new Case(lines, startCase, i - 1));
                    }
                    startCase = i;
                }
            }
            // 最后一个特殊处理
            if (startCase != -1 && startCase != end - 1)
                cases.add(new Case(lines, startCase, end - 1));
            this._cases = cases;
        }

        public void toJavaCode(StringBuilder sb) {
            sb.append("\t\t\tswitch (e) {").append("\n");
            for (Case _case : _cases) {
                _case.toJavaCode(sb);
            }
            sb.append("\t\t\t}").append("\n");
        }
    }

    class Case extends Text {
        int _e;
        Block _block;

        public Case(String[] lines, int start, int end) {
            super(lines, start, end);

            String line = lines[start];
            this._e = Integer.parseInt(line.substring(line.indexOf("case ") + "case ".length(), line.indexOf(":")));
            if (start < end) // 多个case一个逻辑的情况
                this._block = new Block(lines, start + 1, end);
        }

        public void toJavaCode(StringBuilder sb) {
            sb.append("\t\t\t\tcase ").append(_e).append(":").append("\n");
            if (start < end && _block != null)
                _block.toJavaCode(sb);
        }
    }

    // ;代表一个语句，语句内有,，则分裂为多个语句
    // 最后的,转;，无则加;
    // void a. 调用的情况，筛出来，void a.x -> a.x
    // return 开头的单个语句，查找是否有,有的话则查找最后一个语句配置为return;
    class Block extends Text {

        List<Statement> _statements;

        public Block(String[] lines, int start, int end) {
            super(lines, start, end);

            List<String> statements = new LinkedList<>(); // Arrays.asList(text.replace("\r\n", "").split(","))
            for (int i = start; i <= end; i++) {
                statements.add(lines[i]);
            }

            String space = generateStartSpaceByText(statements.get(0));
            String returnStatement = null;
            if (statements.get(0).trim().startsWith("return")) {
                statements.set(0, statements.get(0).replace("return ", ""));
                returnStatement = space + "return;"; // 最后再添加
            }
            for (int i = 0; i < statements.size(); i++) {
                String statement = statements.get(i);
                if (!statement.endsWith(";") && !(statement.endsWith("{") || statement.endsWith("}"))) {
                    if (statement.endsWith(",")) {
                        statements.set(i, statement.substring(0, statement.length() - 1) + ";");
                    } else {
                        statements.set(i, statement + ";");
                    }
                }
            }

            for (int i = 0; i < statements.size(); i++) {
                statements.set(i, statements.get(i).replace("void a.", "a."));
            }
            if (returnStatement != null)
                statements.add(returnStatement);
            _statements = statements.stream().map(Statement::new).collect(Collectors.toList());
        }

        public void toJavaCode(StringBuilder sb) {
            for (Statement _statement : _statements) {
                _statement.toJavaCode(sb);
            }
        }
    }

    class Statement extends Text {
        String _text;

        public Statement(String text) {
            super(new String[] {text}, 0, 0);
            this._text = text;
        }

        private boolean isBreak() {
            return _text.contains("break;");
        }

        private boolean isReturn() {
            return _text.contains("return;");
        }

        private final Map<String, String> replaceTable = new HashMap() {
            {
                put("a.i(", "a.i(this::");
                put("be(", "be(this::");
                put("$ex", "Exception e");
            }
        };

        // FIXME
        private void toJavaCode(StringBuilder sb) {

            // 函数调用
            String code = _text;
            for (Map.Entry<String, String> entry : replaceTable.entrySet()) {
                code = code.replace(entry.getKey(), entry.getValue());
            }

            // 方法调用的类型转换
            int idx = 0;
            while (idx != -1) {
                int tempIdx = _text.indexOf(".", idx);// 方法调用，类型转换
                idx = tempIdx + 1;
                if (tempIdx == -1)
                    break;
                TypeChar typeChar = TypeChar.get(_text.charAt(tempIdx - 1)); // 调用对象边界

                switch (typeChar) {
                    case ARRAY_INVOKE:
                        for (int i = tempIdx - 1; i > 0; i--) {
                            if ('c' == _text.charAt(i)) { // c[xxx].xxx
                                String _s = _text.substring(0, i);
                                String _m = "(toString(" + _text.substring(i, tempIdx) + "))";

                                int braceIdx = tempIdx + 1;
                                while (braceIdx < _text.length()) {
                                    if (_text.charAt(braceIdx) == '(') {
                                        break;
                                    }
                                    braceIdx++;
                                }

                                if (braceIdx == _text.length())
                                    continue;

                                int braceCount = 1;
                                int braceEndIdx = -1;
                                for (int j = braceIdx + 1; j < _text.length(); j++) {
                                    char c = _text.charAt(j);
                                    if (c == '(')
                                        braceCount++;
                                    else if (c == ')')
                                        braceCount--;
                                    if (braceCount == 0) {
                                        braceEndIdx = j;
                                        break;
                                    }
                                }
                                String _e = _text.substring(tempIdx, braceIdx + 1) + "toString("
                                    + _text.substring(braceIdx + 1, braceEndIdx) + ")" + _text.substring(braceEndIdx);
                                code = _s + _m + _e;
                                break;
                            }
                        }

                        break;
                    case PIVOT_INVOKE:
                    case PIVOT_LOOP_INVOKE: // ignored
                        break;
                    case STATIC_INVOKE:
                        // TODO
                        break;
                    default:
                        throw new RuntimeException("unknown typeChar: " + typeChar);
                }
            }

            // 数据的整形计算

            code = toInt(code, "c");
            code = toInt(code, "r");

            sb.append("\t").append(code).append("\n");
        }

        private String toInt(String code, String search) {

            int idx = 0;
            while (idx != -1) {
                int tempIdx = code.indexOf(search + "[", idx);
                idx = tempIdx + 2;
                if (tempIdx == -1)
                    break;

                int braceIdx = tempIdx + 1;
                int braceEndIdx = -1;
                int braceCount = 0;
                while (true) {
                    int cursor = Math.max(braceIdx, braceEndIdx + 1);
                    for (int j = cursor; j < code.length(); j++) {
                        char c = code.charAt(j);
                        if (c == '[')
                            braceCount++;
                        else if (c == ']')
                            braceCount--;
                        if (braceCount == 0) {
                            braceEndIdx = j;
                            break;
                        }
                    }
                    if (code.charAt(braceEndIdx + 1) != '[')
                        break; // 多个 [ 情况
                }

                int tempEndIdx = braceEndIdx;
                char c = code.charAt(tempEndIdx + 1); // 后面的
                if (c == ' ') {
                    c = code.charAt(tempEndIdx + 2);
                    if (c == '<' || c == '>' || c == '^' || c == '&' || c == '|' || c == '+' || c == '-' || c == '*'
                        || c == '/') {
                        char nextChar = code.charAt(tempEndIdx + 3);
                        if ((c == '&' || c == '|') && (nextChar == '&' || nextChar == '|')) {
                            code = code.substring(0, tempIdx) + "toBoolean(" + code.substring(tempIdx, tempEndIdx + 1)
                                + ")" + code.substring(tempEndIdx + 1);
                        } else {
                            code = code.substring(0, tempIdx) + "toInt(" + code.substring(tempIdx, tempEndIdx + 1) + ")"
                                + code.substring(tempEndIdx + 1);
                        }
                    }
                } else {
                    c = code.charAt(tempIdx - 1); // 前面的
                    if (c == ' ') {
                        c = code.charAt(tempIdx - 2);
                        if (c == '<' || c == '>' || c == '^' || c == '&' || c == '|' || c == '+' || c == '-' || c == '*'
                            || c == '/') {
                            char nextChar = code.charAt(tempIdx - 3);
                            if ((c == '&' || c == '|') && (nextChar == '&' || nextChar == '|')) {
                                code = code.substring(0, tempIdx) + "toBoolean("
                                    + code.substring(tempIdx, tempEndIdx + 1) + ")" + code.substring(tempEndIdx + 1);
                            } else {
                                code = code.substring(0, tempIdx) + "toInt(" + code.substring(tempIdx, tempEndIdx + 1)
                                    + ")" + code.substring(tempEndIdx + 1);
                            }
                        }

                    }
                }
            }
            return code;
        }
    }

    enum TypeChar {
        ARRAY_INVOKE(']'), PIVOT_INVOKE(')'), //
        PIVOT_LOOP_INVOKE('a'), //
        STATIC_INVOKE('~'),;

        final char typeChar;

        TypeChar(char typeChar) {
            this.typeChar = typeChar;
        }

        public static TypeChar get(char typeChar) {
            for (TypeChar value : values()) {
                if (value.typeChar == typeChar)
                    return value;
            }
            if (typeChar >= 97 && typeChar <= 122) {
                return STATIC_INVOKE;
            }
            return STATIC_INVOKE; // TF.9
            // throw new RuntimeException("unknown typeChar: [" + typeChar + "]");
        }
    }

    abstract class Text {
        String[] lines;
        int start;
        int end;

        public Text(String[] lines, int start, int end) {
            this.lines = lines;
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return multiLineToText(lines, start, end);
        }
    }

    private int getStartSpaceCount(String text) {
        int c = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ')
                c++;
            else
                break;
        }
        return c;
    }

    private String generateStartSpaceByText(String text) {
        return generateSpace(getStartSpaceCount(text));
    }

    private String generateSpace(int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private int tabSize() {
        return 4;
    }

    private String multiLineToText(String[] lines, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i <= end; i++) {
            sb.append(lines[i]).append("\r\n");
        }
        return sb.toString();
    }
}
