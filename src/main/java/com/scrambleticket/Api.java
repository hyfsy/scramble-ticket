
package com.scrambleticket;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scrambleticket")
public class Api {

    @RequestMapping("/listPassenger")
    public void listPassenger() {}

    @RequestMapping("/getPassenger")
    public void getPassenger() {}

    @RequestMapping("/addPassenger")
    public void addPassenger() {}

    @RequestMapping("/updatePassenger")
    public void updatePassenger() {}

    @RequestMapping("/deletePassenger")
    public void deletePassenger() {}

    @RequestMapping("/listTask")
    public void listTask() {}

    @RequestMapping("/getTask")
    public void getTask() {}

    @RequestMapping("/addTask")
    public void addTask() {}

    @RequestMapping("/updateTask")
    public void updateTask() {}

    @RequestMapping("/checkTasksPrepared")
    public void checkTasksPrepared(List<Long> taskIds) {}

}
