package com.microservice.serviceB.service.impl;

import com.microservice.serviceB.entity.BookingProcess;
import com.microservice.serviceB.mapper.BookingProcessMapper;
import com.microservice.serviceB.model.BookingDetailModel;
import com.microservice.serviceB.model.BookingListModel;
import com.microservice.serviceB.model.BookingTaskModel;
import com.microservice.serviceB.model.CreateBookingModel;
import com.microservice.serviceB.service.BookingService;
import com.microservice.serviceB.service.BookingTaskService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class BookingTaskServiceImpl implements BookingTaskService {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private BookingService bookingService;

    @Override
    public PageImpl<BookingTaskModel> getAllTasksByTechnicianId(String keyword, UUID technicianId, Pageable pageable) {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskCandidateUser(technicianId.toString())
                .orderByTaskCreateTime().desc();
        if(StringUtils.isNotBlank(keyword))
            taskQuery = taskService.createTaskQuery().processInstanceBusinessKeyLike("%" + keyword +"%")
                    .taskCandidateUser(technicianId.toString())
                    .orderByTaskCreateTime().desc();
        return getBookingListModels(pageable, taskQuery);
    }

    @Override
    public PageImpl<BookingTaskModel> getAllTaskAdmin(String keyword, Pageable pageable) {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskCandidateGroup("admin")
                .orderByTaskCreateTime().desc();
        if(StringUtils.isNotBlank(keyword))
            taskQuery = taskService.createTaskQuery().processInstanceBusinessKeyLike("%" + keyword +"%")
                    .taskCandidateGroup("admin")
                    .orderByTaskCreateTime().desc();
        return getBookingListModels(pageable, taskQuery);
    }

    private PageImpl<BookingTaskModel> getBookingListModels(Pageable pageable, TaskQuery taskQuery) {
        List<Task> tasks = taskQuery.listPage(
                pageable.getPageNumber() * pageable.getPageSize(),
                pageable.getPageSize());
        List<BookingTaskModel> bookings = tasks.stream().map(e-> {
            String businessKey = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(e.getProcessInstanceId())
                    .singleResult()
                    .getBusinessKey();
            BookingTaskModel booking = bookingService.getBookingTaskModel(businessKey);
            booking.setTaskName(e.getName());
            booking.setTaskId(e.getTaskDefinitionKey());
            return booking;
        }).toList();
        long total = taskQuery.count();
        return new PageImpl<>(bookings, pageable, total);
    }


    @Override
    public void processTask(String bookingNumber, Map<String, Object> variables) {
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(bookingNumber)
                .singleResult();
        taskService.complete(task.getId(), variables);
    }

    @Override
    @Transactional
    public String createTask(CreateBookingModel model) throws Exception {
        try {
            BookingProcess data = bookingService.createBookingProcess(model);
            Map<String, Object> variables = new HashMap<>();
            variables.put("bookingNumber", data.getBookingNumber());
            variables.put("userId", data.getBookingDetail().getCustomerId());
            variables.put("dueDate", data.getBookingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
            runtimeService.startProcessInstanceByKey(
                "bookingProcess",
                data.getBookingNumber(), variables);
            return data.getBookingNumber();
        }catch(Exception e) {
            log.error("failed create task");
            throw e;
        }
    }

    @Override
    public Integer getTotalTask() {
        return Integer.valueOf((int) taskService.createTaskQuery()
                .active()
                .count());
    }


}
