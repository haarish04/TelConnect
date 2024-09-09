package com.example.TelConnect.service;

import com.example.TelConnect.model.ServicePlan;
import com.example.TelConnect.repository.ServicePlanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicePlanService {

    @Autowired
    private final ServicePlanRepository servicePlanRepository;

    public ServicePlanService(ServicePlanRepository servicePlanRepository){
        this.servicePlanRepository=servicePlanRepository;
    }

    //Service to create new service plan
    public boolean createPlan(ServicePlan plan){
        if(servicePlanRepository.findByPlanId(plan.getPlanId())==null) {
            servicePlanRepository.save(plan);
            return true;
        }
        else
            return false;
    }

    //Get service using Id
    public ServicePlan getPlan(String planId){
        return servicePlanRepository.findByPlanId(planId);
    }

    //Get all the service plans
    public List<ServicePlan> getAllPlans(){
        return servicePlanRepository.findAll();
    }

    //Delete specific plan
    public void deletePlan(String planId){
        servicePlanRepository.deleteById(planId);
    }

}
