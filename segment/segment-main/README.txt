客群管理工程
======================

+++文件路径说明+++
        该工程为普通的Web工程
        分别使用了三个source路径
        /src         应用主要的代码存放; 编译后输出路径 /webapp/WEB-INF/segment-classes
        /src-support 框架启动的公共类和配置文件; 编译后输出路径 /webapp/WEB-INF/classes
        /src-test    测试代码; 编译后输出路径 /target/test-classes
   
   web application的根路径为/webapp
        /config
        /jslib
        /segment
   
   
         建议把Web Context Path设置为/segment
         
         
         
+++当查询中出现group by时建议使用的SQL+++
select count(MEMBER_KEY) from (select distinct member.MEMBER_KEY, row_number() over (partition by 1 order by 1 ASC nulls last) 
from 
  CCS_RPT2_DEV.T_D_CON_MEMBER member,
  CCS_RPT2_DEV.T_F_CON_SALE consale 
where 
  member.MEMBER_KEY=consale.MEMBER_KEY
having 
  sum(consale.BK_SALE_AMOUNT) - sum(consale.RE_SALE_AMOUNT) between 0.0 and 100.0 and 
  count(distinct consale.Bk_CS_ORDER_CODE) between 0 and 20  
group by 
  member.MEMBER_KEY
order by row_number() over (partition by 1 order by 1 ASC nulls last) desc)
where rownum = 1

+++页面地址+++
	特殊积分条件配置页面地址：
	http://localhost:8080/segment/segment/extPointCriteria.jsp
	特殊积分条件配置页面地址-可查看生产SQL:
	http://localhost:8080/segment/segment/extPointCriteria.jsp?test=true
	客群管理页面地址：
	http://localhost:8080/segment/segment/segment.jsp
	客群管理页面测试地址-可查看生产SQL：
	http://localhost:8080/segment/segment/segment.jsp?test=true
	
	
	

   
        