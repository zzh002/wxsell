package com.hnust.wxsell.service.impl;

import com.hnust.wxsell.dataobject.BoxApply;
import com.hnust.wxsell.form.BoxApplyForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author ZZH
 * @date 2018/4/3 0003 15:32
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BoxApplyServiceImplTest {

    @Autowired
    private BoxApplyServiceImpl boxApplyService;

    @Test
    public void create() {
        BoxApplyForm boxApplyForm = new BoxApplyForm();
        boxApplyForm.setUserName("李刚");
        boxApplyForm.setUserPhone("15745756589");
        boxApplyForm.setSchoolNo("1");
        boxApplyForm.setGroupDistrict("3");
        boxApplyForm.setGroupNo("3-2-102");

        BoxApply boxApply = boxApplyService.create(boxApplyForm);
        Assert.assertNotNull(boxApply);
    }

    @Test
    public void delete() {
        BoxApply boxApply = boxApplyService.delete("1522761612049943800");

        Assert.assertNotNull(boxApply);
    }



    @Test
    public void findExistAllBySchoolNo() {
        PageRequest pageRequest = new PageRequest(0,1);

        Page<BoxApply> boxApplyPage = boxApplyService.findExistAllBySchoolNo(pageRequest,"1");
        log.info("boxApplyPage={}",boxApplyPage.getTotalElements());
        Assert.assertNotNull(boxApplyPage);
    }

    @Test
    public void findExistAllBySchoolNoAndDistrict() {
        PageRequest pageRequest = new PageRequest(0,1);

        Page<BoxApply> boxApplyPage = boxApplyService.findExistAllBySchoolNoAndDistrict(pageRequest,"1","2");
        log.info("boxApplyPage={}",boxApplyPage.getTotalElements());
        Assert.assertNotNull(boxApplyPage);
    }

}