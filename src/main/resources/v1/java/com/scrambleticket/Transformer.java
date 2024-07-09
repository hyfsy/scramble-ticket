
package com.scrambleticket;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class Transformer {

    public static void main(String[] args) throws Exception {
        // return_func_transform();
        c_arr_id_count_statistics();
    }

    private static void return_func_transform() throws Exception {
        String scriptFile =
            "C:\\Users\\user\\Desktop\\test\\project\\frame-parent\\frame-examples\\frame-scramble-ticket\\src\\main\\resources\\suite_2.js";
        Path path = Paths.get(scriptFile);
        String script = new String(Files.readAllBytes(path));
        String[] lines = script.split("\r\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int rtnIdx = line.indexOf("return");
            if (rtnIdx == -1) {
                continue;
            }
            int i1 = line.indexOf("return void");
            int i2 = line.indexOf("return;");
            if (i1 != -1 || i2 != -1) {
                continue;
            }
            String nextFuncInvokeText = lines[i + 1];
            if (!nextFuncInvokeText.trim().startsWith("void ")) {
                continue;
            }
            int i3 = nextFuncInvokeText.indexOf("void ");
            String newLineText = line.substring(0, rtnIdx) + line.substring(rtnIdx + "return ".length());
            lines[i] = newLineText;
            lines[i + 1] = nextFuncInvokeText.substring(0, i3) + "return " + nextFuncInvokeText.substring(i3);
        }

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int rtnIdx = line.indexOf("return void ");
            if (rtnIdx == -1) {
                continue;
            }
            if (!line.endsWith(";")) {
                lines[i] = line + ";";
            }
        }

        Files.write(path, Arrays.stream(lines).collect(Collectors.joining("\r\n")).getBytes(StandardCharsets.UTF_8),
            StandardOpenOption.WRITE);

    }

    private static void c_arr_id_count_statistics() throws Exception {
        String scriptFile =
            "C:\\Users\\user\\Desktop\\test\\project\\frame-parent\\frame-examples\\frame-scramble-ticket\\src\\main\\resources\\suite_4.js";
        Path path = Paths.get(scriptFile);
        String script = new String(Files.readAllBytes(path));
        String[] lines = script.split("\r\n");

        Map<Integer, Integer> id_count_map = new TreeMap<>(Integer::compareTo);
        List<int[]> count_id_list = new LinkedList<>();

        String keyword = "c[";
        // String keyword = "r[";
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.trim().startsWith("//")) continue;
            int cursor = 0;
            int idx = -1;
            while ((idx = line.indexOf(keyword, cursor)) != -1) {
                int braceCount = 1;
                int brace_start_idx = idx + keyword.length();
                int brace_end_idx = -1;
                for (int j = brace_start_idx; j < line.length(); j++) {
                    char c = line.charAt(j);
                    if (c == '[') braceCount++;
                    else if (c == ']') braceCount--;
                    if (braceCount == 0) {
                        brace_end_idx = j;
                        break;
                    }
                }
                if (brace_end_idx == -1) throw new RuntimeException("brace_end_idx");
                String id_str = line.substring(brace_start_idx, brace_end_idx);
                try {
                    Integer id = Integer.parseInt(id_str);
                    id_count_map.compute(id, (k, v) -> v == null ? 1 : v + 1);
                } catch (NumberFormatException e) {
                    System.out.println("illegal id: " + id_str);
                }

                cursor = idx + 1;
            }
        }

        // Pattern p = Pattern.compile("c\\[(.*?)]");
        // Matcher matcher = p.matcher(script);
        //
        // while (matcher.find()) {
        //     try {
        //         Integer id = Integer.parseInt(matcher.group(1));
        //         id_count_map.compute(id, (k, v) -> v == null ? 1 : v + 1);
        //     } catch (NumberFormatException e) {
        //         System.out.println("illegal id: " + matcher.group());
        //     }
        // }


        id_count_map.forEach((k, v) -> count_id_list.add(new int[]{k, v}));
        count_id_list.sort(Comparator.comparingInt(o -> o[1]));

        count_id_list.forEach(l -> System.out.println("c[" + l[0] + "] -> " + l[1]));
    }
}
