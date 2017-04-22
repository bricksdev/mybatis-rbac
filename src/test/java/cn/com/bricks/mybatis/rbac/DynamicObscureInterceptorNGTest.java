/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.bricks.mybatis.rbac;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.plugin.Invocation;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author kete2003@gmail.com
 */
public class DynamicObscureInterceptorNGTest {

    public DynamicObscureInterceptorNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
        List<ObscureConfig> configs = new ArrayList<ObscureConfig>();

        configs.add(new ObscureConfig(Long.MIN_VALUE, "14", "PRICE", "0", "***"));
        configs.add(new ObscureConfig(Long.MIN_VALUE, "14", "MATERIAL", "3", "***"));
        configs.add(new ObscureConfig(Long.MIN_VALUE, "14", "NAME", "0", "***"));
        configs.add(new ObscureConfig(Long.MIN_VALUE, "14", "PHONE", "0", "***"));
        List<ObscureFields> fields = new ArrayList<ObscureFields>();
        fields.add(new ObscureFields(0, "PRICE", DomainBean.class.getName(), "price", 0));
        fields.add(new ObscureFields(2, "MATERIAL", DomainBean.class.getName(), "materialCode", 0));
        fields.add(new ObscureFields(1, "NAME", DomainBean.class.getName(), "name", 0));
        fields.add(new ObscureFields(1, "PHONE", DomainBean.class.getName(), "phone", 0));
        ClientObscureFilterService.setObscureData(configs, fields);
        List<String> roles = new ArrayList<String>();
        roles.add("14");
        ClientRoleService.setCurrentUserRoles(roles);
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * intercept方法的测试 (属于类DynamicObscureInterceptor)。
     */
    @Test
    public void testIntercept() throws Exception, Throwable {
        System.out.println("intercept");
        Invocation invocation = new Invocation(this, null, null) {
            @Override
            public Object proceed() throws InvocationTargetException, IllegalAccessException {
                List<DomainBean> domains = new ArrayList<DomainBean>();
                DomainBean domain = new DomainBean(11, "14552.00", "100241-00");
                domain.setPhone("13511111111");
                domain.setName("谷利多");
                domains.add(domain);
                return domains;
            }

        };
        DynamicObscureInterceptor instance = new DynamicObscureInterceptor();
        //Object expResult = null;
        Object result = instance.intercept(invocation);
        System.out.println(result);
//        assertEquals(result, expResult);
        // TODO 检查生成的测试代码并删除失败的默认调用。
    }

}
