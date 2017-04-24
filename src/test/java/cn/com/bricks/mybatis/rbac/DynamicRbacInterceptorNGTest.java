/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.bricks.mybatis.rbac;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionManager;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author kete2003@gmail.com
 */
public class DynamicRbacInterceptorNGTest {

    public DynamicRbacInterceptorNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
        List<FilterConfig> lstConfig = new ArrayList<FilterConfig>();
        List<FilterFields> lstFields = new ArrayList<FilterFields>();
        lstConfig.add(new FilterConfig(1L, "8", "PLANT_CODE", "in", "5001,C990,A200", 0L));

        lstFields.add(new FilterFields(2L, "PLANT_CODE", "CN", "PLANT_CODE", 0));

        ClientRoleFilterService.setFilterData(lstConfig, lstFields);
        List<String> roles = new ArrayList<String>();
        roles.add("8");
        ClientRoleService.setCurrentUserRoles(roles);
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * intercept方法的测试 (属于类DynamicRbacInterceptor)。
     */
    @Test
    public void testIntercept() throws Throwable {
        System.out.println("intercept");
        InputStream inputStream = null;
        try {
            
            inputStream = Resources.getResourceAsStream("cn/com/bricks/mybatis/test/MapperConfig.xml");
            SqlSessionManager sqlSession = SqlSessionManager.newInstance(inputStream);
            
            int count = sqlSession.openSession().selectOne("org.apache.ibatis.domain.blog.mappers.BlogMapper.selectCountOfPosts");
            System.out.println(count);
        } catch (IOException ex) {
            Logger.getLogger(DynamicRbacInterceptorNGTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(DynamicRbacInterceptorNGTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
