package com.hnust.wxsell.repository;

import com.hnust.wxsell.dataobject.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ZZH
 * @date 2018/4/5 0005 11:29
 **/
public interface UserMasterRepository extends JpaRepository<UserMaster , String> {
    UserMaster findByOpenId(String openId);

    List<UserMaster> findBySchoolNoAndGroupNo(String schoolNo,String groupNo);

    List<UserMaster> findBySchoolNo(String schoolNo);

    UserMaster findBySchoolNoAndGroupNoAndOpenId(String schoolNo,String groupNo,String openId);


}
