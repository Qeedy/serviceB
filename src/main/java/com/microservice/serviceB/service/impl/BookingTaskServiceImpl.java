package com.microservice.serviceB.service.impl;

import com.microservice.serviceB.entity.BookingProcess;
import com.microservice.serviceB.enums.RejectReasonEnum;
import com.microservice.serviceB.model.BookingDetailModel;
import com.microservice.serviceB.model.BookingListModel;
import com.microservice.serviceB.model.BookingProcessModel;
import com.microservice.serviceB.model.CreateBookingModel;
import com.microservice.serviceB.service.BookingService;
import com.microservice.serviceB.service.BookingTaskService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public Page<BookingListModel> getAllTasksByTechnicianId(UUID technicianId, Pageable pageable) {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskCandidateOrAssigned(technicianId.toString())
                .orderByTaskDueDate().desc();
        return getBookingListModels(pageable, taskQuery);
    }

    @Override
    public Page<BookingListModel> getAllTaskAdmin(Pageable pageable) {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskVariableValueEquals("role", "admin")
                .orderByTaskCreateTime().desc();
        return getBookingListModels(pageable, taskQuery);
    }

    private PageImpl<BookingListModel> getBookingListModels(Pageable pageable, TaskQuery taskQuery) {
        List<Task> tasks = taskQuery.listPage(
                pageable.getPageNumber() * pageable.getPageSize(),
                pageable.getPageSize());
        List<String> businessKeys = tasks.stream().map(Task::getProcessInstanceId).toList();
        List<BookingListModel> bookings = bookingService.getBookingListByBookingNumber(businessKeys);
        long total = taskQuery.count();
        return new PageImpl<>(bookings, pageable, total);
    }


    @Override
    public BookingDetailModel getTaskByBookingNumber(String bookingNumber) {
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(bookingNumber)
                .singleResult();
        return getBookingDetailModel(task);
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
            variables.put("userId", data.getBookingDetail().getCustomerName());
            runtimeService.startProcessInstanceByKey(
                "bookingProcess",
                data.getBookingNumber(), variables);
            return data.getBookingNumber();
        }catch(Exception e) {
            log.error("failed create task");
            throw e;
        }
    }

    private BookingDetailModel getBookingDetailModel(Task t) {
        return null;
    }
}
