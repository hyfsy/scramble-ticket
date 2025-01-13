
package com.scrambleticket.handler.context;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.scrambleticket.flow.FlowContext;
import com.scrambleticket.model.ScrambleTask;
import com.scrambleticket.model.SeatType;

import lombok.Data;

@Data
public class CandidateContext {

    private static final String CANDIDATE_CONTEXT = "CANDIDATE_CONTEXT";
    private static final List<Integer> cashingCutoffMinuteBeforeList = Arrays.asList(20, 60, 120, 180); // 截止兑现时间支持的列表

    private ScrambleTask task;

    // trainCode -> SeatType
    private Map<String, SeatType> candidatePlans = new LinkedHashMap<>();

    // 兑现截止时间，前120分钟
    private int cashingCutoffMinuteBefore = cashingCutoffMinuteBeforeList.get(2);
    // 接受新增列车
    private boolean acceptNewTrain = true;
    // 新增列车的期望出行时间，00-24
    private String newTrainTimeStart = "19";
    private String newTrainTimeEnd = "20";
    // 接受无座
    private boolean acceptStand = true;

    public static CandidateContext putNew(FlowContext context) {
        CandidateContext scrambleContext = new CandidateContext();
        context.putAttribute(CANDIDATE_CONTEXT, scrambleContext);
        return scrambleContext;
    }

    public static CandidateContext get(FlowContext context) {
        return context.getAttribute(CANDIDATE_CONTEXT, CandidateContext.class);
    }

    public static CandidateContext remove(FlowContext context) {
        return context.removeAttribute(CANDIDATE_CONTEXT, CandidateContext.class);
    }

    public void setCandidatePlans(Map<String, SeatType> candidatePlans) {
        this.candidatePlans = candidatePlans;
    }

    public void setTask(ScrambleTask task) {
        this.task = task;
    }

    public String getFirstPlanTrainCode() {
        if (getCandidatePlans() == null || getCandidatePlans().isEmpty()) {
            throw new IllegalStateException("candidate plans is empty");
        }
        return getCandidatePlans().keySet().iterator().next();
    }

    public void removeFirstPlanTrainCode() {
        if (getCandidatePlans() == null || getCandidatePlans().isEmpty()) {
            throw new IllegalStateException("candidate plans is empty");
        }
        getCandidatePlans().remove(getFirstPlanTrainCode());
    }
}
