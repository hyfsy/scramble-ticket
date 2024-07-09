
package com.scrambleticket;

public class Temp {

    Window window = new Window();
    Object[] c = new Object[2000];
    Object[] r = new Object[2000];
    Document document = new Document();


    static int toInt(Object o) {
        return Integer.parseInt(String.valueOf(o));
    }
    static boolean toBoolean(Object o) {
        return Boolean.parseBoolean(String.valueOf(o));
    }
    static String toString(Object o) {
        return String.valueOf(o);
    }

    class Document {
        public void createElement(Object o) {

        }
    }

    class Window {
        UA_Opt_12306 ua_opt_12306 = new UA_Opt_12306();
        Json_Ua json_ua = new Json_Ua();

        class UA_Opt_12306 {
            Runnable setToken = () -> {
            };
            Runnable reload = () -> {
            };
            String image =
                "data:image/png;base64,Bl4XoyTN.d7N6CYWP0teP_YQvdqCAtPCZj_RpUFKlQt0gPSAg0hF3cBLC9WnqALsJ_IrjyDhNXldB2_Je0lxqdViDfv1vmBE0pEtupdrib2F5VRSj8YzgqVugEdW_35wd5prlowdHEhf3B26_eWfdH3yN8JSTqP0VibpajQuk4kMHJk45hZHEIzaszy82M9ctzokNjBkZXEMsdn8DBfsFw4.bhndKLQJ449benUIr7yV9qKfVGgKkvVEtOnGamK4Z5L18lNyQ_IWogGVW5gBV4JjU2zIXEcPCeVvnBRP0QBK7CdglGhzov2OmZpYYNjzXPwUTY2cXkspXGzqeV69B6aiIKQwXeKg2sas8HnYN4.TJgutI8xKqt86i5yhtGFn1HQnIM4_vYnkoh1U6sxJ_eSV3q5_ukadNs48U1FQnU7a71CPaUXGX6JblNVBYQvpPSTqP1aufvweGTbXbIG2Ti4ofRvPElOtryc8wundyjTVeSAk7HN_iMFJk8t1RF5Ew8acxqI_R1kqSWgeKRmGNoTJgutWqS0Vu5lY_lzYQjhle0won5f7sKwXeKxJzUkfCGlanW6T5TXlCRjbaRcrLu0lxqJE79uxJ5tGQCPVnDBl95zb7cCnVUKvsN1qVn3Gq0kHe4aEpAjDJf8FcY7iGuRJh_GOc4lq4xzI0RIj67kC0TqCFPTomBIq0NaacrVPewM.BuVC3bvhky9vle0woSnXGw22HPWpTzblrfIMM6kNRQRASVvmGSUDG2Y_CnvaDzqWMc45wGf4hN.7W_v7iXPwUT56ZO8z0gPSAHvG24jtJhC.YlXedy602X6jdZaYvUxBDPDgBFlyYzIjeSV3q7JM_1iEcXWxBgFqKNXgKHs0tePW6ceW_zFFos6vnOr7oqWhCgRNMUeY4rwQK3F5NyRB28BO7i8qIxKpB3vi6cQ4L19mUC3Kb9BldDJ.FoZrkq02UPnxBz9rIr2ZYp9qd8Nc8VscLiXDUiqAb4FzUVrdz6VKhldHZgU616yyri4_XBp8TQYPAZ5MZZd4EUr1F2a9bf_EOmJYYhcXfuTQ3orPiUfLqSWgeHWM.Yjnofrs2HPWp.aCva_xELzFE3QRMhDL1D42sxolBhmeKCKUd69Xj62yLsdZQFKDu7.OOcs5uqVR_2YFbkSs0wDUXQDu1LBhQ.ECwDSiNciUU8rApS7mMjrR0hHPAE3Zy_uo_nRij4GwgpYNjoPgT5FROU8YtNBtumJ9M.GGgVnHy33crsEeiDArse2ju2rTbgXy44p_4Yc40UPAlFeuzDWaHS2.x6QElzdKc8kg9PVnD7Q1_ifSSSSSyl8xkR6w6mtayN_Bw4EpxK2meJl7Rz6vA3P02tpN6WXQA.QKgSyLqjIhB0livtEr58hu_qpd3GoyT9KuHkZa2NJksJa.Kr.I9egEaq7yoqHjXE_P7uhOYBwcX2riXmvYJXx6tyC_o7gVuscQ76mbYW9uGDGoEZIdeHNJRXv_SntJqqsYe5fXzuUTCRpl7qmV97QvVWdEJa6Jcajj0P.qc22CkG5XHFbMRVvDCQe5hMOwZfqGJLD0dU3gYAQMcg9gtmPFnboN4R.B7abiL1HlFo5xILFcLiPzzLn6rK4PZUr9OgpQiWWMoOeCER_LsgmUj7ZDObCF0zmhwmiAMf3Z_8GNT6stMTncie1T4acAzSNEcJQ1StUwyrAmRvS";
        }

        class Json_Ua {

        }
    }

    public static void main(String[] args) {
        new Temp().init();
    }

    public void init() {
        // be(this::Re, 0).r();
    }

    private Pivot_R be(PivotConsumer e, int a) {
        return new Pivot_R(e, a);
    }
    class Pivot_R implements PivotConsumer {
        private Object[] j;
        public Pivot_R(PivotConsumer e, int a) {
            this.j = new Object[]{e, a};
        }

        private void r() {
            for (; null != j; ) {
                Object[] e = j;
                j = null;
                try {
                    ((PivotConsumer) e[0]).apply((int) e[1], this);
                } catch (Exception ex) {}
            }
        }

        @Override
        public void i(PivotConsumer e, int a) {
            j = new Object[]{e, a};
        }

        @Override
        public void apply(int e, PivotConsumer a) {
            // 仅实现接口，无实际功能
        }
    }
    interface PivotConsumer {
        void apply(int e, PivotConsumer a); // consumer 使用
        default void i(PivotConsumer e, int a) {
            // Pivot 使用
        }
    }

    private void Ja(int e, PivotConsumer a) {}
    private void Hs(int e, PivotConsumer a) {}
    private void ib(int e, PivotConsumer a) {}

    // function Re(e, a) {
    // private void Re(int e, PivotConsumer a) {
    //     for (; -1 < e; )
    //         switch (e) {
    //             case 0:
    //                 c[929] = c[976];
    //                         e = 1;
    //                 break;
    //             case 1:
    //                 be(this::ib, 11).r(),
    //                         e = 2;
    //                 break;
    //             case 2:
    //                 ((String) c[964]) = c[964].concat(c[934]),
    //                         e = 3;
    //                 break;
    //             case 3:
    //                 c[974] = r[340];
    //                         e = 4;
    //                 break;
    //             case 4:
    //                 c[975] = 6;
    //                         e = 5;
    //                 break;
    //             case 5:
    //                 c[977] = c[963];
    //                         e = 6;
    //                 break;
    //             case 6:
    //                 c[979] = 0;
    //                         e = 7;
    //                 break;
    //             case 7:
    //                 if (c[979] < c[974].length) {
    //                     e = 8;
    //                     break;
    //                 }
    //                 e = 10;
    //                 break;
    //             case 8:
    //                 c[977] += String.fromCharCode(c[974].charCodeAt(c[979]) ^ c[975]),
    //                         e = 9;
    //                 break;
    //             case 9:
    //                 c[979]++;
    //                         e = 7;
    //                 break;
    //             case 10:
    //                 e = 11;
    //                 break;
    //             case 11:
    //                 c[976] = c[977];
    //                         e = 12;
    //                 break;
    //             case 12:
    //                 c[929] = c[976];
    //                         e = 13;
    //                 break;
    //             case 13:
    //                 c[1635] = c[1634] + c[1635];
    //                 be(this::ib, 11).r();
    //                         e = 14;
    //                 break;
    //             case 14:
    //                 c[964] = c[964].concat(c[934]);
    //                         e = 15;
    //                 break;
    //             case 15:
    //                 c[964] = c[964].concat([8192 & r[341] ? 3 : 2]);
    //                 e = 16;
    //                 break;
    //             case 16:
    //                 c[915] = 531556703 ^ r[342];
    //                         e = 17;
    //                 break;
    //             case 17:
    //                 be(this::Hs, 1).r();
    //                         e = 18;
    //                 break;
    //             case 18:
    //                 c[964] = c[964].concat(c[919]);
    //                         e = 19;
    //                 break;
    //             case 19:
    //                  c[915] = 4;
    //             a.i(this::Ja, 0);
    //                 return;
    //         }
    // }

}
