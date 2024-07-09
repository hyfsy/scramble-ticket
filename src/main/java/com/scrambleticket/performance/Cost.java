
package com.scrambleticket.performance;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.scrambleticket.util.CollectionUtil;

public class Cost {

    static Cost cost = new Cost();

    boolean enabled = false;

    public static Cost getInstance() {
        return cost;
    }

    private List<Long> network = new CopyOnWriteArrayList<>();
    private List<Long> task = new CopyOnWriteArrayList<>();
    private List<Long> delay = new CopyOnWriteArrayList<>();
    private List<Long> connect = new CopyOnWriteArrayList<>();
    private List<Long> other = new CopyOnWriteArrayList<>();

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public void network(long cost) {
        if (enabled && cost > 0)
            network.add(cost);
    }

    public void task(long cost) {
        if (enabled && cost > 0)
            task.add(cost);
    }

    public void delay(long cost) {
        if (enabled && cost > 0)
            delay.add(cost);
    }

    public void connect(long cost) {
        if (enabled && cost > 0)
            connect.add(cost);
    }

    public void other(long cost) {
        if (enabled && cost > 0)
            other.add(cost);
    }

    public void reset() {
        connect = new CopyOnWriteArrayList<>();
        network = new CopyOnWriteArrayList<>();
        task = new CopyOnWriteArrayList<>();
        delay = new CopyOnWriteArrayList<>();
        other = new CopyOnWriteArrayList<>();
    }

    public void print() {
        if (!enabled) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("=======================================\n");
        sb.append("connect cost: ").append(toCostString(connect)).append('\n');
        sb.append("network cost: ").append(toCostString(network)).append('\n');
        sb.append("task    cost: ").append(toCostString(task)).append('\n');
        sb.append("delay   cost: ").append(toCostString(delay)).append('\n');
        sb.append("other   cost: ").append(toCostString(other)).append('\n');
        sb.append("=======================================");
        System.out.println(sb);
    }

    private String toCostString(List<Long> costs) {
        Long cost = costs.stream().reduce(Long::sum).orElse(0L);
        if (CollectionUtil.isEmpty(costs)) {
            return "0";
        } else if (costs.size() == 1) {
            return String.valueOf(costs.get(0));
        } else {
            return costs.stream().map(String::valueOf).collect(Collectors.joining("+")) + "=" + cost;
        }
    }

    // ====================== get ============================

    public List<Long> network() {
        return network;
    }

    public List<Long> task() {
        return task;
    }

    public List<Long> delay() {
        return delay;
    }

    public List<Long> connect() {
        return connect;
    }

    public List<Long> other() {
        return other;
    }
}
