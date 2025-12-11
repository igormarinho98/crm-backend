package com.crm.repository;

import com.crm.model.Activity;
import java.util.List;

public interface ActivityRepositoryCustom {
    List<Activity> findByCompanyId(String companyId);
}
