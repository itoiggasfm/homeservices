package com.maktabsharif.homeservices.repository;

import com.maktabsharif.homeservices.domain.Client;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.domain.enumeration.Role;

import java.sql.Timestamp;
import java.util.List;

public interface UserRepositoryCustom {

    List<User> findUserByUserRoleAndNameAndFamilyNameAndEmailAndExpertPoint(User user, String subserviceTitle, Integer lowExpertPoint, Integer highExpertPoint);
    List<Client> findUserRegisterDateAndRedisteredOrdersCountAndCompletedOrdersCount(Timestamp registerDte, Integer registeredOrders, Integer completedOrders);
}
