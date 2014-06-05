<?xml version='1.0' encoding='UTF-8'?>

<adb>
	<property name="seqId" id="sid" />
	<property name="searchCode" get="getPublisherCode" set="setSearchCode"
		id="code">
		<label lsid="CN">编码</label>
		<label lsid="EN">Code</label>

		<in>
			<text width="10" maxlength="20" />
		</in>
	</property>

	<property name="searchName" get="getPublisherName" set="setSearchName"
		id="name">
		<label lsid="CN">发行商名称</label>
		<label lsid="EN">Name</label>

		<in>
			<text width="10" maxlength="30" />
		</in>
	</property>

	<property name="searchPinCode" get="getPinCode" set="setSearchPinCode"
		id="pinCode">
		<label lsid="CN">发行商拼码</label>
		<label lsid="EN">pinCode</label>

		<in>
			<text width="10" maxlength="20" />
		</in>
	</property>

	<property name="isDelete"  id="isDelete">
		<label> 是否已删除</label>
		<in>
			<radio />
		</in>
	</property>
	
	<property name="isEmail"  id="isEmail">
		<in>
			<checkbox />
		</in>
		<label>发送报表</label>
	</property>
	
	<property name="isEmail1"  id="isEmail1">
		<in>
			<checkbox />
		</in>
		<label>发送报表</label>
	</property>
	
	<property name="isEmail2"  id="isEmail2">
		<in>
			<checkbox />
		</in>
		<label>发送报表</label>
	</property>
	
	<property name="isEmail3"  id="isEmail3">
		<in>
			<checkbox />
		</in>
		<label>发送报表</label>
	</property>
	
	<property name="isEmail4"  id="isEmail4">
		<in>
			<checkbox />
		</in>
		<label>发送报表</label>
	</property>
	
	<property name="publisherCode" id="code" mandatory="yes">
		<label>编码</label>

		<out>
			<align>left</align>
			<wrap>yes</wrap>
		</out>
		<in>
		<!-- 
			<text width="10" maxlength="20" />
			<validator id="ne" skip="next">
				<msg>请输入编码。</msg>
			</validator>
			<validator skip="next" method="checkCode">
				<msg>该编码已经存在!</msg>
			</validator>
			<validator id="maxlen" skip="next">
				<arg>20</arg>
				<msg>编码长度不能超过20。</msg>
			</validator>
		 -->
		</in>
	</property>
	
	<property name="shortName" id="shortname" mandatory="yes">
		<label>简称</label>

		<out>
			<align>left</align>
			<wrap>yes</wrap>
		</out>
		<in>
			<text width="10" maxlength="20" />

			<validator id="ne" skip="next">
				<msg>请输入发行商简称。</msg>
			</validator>
			<validator id="maxlen" skip="next">
				<arg>20</arg>
				<msg>简称长度不能超过20。</msg>
			</validator>
		</in>
	</property>
	
	<property name="publisherName" id="name" mandatory="yes">
		<label>全称</label>
		<out>
			<align>left</align>
			<wrap>yes</wrap>
		</out>

		<in>
			<text width="20" maxlength="30" />

			<validator id="ne" skip="next">
				<msg>请输入发行商全称。</msg>
			</validator>
			<validator id="maxlen" skip="next">
				<arg>30</arg>
				<msg>全称长度不能超过30。</msg>
			</validator>
		</in>
	</property>

	<property name="pinCode" id="pinCode" mandatory="yes">
		<label>拼码</label>
		<out>
			<align>left</align>
		</out>

		<in>
			<text width="20" maxlength="20" />
			<validator id="ne" skip="next">
				<msg>请输入发行商拼码。</msg>
			</validator>
			<validator id="maxlen" skip="next">
				<arg>20</arg>
				<arg>2</arg>
				<msg>拼码长度不能超过20。</msg>
			</validator>
		</in>
	</property>

	<property name="publisherPhone" id="publisherPhone" mandatory="yes">
		<label>电话</label>

		<out>
			<align>left</align>
		</out>

		<in>
			<text width="20" maxlength="20" />
			<validator id="ne" skip="next">
				<msg>请输入发行商电话。</msg>
			</validator>
			<validator id="maxlen" skip="next">
				<arg>20</arg>
				<msg>电话号码长度不能超过20。</msg>
			</validator>
			<validator id="re" skip="next">
				<arg>(^1[23456789]\d{9}$)|(^((\d{3,4})|\d{3,4}-)?\d{7,8}$)|(^\d{7,8}$)</arg>
				<msg>发行商电话的输入格式不正确,请重新输入发行商电话.</msg>
			</validator>
		</in>
	</property>
	
	<property name="publisherFax" id="publisherFax" mandatory="yes">
		<label>传真</label>

		<out>
			<align>left</align>
		</out>
		<in>
			<text width="20" maxlength="20" />

			<validator id="ne" skip="next">
				<msg>请输入发行商传真。</msg>
			</validator>
			<validator id="maxlen" skip="next">
				<arg>20</arg>
				<msg>传真长度不能超过20。</msg>
			</validator>
			<validator id="re" skip="next">
				<arg>(^((\d{3,4})|\d{3,4}-)?\d{7,8}$)|(^\d{7,8}$)</arg>
				<msg>发行商传真的输入格式不正确,请重新输入发行商传真.</msg>
			</validator>
		</in>
	</property>
	
	<property name="publisherAddress" id="publisherAddress" mandatory="yes">
		<label>详细地址</label>

		<out>
			<align>left</align>
		</out>
		<in>
			<text width="20" maxlength="50" />

			<validator id="ne" skip="next">
				<msg>请输入发行商详细地址。</msg>
			</validator>
			<validator id="maxlen" skip="next">
				<arg>50</arg>
				<msg>详细地址长度不能超过50。</msg>
			</validator>
		</in>
	</property>
	
	<property name="contact" id="contact" mandatory="yes">
		<label>主要联系人</label>

		<out>
			<align>left</align>
			<wrap>yes</wrap>
		</out>
		<in>
			<text width="10" maxlength="20" />

			<validator id="ne" skip="next">
				<msg>请输入主要联系人。</msg>
			</validator>
			<validator id="maxlen" skip="next">
				<arg>20</arg>
				<msg>主要联系人长度不能超过20。</msg>
			</validator>
		</in>
	</property>
	
	<property name="contactPhone" id="contactPhone" mandatory="yes">
		<label>主要联系人电话</label>

		<out>
			<align>left</align>
		</out>
		<in>
			<text width="20" maxlength="20" />

			<validator id="ne" skip="next">
				<msg>请输入主要联系人电话。</msg>
			</validator>
			<validator id="maxlen" skip="next">
				<arg>20</arg>
				<msg>主要联系人电话长度不能超过20。</msg>
			</validator>
			<validator id="re" skip="next">
				<arg>(^1[23456789]\d{9}$)|(^((\d{3,4})|\d{3,4}-)?\d{7,8}$)|(^\d{7,8}$)</arg>
				<msg>主要联系人电话的输入格式不正确,请重新输入主要联系人电话.</msg>
			</validator>
		</in>
	</property>
	
	<property name="contactEmail" id="contactEmail" mandatory="yes">
		<label>联系人Email1</label>

		<out>
			<align>left</align>
			<wrap>yes</wrap>
		</out>
		<in>
			<text width="20" maxlength="30" />
			<validator id="ne" skip="next">
				<msg> 请输入联系人Email1</msg>
			</validator>
			<validator id="maxlen" skip="next">
				<arg>30</arg>
				<msg>联系人Email1长度不能超过30。</msg>
			</validator>
			<validator id="re" skip="next">
				<arg>^[a-zA-Z]([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$</arg>
				<msg>联系人Email1的输入格式不正确,请重新输入联系人Email1.</msg>
			</validator>
		</in>
	</property>
	
	<property name="contactEmail1" id="contactEmail1" >
		<label>联系人Email2</label>
		<out>
			<align>left</align>
		</out>
		<in>
			<text width="20" maxlength="30" />
			<validator id="maxlen" skip="next">
				<arg>30</arg>
				<msg>联系人Email2长度不能超过30。</msg>
			</validator>
			<validator id="re" skip="next">
				<arg>(^[a-zA-Z]([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$)|(^\s*$)</arg>
				<msg>联系人Email2的输入格式不正确,请重新输入联系人Email2.</msg>
			</validator>
		</in>
	</property>
	
	<property name="contactEmail2" id="contactEmail2" >
		<label>联系人Email3</label>

		<out>
			<align>left</align>
		</out>
		<in>
			<text width="20" maxlength="30" />
			<validator id="maxlen" skip="next">
				<arg>30</arg>
				<msg>联系人Email3长度不能超过30。</msg>
			</validator>
			<validator id="re" skip="next">
				<arg>(^[a-zA-Z]([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$)|(^\s*$)</arg>
				<msg>联系人Email3的输入格式不正确,请重新输入联系人Email3.</msg>
			</validator>
		</in>
	</property>
	
	<property name="contactEmail3" id="contactEmail3" >
		<label>联系人Email4</label>

		<out>
			<align>left</align>
		</out>
		<in>
			<text width="20" maxlength="30" />
			<validator id="maxlen" skip="next">
				<arg>30</arg>
				<msg>联系人Email4长度不能超过30。</msg>
			</validator>
			<validator id="re" skip="next">
				<arg>(^[a-zA-Z]([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$)|(^\s*$)</arg>
				<msg>联系人Email4的输入格式不正确,请重新输入联系人Email4.</msg>
			</validator>
		</in>
	</property>
	
	<property name="contactEmail4" id="contactEmail4" >
		<label>联系人Email5</label>

		<out>
			<align>left</align>
		</out>
		<in>
			<text width="20" maxlength="30" />
			<validator id="maxlen" skip="next">
				<arg>30</arg>
				<msg>联系人Email5长度不能超过30。</msg>
			</validator>
			<validator id="re" skip="next">
				<arg>(^[a-zA-Z]([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$)|(^\s*$)</arg>
				<msg>联系人Email5的输入格式不正确,请重新输入联系人Email5.</msg>
			</validator>
		</in>
	</property>
	
	<property name="bank" id="bank" mandatory="yes">
		<label>开户银行</label>

		<out>
			<align>left</align>
		</out>
		<in>
			<text width="20" maxlength="50" />
			<validator id="ne" skip="next">
				<msg>请输入开户银行。</msg>
			</validator>
			<validator id="maxlen" skip="next">
				<arg>50</arg>
				<msg>开户银行长度不能超过50。</msg>
			</validator>
		</in>
	</property>
	
	<property name="bankAccount" id="bankAccount" mandatory="yes">
		<label>银行帐号</label>

		<out>
			<align>left</align>
		</out>
		<in>
			<text width="20" maxlength="30" />
			<validator id="ne" skip="next">
				<msg>请输入银行 帐号。</msg>
			</validator>
			<validator id="re" skip="next">
				<arg>^[a-zA-Z0-9]*$</arg>
				<msg>银行 帐号必须是数字和字母</msg>
			</validator>
			<validator id="maxlen" skip="next">
				<arg>30</arg>
				<msg>银行帐号长度不能超过30。</msg>
			</validator>
		</in>
	</property>
	
	<group id="search">
		<propref name="searchName" />
		<propref name="searchPinCode" />
		<propref name="searchCode" />
	</group>

	<group id="list">
		<propref name="publisherCode" sortasc="0" sortdesc="1" />
		<propref name="shortName" sortasc="2" sortdesc="3" />
		<propref name="publisherPhone" />
		<propref name="contact" />
		<propref name="contactPhone" />
		<propref name="contactEmail" />
	</group>

	<group id="edit">
		<propref name="publisherCode" />
		<propref name="shortName" />
		<propref name="publisherName" />
		<propref name="pinCode" />
		<propref name="publisherPhone" />
		<propref name="publisherFax" />
		<propref name="publisherAddress" />
		<propref name="contact" />
		<propref name="contactPhone" />
		<propref name="contactEmail" />
		<propref name="isEmail"/>
		<propref name="contactEmail1" />
		<propref name="isEmail1"/>
		<propref name="contactEmail2" />
		<propref name="isEmail2"/>
		<propref name="contactEmail3" />
		<propref name="isEmail3"/>
		<propref name="contactEmail4" />
		<propref name="isEmail4"/>
		<propref name="bank" />
		<propref name="bankAccount" />
	</group>
	
</adb>